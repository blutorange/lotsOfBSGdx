package de.homelab.madgaksha.lotsofbs.enums;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.math.Rectangle;

public enum VerticalAlignPosition {
	BETWEEN_BASE_LINE_AND_X_HEIGHT,
	BETWEEN_BASE_LINE_AND_CAP_HEIGHT,
	BETWEEN_DESCENT_AND_ASCENT;

	/**
	 * Computes the position that needs to be passed to
	 * {@link BitmapFont#draw(com.badlogic.gdx.graphics.g2d.Batch, CharSequence, float, float, int, int, float, int, boolean, String)}
	 * etc. in order to center the text vertically. <br>
	 * <br>
	 * Note that according to the javadocs for {@link BitmapFontData#capHeight},
	 * an instance of {@link BitmapFont} always draws text such that the cap
	 * height line will be at the given y-coordinate.
	 * 
	 * @param box
	 *            Rectangular region where the text will be drawn.
	 * @param font
	 *            Font to be drawn.
	 * @param lineCount
	 *            Number of lines within this box. Divides the box vertically
	 *            into <code>lineCount</code> rectangles used as the box for
	 *            each line.
	 * @param The
	 *            additional spacing between two lines in pixel.
	 * @return The vertical position for drawing the the first (topmost) line of
	 *         the text. The get the position of the (i+1)-th line, subtract
	 *         <code>i*box.height/lineCount</code>
	 */
	public float positionForCentered(Rectangle box, BitmapFont font, int lineCount, float lineSpacing) {
		if (lineCount < 1)
			throw new IllegalArgumentException("lineCount cannot be smaller than 1");
		float posY = box.y + box.height
				- 0.5f * ((box.height - lineSpacing * ((lineCount) - 1.0f)) / lineCount);
		switch (this) {
		case BETWEEN_BASE_LINE_AND_CAP_HEIGHT:
			posY += 0.5f * font.getCapHeight();
			break;
		case BETWEEN_DESCENT_AND_ASCENT:
			posY += 0.5f * (font.getDescent() + font.getAscent() + font.getCapHeight());
			break;
		case BETWEEN_BASE_LINE_AND_X_HEIGHT:
		default:
			posY += font.getCapHeight() - 0.5f * font.getXHeight();
			break;
		}
		return posY;
	}

	public float positionForCentered(Rectangle box, BitmapFont font) {
		return positionForCentered(box, font, 1, 0.0f);
	}

}
