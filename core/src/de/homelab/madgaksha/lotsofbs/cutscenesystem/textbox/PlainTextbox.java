package de.homelab.madgaksha.lotsofbs.cutscenesystem.textbox;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.GlyphLayout.GlyphRun;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETextbox;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

/**
 * A plain textbox providing basic functionality.
 * 
 * @author madgaksha
 * @see FancyTextbox
 */
public abstract class PlainTextbox implements Disposable {

	private final static Logger LOG = Logger.getLogger(PlainTextbox.class);

	/**
	 * Maximum number of lines for any textbox. If there are more lines, they
	 * get cut off.
	 */
	private final static int MAX_LINE_COUNT = 4;

	public final static float DEFAULT_TEXT_HEIGHT_RATIO = 0.025f;

	/** @see BitmapFont#usesIntegerPositions() */
	protected final static boolean USE_INTEGER_POSITIONS = true;

	/**
	 * Each text is rendered once at the reference font size and once at the
	 * target font size. This is used to determine a scaling factor so that the
	 * width of the resulting font will be the same for all screen resolutions.
	 */
	private final static int REFERENCE_FONT_SIZE = 12;

	// Parameters that can be changed.
	// One additonal entry for the speaker.
	/** Array of text lines to draw. */
	private final CharSequence[] lineList = new CharSequence[MAX_LINE_COUNT + 1];
	/** Text color. Default is {@link Color#WHITE}. */
	private Color textColor = new Color(Color.WHITE);
	/**
	 * Relative height of the text of one line in percent relative to the game
	 * viewport height.
	 */
	private float textHeightRatio = DEFAULT_TEXT_HEIGHT_RATIO;
	/** Space between two lines, relative to the line height. */
	private float textLineSpacing = 0.0f;
	/**
	 * If true, space is reserved for all possible lines. If false, textbox is
	 * only as tall as the number of lines require.
	 */
	private boolean fullHeight = true;
	/** Number of glyphs that will be drawn. */
	private int renderedGlyphCount = 0;
	/**
	 * If enabled, renders the text again a {@link #REFERENCE_FONT_SIZE} and
	 * scales the text so that its width will be the same for all screen
	 * resolutions.
	 */
	private boolean scaleToReference = true;

	// Main text font.
	/** Storing font parameters. */
	private final FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontParameter();
	private final FreeTypeFontParameter freeTypeFontParameterReference = new FreeTypeFontParameter();
	/** Font to use for rendering text. */
	protected FreeTypeFontGenerator freeTypeFontGenerator = null;
	/** Free type font converted to bitmap font. */
	protected BitmapFont bitmapFont = null;
	/**
	 * Converted bitmap font used as a reference to determine the scaling
	 * factor.
	 */
	protected BitmapFont bitmapFontReference = null;
	/** Number of lines available. */
	private int lineCount = 0;
	/** Characters required for the lines to be rendered. */
	private String requiredCharacters = StringUtils.EMPTY;
	/** Total number of glyphs set */
	private int glyphCount = 0;
	/** Whether we need recompute the layout or reload the font. */
	protected boolean dirty = true;

	// Internal parameters storing the pre-computed layout
	/** Layout coordinates for the main text box frame. */
	protected Rectangle mainBoxFrame = new Rectangle();
	/** Layout coordinates for the main text box content area. */
	protected Rectangle mainBoxContent = new Rectangle();

	protected final ETextbox type;
	protected EFreeTypeFontGenerator fontType;

	/** Distance between one line and the next, includes the line spacing. */
	private float lineDistance;

	/**
	 * Creates a new textbox with default options. Use {@link #setLines(String)}
	 * etc. to setup the textbox.
	 */
	public PlainTextbox(ETextbox type) {
		initialize();
		this.type = type;
		dirty = true;
	}

