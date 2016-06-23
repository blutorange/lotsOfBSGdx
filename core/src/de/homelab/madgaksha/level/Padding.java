package de.homelab.madgaksha.level;

import com.badlogic.gdx.math.Rectangle;

public abstract class Padding {
	protected float topPixel, bottomPixel, leftPixel, rightPixel, horizontalPixel, verticalPixel;
	protected float topValue, bottomValue, leftValue, rightValue, verticalValue, horizontalValue;
	private Rectangle rectangle = new Rectangle();
	private boolean dirty = true;
	private int countHorizontal;
	private int countVertical;

	public Padding() {
		topValue = bottomValue = leftValue = rightValue = verticalValue = horizontalValue = 0.0f;
	}

	public Padding top(float x) {
		topValue = x;
		dirty = true;
		return this;
	}

	public Padding bottom(float x) {
		bottomValue = x;
		dirty = true;
		return this;
	}

	public Padding left(float x) {
		leftValue = x;
		dirty = true;
		return this;
	}

	public Padding right(float x) {
		rightValue = x;
		dirty = true;
		return this;
	}

	public Padding horizontal(float x) {
		horizontalValue = x;
		dirty = true;
		return this;
	}

	public Padding vertical(float x) {
		verticalValue = x;
		dirty = true;
		return this;
	}

	public Padding all(float x) {
		topValue = bottomValue = leftValue = rightValue = horizontalValue = verticalValue = x;
		dirty = true;
		return this;
	}

	public float top() {
		if (dirty)
			apply();
		return topPixel;
	}

	public float bottom() {
		if (dirty)
			apply();
		return bottomPixel;
	}

	public float left() {
		if (dirty)
			apply();
		return leftPixel;
	}

	public float right() {
		if (dirty)
			apply();
		return rightPixel;
	}

	public float horizontal() {
		if (dirty)
			apply();
		return horizontalPixel;
	}

	public float vertical() {
		if (dirty)
			apply();
		return verticalPixel;
	}

	public void at(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public void at(Rectangle rectangle, int countHorizontal, int countVertical) {
		this.rectangle = rectangle;
		this.countHorizontal = countHorizontal;
		this.countVertical = countVertical;
		if (this.countHorizontal < 1)
			this.countHorizontal = 1;
		if (this.countVertical < 1)
			this.countVertical = 1;
	}

	protected void apply() {
		compute(rectangle);
		if (topPixel + bottomPixel + (countVertical - 1) * verticalPixel > rectangle.height) {
			topPixel = bottomPixel = verticalPixel = 1.0f;
		}
		if (leftPixel + rightPixel + (countHorizontal - 1) * horizontalPixel > rectangle.width) {
			leftPixel = rightPixel = horizontalPixel = 1.0f;
		}
	}

	protected abstract void compute(Rectangle rectangle);

	public final static class Relative extends Padding {
		@Override
		public void compute(Rectangle rectangle) {
			topPixel = topValue * rectangle.height;
			bottomPixel = bottomValue * rectangle.height;
			verticalPixel = verticalValue * rectangle.height;

			leftPixel = leftValue * rectangle.width;
			rightPixel = rightValue * rectangle.width;
			horizontalPixel = horizontalValue * rectangle.width;
		}
	}

	public final static class Absolute extends Padding {
		@Override
		public void compute(Rectangle rectangle) {
			topPixel = topValue;
			bottomPixel = bottomValue;
			verticalPixel = verticalValue;

			leftPixel = leftValue;
			rightPixel = rightValue;
			horizontalPixel = horizontalValue;
		}
	}
}