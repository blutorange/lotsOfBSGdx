package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;

public class FrameSeekButton extends Button {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FrameSeekButton.class);
	
	private final TimelineProvider timelineProvider;
	private final boolean forward;
	
	public FrameSeekButton (Skin skin, TimelineProvider timeline, boolean forward) {
		this(skin.get(FrameSeekButtonStyle.class), timeline, forward);
	}
	
	public FrameSeekButton (Skin skin, String styleName, TimelineProvider timeline, boolean forward) {
		this(skin.get(styleName, FrameSeekButtonStyle.class), timeline, forward);
	}

	public FrameSeekButton (FrameSeekButtonStyle style, TimelineProvider timeline, boolean forward) {
		super(style);
		this.timelineProvider = timeline;
		this.forward = forward;
		initialize();
	}

	
	private void initialize() {
		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor != null && (actor instanceof Button)) {
					final Button tb = (Button)actor;
					if (tb.isPressed() && timelineProvider != null) {
							final ModelTimeline tl = timelineProvider.getTimeline();
							tl.setCurrentTime(tl.getCurrentTime() + (forward ? tl.getDeltaTime() : - tl.getDeltaTime()));
					}
				}
			}
		});
	}
	
	static public class FrameSeekButtonStyle extends ButtonStyle {
		
	}
	
}