	private final void initialize() {
		// Set constant font parameters.
		freeTypeFontParameter.kerning = true;
		freeTypeFontParameter.magFilter = Texture.TextureFilter.Linear;
		freeTypeFontParameter.minFilter = Texture.TextureFilter.Linear;
		freeTypeFontParameter.color = Color.WHITE;
		freeTypeFontParameter.borderColor = Color.BLACK;
		freeTypeFontParameter.borderWidth = 1;
		freeTypeFontParameter.shadowOffsetX = 1;
		freeTypeFontParameter.shadowOffsetY = 2;
		freeTypeFontParameter.shadowColor = new Color(0f, 0f, 0f, 1.0f);
		freeTypeFontParameter.genMipMaps = false;
		freeTypeFontParameter.incremental = false;

		freeTypeFontParameterReference.kerning = true;
		freeTypeFontParameterReference.color = Color.WHITE;
		freeTypeFontParameterReference.genMipMaps = false;
		freeTypeFontParameterReference.incremental = false;
		freeTypeFontParameterReference.size = REFERENCE_FONT_SIZE;
	}

	// Setters for parameters that can be changed.
	public void setLines(String lineList) {
		setLines(lineList.split("[\\r\\n]+"));
	}

	public void setLines(CharSequence[] lineList) {
		glyphCount = 0;

		// Limit to maximum number of lines.
		lineCount = Math.min(lineList.length, MAX_LINE_COUNT);
		// Add lines.
		for (int i = 0; i != lineCount; ++i) {
			this.lineList[i] = lineList[i];
			glyphCount += this.lineList[i].length();
		}

		// Default to drawing all glyphs.
		renderedGlyphCount = glyphCount;

		// Extract unique characters that need to be rasterized.
		setRequiredCharacters();

		// Determine number of glyphs.

		dirty = true;
	}

	public void setTextHeightRatio(float textHeightRatio) {
		this.textHeightRatio = textHeightRatio;
		dirty = true;
	}

	public void setTextLineSpacing(float textLineSpacing) {
		this.textLineSpacing = textLineSpacing;
		dirty = true;
	}

	public void setTextColor(Color textColor) {
		if (textColor != null)
			this.textColor.set(textColor);
	}

	public void setFont(EFreeTypeFontGenerator freeTypeFontGenerator) {
		final FreeTypeFontGenerator newGenerator = ResourceCache.getFreeTypeFontGenerator(freeTypeFontGenerator);
		if (newGenerator != this.freeTypeFontGenerator) {
			dirty = true;
		}
		this.fontType = freeTypeFontGenerator;
		this.freeTypeFontGenerator = newGenerator;
	}

	public void setFullHeight(boolean fullHeight) {
		if (this.fullHeight != fullHeight)
			dirty = true;
		this.fullHeight = fullHeight;
	}

	/**
	 * Computes the layout for a rectangular area of text.
	 * 
	 * @param widthRatio
	 *            TextboxWidth / GameWindowWidth
	 * @param startHeight
	 *            y-coordinate of the bottom edge of the text area.
	 * @param ninePatch
	 *            Text area background.
	 * @param frame
	 *            Rectangle which will be filled with the layoutted dimensions
	 *            for the nine patch.
	 * @param frame
	 *            Rectangle which will be filled with the layoutted dimensions
	 *            for the text content.
	 * @param numberOfLines
	 *            How many lines the text contains.
	 * @param reservedNumberOfLines
	 *            How much space to reserve. Must not be smaller than
	 *            numberOfLines.
	 * @return The distance of one line to the next, including any additional
	 *         line spacing.
	 */
	protected float layoutTextArea(float widthRatio, float startHeight, NinePatch ninePatch, Rectangle frame,
			Rectangle content, int numberOfLines, int reservedNumberOfLines) {
		if (reservedNumberOfLines < numberOfLines)
			reservedNumberOfLines = numberOfLines;

		// Height and width, and difference between the actual number of lines
		// and the reserved number of lines.
		float boxHeight = getLineHeight() * (numberOfLines + textLineSpacing * (numberOfLines - 1.0f));
		float boxWidth = widthRatio * viewportGame.getScreenWidth();
		float textHeightDifference = boxHeight
				- (getLineHeight() * (reservedNumberOfLines + textLineSpacing * (reservedNumberOfLines - 1)));

		// Distance between two lines, including additional line spacing.
		float lineDistance = getLineHeight() * (textLineSpacing + 1.0f);

		// Dimensions of the content within the nine patch.
		content.x = ninePatch.getPadLeft();
		content.y = ninePatch.getPadBottom() + textHeightDifference + startHeight;
		content.width = boxWidth - ninePatch.getPadLeft() - ninePatch.getPadRight();
		content.height = boxHeight - textHeightDifference;

		// Dimensions of the surrounding nine patch.
		frame.x = 0;
		frame.y = startHeight;
		frame.width = boxWidth;
		frame.height = boxHeight + ninePatch.getPadTop() + ninePatch.getPadBottom();

		return lineDistance;
	}

