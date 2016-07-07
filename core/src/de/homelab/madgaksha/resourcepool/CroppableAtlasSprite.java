/*******************************************************************************
 * THIS IS THE SAME AS AtlasSprite, BUT SLIGHTLY MODIFIED BY ME TO ALLOW
 * FOR POOLING AND CROPPING SPRITES. IT ALSO FIXES A BUG WITH AtlasSprite#setCenter

 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.logging.Logger;

/**
 * Makes minor change to {@link AtlasSprite} to make them poolable, allow
 * for cropping and fixes a bug with {@link AtlasSprite#setCenter} and
 * {@link AtlasSprite#setOriginCenter}.
 * 
 * @author mzechner
 * @author Nathan Sweet 
 * @author madgaksha
 */
public class CroppableAtlasSprite extends Sprite implements Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CroppableAtlasSprite.class);

	private AtlasRegion region;
	private float originalOffsetX, originalOffsetY;
	private float cropLeft = 1.0f;
	private float cropRight = 1.0f;
	private float cropBottom = 1.0f;
	private float cropTop = 1.0f;
	/**	 Original texture coordinates, before cropping was applied. */
	private float originalU, originalU2, originalV, originalV2;
	/**	 Original origin x and y, before cropping was applied. */
	private float originalOriginX, originalOriginY;
	/** Original width and height, before cropping was applied. */
	private float originalWidth, originalHeight;
	/**	 Original position x and y, before cropping was applied. */
	private float originalX, originalY;
	private boolean cropped = false;
	private boolean dirtyCrop = false;

	public CroppableAtlasSprite() {
		super();
		reset();
	}
	
	public void reset() {
		cropLeft = cropRight = cropBottom = cropTop = 1.0f;
		originalX = originalY = 0.0f;
		originalWidth = originalHeight = 0.0f;
		originalOffsetX = originalOffsetY = 0.0f;
		originalOriginX = originalOriginY = 0.0f;
		originalU = originalU2 = originalV = originalV2 = 0.0f;
		cropped = false;
		dirtyCrop = false;
		super.setBounds(0f,0f,0f,0f);
		super.setColor(1f, 1f, 1f, 1f);
		super.setTexture(null);
		super.setScale(1f);
		region = null;
	}

	public CroppableAtlasSprite(AtlasRegion region) {
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
		this.originalX = super.getX();
		this.originalY = super.getY();
		this.originalWidth = region.getRegionWidth();
		this.originalHeight = region.getRegionHeight();
		this.originalOriginX = super.getOriginX();
		this.originalOriginY = super.getOriginY();
	}

	@Override
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	/**
	 * Sets the position relative to the current origin of this sprite.
	 * For example, if the current origin is at the top-left corner,
	 * this sets the position so that the top-left corner is at (x,y).
	 * 
	 * @param x
	 *            x coordinate.
	 * @param y
	 *            y coordinate.
	 */
	public void setPositionOrigin(float x, float y) {
		setX(x - originalOriginX - region.offsetX);
		setY(y - originalOriginY - region.offsetY);
	}

	@Override
	public void setX(float x) {
		if (cropped) {
			originalX = x + region.offsetX;
			dirtyCrop = true;
		} else {
			super.translateX(x + region.offsetX - originalX);
			originalX = x + region.offsetX;
		}
	}

	@Override
	public void setY(float y) {
		if (cropped) {
			originalY = y + region.offsetY;
			dirtyCrop = true;
		} else {
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
		if (cropped)
			dirtyCrop = true;
		else
			super.setBounds(originalX, originalY, originalWidth, originalHeight);
	}

	@Override
	public void setCenterX(float x) {
		setX(x - region.originalWidth / 2);
	}

	@Override
	public void setCenterY(float y) {
		setY(y - region.originalHeight / 2);
	}

	/** Sets the size of this sprite when drawn, before scaling and rotation are applied. This modifies
	 * the top-right corner of this sprite so that it lies at (width, height) relative to the current
	 * position of this sprite's bottom-left corner.
	 * <br><br>
	 * If origin, rotation, or scale are changed, it is slightly more efficient to set the size after those operations. If both position and size are to be changed, it is
	 * better to use {@link #setBounds(float, float, float, float)}. */
	@Override
	public void setSize(float width, float height) {
		setBounds(getX(), getY(), width, height);
	}

	/** Sets the origin in relation to the sprite's position for scaling, cropping and rotating it. */
	@Override
	public void setOrigin(float originX, float originY) {
		originalOriginX = originX - region.offsetX;
		originalOriginY = originY - region.offsetY;
		if (cropped)
			dirtyCrop = true;
		else
			super.setOrigin(originalOriginX, originalOriginY);
	}

	/**
	 * Similar to {@link #setOrigin(float, float)}, but coordinates are relative
	 * to this sprite's current width and height, ie. between 0 and 1.
	 * 
	 * @param origin
	 *            Origin to be set.
	 */
	public void setOriginRelative(Vector2 origin) {
		setOrigin(origin.x * region.originalWidth, origin.y * region.originalHeight);
	}

	/**
	 * Similar to {@link #setOrigin(float, float)}, but coordinates are relative
	 * to this sprite's current width and height, ie. between 0 and 1.
	 * 
	 * @param originX
	 *            Origin to be set.
	 * @param originY
	 *            Origin to be set.
	 */
	public void setOriginRelative(float originX, float originY) {
		setOrigin(originX * region.originalWidth, originY * region.originalHeight);
	}

	@Override
	public void setOriginCenter() {
		setOrigin(region.originalWidth / 2, region.originalHeight / 2);
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
	 * 
	 * @param cropX
	 *            Relative amount to crop to the left and right of the current origin.
	 * @param cropY
	 *            Relative amount to crop to the bottom and top of the current origin.
	 */
	public void setCrop(Vector2 cropX, Vector2 cropY) {
		setCrop(cropX.x, cropX.y, cropY.x, cropY.y);
	}

	/**
	 * Crops the sprites to the given boundaries, relative to the origin.
	 * 
	 * @param cropLR
	 *            Relative amount to crop to the left and right of the current origin.
	 * @param cropBT
	 *            Relative amount to crop to the bottom and top of the current origin.
	 */
	public void setCrop(float cropLR, float cropBT) {
		setCrop(cropLR, cropLR, cropBT, cropBT);
	}
	
	/**
	 * Crops the sprites to the given boundaries, relative to the origin.
	 * 
	 * @param cropLeft
	 *            Relative amount to crop to the left of the current origin.
	 * @param cropRight
	 *            Relative amount to crop to the right of the current origin.
	 * @param cropBottom
	 *            Relative amount to crop to the bottom of the current origin.
	 * @param cropTop
	 *            Relative amount to crop to the top of the current origin.
	 */
	public void setCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
		this.cropLeft = cropLeft;
		this.cropRight = cropRight;
		this.cropBottom = cropBottom;
		this.cropTop = cropTop;
		cropped = cropLeft != 1.0f || cropRight != 1.0f || cropBottom != 1.0f || cropTop != 1.0f;
		if (cropped) {
			setRegion(originalU, originalV, originalU2, originalV2);
			dirtyCrop = true;
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
		} else
			super.setV(v);
	}

	@Override
	public void setV2(float v2) {
		originalV2 = v2;
		if (cropped) {
			float v = 0.5f * ((originalV + originalV2) - (originalV2 - originalV) * cropTop);
			v2 = 0.5f * ((originalV + originalV2) + (originalV2 - originalV) * cropBottom);
			super.setV(v);
			super.setV2(v2);
		} else
			super.setV2(v2);
	}

	@Override
	public void setU(float u) {
		originalU = u;
		if (cropped) {
			u = 0.5f * ((originalU + originalU2) - (originalU2 - originalU) * cropRight);
			float u2 = 0.5f * ((originalU + originalU2) + (originalU2 - originalU) * cropLeft);
			super.setU(u);
			super.setU2(u2);
		} else
			super.setU(u);
	}

	@Override
	public void setU2(float u2) {
		originalU2 = u2;
		if (cropped) {
			float u = 0.5f * ((originalU + originalU2) - (originalU2 - originalU) * cropRight);
			u2 = 0.5f * ((originalU + originalU2) + (originalU2 - originalU) * cropLeft);
			super.setU(u);
			super.setU2(u2);
		} else
			super.setU2(u2);
	}

	//TODO rethink this computation, origin... are values accounting for stripWhitespace
	/**
	 * Sets the dimensions of the image for the current crop. Texture
	 * coordinates are set separately by
	 * {@link CroppableAtlasSprite#setRegion(float, float, float, float)}
	 */
	private void applyCrop() {
		// Compute new position, width, height, and origin.
		float x = getX() + getOriginX() * (1.0f - cropLeft);
		float y = getY() + getOriginY() * (1.0f - cropBottom);
		float w = (getOriginX() * cropLeft + (getWidth() - getOriginX()) * cropRight);
		float h = (getOriginY() * cropBottom + (getHeight() - getOriginY()) * cropTop);
		float ox = getOriginX() * cropLeft;
		float oy = getOriginY() * cropBottom;

		x = Math.max(originalX, x);
		y = Math.max(originalY, y);
		if (x+w > originalX + originalWidth) w = originalX + originalWidth - x;
		if (y+h > originalY + originalHeight) h = originalY + originalHeight - y;
		
		// Apply origin and bounds.
		super.setBounds(x, y, w, h);
		super.setOrigin(ox, oy);
	}

	@Override
	public void draw(Batch batch) {
		if (dirtyCrop) {
			applyCrop();
			dirtyCrop = false;
		}
		super.draw(batch);
	}

	@Override
	public float getX() {
		return originalX - region.offsetX;
	}

	@Override
	public float getY() {
		return originalY - region.offsetY;
	}

	@Override
	public float getOriginX() {
		return originalOriginX + region.offsetX;
	}

	@Override
	public float getOriginY() {
		return originalOriginY + region.offsetY;
	}

	@Override
	public float getWidth() {
		return originalWidth / region.getRotatedPackedWidth() * region.originalWidth;
	}

	@Override
	public float getHeight() {
		return originalHeight / region.getRotatedPackedHeight() * region.originalHeight;
	}

	public float getWidthRatio() {
		return originalWidth / region.getRotatedPackedWidth();
	}

	public float getHeightRatio() {
		return originalHeight / region.getRotatedPackedHeight();
	}

	public AtlasRegion getAtlasRegion() {
		return region;
	}

	public String toString() {
		return region.toString();
	}
}