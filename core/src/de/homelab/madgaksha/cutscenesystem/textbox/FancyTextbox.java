package de.homelab.madgaksha.cutscenesystem.textbox;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.enums.ESpeaker;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETextbox;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * A textbox supporting character portraits and character names.
 * 
 * @author madgaksha
 *
 */
public class FancyTextbox extends PlainTextbox {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FancyTextbox.class);
		
	private final static float DEFAULT_FACE_HEIGHT_RATIO = 0.18f;
	
	/** Speaker with an optional name and/or face. */
	private ESpeaker speaker = null;
	/** Variation of the speaker face to use. */
	private EFaceVariation faceVariation = null;
	/** Ratio of face height to game screen height. */
	private float faceHeightRatio = DEFAULT_FACE_HEIGHT_RATIO;
	
	private boolean requestedFullHeight = false;
	private boolean hasSpeakerName = false;
	private boolean hasFaceVariation = false;

	/** Nine patch for the text area when there is only text. */
	private final NinePatch ninePatchAllBox;
	/** Nine patch for the text area when there is only a speaker. */
	private final NinePatch ninePatchBottomBox;
	/** Nine patch for the speaker area. */
	private final NinePatch ninePatchTopBox;
	/**Nine patch for the speaker area when there is both a speaker and face. */
	private final NinePatch ninePatchBottomLeftBox;
	/** Nine patch for the face when there is a speaker as well. */
	private final NinePatch ninePatchBottomRightBox;
	/** Nine patch for the text area when there is no speaker. */
	private final NinePatch ninePatchLeftBox;
	/** Nine patch for the face when there is no speaker. */
	private final NinePatch ninePatchRightBox;
	
	/** A separate the bitmap font cache for the speaker's name. */ 
	private BitmapFontCache bitmapFontCacheSpeaker;
	
	/** Texture with the face of the character speaking. */
	private AtlasRegion faceTexture;

	private NinePatch ninePatchText;
	private NinePatch ninePatchSpeaker;
	private NinePatch ninePatchFace;
	
	private final Rectangle speakerBoxFrame = new Rectangle();
	private final Rectangle speakerBoxContent = new Rectangle();

	private final Rectangle faceBoxFrame = new Rectangle();
	private final Rectangle faceBoxContent = new Rectangle();
	
	private final ETextbox type;
	
	public FancyTextbox(NinePatch ninePatchAllBox, NinePatch ninePatchBottomBox, NinePatch ninePatchTopBox,
			NinePatch ninePatchLeftBox, NinePatch ninePatchRightBox, NinePatch ninePatchBottomLeftBox,
			NinePatch ninePatchBottomRightBox, ETextbox type) {
		super();
		this.ninePatchAllBox = ninePatchAllBox;
		
		this.ninePatchBottomBox = ninePatchBottomBox;
		this.ninePatchTopBox = ninePatchTopBox;

		this.ninePatchLeftBox = ninePatchLeftBox;
		this.ninePatchRightBox = ninePatchRightBox;
		
		this.ninePatchBottomLeftBox = ninePatchBottomLeftBox;
		this.ninePatchBottomRightBox = ninePatchBottomRightBox;
		
		this.type = type;
		
		initialize();
	}
	
	private final void initialize() {
		ninePatchAllBox.setColor(Color.WHITE);
		ninePatchBottomBox.setColor(Color.WHITE);
		ninePatchTopBox.setColor(Color.WHITE);
		ninePatchBottomLeftBox.setColor(Color.WHITE);
		ninePatchBottomRightBox.setColor(Color.WHITE);
		ninePatchLeftBox.setColor(Color.WHITE);
		ninePatchRightBox.setColor(Color.WHITE);
	}

	public void setSpeaker(ESpeaker speaker) {
		this.speaker = speaker;
		hasSpeakerName = speaker != null && speaker.hasName();
		setRequiredCharacters();
		dirty = true;
	}

	public void setFaceVariation(EFaceVariation faceVariation) {
		if ((faceVariation != null && !hasFaceVariation) || (faceVariation == null && hasFaceVariation))
			dirty = true;
		hasFaceVariation = faceVariation != null;
		this.faceVariation = faceVariation;
	}
	
	/** Ratio of the face height and game screen height.
	 * You might want to change this together with {@link PlainTextbox#setTextHeightRatio(float)}
	 */
	public void setFaceHeightRatio(float faceHeightRatio) {
		if (this.faceHeightRatio != faceHeightRatio) dirty = true;
		this.faceHeightRatio = faceHeightRatio;
		if (this.faceHeightRatio <= 0.0f) this.faceHeightRatio = DEFAULT_FACE_HEIGHT_RATIO;
	}

	@Override
	public void setBoxColor(Color boxColor) {
		ninePatchAllBox.setColor(boxColor);
		ninePatchBottomBox.setColor(boxColor);
		ninePatchTopBox.setColor(boxColor);
		ninePatchBottomLeftBox.setColor(boxColor);
		ninePatchBottomRightBox.setColor(boxColor);
		ninePatchBottomLeftBox.setColor(boxColor);
		ninePatchBottomRightBox.setColor(boxColor);
	}

	@Override
	public void setFullHeight(boolean fullHeight) {
		super.setFullHeight(fullHeight);
		this.requestedFullHeight = fullHeight;
	}

	@Override
	protected void updateBox() {
		// Convert ttf font to a bitmap font.
		rasterizeFont();
		
		bitmapFontCacheSpeaker = new BitmapFontCache(bitmapFont);
		
		// Apply original value for fullHeight property. Need to switch to full
		// height when there is a face.
		setFullHeight(requestedFullHeight);
		
		// Check if a texture exists for this face variation.
		hasFaceVariation = speaker != null && hasFaceVariation && speaker.hasFaceVariation(faceVariation);
		
		// Try to load the face texture.
		faceTexture = null;
		if (hasFaceVariation) {
			faceTexture = ResourceCache.getTexture(speaker.getFaceVariation(faceVariation));
			hasFaceVariation = hasFaceVariation && faceTexture != null;
		}
		// Layout textbox with or without speaker and face.
		if (hasSpeakerName && hasFaceVariation) {
			// speaker name and face
			setFullHeight(true);
			layoutMainBox(1.0f, ninePatchBottomLeftBox);
			layoutSpeakerBox(mainBoxFrame.height, ninePatchTopBox);
			layoutFaceBox(mainBoxFrame.height, ninePatchBottomRightBox);
			layoutMainBox(1.0f - ((float)faceBoxFrame.width) / ((float) viewportGame.getScreenWidth()),
					ninePatchBottomLeftBox);
			ninePatchText = ninePatchBottomLeftBox;
			ninePatchFace = ninePatchBottomRightBox;
			ninePatchSpeaker = ninePatchTopBox;
		} else if (hasSpeakerName) {
			// speaker name only
			layoutMainBox(1.0f, ninePatchBottomBox);
			layoutSpeakerBox(mainBoxFrame.height, ninePatchTopBox);
			ninePatchText = ninePatchBottomBox;
			ninePatchSpeaker = ninePatchTopBox;
		} else if (hasFaceVariation) {
			// face only
			setFullHeight(true);
			layoutMainBox(1.0f, ninePatchLeftBox);
			layoutFaceBox(mainBoxFrame.height, ninePatchRightBox);
			layoutMainBox(1.0f - ((float)faceBoxFrame.width) / ((float) viewportGame.getScreenWidth()),
					ninePatchLeftBox);
			ninePatchText = ninePatchLeftBox;
			ninePatchFace = ninePatchRightBox;
		} else {
			// plain text box
			layoutMainBox(1.0f, ninePatchAllBox);
			ninePatchText = ninePatchAllBox;
		}
		
		finishTexboxFontLayout();

		
		// Compute layout for speaker name.
		if (hasSpeakerName) {
			bitmapFontCacheSpeaker.clear();
			bitmapFontCacheSpeaker.setColor(speaker.getColor());
			bitmapFontCacheSpeaker.addText(speaker.getName(), speakerBoxContent.x,
					speakerBoxContent.y + speakerBoxContent.height, 0, speaker.getName().length(),
					speakerBoxContent.width, Align.topLeft, false, StringUtils.EMPTY);
		}
		
		dirty = false;
	}

	@Override
	public void mainRender() {
		// Draw background first.
		ninePatchText.draw(batchPixel, mainBoxFrame.x, mainBoxFrame.y, mainBoxFrame.width, mainBoxFrame.height);		
		if (hasSpeakerName) {
			ninePatchSpeaker.draw(batchPixel, speakerBoxFrame.x, speakerBoxFrame.y, speakerBoxFrame.width,
					speakerBoxFrame.height);
		}
		if (hasFaceVariation) {
			ninePatchFace.draw(batchPixel, faceBoxFrame.x, faceBoxFrame.y, faceBoxFrame.width, faceBoxFrame.height);
			batchPixel.draw(faceTexture, faceBoxContent.x, faceBoxContent.y, faceBoxContent.width, faceBoxContent.height);
		}
		// Draw text last.
		renderTextboxText();
		bitmapFontCacheSpeaker.draw(batchPixel);
	}
	
	@Override
	protected String getSpeakerName() {
		return hasSpeakerName ? speaker.getName() : StringUtils.EMPTY;
	}

	/**
	 * Computes the layout for the speaker box, positioned beginning at height.
	 * 
	 * @param startY Bottom left edge of the speaker box.
	 * @param NinePatch The nine patch to be used.
	 */
	private void layoutSpeakerBox(float startY, NinePatch ninePatch) {
		layoutTextArea(1.0f, mainBoxFrame.height, ninePatch, speakerBoxFrame, speakerBoxContent, 1, 1);
	}
	
	private void layoutFaceBox(float height, NinePatch ninePatch) {
		faceBoxFrame.height = height;
		
		faceBoxContent.height = faceHeightRatio * viewportGame.getScreenHeight();
		faceBoxContent.width = faceBoxContent.height * faceTexture.getRegionWidth() / faceTexture.getRegionHeight();

		faceBoxFrame.width = faceBoxContent.width + ninePatch.getPadLeft() + ninePatch.getPadRight();
		
		faceBoxContent.x = viewportGame.getScreenWidth() -faceBoxContent.width - ninePatch.getPadRight();
		faceBoxContent.y = ninePatch.getPadBottom();
		
		faceBoxFrame.x = faceBoxContent.x - ninePatch.getPadLeft();
		faceBoxFrame.y = 0;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		bitmapFontCacheSpeaker.clear();
		faceTexture = null;
		ninePatchFace = null;
		ninePatchSpeaker = null;
		ninePatchText = null;
		bitmapFontCacheSpeaker = null;
	}

	@Override
	public void reset() {
		super.reset();
		setSpeaker(null);
		setFaceVariation(null);
		setFaceHeightRatio(DEFAULT_FACE_HEIGHT_RATIO);
		bitmapFontCacheSpeaker.clear();
	}

	@Override
	public boolean isSimiliar(Object o) {
		if (o instanceof FancyTextbox) {
			FancyTextbox fb = (FancyTextbox) o;			
			return hasSpeakerName == fb.hasSpeakerName && hasFaceVariation == fb.hasFaceVariation && speaker == fb.speaker && type == fb.type;
		}
		return false;
			
	}
}