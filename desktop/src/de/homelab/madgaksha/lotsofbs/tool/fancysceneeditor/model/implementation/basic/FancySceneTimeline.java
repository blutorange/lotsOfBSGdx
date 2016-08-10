package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.basic;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InsufficientResourcesException;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.homelab.madgaksha.lotsofbs.Game;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.IdProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.RenderableTimeline;
import de.homelab.madgaksha.scene2dext.drawable.DrawableMock;

public class FancySceneTimeline extends BasicTimeline implements RenderableTimeline, IdProvider {
	private final static Logger LOG = Logger.getLogger(FancySceneTimeline.class);
	
	private final static long RAND_SEED = 42L;
	private final static Skin drawUtils = new Skin();

	private final List<AFancyEvent> eventList = new ArrayList<AFancyEvent>();
	private FileHandle defaultFileHandle = new FileHandle(StringUtils.EMPTY);
	private boolean dirty = true;
	private EventFancyScene scene;
	private final Matrix4 oldTransform = new Matrix4();
	private final Color oldColor = new Color();
	private final Color tmpColor = new Color();
	private boolean drawAtCurrentTime = false;

	private float barLeft, barRight, barBottom, barTop;
	private float dx, dy, sxy;

	private final StringBuilder stringBuilder = new StringBuilder(2000);
	
	private FancySceneTimelineStyle style;

	FancySceneTimeline(FancySceneTimelineStyle style) {
		super();
		this.style = new FancySceneTimelineStyle(style);
		
		registerChangeListener(ClipChangeType.ALL, new ClipChangeListener() {
			@Override
			public void handle(ModelClip clip, ClipChangeType type) {
				dirty = true;
			}
		});		
	}

	private void rebuildScene() {
		dirty = false;
		stringBuilder.setLength(0);
		resetIds();
		for (final ModelTrack track : this) {
			for (final ModelClip clip : track) {
				final ModelClipData clipData = clip.getClipData();
				try {
					clipData.renderEvent(stringBuilder, this);
					stringBuilder.append(StringUtils.LF);
				} catch (InsufficientResourcesException e) {
					LOG.debug("failed to render clip data:" + clipData, e);
				}
			}
		}
		eventList.clear();
		if (!EventFancyScene.readEventList(defaultFileHandle, eventList, stringBuilder.toString())) return;
		if (scene != null) {
			scene.end();
			scene.reset();
		}
		final EventFancyScene newScene = new EventFancyScene(eventList);
		MathUtils.random.setSeed(42L);
		newScene.begin();
		newScene.update(0.0f, false);
		scene = newScene;
		seekInternal(getCurrentTime());
		LOG.debug(stringBuilder.toString());
	}

	@Override
	public void seek(float time) {
		setDrawAtCurrentTime(false);
		seekInternal(time);
	}
	
	private void seekInternal(float timelineTime) {
		float sceneTime = scene.getStateTime();
		if (timelineTime < sceneTime) {
			scene.end();
			MathUtils.random.setSeed(RAND_SEED);
			scene.begin();
			scene.update(0.0f, false);
			sceneTime = scene.getStateTime();
		}
		final float deltaTime = getDeltaTime();
		for (; sceneTime + deltaTime <= timelineTime; sceneTime += deltaTime) {
			scene.update(deltaTime, false);
		}
	}

	@Override
	public void draw(Batch batch) {
		if (dirty) rebuildScene();
		
		if (drawAtCurrentTime)
			seekInternal(getCurrentTime());
		
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
	public void setDrawAtCurrentTime(boolean drawAtCurrentTime) {
		this.drawAtCurrentTime = drawAtCurrentTime;
	}

	// Compute coordinate transformation for the batch s
	// so that we can draw at coordinates (-4,-4.5) to (4,4.5).
	@Override
	public void layout(Rectangle area) {
		barLeft = barRight = area.width;
		barTop = barBottom = area.height;

		dx = 0.5f * area.width + area.x;
		dy = 0.5f * area.height + area.y;

		if (area.width < 1f || area.height < 1f)
			return;

		if (Game.VIEWPORT_GAME_AR_DEN * area.width > Game.VIEWPORT_GAME_AR_NUM * area.height)
			sxy = area.height / Game.VIEWPORT_GAME_AR_DEN;
		else
			sxy = area.width / Game.VIEWPORT_GAME_AR_NUM;
	}

	@Override
	public void close() {
		if (scene != null) scene.end();
	}
	
	/**
	 * @param duration
	 *            Duration of the timeline in seconds.
	 * @param skin
	 *            The skin to be used for drawing this timeline.
	 * @return Returns a timeline starting a 0 and ending at duration. The
	 *         current position is set to the start time.
	 */
	public static FancySceneTimeline newTimeline(float duration, Skin skin, String styleName) {
		final FancySceneTimelineStyle style = styleName == null ? skin.get(FancySceneTimelineStyle.class)
				: skin.get(styleName, FancySceneTimelineStyle.class);
		final FancySceneTimeline timeline = new FancySceneTimeline(style);
		timeline.setStartTime(0f);
		timeline.setEndTime(duration);
		timeline.setCurrentTime(0f);
		return timeline;
	}

	public void setStyle(FancySceneTimelineStyle style) {
		this.style.set(style);
	}

	public static class FancySceneTimelineStyle {
		public Drawable overscanArea;
		public Color overscanColor;

		public static FancySceneTimelineStyle getDefault() {
			return new FancySceneTimelineStyle(null);
		}

		/** Exists only for deserialization from skin files. */
		@SuppressWarnings("unused")
		private FancySceneTimelineStyle() {
		}

		public FancySceneTimelineStyle(Drawable overscanArea, Color overscanColor) {
			set(overscanArea, overscanColor);
		}

		public FancySceneTimelineStyle(FancySceneTimelineStyle style) {
			set(style);
		}

		public final void set(FancySceneTimelineStyle style) {
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

	private int currentId = 0;
	
	@Override
	public String uniqueId() throws InsufficientResourcesException {
		return "i" + Integer.toString(currentId++, Character.MAX_RADIX);
	}

	private void resetIds() {
		currentId = 0;
	}	
}
