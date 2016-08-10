package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;

public class FrameSeekButton extends Button {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FrameSeekButton.class);

	private final boolean forward;
	private EventListener listener;
	
	public FrameSeekButton(Skin skin, TimelineProvider timeline, boolean forward) {
		this(skin.get(FrameSeekButtonStyle.class), timeline, forward);
	}

	public FrameSeekButton(Skin skin, String styleName, TimelineProvider timeline, boolean forward) {
		this(skin.get(styleName, FrameSeekButtonStyle.class), timeline, forward);
	}

	public FrameSeekButton(FrameSeekButtonStyle style, TimelineProvider timeline, boolean forward) {
		super(style);
		this.forward = forward;
		timeline.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(TimelineGetter getter, TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
	}

	protected void connectTimeline(TimelineGetter getter) {
		setup(getter.getTimeline());
	}

	private void setup(ModelTimeline timeline) {
		if (listener != null) removeListener(listener);
		listener = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor != null && (actor instanceof Button)) {
					final Button tb = (Button) actor;
					if (tb.isPressed()) {
						timeline.setCurrentTime(timeline.getCurrentTime()
								+ (forward ? timeline.getDeltaTime() : -timeline.getDeltaTime()));
					}
				}
			}
		};
		addListener(listener);
	}

	static public class FrameSeekButtonStyle extends ButtonStyle {
	}

}
