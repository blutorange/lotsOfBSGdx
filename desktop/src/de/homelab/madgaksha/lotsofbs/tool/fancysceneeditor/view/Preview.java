package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import java.io.Closeable;
import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.RenderableTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;

public class Preview extends Widget implements Closeable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Preview.class);

	private final Rectangle rectangle = new Rectangle();
	private RenderableTimeline renderableTimeline = null;
	private float posX, posY;

	public Preview(TimelineProvider timelineProvider) {
		super();
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED,
				new TimelineProviderChangeListener() {
					@Override
					public void handle(TimelineGetter getter, TimelineProviderChangeType type) {
						connectTimeline(getter);
					}
				});
	}

	private void connectTimeline(TimelineGetter getter) {
		final ModelTimeline timeline = getter.getTimeline();
		renderableTimeline = (timeline instanceof RenderableTimeline) ? (RenderableTimeline)timeline : null;		
		if (renderableTimeline != null) renderableTimeline.setDrawAtCurrentTime(true);		
		layout();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (renderableTimeline != null) renderableTimeline.draw(batch);		
	}

	@Override
	public void layout() {
		super.layout();
		posX = getX();
		posY = getY();
		rectangle.set(posX, posY, getWidth(), getHeight());
		renderableTimeline.layout(rectangle);
	}

	@Override
	public void close() throws IOException {
		renderableTimeline.close();
	}

	@Override
	public void validate() {
		if (posX != getX() || posY != getY())
			invalidate();
		super.validate();
	}

	@Override
	public float getPrefWidth() {
		return 10f;
	}

	@Override
	public float getPrefHeight() {
		return 10f;
	}
}
