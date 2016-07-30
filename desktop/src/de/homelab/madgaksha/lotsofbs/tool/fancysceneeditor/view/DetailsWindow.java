package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;

public class DetailsWindow extends Table {
	private final Label heading;
	private final Label description;
	private final ScrollPane scrollPane; 
	private TimelineProvider timelineProvider;
	public DetailsWindow (TimelineProvider timelineProvider, Skin skin) {
		super(skin);
		this.timelineProvider = timelineProvider;
		heading = new Label(StringUtils.EMPTY, skin);
		description = new Label(StringUtils.EMPTY, skin);
		scrollPane = new ScrollPane(null, skin);
		init();
	}

	private void init() {
		add(heading).fillX().center();
		row();
		add(description).fillX().center();
		row();
		add(scrollPane).expand().fill();
		
		heading.setAlignment(Align.center);
		heading.setWrap(true);
		heading.setFontScale(2f);
		description.setWrap(true);
		description.setAlignment(Align.left);
		description.setFontScale(1.5f);
		
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.SELECTED, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				final DetailsPanel panel = timeline.getSelected();
				if (panel == null) return;
				final Actor actor = panel.getActor(timelineProvider, getSkin());
				if (actor == null) return;
				heading.setText(panel.getTitle());
				description.setText(panel.getDescription());				
				scrollPane.setWidget(actor);
				
				heading.invalidateHierarchy();
				description.invalidateHierarchy();
				scrollPane.invalidateHierarchy();
			}
		});
	}	
}
