package de.homelab.madgaksha.cutscenesystem.textbox;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * A plain textbox providing basic functionality.
 * @author madgaksha
 * @see FancyTextbox
 */
public abstract class PlainTextbox implements Disposable {

	private final static Logger LOG = Logger.getLogger(PlainTextbox.class);
	
	/** Maximum number of lines for any textbox. If there are more lines,
	 * they get cut off.
	 */
	private final static int MAX_LINE_COUNT = 4;
	
	// Parameters that can be changed.
	// One additonal entry for the speaker.
	/** Array of text lines to draw. */
	private final CharSequence[] lineList = new CharSequence[MAX_LINE_COUNT+1];
	/** Text color. Default is {@link Color#WHITE}. */
	private Color textColor = Color.WHITE;
	/** Relative height of the text of one line in percent relative to the game viewport height. */
	private float textHeightRatio = 0.02f;
	/** Space between two lines, relative to the line height. */
	private float textLineSpacing = 0.0f;
	/** If true, space is reserved for all possible lines. If false, textbox is only as tall as the number of lines require.*/
	private boolean fullHeight = true;

	// Main text font.
	/** Storing font parameters. */
	private final FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontParameter();
	/** Font to use for rendering text. */
	private FreeTypeFontGenerator freeTypeFontGenerator = null;
	private float balancingScale;
	/** Free type font converted to bitmap font. */
	protected BitmapFont bitmapFont = null;
	/** Number of lines available. */
	private int lineCount = 0;
	/** Characters required for the lines to be rendered. */
	private String requiredCharacters = StringUtils.EMPTY;

	// Textbox texture is fixed. Use another ETextbox for a different texture.
	/** Nine patch for drawing the main textbox. */
	protected final NinePatch ninePatchAllBox;
	
	/** Whether we need recompute the layout or reload the font. */
	protected boolean dirty = true;

	// Internal parameters storing the pre-computed layout
	/** Layout coordinates for the main text box frame. */ 
	protected Rectangle mainBoxFrame = new Rectangle();
	/** Layout coordinates for the main text box content area. */
	protected Rectangle mainBoxContent = new Rectangle();
	
	
	/** Distance between one line and the next, includes the line spacing. */
	private float lineDistance;
	
	/**
	 * Creates a new textbox with default options.
	 * Use {@link #setLines(String)} etc. to setup the textbox.
	 */
	public PlainTextbox(NinePatch ninePatch) {
		this.ninePatchAllBox = ninePatch;
		initialize();
		dirty = true;
	}
	
	private final void initialize() {
		// Initialize nine patch color.
		ninePatchAllBox.setColor(Color.WHITE);
		// Set constant font parameters.
		freeTypeFontParameter.kerning = true;
		freeTypeFontParameter.magFilter = Texture.TextureFilter.Linear;
		freeTypeFontParameter.minFilter = Texture.TextureFilter.Linear;
		freeTypeFontParameter.color = Color.WHITE;
		freeTypeFontParameter.borderColor = Color.BLACK;
		freeTypeFontParameter.borderWidth = 1;
		freeTypeFontParameter.shadowOffsetX = 1;
		freeTypeFontParameter.shadowOffsetY = 2;
		freeTypeFontParameter.shadowColor = new Color(0f,0f,0f,1.0f);
		freeTypeFontParameter.genMipMaps = false;
		freeTypeFontParameter.incremental = false;
	}
	
	// Setters for parameters that can be changed.
	public void setLines(String lineList)  {
		setLines(lineList.split("[\\r\\n]+"));
	}
	public void setLines(CharSequence[] lineList)  {
		// Limit to maximum number of lines.
		lineCount = Math.min(lineList.length,MAX_LINE_COUNT);
		// Add lines.
		for (int i = 0; i != lineCount; ++i)
			this.lineList[i] = lineList[i];	
		// Extract unique characters that need to be rasterized. 
		setRequiredCharacters();

		dirty = true;
	}

	public void setTextHeightRatio(float textHeightRatio) {
		this.textHeightRatio = textHeightRatio;
		dirty = true;
	}
	
