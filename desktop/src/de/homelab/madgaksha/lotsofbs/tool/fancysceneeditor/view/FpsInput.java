package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;

public class FpsInput extends NumericInput {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FpsInput.class);
	private final TimelineProvider timelineProvider;
	
	public FpsInput(TimelineProvider timelineProvider, Skin skin) {
		super(60f, skin);
		this.timelineProvider = timelineProvider;
		init();
	}

	private void init() {
		setErrorColor(Color.RED);
		setFormat("%.0f");
		setMinMax(1f, 120f);
		setQuantizer(1);
		setValue(1.0f / timelineProvider.getTimeline().getDeltaTime());
		addListener(new NumericInputListener() {
			@Override
			protected void changed(float value, Actor actor) {
				timelineProvider.getTimeline().setDeltaTime(1.0f/value);
			}
		});
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.DELTA_TIME, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				setValue(1.0f/timeline.getDeltaTime());
			}
		});
	}
	
}
