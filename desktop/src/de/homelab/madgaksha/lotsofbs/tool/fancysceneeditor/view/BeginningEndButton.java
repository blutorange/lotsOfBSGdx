package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;

public class BeginningEndButton extends Button {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BeginningEndButton.class);
	
	private final TimelineProvider timelineProvider;
	private final boolean beginning;
	
	public BeginningEndButton (Skin skin, TimelineProvider timeline, boolean beginning) {
		this(skin.get(BeginningEndButtonStyle.class), timeline, beginning);
	}
	
	public BeginningEndButton(Skin skin, String styleName, TimelineProvider timeline, boolean beginning) {
		this(skin.get(styleName, BeginningEndButtonStyle.class), timeline, beginning);
	}
	
	public BeginningEndButton (BeginningEndButtonStyle style, TimelineProvider timeline, boolean beginning) {
		super(style);
		this.timelineProvider = timeline;
		this.beginning = beginning;
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
							tl.setCurrentTime(beginning ? 0f : tl.getEndTime());
					}
				}
			}
		});
	}

	public static class BeginningEndButtonStyle extends ButtonStyle {
		
	}
	
}
