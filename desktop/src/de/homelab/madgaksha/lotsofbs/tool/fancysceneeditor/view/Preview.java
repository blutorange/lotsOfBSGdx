package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import java.io.Closeable;
import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EFancyScene;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;

public class Preview extends Widget implements Closeable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Preview.class);
	private final static Skin drawUtils = new Skin();
	private final static long RAND_SEED = 42L;

	private EventFancyScene scene;
	private final PreviewStyle style;
	private final TimelineProvider timelineProvider;

	private final Matrix4 oldTransform = new Matrix4();

	private final Color oldColor = new Color();
	private final Color tmpColor = new Color();

	private float posX, posY;
	
	private float barLeft, barRight, barBottom, barTop;
	private float dx, dy, sxy;

	public Preview(TimelineProvider timelineProvider, Skin skin) {
		this(timelineProvider, skin.get(PreviewStyle.class));
	}

	public Preview(TimelineProvider timelineProvider, PreviewStyle style) {
		this.style = new PreviewStyle(style);
		this.timelineProvider = timelineProvider;
		initialize();
	}

	private void initialize() {
		scene = ResourceCache.getEventFancyScene(EFancyScene.OUKA_MUSOUGEKI_BIN);
		MathUtils.random.setSeed(42L);
		scene.begin();
		scene.update(0.0f, false);
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.SEEK, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				final float timelineTime = timeline.getCurrentTime();
				float sceneTime = scene.getStateTime();
				if (timelineTime < sceneTime) {
					scene.end();
					MathUtils.random.setSeed(RAND_SEED);
					scene.begin();
					scene.update(0.0f, false);
					sceneTime = scene.getStateTime();
				}
				final float deltaTime = timeline.getDeltaTime();
				for (; sceneTime + deltaTime <= timelineTime; sceneTime += deltaTime) {
					scene.update(deltaTime, false);
				}
			}
		});
	}

	public void setStyle(PreviewStyle style) {
		this.style.set(style);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
	
		// Save batch settings before modifying them.
		oldTransform.set(batch.getTransformMatrix());
		oldColor.set(batch.getColor());

		// Apply transform so that we can draw at coordinates
		// from (-4.0,-4.5) to (+4.0, +4.5).
		batch.setTransformMatrix(batch.getTransformMatrix().translate(dx, dy, 0f).scale(sxy, sxy, 1f));

		// Draw the scene itself.
		if (scene != null)
			scene.render(batch);

		// Draw bars outside the visible area of the scene.
		batch.setColor(tmpColor.set(oldColor).mul(style.overscanColor));
		style.overscanArea.draw(batch, -4f - barLeft, -4.5f - barBottom, barLeft, 9f + barBottom + barTop);
		style.overscanArea.draw(batch, 4f, -4.5f - barBottom, barRight, 9f + barBottom + barTop);
		style.overscanArea.draw(batch, -4f, -4.5f - barBottom, 8f, barBottom);
		style.overscanArea.draw(batch, -4f, 4.5f, 8f, barTop);

		// Restore batch settings
		batch.setColor(oldColor);
		batch.setTransformMatrix(oldTransform);
	}

	@Override
	public void layout() {
		// Compute coordinate transformation for the batch s
		// so that we can draw at coordinates (-4,-4.5) to (4,4.5).	
		final float width = getWidth();
		final float height = getHeight();
	
		posX = getX();
		posY = getY();
		
		barLeft = barRight = width;
		barTop = barBottom = height;

		if (width < 1f || height < 1f)
			return;

		dx = 0.5f * width + posX;
		dy = 0.5f * height + posY ;

		if (9 * width > 8 * height)
			sxy = height / 9f;
		else
			sxy = width / 8f;
	}

	@Override
	public void close() throws IOException {
		if (scene != null)
			scene.end();
	}

	public static class PreviewStyle {
		public Drawable overscanArea;
		public Color overscanColor;

		public static PreviewStyle getDefault() {
			return new PreviewStyle(null);
		}

		/** Exists only for deserialization from skin files. */
		@SuppressWarnings("unused")
		private PreviewStyle() {
		}

		public PreviewStyle(Drawable overscanArea, Color overscanColor) {
			set(overscanArea, overscanColor);
		}

		public PreviewStyle(PreviewStyle style) {
			set(style);
		}

		public final void set(PreviewStyle style) {
			set(style != null ? style.overscanArea : null, style != null ? style.overscanColor : null);
		}

		public final void set(Drawable overscanArea, Color overscanColor) {
			if (overscanArea != null)
				this.overscanArea = drawUtils.newDrawable(overscanArea);
			if (overscanColor != null)
				this.overscanColor = new Color(overscanColor);
			if (this.overscanArea == null)
				this.overscanArea = new DrawableMock();
			if (this.overscanColor == null)
				this.overscanColor = new Color(Color.WHITE);
		}
	}
	
	@Override
	public void validate() {
		if (posX != getX() || posY != getY()) invalidate();
		super.validate();
	}
	
	@Override public float getPrefWidth() {return 10f;}
	@Override public float getPrefHeight() {return 10f;}
}