	protected float getLineHeight() {
		// We only ever scale proportionally, thus scaleX==scaleY.
		return bitmapFont.getData().scaleX * bitmapFont.getLineHeight();
	}

	/**
	 * Computes the layout for the main text box.
	 * 
	 * @param widthRatio
	 *            TextboxWidth / GameWindowWidth
	 * @param ninePatch
	 *            Text area background.
	 */
	protected void layoutMainBox(float widthRatio, NinePatch ninePatch) {
		lineDistance = layoutTextArea(widthRatio, 0.0f, ninePatch, mainBoxFrame, mainBoxContent,
				getEffectiveLineCount(), lineCount);
	}

	protected void beginTexboxFontLayout() {
		// Dispose old rendered font.
		if (bitmapFont != null)
			bitmapFont.dispose();
		if (bitmapFontReference != null)
			bitmapFontReference.dispose();
		bitmapFont = null;
		bitmapFontReference = null;

		int fontSize = Math.max(5, Math.round(viewportGame.getScreenHeight() * textHeightRatio));
		float targetFontSize = textHeightRatio * viewportGame.getScreenHeight();

		// Setup font parameters.
		freeTypeFontParameter.size = fontSize;
		freeTypeFontParameter.characters = requiredCharacters;
		freeTypeFontParameterReference.characters = requiredCharacters;

		// Create main bitmap font.
		bitmapFont = freeTypeFontGenerator.generateFont(freeTypeFontParameter);
		bitmapFont.setUseIntegerPositions(USE_INTEGER_POSITIONS);

		if (scaleToReference) {
			// Create bitmap font used as a reference for scaling.
			bitmapFontReference = freeTypeFontGenerator.generateFont(freeTypeFontParameterReference);
			bitmapFontReference.setUseIntegerPositions(USE_INTEGER_POSITIONS);

			// Scale text so that it will always be the same width irrespective
			// of the font size and screen resolution.
			if (lineList.length > 0) {
				float referenceWidth = calucateTextWidth(bitmapFontReference, 0.0f, 0.0f, 0.0f,
						viewportGame.getScreenWidth(), null);
				float currentWidth = calucateTextWidth(bitmapFont, 0.0f, 0.0f, 0.0f, viewportGame.getScreenWidth(),
						null);
				float scale;
				if (currentWidth < 1.0f)
					scale = 1.0f;
				else {
					float targetWidth = referenceWidth * targetFontSize / REFERENCE_FONT_SIZE;
					scale = targetWidth / currentWidth;
				}
				bitmapFont.getData().setScale(scale);
			}
		}
	}

	/**
	 * Computes the layout for the main text and stores the result in the
	 * bitmapFont's bitmapFontCache.
	 * 
	 * @param bitmapFont
	 *            Bitmap font to use.
	 * @param initialY
	 *            Initial y-coordinate for the first line of text.
	 * @param advanceY
	 *            Height added to the initial y-coordinate to obtain the
	 *            y-coordinate of the next lines of text.
	 * @param positionX
	 *            x coordinate of the leftmost part of the text.
	 * @param targetWidth
	 *            Maximum width the rendered text may occupy, used for wrapping
	 *            and truncating.
	 * @param truncate
	 *            See parameter truncate in
	 *            {@link BitmapFontCache#addText(CharSequence, float, float, int, int, float, int, boolean, String)}
	 * @return The width of the rendered text, and the maximum glyph
	 */
	private float calucateTextWidth(BitmapFont bitmapFont, float initialY, float advanceY, float positionX,
			float targetWidth, String truncate) {
		final BitmapFontCache bitmapFontCache = bitmapFont.getCache();
		float maxTextWidth = 0.0f;
		glyphCount = 0;
		for (int i = 0; i != lineCount; ++i) {
			final GlyphLayout layout = bitmapFontCache.addText(lineList[i], positionX, initialY, 0,
					lineList[i].length(), targetWidth, Align.topLeft, false, truncate);
			maxTextWidth = Math.max(maxTextWidth, layout.width);
			for (GlyphRun gr : layout.runs) {
				glyphCount += gr.glyphs.size;
			}
			initialY += advanceY;
		}
		// Update glyph count. If text contains some unrenderable glyphs, it
		// will be smaller.
		renderedGlyphCount = Math.min(glyphCount, renderedGlyphCount);
		return maxTextWidth;
	}

