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

public class FrameCountInput extends NumericInput {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FrameCountInput.class);
	private boolean setTimeDisabled = false;
	private EventListener listener;
	
	public FrameCountInput(TimelineProvider timelineProvider, Skin skin) {
		super(0f, skin);
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(TimelineGetter getter, TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
		init();
	}

	private void connectTimeline(TimelineGetter getter) {
		getter.getTimeline().setData("fci", this);
		getter.getTimeline().registerChangeListener(TimelineChangeType.SEEK, currentFrameListener);
		getter.getTimeline().registerChangeListener(TimelineChangeType.DELTA_TIME, currentFrameListener);
		getter.getTimeline().registerChangeListener(TimelineChangeType.START_TIME, startTimeListener);
		getter.getTimeline().registerChangeListener(TimelineChangeType.END_TIME, endTimeListener);
		setup(getter.getTimeline());
	}
	
	private void setup(ModelTimeline timeline) {
		if (listener != null) removeListener(listener);
		listener = new NumericInputListener() {
			@Override
			protected void changed(float frameCount, Actor actor) {
				if (!setTimeDisabled) timeline.setCurrentTime(timeline.getDeltaTime()*frameCount);
			}
		};
		addListener(listener);
		setMinMax(0f, (int)(timeline.getEndTime() / timeline.getDeltaTime()));
	}
	
	private void init() {
		setErrorColor(Color.RED);
		setFormat("%.0f");
		setStep(1);
		setMinMax(0f, 1f);
		setValue(0f);
	}
	
	private static final TimelineChangeListener currentFrameListener = new TimelineChangeListener() {
		@Override
		public void handle(ModelTimeline timeline, TimelineChangeType type) {
			final FrameCountInput fci = timeline.getData("fci", FrameCountInput.class);
			final int frame = (int)(timeline.getCurrentTime() / timeline.getDeltaTime());
			fci.setTimeDisabled = true;
			fci.setValue(frame);
			fci.setTimeDisabled = false;
		}
	};
	private static final TimelineChangeListener startTimeListener = new TimelineChangeListener() {
		@Override
		public void handle(ModelTimeline timeline, TimelineChangeType type) {
			final FrameCountInput fci = timeline.getData("fci", FrameCountInput.class);
			fci.setTimeDisabled = true;
			fci.setMin(timeline.getStartTime());
			fci.setTimeDisabled = false;
		}
	};
	
	private static final TimelineChangeListener endTimeListener = new TimelineChangeListener() {
		@Override
		public void handle(ModelTimeline timeline, TimelineChangeType type) {
			final FrameCountInput fci = timeline.getData("fci", FrameCountInput.class);
			fci.setTimeDisabled = true;
			fci.setMax(timeline.getEndTime());
			fci.setTimeDisabled = false;
		}
	};
}
