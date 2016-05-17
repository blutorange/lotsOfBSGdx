package de.homelab.madgaksha.textboxsystem;
import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EBitmapFont;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public class Textbox {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Textbox.class);
	
	protected static float textHeight;
	protected static float targetWidth;
	protected static float textx;
	protected static float texty;
	protected static float boxx;
	protected static float boxy;
	protected static float boxw;
	protected static float boxh;
	protected static float lineHeight;
	protected final static String emptyString = "";
	
	/** Array of text lines to draw. */
	protected CharSequence[] lines;
	/** Textbox color. Default is {@link Color#WHITE}. */
	protected Color boxColor;
	/** Text color. Default is {@link Color#WHITE}. */
	protected Color textColor;
	/** Font to use for rendering text. */
	protected BitmapFont bitmapFont;
	protected EBitmapFont eBitmapFont;
	/** Nine patch for drawing the textbox. */
	protected NinePatch ninePatch;
	
	/**
	 * Creates a new textbox by splitting the lines at \n or \r\n
	 * 
	 * @param lines
	 */
	public Textbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch) {
		this(lines.split("[\\r\\n]+"), bitmapFont, ninePatch, Color.WHITE, Color.WHITE);
	}
	public Textbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, Color textColor) {
		this(lines.split("[\\r\\n]+"), bitmapFont, ninePatch, textColor, Color.WHITE);
	}
	public Textbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, Color textColor, Color boxColor) {
		this(lines.split("[\\r\\n]+"), bitmapFont, ninePatch, textColor, boxColor);
	}
	public Textbox(CharSequence[] lines, EBitmapFont bitmapFont, NinePatch ninePatch, Color textColor) {
		this(lines, bitmapFont, ninePatch, textColor, Color.WHITE);
	}
	public Textbox(CharSequence[] lines, EBitmapFont eBitmapFont, NinePatch ninePatch, Color textColor, Color boxColor) {
		this.lines = lines;
		this.boxColor = boxColor;
		this.textColor = textColor;
		this.eBitmapFont = eBitmapFont;
		this.ninePatch = ninePatch;
	}
	
	public void setLines(String lines)  {
		this.lines = lines.split("[\\r\\n]+");
	}
	public void setLines(CharSequence[] lines)  {
		this.lines = lines;
	}
	public void setBoxColor(Color boxColor) {
		this.boxColor = boxColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	public void setBitmapFont(BitmapFont bitmapFont) {
		this.bitmapFont = bitmapFont;
	}
	public void setNinePatch(NinePatch ninePatch) {
		this.ninePatch = ninePatch;
	}
	
	public void render(float animationFactor) {
		bitmapFont = ResourceCache.getBitmapFont(eBitmapFont);
		
		// Get height of the text content.
		textHeight = bitmapFont.getLineHeight() * lines.length;
		
		// Get maximum width available for each text line.
		targetWidth = viewportGame.getScreenWidth() - ninePatch.getPadLeft() - ninePatch.getPadRight();
		
		// Get bottom-left position for the starting line.
		textx = ninePatch.getPadLeft() - 0.5f*viewportPixel.getScreenWidth();
		texty = ninePatch.getPadBottom() + textHeight - 0.5f*viewportPixel.getScreenHeight();
		
		boxx = -0.5f*viewportPixel.getScreenWidth();
		boxy = -0.5f*viewportPixel.getScreenHeight();
		boxw = viewportGame.getScreenWidth();
		boxh = textHeight + ninePatch.getPadBottom() + ninePatch.getPadTop();
		
		// Distance of between one line and the next.
		lineHeight = bitmapFont.getLineHeight();
		
		// Apply style.
		bitmapFont.setColor(textColor);
		ninePatch.setColor(boxColor);
		
		// Render textbox
		ninePatch.draw(batchPixel, boxx, boxy-animationFactor*boxh, boxw, boxh);

		float textpos = texty;
		// Render text.
		for (CharSequence line : lines) {
			bitmapFont.draw(batchPixel, line, textx, textpos-animationFactor*boxh, 0, line.length(), targetWidth, Align.left, false, null);
			textpos -= lineHeight;
		}
	}
}
