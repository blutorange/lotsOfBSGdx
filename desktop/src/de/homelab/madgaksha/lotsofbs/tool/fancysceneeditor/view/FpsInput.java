package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;

public class FpsInput extends NumericInput {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FpsInput.class);
	private EventListener listener;
	
	public FpsInput(TimelineProvider timelineProvider, Skin skin) {
		super(60f, skin);
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(TimelineGetter getter, TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
		init();
	}

	private void connectTimeline(TimelineGetter getter) {
		getter.getTimeline().registerChangeListener(TimelineChangeType.DELTA_TIME, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				setValue(1.0f/timeline.getDeltaTime());
			}
		});
		setup(getter.getTimeline());
	}
	
	private void setup(ModelTimeline timeline) {
		if (listener != null) removeListener(listener);
		listener = new NumericInputListener() {
			@Override
			protected void changed(float value, Actor actor) {
				timeline.setDeltaTime(1.0f/value);
			}
		};
		addListener(listener);
		setValue(1.0f/timeline.getDeltaTime());

	}

	private void init() {
		setErrorColor(Color.RED);
		setFormat("%.0f");
		setMinMax(1f, 120f);
		setStep(1);
		setValue(60f);
	}	
}
