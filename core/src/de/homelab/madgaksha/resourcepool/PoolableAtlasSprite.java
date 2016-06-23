package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Makes minor change to {@link AtlasSprite} to make them poolable. Namely,
 * adding a no-arg constructor and allowing for the atlasRegion to be set later.
 * 
 * @author madgaksha
 *
 */
public class PoolableAtlasSprite extends Sprite implements Poolable {
	private AtlasRegion region;
	private float originalOffsetX, originalOffsetY;

	public PoolableAtlasSprite() {
		originalOffsetX = 0;
		originalOffsetY = 0;
		reset();
	}

	public void reset() {
		this.originalOffsetX = this.originalOffsetY = 0.0f;
		setColor(1f, 1f, 1f, 1f);
		setTexture(null);
		region = null;
		setScale(1f);
	}

	public PoolableAtlasSprite(AtlasRegion region) {
		setAtlasRegion(region);
	}

	public void setAtlasRegion(AtlasRegion region) {
		this.region = region;
		originalOffsetX = region.offsetX;
		originalOffsetY = region.offsetY;
		setRegion(region);
		setOrigin(region.originalWidth / 2f, region.originalHeight / 2f);
		final int width = region.getRegionWidth();
		final int height = region.getRegionHeight();
		if (region.rotate) {
			super.rotate90(true);
			super.setBounds(region.offsetX, region.offsetY, height, width);
		} else
			super.setBounds(region.offsetX, region.offsetY, width, height);
		setColor(1, 1, 1, 1);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x + region.offsetX, y + region.offsetY);
	}

	@Override
	public void setX(float x) {
		super.setX(x + region.offsetX);
	}

	@Override
	public void setY(float y) {
		super.setY(y + region.offsetY);
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		float widthRatio = width / region.originalWidth;
		float heightRatio = height / region.originalHeight;
		region.offsetX = originalOffsetX * widthRatio;
		region.offsetY = originalOffsetY * heightRatio;
		int packedWidth = region.rotate ? region.packedHeight : region.packedWidth;
		int packedHeight = region.rotate ? region.packedWidth : region.packedHeight;
		super.setBounds(x + region.offsetX, y + region.offsetY, packedWidth * widthRatio, packedHeight * heightRatio);
	}

	@Override
	public void setSize(float width, float height) {
		setBounds(getX(), getY(), width, height);
	}

	@Override
	public void setOrigin(float originX, float originY) {
		super.setOrigin(originX - region.offsetX, originY - region.offsetY);
	}

	@Override
	public void setOriginCenter() {
		super.setOrigin(getWidth() / 2 - region.offsetX, getHeight() / 2 - region.offsetY);
	}

	@Override
	public void flip(boolean x, boolean y) {
		// Flip texture.
		if (region.rotate)
			super.flip(y, x);
		else
			super.flip(x, y);

		float oldOriginX = getOriginX();
		float oldOriginY = getOriginY();
		float oldOffsetX = region.offsetX;
		float oldOffsetY = region.offsetY;

		float widthRatio = getWidthRatio();
		float heightRatio = getHeightRatio();

		region.offsetX = originalOffsetX;
		region.offsetY = originalOffsetY;
		region.flip(x, y); // Updates x and y offsets.
		originalOffsetX = region.offsetX;
		originalOffsetY = region.offsetY;
		region.offsetX *= widthRatio;
		region.offsetY *= heightRatio;

		// Update position and origin with new offsets.
		translate(region.offsetX - oldOffsetX, region.offsetY - oldOffsetY);
		setOrigin(oldOriginX, oldOriginY);
	}

	@Override
	public void rotate90(boolean clockwise) {
		// Rotate texture.
		super.rotate90(clockwise);

		float oldOriginX = getOriginX();
		float oldOriginY = getOriginY();
		float oldOffsetX = region.offsetX;
		float oldOffsetY = region.offsetY;

		float widthRatio = getWidthRatio();
		float heightRatio = getHeightRatio();

		if (clockwise) {
			region.offsetX = oldOffsetY;
			region.offsetY = region.originalHeight * heightRatio - oldOffsetX - region.packedWidth * widthRatio;
		} else {
			region.offsetX = region.originalWidth * widthRatio - oldOffsetY - region.packedHeight * heightRatio;
			region.offsetY = oldOffsetX;
		}

		// Update position and origin with new offsets.
		translate(region.offsetX - oldOffsetX, region.offsetY - oldOffsetY);
		setOrigin(oldOriginX, oldOriginY);
	}

	@Override
	public float getX() {
		return super.getX() - region.offsetX;
	}

	@Override
	public float getY() {
		return super.getY() - region.offsetY;
	}

	@Override
	public float getOriginX() {
		return super.getOriginX() + region.offsetX;
	}

	@Override
	public float getOriginY() {
		return super.getOriginY() + region.offsetY;
	}

	@Override
	public float getWidth() {
		return super.getWidth() / region.getRotatedPackedWidth() * region.originalWidth;
	}

	@Override
	public float getHeight() {
		return super.getHeight() / region.getRotatedPackedHeight() * region.originalHeight;
	}

	public float getWidthRatio() {
		return super.getWidth() / region.getRotatedPackedWidth();
	}

	public float getHeightRatio() {
		return super.getHeight() / region.getRotatedPackedHeight();
	}

	public AtlasRegion getAtlasRegion() {
		return region;
	}

	public String toString() {
		return region.toString();
	}
}