	public void setBoxColor(Color boxColor) {
		// ninePatchAllBox is final and fixed for a certain textbox
		ninePatchAllBox.setColor(boxColor);
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	public void setFont(EFreeTypeFontGenerator freeTypeFontGenerator) {
		final FreeTypeFontGenerator newGenerator = ResourceCache.getFreeTypeFontGenerator(freeTypeFontGenerator);
		if (newGenerator != this.freeTypeFontGenerator) dirty = true;
		this.freeTypeFontGenerator = newGenerator;
		this.balancingScale = freeTypeFontGenerator.getBalancingScale();
	}
	public void setFullHeight(boolean fullHeight) {
		if (this.fullHeight != fullHeight) dirty = true;
		this.fullHeight = fullHeight;		
	}
	
	/**
	 * Computes the layout for a rectangular area of text.
	 * @param widthRatio TextboxWidth / GameWindowWidth
	 * @param startHeight y-coordinate of the bottom edge of the text area.
	 * @param ninePatch Text area background.
	 * @param frame Rectangle which will be filled with the layoutted dimensions for the nine patch.
	 * @param frame Rectangle which will be filled with the layoutted dimensions for the text content.
	 * @param numberOfLines How many lines the text contains.
	 * @param reservedNumberOfLines How much space to reserve. Must not be smaller than numberOfLines.
	 */
	protected float layoutTextArea(float widthRatio, float startHeight, NinePatch ninePatch, Rectangle frame,
			Rectangle content, int numberOfLines, int reservedNumberOfLines) {
		if (reservedNumberOfLines < numberOfLines) reservedNumberOfLines = numberOfLines;

		float boxHeight = bitmapFont.getLineHeight() * (numberOfLines + textLineSpacing * (numberOfLines-1));
		float boxWidth = widthRatio * (float)viewportGame.getScreenWidth();
		float textHeightDifference = boxHeight - (bitmapFont.getLineHeight() * (reservedNumberOfLines + textLineSpacing * (lineCount-1)));
			
		float lineDistance = bitmapFont.getLineHeight() * (textLineSpacing +  1.0f);

		content.x = ninePatch.getPadLeft();
		content.y = ninePatch.getPadBottom() + textHeightDifference + startHeight;
		content.width = boxWidth - ninePatch.getPadLeft() - ninePatch.getPadRight();
		content.height = boxHeight - textHeightDifference;

		frame.x = 0;
		frame.y = startHeight;
		frame.width = boxWidth; 
		frame.height = boxHeight + ninePatch.getPadTop() + ninePatch.getPadBottom();
		return lineDistance;
	}
	
	/**
	 * Computes the layout for the main text box.
	 * @param widthRatio TextboxWidth / GameWindowWidth
	 * @param ninePatch Text area background.
	 */
	protected void layoutMainBox(float widthRatio, NinePatch ninePatch) {
		lineDistance = layoutTextArea(widthRatio, 0.0f, ninePatch, mainBoxFrame, mainBoxContent, getEffectiveLineCount(), lineCount);
	}

	protected void rasterizeFont() {
		// Dispose old rendered font.
		if (bitmapFont != null) bitmapFont.dispose();
		bitmapFont = null;

		int actualSize = Math.max(5,Math.round(viewportGame.getScreenHeight() * textHeightRatio));
		float desiredSize = viewportGame.getScreenHeight() * textHeightRatio;

		desiredSize += balancingScale * (desiredSize-20.0f);		
		
		// Setup font parameters.
		freeTypeFontParameter.size = actualSize;
		freeTypeFontParameter.characters = requiredCharacters;
		
		// Create new bitmap font
		bitmapFont = freeTypeFontGenerator.generateFont(freeTypeFontParameter);
		
		// Scale font so that it (mostly...) matches the desired size.
		bitmapFont.getData().setScale(desiredSize / (float)actualSize);  
	}

	protected boolean prepareRender() {
		// Check if textbox has been set up completely.
		if (freeTypeFontGenerator == null || ninePatchAllBox == null) return false;
		if (dirty) updateBox();
		return true;
	}
	
	/** Draws this textbox to the screen. */
	protected void renderTextArea(NinePatch ninePatch) {		
		// Draw the textbox.
		ninePatch.draw(batchPixel, mainBoxFrame.x, mainBoxFrame.y, mainBoxFrame.width, mainBoxFrame.height);
		
		// Draw text.
		bitmapFont.setColor(textColor);
		float y = mainBoxContent.y + mainBoxContent.height;
		for (int i = 0; i != lineCount ; ++i) {
			bitmapFont.draw(batchPixel, lineList[i], mainBoxContent.x, y, 0, lineList[i].length(), mainBoxContent.width, Align.topLeft, false, StringUtils.EMPTY);
			y -= lineDistance;
		}
	}
	
	public void resize(int width, int height) {
		dirty = true;
	}
	
	@Override
	public void dispose() {
		LOG.debug("disposing textbox bitmap font");
		if (bitmapFont != null) bitmapFont.dispose();
	}
	
	/**
	 * Determines characters required for the current text, ie. the set of unique characters in {@link PlainTextbox#lineList}. 
	 */
	protected final void setRequiredCharacters() {
		lineList[lineCount] = getSpeakerName();
		requiredCharacters = EFreeTypeFontGenerator.getRequiredCharacters(lineList, 0, lineCount+1);
	}
	
	protected abstract String getSpeakerName();
	
	protected int getEffectiveLineCount() {
		return  fullHeight ? MAX_LINE_COUNT : lineCount;
	}
	
	/** Used for determining if textbox needs to move down and back again,
	 * or if it is sufficient to leave the textbox on screen and simply replace
	 * the text when transitioning to the next textbox.
	 * @param box The other object to compare this textbox to.
	 */
	public boolean isSimiliar(PlainTextbox box) {
		if (box instanceof PlainTextbox) {
			return ninePatchAllBox == box.ninePatchAllBox && getEffectiveLineCount() == box.getEffectiveLineCount();
		}
		return false;
	}

	public final void render() {
		// Check if textbox has been set up completely.
		if (freeTypeFontGenerator == null) return;
		if (dirty) updateBox();
		mainRender();
	};
	
	protected abstract void mainRender();
	protected abstract void updateBox();

	/** Resets the textbox to its initial state. Not called automatically. */
	public void reset() {
		setFont(null);
		setLines(StringUtils.EMPTY);
		setBoxColor(Color.WHITE);
		setTextColor(Color.WHITE);
		setFullHeight(true);
		setTextHeightRatio(0.02f);
	}
	
}
