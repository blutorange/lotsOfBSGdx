package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;

public class DetailsWindow extends Table {
	private TimelineProvider timelineProvider;
	public DetailsWindow (TimelineProvider timelineProvider, Skin skin) {
		super(skin);
		this.timelineProvider = timelineProvider;
		init();
	}

	private void init() {
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.SELECTED, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				final DetailsPanel panel = timeline.getSelected();
				if (panel == null) return;
				final Actor actor = panel.getActor(timelineProvider, getSkin());
				if (actor == null) return;
				clearChildren();
				add(actor).expand().fill();
			}
		});
	}	
}
