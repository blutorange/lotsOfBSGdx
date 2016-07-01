package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.logging.Logger;

/**
 * Makes minor change to {@link AtlasSprite} to make them poolable. Namely,
 * adding a no-arg constructor and allowing for the atlasRegion to be set later.
 * 
 * @author madgaksha
 *
 */
public class PoolableAtlasSprite extends Sprite implements Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PoolableAtlasSprite.class);
	
	private AtlasRegion region;
	private float originalOffsetX, originalOffsetY;
	private float cropLeft = 1.0f;
	private float cropRight = 1.0f;
	private float cropBottom = 1.0f;
	private float cropTop = 1.0f;
	private float originalU, originalU2, originalV, originalV2;
	private float originalOriginX, originalOriginY, originalWidth, originalHeight, originalX, originalY;
	private boolean cropped = false;
	private boolean dirtyCrop = false;
	
	public PoolableAtlasSprite() {
		originalOffsetX = 0;
		originalOffsetY = 0;
		reset();
	}

	public void reset() {
		this.originalOffsetX = this.originalOffsetY = 0.0f;
		cropLeft = cropRight = cropBottom = cropTop = 1.0f;
		cropped = false;
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
		this.originalU = region.getU();
		this.originalV = region.getV();
		this.originalU2 = region.getU2();
		this.originalV2 = region.getV2();
		this.originalX = getX();
		this.originalY = getY();
		this.originalWidth = region.getRegionWidth();
		this.originalHeight = region.getRegionHeight();
		this.originalOriginX = getOriginX();
		this.originalOriginY = getOriginY();
	}

	@Override
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}
	
	/**
	 * Sets the position relative to the current origin of this sprite.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public void setPositionOrigin(float x, float y) {
		setX(x - originalOriginX);
		setY(y - originalOriginY);
	}

	@Override
	public void setX(float x) {
		if (cropped){
			originalX = x + region.offsetX;
			dirtyCrop = true;//applyCrop();
		}
		else {
			super.translateX(x + region.offsetX - originalX);
			originalX = x + region.offsetX;
		}
	}

	@Override
	public void setY(float y) {
		if (cropped) {
			originalY = y + region.offsetY;
			dirtyCrop = true;//applyCrop();
		}
		else {
			super.translateY(y + region.offsetY - originalY);
			originalY = y + region.offsetY;
		}
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		float widthRatio = width / region.originalWidth;
		float heightRatio = height / region.originalHeight;
		region.offsetX = originalOffsetX * widthRatio;
		region.offsetY = originalOffsetY * heightRatio;
		int packedWidth = region.rotate ? region.packedHeight : region.packedWidth;
		int packedHeight = region.rotate ? region.packedWidth : region.packedHeight;
		originalX = x + region.offsetX;
		originalY = y + region.offsetY;
		originalWidth = packedWidth * widthRatio;
		originalHeight = packedHeight * heightRatio;
		if (cropped) dirtyCrop = true;//applyCrop();
		else super.setBounds(originalX, originalY, originalWidth, originalHeight);
	}

	
	@Override
	public void setCenterX(float y){
		setX(y - originalWidth / 2);
	}
	
	@Override
	public void setCenterY(float y){
		setY(y - originalHeight / 2);
	}
	
	@Override
	public void setSize(float width, float height) {
		setBounds(getX(), getY(), width, height);
	}

	@Override
	public void setOrigin(float originX, float originY) {
		originalOriginX = originX - region.offsetX;
		originalOriginY = originY - region.offsetY;
		if (cropped) dirtyCrop = true;//applyCrop();
		else super.setOrigin(originalOriginX, originalOriginY);
	}

	/**
	 * Similar to {@link #setOrigin(float, float)}, but coordinates are relative to
	 * this sprite's current width and height, ie. between 0 and 1.
	 * @param origin Origin to be set.
	 */
	public void setOriginRelative(Vector2 origin) {
		setOrigin(origin.x*originalWidth, origin.y*originalHeight);
	}
	
	/**
	 * Similar to {@link #setOrigin(float, float)}, but coordinates are relative to
	 * this sprite's current width and height, ie. between 0 and 1.
	 * @param originX Origin to be set.
	 * @param originY Origin to be set.
	 */
	public void setOriginRelative(float originX, float originY) {
		setOrigin(originX*originalWidth, originY*originalHeight);
	}
	
	@Override
	public void setOriginCenter() {
		setOrigin(originalWidth / 2, originalHeight / 2);
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

	/**
	 * Crops the sprites to the given boundaries, relative to the origin.
	 * @param cropX Relative amount to crop left and right.
	 * @param cropY Relative amount to crop bottom and top.
	 */
	public void setCrop(Vector2 cropX, Vector2 cropY) {
		cropLeft = cropX.x;
		cropRight = cropX.y;
		cropBottom = cropY.x;
		cropTop = cropY.y;
		cropped = cropLeft != 1.0f || cropRight != 1.0f || cropBottom != 1.0f || cropTop != 1.0f;
		if (cropped) {
			setRegion(originalU, originalV, originalU2, originalV2);
			dirtyCrop = true;//applyCrop();
		}
	}

	@Override
	public void setRegion(float u, float v, float u2, float v2) {
		originalU = u;
		originalV = v;
		originalU2 = u2;
		originalV2 = v2;
		if (cropped) {
			u = 0.5f * ((originalU + originalU2) - (originalU2 - originalU) * cropRight);
			u2 = 0.5f * ((originalU + originalU2) + (originalU2 - originalU) * cropLeft);
			v = 0.5f * ((originalV + originalV2) - (originalV2 - originalV) * cropTop);
			v2 = 0.5f * ((originalV + originalV2) + (originalV2 - originalV) * cropBottom);
		}
		super.setRegion(u, v, u2, v2);
	}
	
	@Override
	public void setV(float v) {
		originalV = v;
		if (cropped) {
			v = 0.5f * ((originalV + originalV2) - (originalV2 - originalV) * cropTop);
			float v2 = 0.5f * ((originalV + originalV2) + (originalV2 - originalV) * cropBottom);
			super.setV(v);
			super.setV2(v2);
		}
		else super.setV(v);
	}
	
	@Override
	public void setV2(float v2) {
		originalV2 = v2;
		if (cropped) {
			float v = 0.5f * ((originalV + originalV2) - (originalV2 - originalV) * cropTop);
			v2 = 0.5f * ((originalV + originalV2) + (originalV2 - originalV) * cropBottom);
			super.setV(v);
			super.setV2(v2);
		}
		else super.setV2(v2);
	}
	
	@Override
	public void setU(float u) {
		originalU = u;
		if (cropped) {
			u = 0.5f * ((originalU + originalU2) - (originalU2 - originalU) * cropRight);
			float u2 = 0.5f * ((originalU + originalU2) + (originalU2 - originalU) * cropLeft);
			super.setU(u);
			super.setU2(u2);
		}
		else super.setU(u);
	}
	
	@Override
	public void setU2(float u2) {
		originalU2 = u2;
		if (cropped) {
			float u = 0.5f * ((originalU + originalU2) - (originalU2 - originalU) * cropRight);
			u2 = 0.5f * ((originalU + originalU2) + (originalU2 - originalU) * cropLeft);
			super.setU(u);
			super.setU2(u2);
		}
		else super.setU2(u2);
	}	
	
	/**
	 * Sets the dimensions of the image for the current crop.
	 * Texture coordinates are set separately by {@link PoolableAtlasSprite#setRegion(float, float, float, float)}
	 */
	private void applyCrop() {
		// Compute new position, width, height, and origin.
		float x = originalX + originalOriginX * (1.0f - cropLeft);
		float y = originalY + originalOriginY * (1.0f - cropBottom);
		float w = (originalOriginX * cropLeft   + (originalWidth -  originalOriginX) * cropRight);
		float h = (originalOriginY * cropBottom + (originalHeight - originalOriginY) * cropTop  );
		float ox = originalOriginX * cropLeft;
		float oy = originalOriginY * cropBottom;
		
		// Apply origin and bounds.
		super.setBounds(x, y, w, h);
		super.setOrigin(ox, oy);
	}
	
	@Override
	public void draw(Batch batch) {
		if (dirtyCrop ) {
			applyCrop();
			dirtyCrop = false;
		}
		super.draw(batch);
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