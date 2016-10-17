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
import de.homelab.madgaksha.scene2dext.view.NumericInput.FloatNumericInput;

public class FpsInput extends FloatNumericInput {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FpsInput.class);
	private EventListener listener;

	public FpsInput(final TimelineProvider timelineProvider, final Skin skin) {
		super(60f, skin);
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(final TimelineGetter getter, final TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
		init();
	}

	private void connectTimeline(final TimelineGetter getter) {
		getter.getTimeline().registerChangeListener(TimelineChangeType.DELTA_TIME, new TimelineChangeListener() {
			@Override
			public void handle(final ModelTimeline timeline, final TimelineChangeType type) {
				setValue(1.0f/timeline.getDeltaTime());
			}
		});
		setup(getter.getTimeline());
	}

	private void setup(final ModelTimeline timeline) {
		if (listener != null) {
			removeListener(listener);
		}
		listener = new NumericInputListener<Float>() {
			@Override
			protected void changed(final Float value, final Actor actor) {
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
		setStep(1f);
		setValue(60f);
	}
}