	/**
	 * Compute final position for each glyph. Needs to be done after layout of
	 * textbox nine patches is done.
	 */
	protected void finishTexboxFontLayout() {
		float positionY = fontType.getVerticalAlignPosition().positionForCentered(mainBoxContent, bitmapFont,
				getEffectiveLineCount(), getLineHeight() * textLineSpacing);
		bitmapFont.getCache().clear();
		bitmapFont.getCache().setColor(textColor);
		calucateTextWidth(bitmapFont, positionY, -lineDistance, mainBoxContent.x, mainBoxContent.width,
				StringUtils.EMPTY);
	}

	/**
	 * Draws the main area text to the screen.
	 */
	protected void renderTextboxText() {
		// Draw the text.
		bitmapFont.getCache().draw(batchPixel, 0, renderedGlyphCount);
	}

	/**
	 * @param width New width in pixels.
	 * @param height New height in pixels.
	 */
	public void resize(int width, int height) {
		dirty = true;
	}

	@Override
	public void dispose() {
		LOG.debug("disposing textbox bitmap font");
		if (bitmapFont != null)
			bitmapFont.dispose();
	}

	/**
	 * Determines characters required for the current text, ie. the set of
	 * unique characters in {@link PlainTextbox#lineList}.
	 */
	protected final void setRequiredCharacters() {
		lineList[lineCount] = getSpeakerName();
		requiredCharacters = EFreeTypeFontGenerator.getRequiredCharacters(lineList, 0, lineCount + 1);
	}

	public final void render() {
		// Cannot to anything when no font has been provided yet.
		if (freeTypeFontGenerator == null)
			return;
		// Check if box needs to be updated.
		if (dirty) {
			LOG.debug("updating textbox layout");
			updateBox();
		}
		applySlideEffect();
		mainRender();
	}

	protected int getEffectiveLineCount() {
		return fullHeight ? MAX_LINE_COUNT : lineCount;
	}

	/**
	 * 
	 * @return The total number of glyphs rendered by this textbox.
	 * @see #setRenderedGlyphCount(int)
	 */
	public int getGlyphCount() {
		return glyphCount;
	}

	/**
	 * Sets the number of glyphs that should be rendered, starting from the
	 * first glyph.
	 * 
	 * @param renderedGlyphCount
	 *            Number of glyphs to draw. Must not exceed the total glyph
	 *            count.
	 * @see PlainTextbox#getGlyphCount()
	 */
	public void setRenderedGlyphCount(int renderedGlyphCount) {
		this.renderedGlyphCount = renderedGlyphCount > glyphCount ? glyphCount : renderedGlyphCount;
	}

	/**
	 * @param scaleToReference
	 *            Whether to scale to the reference font.
	 * @see #scaleToReference
	 */
	public void setScaleToReference(boolean scaleToReference) {
		this.scaleToReference = scaleToReference;
	}

	/**
	 * Gets the number of glyphs that should be rendered, starting from the
	 * first glyph.
	 * 
	 * @return Number of glyphs to draw.
	 */
	public int getRenderedGlyphCount() {
		return renderedGlyphCount;
	}

	/** Resets the textbox to its initial state. Not called automatically. */
	public void reset() {
		setFont(null);
		setLines(StringUtils.EMPTY);
		setBoxColor(Color.WHITE);
		setTextColor(Color.WHITE);
		setFullHeight(true);
		setTextHeightRatio(DEFAULT_TEXT_HEIGHT_RATIO);
	}

	/** @return The name of the speaker of this box, empty string if none. */
	protected abstract String getSpeakerName();

	/** Main render method rendering the actual textbox. */
	protected abstract void mainRender();

	/**
	 * Called when box layout needs to be updated, eg. after resize or when
	 * parameters or text changes.
	 */
	protected abstract void updateBox();

	/** Applies the slide effect and translates text etc. */
	protected abstract void applySlideEffect();

	/**
	 * Sets the color (tint) for the background of this textbox. Default is
	 * WHITE, ie. the color of the nine patch.
	 * 
	 * @param color
	 *            Color for this textbox.
	 */
	public abstract void setBoxColor(Color boxColor);
}
