package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;

public class PlayPauseButton extends Button {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PlayPauseButton.class);

	private ModelTimeline timeline = null;
	private ButtonStyle play, pause;
	private boolean playing = false;

	public PlayPauseButton(Skin skin, TimelineProvider timeline) {
		this(skin.get(ButtonStyle.class), skin.get(ButtonStyle.class), timeline);
	}

	public PlayPauseButton(Skin skin, String styleNamePlay, String styleNamePause, TimelineProvider timeline) {
		this(skin.get(styleNamePlay, ButtonStyle.class), skin.get(styleNamePause, ButtonStyle.class), timeline);
	}

	public PlayPauseButton(ButtonStyle play, ButtonStyle pause, TimelineProvider timelineProvider) {
		super(pause);
		this.play = play;
		this.pause = pause;
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(TimelineGetter getter, TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
		init();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (playing && timeline != null) {
			timeline.setCurrentTime(timeline.getCurrentTime() + Gdx.graphics.getRawDeltaTime());
			if (timeline.getCurrentTime() >= timeline.getEndTime())
				setPlaying(false);
		}
	}

	protected void connectTimeline(TimelineGetter getter) {
		this.timeline = getter.getTimeline();
	}
	
	private void init() {
		playing = false;
		setPlaying(playing);
		addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor != null && (actor instanceof Button)) {
					final Button tb = (Button) actor;
					if (tb.isPressed()) {
						setPlaying(!playing);
					}
				}
			}
		});
	}

	protected void setPlaying(boolean playing) {
		this.playing = playing;
		final ButtonStyle s = playing ? (pause != null ? pause : play) : play;
		if (s == null)
			return;
		setStyle(s);
	}

}
