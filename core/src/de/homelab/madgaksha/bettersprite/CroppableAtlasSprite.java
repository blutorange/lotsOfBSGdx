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

package de.homelab.madgaksha.bettersprite;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.FloatTextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * A sprite that, if whitespace was stripped from the region when it was packed,
 * is automatically positioned as if whitespace had not been stripped.
 */
public class CroppableAtlasSprite extends CroppableSprite implements Poolable {
	private final AtlasRegion region = new AtlasRegion(new Texture(new FloatTextureData(1, 1)), 0, 0, 1, 1);
	private float originalOffsetX, originalOffsetY;

	public CroppableAtlasSprite(AtlasRegion region) {
		setAtlasRegion(region);
	}

	public CroppableAtlasSprite() {
		super();
		reset();
	}

	public CroppableAtlasSprite(CroppableAtlasSprite sprite) {
		set(sprite);
	}

	public CroppableAtlasSprite(CroppableSprite sprite) {
		set(sprite);
	}

	public void set(CroppableAtlasSprite sprite) {
		setAtlasRegion(sprite.region);
		super.set(sprite);
	}

	@Override
	public void set(CroppableSprite sprite) {
		region.index = -1;
		region.name = StringUtils.EMPTY;
		region.offsetX = 0.0f;
		region.offsetY = 0.0f;
		region.packedWidth = sprite.getRegionWidth();
		region.packedHeight = sprite.getRegionHeight();
		region.originalWidth = sprite.getRegionWidth();
		region.originalHeight = sprite.getRegionHeight();
		region.rotate = false;
		region.splits = null;
		region.pads = null;
		region.setRegion(sprite);
		setAtlasRegionInternals(region);
		super.set(sprite);
	}

	public void setAtlasRegion(AtlasRegion atlasRegion) {
		region.index = atlasRegion.index;
		region.name = atlasRegion.name;
		region.offsetX = atlasRegion.offsetX;
		region.offsetY = atlasRegion.offsetY;
		region.packedWidth = atlasRegion.packedWidth;
		region.packedHeight = atlasRegion.packedHeight;
		region.originalWidth = atlasRegion.originalWidth;
		region.originalHeight = atlasRegion.originalHeight;
		region.rotate = atlasRegion.rotate;
		region.splits = atlasRegion.splits;
		region.pads = atlasRegion.pads;
		region.setRegion(atlasRegion);
		setAtlasRegionInternals(atlasRegion);
	}

	private void setAtlasRegionInternals(AtlasRegion atlasRegion) {
		originalOffsetX = atlasRegion.offsetX;
		originalOffsetY = atlasRegion.offsetY;
		setRegion(atlasRegion);
		setOrigin(atlasRegion.originalWidth / 2f, atlasRegion.originalHeight / 2f);
		int width = atlasRegion.getRegionWidth();
		int height = atlasRegion.getRegionHeight();
		if (atlasRegion.rotate) {
			super.rotate90(true);
			super.setBounds(atlasRegion.offsetX, atlasRegion.offsetY, height, width);
		} else
			super.setBounds(atlasRegion.offsetX, atlasRegion.offsetY, width, height);
		setCrop(this);
	}

	@Override
	public void reset() {
		originalOffsetX = originalOffsetY = 0.0f;
		super.setBounds(0f, 0f, 0f, 0f);
		super.setColor(1f, 1f, 1f, 1f);
		super.setScale(1f);
		super.setRotation(0f);
		super.setTexture(null);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x + region.offsetX, y + region.offsetY);
	}

	// TODO check
	@Override
	public void setCenterX(float x) {
		setX(x - region.originalWidth / 2);
	}

	@Override
	public void setCenterY(float y) {
		setY(y - region.originalHeight / 2);
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
	public void setCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
		final float thisWidth = width / region.getRotatedPackedWidth() * region.originalWidth;
		final float thisHeight = height / region.getRotatedPackedHeight() * region.originalHeight;
		final float thisOriginX = originX + region.offsetX;
		final float thisOriginY = originY + region.offsetY;
		setCropAbsolute(cropLeft * thisOriginX, cropRight * (thisWidth - thisOriginX), cropBottom * thisOriginY,
				cropTop * (thisHeight - thisOriginY));
	}

	@Override
	public void setOrigin(float originX, float originY) {
		super.setOrigin(originX - region.offsetX, originY - region.offsetY);
		setCrop(this);
	}

	@Override
	public void setOriginCenter() {
		super.setOrigin(region.originalWidth / 2 - region.offsetX, region.originalWidth / 2 - region.offsetY);
		setCrop(this);
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
	 * Sets the position relative to the current origin of this sprite. For
	 * example, if the current origin is at the top-left corner, this sets the
	 * position so that the top-left corner is at (x,y).
	 * 
	 * @param x
	 *            x coordinate.
	 * @param y
	 *            y coordinate.
	 */
	public void setPositionOrigin(float x, float y) {
		setPosition(x - getOriginX(), y - getOriginY());
	}

	public void setCropAbsolute(float cropLeftAbsolute, float cropRightAbsolute, float cropBottomAbsolute,
			float cropTopAbsolute) {
		final float newCropLeft = originX <= 0f ? (cropLeftAbsolute == 0f ? 1f : 0f) : cropLeftAbsolute / originX;
		final float newCropRight = originX >= width ? 1f : cropRightAbsolute / (width - originX);
		final float newCropBottom = originY <= 0f ? (cropBottomAbsolute == 0f ? 1f : 0f) : cropBottomAbsolute / originY;
		final float newCropTop = originY >= height ? 1f : cropTopAbsolute / (height - originY);
		super.setCrop((newCropLeft > 1f) ? 1f : newCropLeft, (newCropRight > 1f) ? 1f : newCropRight,
				(newCropBottom > 1f) ? 1f : newCropBottom, (newCropTop > 1f) ? 1f : newCropTop);
	}
}