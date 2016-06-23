package de.homelab.madgaksha.util;

import java.util.Arrays;

import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.level.Padding;
import de.homelab.madgaksha.level.StatusScreen.Mode;
import de.homelab.madgaksha.logging.Logger;

/**
 * For layouting boxes horizontally and vertically inside other boxes with
 * padding.
 * 
 * @author madgaksha
 *
 */
public final class Layouter {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Layouter.class);

	private Layouter() {
	};

	public static Rectangle layoutCenteredInBoxAndKeepAspectRatio(Rectangle rectangle, Padding padding,
			float aspectRatio) {
		return layoutAlignedInBoxAndKeepAspectRatio(rectangle, padding, aspectRatio, 0.5f, 0.5f);
	}

	public static Rectangle layoutAlignedInBoxAndKeepAspectRatio(Rectangle rectangle, Padding padding,
			float aspectRatio, float alignHorizontal, float alignVertical) {

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(rectangle);

		float availableWidth = rectangle.width - padding.left() - padding.right();
		float availableHeight = rectangle.height - padding.top() - padding.bottom();

		float height, width;

		if (availableWidth / availableHeight > aspectRatio) {
			height = availableHeight;
			width = height * aspectRatio;
		} else {
			width = availableWidth;
			height = width / aspectRatio;
		}

		Rectangle aligned = new Rectangle();

		aligned.x = rectangle.x + padding.left() + alignHorizontal * (availableWidth - width);
		aligned.y = rectangle.y + padding.bottom() + alignVertical * (availableHeight - height);

		aligned.width = width;
		aligned.height = height;

		return aligned;
	}

	public static void layoutHorizontallyWithRelativeWidth(Rectangle parent, Padding padding, float relativeWidth,
			Rectangle... children) {
		float[] list = new float[children.length];
		Arrays.fill(list, relativeWidth);
		layoutHorizontallyWithRelativeWidth(parent, padding, list, children);
	}

	public static void layoutHorizontallyWithRelativeWidth(Rectangle parent, Padding padding, float[] relativeWidthList,
			Rectangle... children) {
		if (relativeWidthList.length != children.length)
			return;

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, relativeWidthList.length, 1);

		float totalWidth = 0.0f;
		for (float width : relativeWidthList) {
			totalWidth += width;
		}

		float scale = 1.0f / totalWidth;
		float availableWidth = parent.width - padding.left() - padding.right()
				- (relativeWidthList.length - 1) * padding.horizontal();
		float availableHeight = parent.height - padding.bottom() - padding.top();

		float posX = parent.x + padding.left();
		float posY = parent.y + padding.bottom();
		for (int i = 0; i != children.length; ++i) {
			float width = relativeWidthList[i] * availableWidth * scale;
			children[i].x = posX;
			children[i].y = posY;
			children[i].width = width;
			children[i].height = availableHeight;
			posX += width + padding.horizontal();
		}
	}

	public static void layoutHorizontallyWithAbsoluteWidth(Rectangle parent, Padding padding, float align,
			boolean keepAspectRatio, float absoluteWidth, Rectangle... children) {
		float[] list = new float[children.length];
		Arrays.fill(list, absoluteWidth);
		layoutHorizontallyWithAbsoluteWidth(parent, padding, align, keepAspectRatio, list, children);
	}

	public static void layoutHorizontallyWithAbsoluteWidth(Rectangle parent, Padding padding, float align,
			boolean keepAspectRatio, float[] absoluteWidthList, Rectangle... children) {
		if (absoluteWidthList.length != children.length)
			return;

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, absoluteWidthList.length, 1);

		float totalWidth = 0.0f;
		for (float width : absoluteWidthList) {
			totalWidth += width;
		}
		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.bottom() - padding.top();

		// Scale widths if content would not fit otherwise.
		float scaleX = 1.0f;
		float scaleY = 1.0f;
		if (totalWidth + (absoluteWidthList.length - 1) * padding.horizontal() > availableWidth) {
			scaleX = (availableWidth - padding.horizontal() * (absoluteWidthList.length - 1)) / totalWidth;
			if (keepAspectRatio)
				scaleY = scaleX;
			totalWidth *= scaleX;
		}

		float posX = parent.x + padding.left()
				+ align * (availableWidth - totalWidth - (absoluteWidthList.length - 1) * padding.horizontal());
		float posY = parent.y + padding.bottom() + (1.0f - scaleY) * availableHeight * 0.5f;
		for (int i = 0; i != children.length; ++i) {
			float width = absoluteWidthList[i] * scaleX;
			children[i].x = posX;
			children[i].y = posY;
			children[i].width = width;
			children[i].height = availableHeight * scaleY;
			posX += width + padding.horizontal();
		}
	}

	public static void layoutVerticallyWithRelativeHeight(Rectangle parent, Padding padding, float relativeHeight,
			Rectangle... children) {
		float[] list = new float[children.length];
		Arrays.fill(list, relativeHeight);
		layoutVerticallyWithRelativeHeight(parent, padding, list, children);
	}

	public static void layoutVerticallyWithRelativeHeight(Rectangle parent, Padding padding, float[] relativeHeightList,
			Rectangle... children) {
		if (relativeHeightList.length != children.length)
			return;

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, relativeHeightList.length, 1);

		float totalHeight = 0.0f;
		for (float height : relativeHeightList) {
			totalHeight += height;
		}

		float scale = 1.0f / totalHeight;

		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.bottom() - padding.top()
				- (relativeHeightList.length - 1) * padding.vertical();

		float posX = parent.x + padding.left();
		float posY = parent.y + padding.bottom();

		for (int i = 0; i != children.length; ++i) {
			float height = relativeHeightList[i] * availableHeight * scale;
			children[i].x = posX;
			children[i].y = posY;
			children[i].height = height;
			children[i].width = availableWidth;
			posY += height + padding.vertical();
		}
	}

	public static void layoutVerticallyWithAbsoluteHeight(Rectangle parent, Padding padding, float align,
			boolean keepAspectRatio, float absoluteHeight, Rectangle... children) {
		float[] list = new float[children.length];
		Arrays.fill(list, absoluteHeight);
		layoutVerticallyWithAbsoluteHeight(parent, padding, align, keepAspectRatio, list, children);
	}

	public static void layoutVerticallyWithAbsoluteHeight(Rectangle parent, Padding padding, float align,
			boolean keepAspectRatio, float[] absoluteHeightList, Rectangle... children) {
		if (absoluteHeightList.length != children.length)
			return;

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, absoluteHeightList.length, 1);

		float totalHeight = 0.0f;
		for (float height : absoluteHeightList) {
			totalHeight += height;
		}
		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.bottom() - padding.top();

		// Scale widths if content would not fit otherwise.
		float scaleX = 1.0f;
		float scaleY = 1.0f;
		if (totalHeight + (absoluteHeightList.length - 1) * padding.vertical() > availableHeight) {
			scaleY = (availableHeight - padding.vertical() * (absoluteHeightList.length - 1)) / totalHeight;
			if (keepAspectRatio)
				scaleX = scaleY;
			totalHeight *= scaleY;
		}

		float posX = parent.x + padding.left() + (1.0f - scaleX) * availableWidth * 0.5f;
		float posY = parent.y + padding.bottom()
				+ align * (availableHeight - totalHeight - (absoluteHeightList.length - 1) * padding.vertical());
		for (int i = 0; i != children.length; ++i) {
			float height = absoluteHeightList[i] * scaleY;
			children[i].x = posX;
			children[i].y = posY;
			children[i].height = height;
			children[i].width = availableWidth * scaleX;
			posY += height + padding.vertical();
		}
	}

	public static void layoutTopBottomWithRelativeWidth(Rectangle parent, Padding padding, float heightTop,
			float heightBottom, Rectangle top, Rectangle bottom) {

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, 1, 2);

		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.top() - padding.bottom() - padding.vertical();

		float totalHeight = 1.0f / (heightTop + heightBottom);
		float relativeHeightTop = heightTop * totalHeight;
		float relativeHeightBottom = heightBottom * totalHeight;

		bottom.x = parent.x + padding.left();
		bottom.y = parent.y + padding.bottom();
		bottom.width = availableWidth;
		bottom.height = availableHeight * relativeHeightBottom;

		top.x = parent.x + padding.left();
		top.y = bottom.y + bottom.height + padding.vertical();
		top.width = availableWidth;
		top.height = availableHeight * relativeHeightTop;
	}

	public static void layoutLeftRightWithRelativeWidth(Rectangle parent, Padding padding, float widthLeft,
			float widthRight, Rectangle left, Rectangle right) {

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, 2, 1);

		float availableWidth = parent.width - padding.left() - padding.right() - padding.horizontal();
		float availableHeight = parent.height - padding.top() - padding.bottom();

		float totalWidth = 1.0f / (widthLeft + widthRight);
		float relativeWidthLeft = widthLeft * totalWidth;
		float relativeWidthRight = widthRight * totalWidth;

		left.x = parent.x + padding.left();
		left.y = parent.y + padding.bottom();
		left.width = availableWidth * relativeWidthLeft;
		left.height = availableHeight;

		right.x = parent.x + padding.left() + left.width + padding.horizontal();
		right.y = parent.y + padding.bottom();
		right.width = availableWidth * relativeWidthRight;
		right.height = availableHeight;
	}

	public static Mode layoutTopBottomWithAspectRatio(Rectangle parent, Padding padding, float inverseAspectRatioTop,
			float inverseAspectRatioBottom, float alignTop, float alignBottom, Rectangle top, Rectangle bottom) {

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, 1, 2);

		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.top() - padding.bottom() - padding.vertical();

		Mode mode;

		if (availableHeight / availableWidth < inverseAspectRatioTop + inverseAspectRatioBottom) {
			mode = Mode.COMPACTED;
			Rectangle rectangleTop = new Rectangle();
			rectangleTop.x = parent.x + padding.left();
			rectangleTop.y = parent.y + padding.bottom();
			rectangleTop.width = availableWidth;
			rectangleTop.height = availableHeight + padding.vertical();
			top.set(layoutCenteredInBoxAndKeepAspectRatio(rectangleTop, padding, 1.0f / inverseAspectRatioTop));
		} else {
			mode = Mode.FULL;

			float totalHeight = 1.0f / (inverseAspectRatioTop + inverseAspectRatioBottom);
			float relativeHeightTop = inverseAspectRatioTop * totalHeight;
			float relativeHeightBottom = inverseAspectRatioBottom * totalHeight;

			Rectangle rectangleTop = new Rectangle();
			Rectangle rectangleBottom = new Rectangle();

			rectangleBottom.x = parent.x + padding.left();
			rectangleBottom.y = parent.y + padding.bottom();
			rectangleBottom.width = availableWidth;
			rectangleBottom.height = availableHeight * relativeHeightBottom;

			rectangleTop.x = parent.x + padding.left();
			rectangleTop.y = rectangleBottom.y + rectangleBottom.height + padding.vertical();
			rectangleTop.width = availableWidth;
			rectangleTop.height = availableHeight * relativeHeightTop;

			bottom.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleBottom, null, 1.0f / inverseAspectRatioBottom,
					0.5f, alignBottom));
			top.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleTop, null, 1.0f / inverseAspectRatioTop, 0.5f,
					alignTop));
		}

		return mode;
	}

	public static Mode layoutLeftRightWithAspectRatio(Rectangle parent, Padding padding, float aspectRatioLeft,
			float aspectRatioRight, float alignLeft, float alignRight, Rectangle left, Rectangle right) {

		if (padding == null)
			padding = new Padding.Absolute();
		padding.at(parent, 2, 1);

		float availableWidth = parent.width - padding.left() - padding.right() - padding.horizontal();
		float availableHeight = parent.height - padding.top() - padding.bottom();

		Mode mode;

		if (availableWidth / availableHeight < aspectRatioLeft + aspectRatioRight) {
			mode = Mode.COMPACTED;
			Rectangle rectangleLeft = new Rectangle();
			rectangleLeft.x = parent.x + padding.left();
			rectangleLeft.y = parent.y + padding.bottom();
			rectangleLeft.width = availableWidth + padding.horizontal();
			rectangleLeft.height = availableHeight;
			left.set(layoutCenteredInBoxAndKeepAspectRatio(rectangleLeft, padding, aspectRatioLeft));
		} else {
			mode = Mode.FULL;

			float totalWidth = 1.0f / (aspectRatioLeft + aspectRatioRight);
			float relativeWidthLeft = aspectRatioLeft * totalWidth;
			float relativeWidthRight = aspectRatioRight * totalWidth;

			Rectangle rectangleLeft = new Rectangle();
			Rectangle rectangleRight = new Rectangle();

			rectangleLeft.x = parent.x + padding.left();
			rectangleLeft.y = parent.y + padding.bottom();
			rectangleLeft.width = availableWidth * relativeWidthLeft;
			rectangleLeft.height = availableHeight;

			rectangleRight.x = rectangleLeft.x + rectangleLeft.width + padding.horizontal();
			rectangleRight.y = parent.y + padding.bottom();
			rectangleRight.width = availableWidth * relativeWidthRight;
			rectangleRight.height = availableHeight;

			left.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleLeft, null, aspectRatioLeft, alignLeft, 0.5f));
			right.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleRight, null, aspectRatioRight, alignRight, 0.5f));
		}

		return mode;
	}

}
