package de.homelab.madgaksha.lotsofbs.bettersprite.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

public abstract class ADrawable<RESOURCE, LOADED> implements Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ADrawable.class);

	private final Vector2 position = new Vector2();
	private final Vector2 origin = new Vector2();
	private final Vector2 scale = new Vector2();
	private final Vector2 cropX = new Vector2();
	private final Vector2 cropY = new Vector2();
	private final Color color = new Color(Color.WHITE);
	private float opacity = 1.0f;
	private float rotation = 0.0f; // degrees
	protected LOADED drawable = null;
	private boolean disposed = true; // must be true
	private boolean managed = false;

	public ADrawable() {
		reset();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + position + (disposed ? "Disposed" : "Loaded");
	}

	@Override
	public final void reset() {
		position.set(0f, 0f);
		origin.set(0.5f, 0.5f);
		scale.set(1f, 1f);
		cropX.set(1f, 1f);
		cropY.set(1f, 1f);
		color.set(Color.WHITE);
		opacity = 1.0f;
		rotation = 0.0f;
		if (!disposed && managed)
			dispose();
		drawable = null;
		disposed = true;
		managed = false;
	}

	public final boolean setDrawable(final RESOURCE resource, final float pixelPerUnit) {
		return managed = setLoadedDrawable(loadResource(resource), pixelPerUnit);
	}

	public final boolean setLoadedDrawable(final LOADED loaded, final float pixelPerUnit) {
		drawable = loaded;
		if (drawable == null) {
			disposed = true;
			reset();
			return false;
		}
		disposed = false;
		managed = false;
		initResource(1.0f/pixelPerUnit);
		applyAll();
		return true;
	}

	/**
	 *
	 * Sets the position relative to the {@link #origin}.
	 *
	 * @param position
	 *            relative to the origin.
	 */
	public final void setPosition(final Vector2 position) {
		if (disposed)
			return;
		this.position.set(position);
		applyPosition(position.x, position.y);
	}

	/**
	 * Sets the position relative to the {@link #origin}.
	 *
	 * @param x
	 *            position coordinate.
	 * @param y
	 *            position coordinate.
	 */
	public final void setPosition(final float x, final float y) {
		if (disposed)
			return;
		this.position.set(x, y);
		applyPosition(x, y);
	}

	/**
	 *
	 * Sets the origin relative to which {@link #rotation} and {@link #position}
	 * are specified.
	 *
	 * @param position
	 */
	public final void setOrigin(final Vector2 origin) {
		if (disposed)
			return;
		this.origin.set(origin);
		applyOrigin(origin.x, origin.y);
	}

	/**
	 * Sets the origin relative to which {@link #rotation} and {@link #position}
	 * are specified.
	 *
	 * @param originX
	 *            origin coordinate.
	 * @param originY
	 *            origin coordinate.
	 */
	public final void setOrigin(final float originX, final float originY) {
		if (disposed)
			return;
		this.origin.set(originX, originY);
		applyOrigin(origin.x, origin.y);
	}

	/**
	 *
	 * Sets the scale relative to the {@link ADrawable#origin}.
	 *
	 * @param scale
	 *            scaleX and scaleY.
	 */
	public final void setScale(final Vector2 scale) {
		if (disposed)
			return;
		this.scale.set(scale);
		applyScale(scale.x, scale.y);
	}

	public final void setScaleLerp(final Vector2 scaleStart, final Vector2 scaleEnd, final float alpha) {
		if (disposed)
			return;
		scale.set(scaleStart).lerp(scaleEnd, alpha);
		applyScale(scale.x, scale.y);
	}

	/**
	 * Sets the scale relative to the {@link ADrawable#origin}.
	 *
	 * @param scaleXY
	 *            Scale in both x and y direction.
	 */
	public final void setScale(final float scaleXY) {
		if (disposed)
			return;
		this.scale.set(scaleXY, scaleXY);
		applyScale(scale.x, scale.y);
	}

	/**
	 * Sets the scale relative to the {@link ADrawable#origin}.
	 *
	 * @param scaleX
	 *            Scale factor.
	 * @param scaleY
	 *            Scale factor.
	 */
	public final void setScale(final float scaleX, final float scaleY) {
		if (disposed)
			return;
		this.scale.set(scaleX, scaleY);
		applyScale(scaleX, scaleY);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 *
	 * @param cropLeft
	 * @param cropRight
	 * @param cropBottom
	 * @param cropTop
	 */
	public final void setCrop(final float cropLeft, final float cropRight, final float cropBottom, final float cropTop) {
		if (disposed)
			return;
		this.cropX.set(cropLeft, cropRight);
		this.cropY.set(cropBottom, cropTop);
		applyCrop(cropLeft, cropRight, cropBottom, cropTop);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 *
	 * @param cropLeft
	 * @param cropRight
	 * @param cropBottom
	 * @param cropTop
	 */
	public final void setCrop(final Vector2 cropX, final Vector2 cropY) {
		if (disposed)
			return;
		this.cropX.set(cropX);
		this.cropY.set(cropY);
		applyCrop(cropX.x, cropX.y, cropY.x, cropY.y);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 *
	 * @param cropX
	 */
	public final void setCropX(final Vector2 cropX) {
		if (disposed)
			return;
		this.cropX.set(cropX);
		applyCrop(cropX.x, cropX.y, cropY.x, cropY.y);
	}

	public final void setCropXLerp(final Vector2 cropXStart, final Vector2 cropXEnd, final float alpha) {
		if (disposed)
			return;
		cropX.set(cropXStart).lerp(cropXEnd, alpha);
		applyCrop(cropX.x, cropX.y, cropY.x, cropY.y);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 *
	 * @param cropY
	 */
	public final void setCropY(final Vector2 cropY) {
		if (disposed)
			return;
		this.cropY.set(cropY);
		applyCrop(cropX.x, cropX.y, cropY.x, cropY.y);
	}

	public final void setCropYLerp(final Vector2 cropYStart, final Vector2 cropYEnd, final float alpha) {
		if (disposed)
			return;
		this.cropY.set(cropYStart).lerp(cropYEnd, alpha);
		applyCrop(cropX.x, cropX.y, cropY.x, cropY.y);
	}

	public final void setOpacity(final float opacity) {
		if (disposed)
			return;
		this.opacity = opacity;
		applyOpacity(opacity);
	}

	public final void setColor(final Color color) {
		if (disposed)
			return;
		this.color.set(color);
		this.color.a = opacity;
		applyColor(color.r, color.g, color.b, opacity);
	}

	public final void setColor(final float r, final float g, final float b) {
		if (disposed)
			return;
		this.color.set(r, g, b, opacity);
		applyColor(color.r, color.g, color.b, opacity);
	}

	public final void setRotation(final float rotation) {
		if (disposed)
			return;
		this.rotation = rotation;
		applyRotation(rotation);
	}

	protected final void applyAll() {
		if (disposed)
			return;
		applyColor(color.r, color.g, color.b, opacity);
		applyOpacity(opacity);
		applyOrigin(origin.x, origin.y);
		applyScale(scale.x, scale.y);
		applyPosition(position.x, position.y);
		applyCrop(cropX.x, cropX.y, cropY.x, cropY.y);
		applyRotation(rotation);
	}

	public final void dispose() {
		if (managed && !disposed) performDispose();
		disposed = true;
		managed = false;
		drawable = null;
	}

	/**
	 * Updates the state of this drawable. Used for example by animations.
	 * @param deltaTime Time since the last frame in seconds.
	 * @param passedTime Total time since the beginning in seconds.
	 * @return True iff this drawable is finished (eg. animations) or could not be updated.
	 */
	public final boolean update(final float deltaTime, final float passedTime) {
		if (disposed)
			return false;
		return performUpdate(deltaTime, passedTime);
	}

	/**
	 * Assumes the batch has been setup with a viewport that supports
	 * a virtual screen size.
	 * @param batch Batch to be used for drawing.
	 */
	public final void render(final Batch batch) {
		if (disposed)
			return;
		performRender(batch);
	}

	public final void getPosition(final Vector2 vector) {
		vector.set(position);
	}

	public final void getCropX(final Vector2 vector) {
		vector.set(cropX);
	}

	public final void getCropY(final Vector2 vector) {
		vector.set(cropY);
	}

	public final void getScale(final Vector2 vector) {
		vector.set(scale);
	}

	public final void getOrigin(final Vector2 vector) {
		vector.set(origin);
	}

	public final void getColor(final Color color) {
		color.set(this.color);
	}

	public final float getColorR() {
		return color.r;
	}

	public final float getColorG() {
		return color.g;
	}

	public final float getColorB() {
		return color.b;
	}

	public final float getColorA() {
		return color.a;
	}

	public final boolean isColorSet() {
		return color.toIntBits() != Color.WHITE.toIntBits();
	}

	/**
	 * @see Color#toFloatBits()
	 * @return Current color.
	 */
	public final float getColorAsFloat() {
		return color.toFloatBits();
	}

	/**
	 * @see Color#toIntBits()
	 * @return Current color.
	 */
	public final int getColorAsInt() {
		return color.toIntBits();
	}

	public final float getOpacity() {
		return opacity;
	}

	public final float getRotation() {
		return rotation;
	}

	public final float getPositionX() {
		return position.x;
	}

	public final float getPositionY() {
		return position.y;
	}

	public final float getScaleX() {
		return scale.x;
	}

	public final float getScaleY() {
		return scale.y;
	}

	public final float getOriginX() {
		return origin.x;
	}

	public final float getOriginY() {
		return origin.y;
	}

	/**
	 * Called when this drawable gets updated.
	 *
	 * @param deltaTime
	 * @param passedTime
	 * @return Whether this drawable is finished. Will always return false for
	 *         {@link ENinePatch}es and {@link ETexture}s, but not for
	 *         necessarily for {@link EAnimation}s or {@link EParticleEffect}s.
	 */
	protected abstract boolean performUpdate(float deltaTime, float passedTime);

	protected abstract void performRender(Batch batch);

	/**
	 * Called when this drawable is not needed any longer. Resources loaded via
	 * the {@link ResourcePool} should be released.
	 */
	protected abstract void performDispose();

	protected abstract void applyPosition(float positionX, float positionY);

	protected abstract void applyOrigin(float originX, float originY);

	protected abstract void applyRotation(float degree);

	protected abstract void applyScale(float scaleX, float scaleY);

	protected abstract void applyCrop(float cropLeft, float cropRight, float cropBottom, float cropTop);

	protected abstract void applyColor(float r, float g, float b, float a);

	protected abstract void applyOpacity(float opacity);

	/**
	 * Loads the resource for this drawable.
	 *
	 * @param resource
	 *            Resource to be loaded.
	 * @return The resource, or null if it could not be loaded.
	 */
	protected abstract LOADED loadResource(RESOURCE resource);
	protected abstract void initResource(float unitPerPixel);
	public abstract ADrawable<RESOURCE, LOADED> newObject();
}