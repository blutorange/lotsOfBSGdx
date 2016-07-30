package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;

public class FrameCountInput extends NumericInput {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FrameCountInput.class);
	private final TimelineProvider timelineProvider;
	private boolean setTimeDisabled = false;
	
	public FrameCountInput(TimelineProvider timelineProvider, Skin skin) {
		super(0f, skin);
		this.timelineProvider = timelineProvider;
		init();
	}

	private void init() {
		setErrorColor(Color.RED);
		setFormat("%.0f");
		setMinMax(0f, (int)(timelineProvider.getTimeline().getEndTime() / timelineProvider.getTimeline().getDeltaTime()));
		setQuantizer(1);
		addListener(new NumericInputListener() {
			@Override
			protected void changed(float frameCount, Actor actor) {
				if (!setTimeDisabled) timelineProvider.getTimeline().setCurrentTime(timelineProvider.getTimeline().getDeltaTime()*frameCount);
			}
		});
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.SEEK, currentFrameListener);
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.DELTA_TIME, currentFrameListener);
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.START_TIME, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				setTimeDisabled = true;
				setMin(timeline.getStartTime());
				setTimeDisabled = false;
			}
		});
		timelineProvider.getTimeline().registerChangeListener(TimelineChangeType.END_TIME, new TimelineChangeListener() {
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				setTimeDisabled = true;
				setMax(timeline.getEndTime());
				setTimeDisabled = false;
			}
		});
	}
	private final TimelineChangeListener currentFrameListener = new TimelineChangeListener() {
		@Override
		public void handle(ModelTimeline timeline, TimelineChangeType type) {
			final int frame = (int)(timeline.getCurrentTime() / timeline.getDeltaTime());
			setTimeDisabled = true;
			setValue(frame);
			setTimeDisabled = false;
		}
	};
}
