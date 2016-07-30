package de.homelab.madgaksha.scene2dext.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class DrawableMock implements Drawable {

	private float leftWidth = 0f, rightWidth = 0f, topHeight = 0f, bottomHeight = 0f, minWidth = 0f, minHeight = 0f; 
	
	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
	}

	@Override
	public float getLeftWidth() {
		return leftWidth;
	}

	@Override
	public void setLeftWidth(float leftWidth) {
		this.leftWidth = leftWidth;
	}

	@Override
	public float getRightWidth() {
		return rightWidth;
	}

	@Override
	public void setRightWidth(float rightWidth) {
		this.rightWidth = rightWidth;
	}

	@Override
	public float getTopHeight() {
		return topHeight;
	}

	@Override
	public void setTopHeight(float topHeight) {
		this.topHeight = topHeight;
	}

	@Override
	public float getBottomHeight() {
		return bottomHeight;
	}

	@Override
	public void setBottomHeight(float bottomHeight) {
		this.bottomHeight = bottomHeight;
	}

	@Override
	public float getMinWidth() {
		return minWidth;
	}

	@Override
	public void setMinWidth(float minWidth) {
		this.minWidth = minWidth;
	}

	@Override
	public float getMinHeight() {
		return minHeight;
	}

	@Override
	public void setMinHeight(float minHeight) {
		this.minHeight = minHeight;
	}

}
