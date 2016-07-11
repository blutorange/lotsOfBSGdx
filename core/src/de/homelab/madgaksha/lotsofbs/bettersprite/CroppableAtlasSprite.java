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

package de.homelab.madgaksha.lotsofbs.bettersprite;

import java.nio.channels.UnsupportedAddressTypeException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.FloatTextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * A sprite that, if whitespace was stripped from the region when it was packed,
 * is automatically positioned as if whitespace had not been stripped.
 */
public class CroppableAtlasSprite extends CroppableSprite implements Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CroppableAtlasSprite.class);
	private final static AtlasRegion DEFAULT_REGION = new AtlasRegion(new Texture(new FloatTextureData(1, 1)), 0, 0, 1, 1);
	private AtlasRegion region = DEFAULT_REGION;
	private float currentOffsetX, currentOffsetY;

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

	public void setAtlasRegion(AtlasRegion atlasRegion) throws UnsupportedOperationException {
		region = atlasRegion;
		currentOffsetX = atlasRegion.offsetX;
		currentOffsetY = atlasRegion.offsetY;
		setRegion(atlasRegion);
		int width = atlasRegion.getRegionWidth();
		int height = atlasRegion.getRegionHeight();
		if (atlasRegion.rotate) {
			throw new UnsupportedOperationException();
		}
		super.setBounds(currentOffsetX, currentOffsetY, width, height);
		if (cropped) {
			setOrigin(atlasRegion.originalWidth / 2f, atlasRegion.originalHeight / 2f);
		}
		else {
			super.setOrigin(atlasRegion.originalWidth / 2f - currentOffsetX, atlasRegion.originalHeight / 2f - currentOffsetY);
		}
	}

	@Override
	public void reset() {
		currentOffsetX = currentOffsetY = 0.0f;
		cropLeft = cropRight = cropBottom = cropTop = 1f;
		region = DEFAULT_REGION;
		super.setBounds(0f, 0f, 0f, 0f);
		super.setColor(1f, 1f, 1f, 1f);
		super.setScale(1f);
		super.setRotation(0f);
		super.setTexture(region.getTexture());
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x + currentOffsetX, y + currentOffsetY);
	}

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
		super.setX(x + currentOffsetX);
	}

	@Override
	public void setY(float y) {
		super.setY(y + currentOffsetY);
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		float widthRatio = width / region.originalWidth;
		float heightRatio = height / region.originalHeight;
		currentOffsetX = region.offsetX * widthRatio;
		currentOffsetY = region.offsetY * heightRatio;
		super.setBounds(x + currentOffsetX, y + currentOffsetY, region.packedWidth * widthRatio, region.packedHeight * heightRatio);
	}

	@Override
	public void setSize(float width, float height) {
		setBounds(getX(), getY(), width, height);
	}

	@Override
	public void setCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
		final float thisWidth = width / region.packedWidth * region.originalWidth;
		final float thisHeight = height / region.packedHeight * region.originalHeight;
		final float thisOriginX = originX + currentOffsetX;
		final float thisOriginY = originY + currentOffsetY;
		setCropAbsolute(cropLeft * thisOriginX, cropRight * (thisWidth - thisOriginX), cropBottom * thisOriginY,
				cropTop * (thisHeight - thisOriginY));
	}

	@Override
	public void setOrigin(float originX, float originY) {
		super.setOrigin(originX - currentOffsetX, originY - currentOffsetY);
		setCrop(cropLeft, cropRight, cropBottom, cropTop);
	}

	@Override
	public void setOriginCenter() {
		super.setOrigin(region.originalWidth / 2 - currentOffsetX, region.originalWidth / 2 - currentOffsetY);
		setCrop(cropLeft, cropRight, cropBottom, cropTop);
	}

	@Override
	@Deprecated
	public void flip(boolean x, boolean y) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void rotate90(boolean clockwise) throws UnsupportedOperationException {
		throw new UnsupportedAddressTypeException();
	}

	@Override
	public float getX() {
		return super.getX() - currentOffsetX;
	}

	@Override
	public float getY() {
		return super.getY() - currentOffsetY;
	}

	@Override
	public float getOriginX() {
		return super.getOriginX() + currentOffsetX;
	}

	@Override
	public float getOriginY() {
		return super.getOriginY() + currentOffsetY;
	}

	@Override
	public float getWidth() {
		return super.getWidth() / region.packedWidth * region.originalWidth;
	}

	@Override
	public float getHeight() {
		return super.getHeight() / region.packedHeight * region.originalHeight;
	}

	public float getWidthRatio() {
		return super.getWidth() / region.packedWidth;
	}

	public float getHeightRatio() {
		return super.getHeight() / region.packedHeight;
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
	
	@Override
	public boolean isOrigin(Vector2 origin) {
		return originX + currentOffsetX == origin.x && originY + currentOffsetY== origin.y;
	}
	
	@Override
	public boolean isOriginRelative(Vector2 origin) {
		return originX + currentOffsetX == origin.x*region.originalWidth && originY + currentOffsetY== origin.y*region.originalHeight;
	}

	public boolean isAtlasRegionEqualTo(AtlasRegion region) {
		return this.region == region;
	}
}