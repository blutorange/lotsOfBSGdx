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

//TODO Get play pause string from skin.

public class PlayPauseButton extends Button {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PlayPauseButton.class);

	private ButtonStyle play, pause;
	private final TimelineProvider timelineProvider;
	private boolean playing = false;

	public PlayPauseButton(Skin skin, TimelineProvider timeline) {
		this(skin.get(ButtonStyle.class), skin.get(ButtonStyle.class), timeline);
	}

	public PlayPauseButton(Skin skin, String styleNamePlay, String styleNamePause, TimelineProvider timeline) {
		this(skin.get(styleNamePlay, ButtonStyle.class), skin.get(styleNamePause, ButtonStyle.class), timeline);
	}

	public PlayPauseButton(ButtonStyle play, ButtonStyle pause, TimelineProvider timeline) {
		super(pause);
		this.timelineProvider = timeline;
		this.play = play;
		this.pause = pause;
		initialize();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (playing && timelineProvider != null) {
			final ModelTimeline tl = timelineProvider.getTimeline();
			tl.setCurrentTime(tl.getCurrentTime() + Gdx.graphics.getRawDeltaTime());
			if (tl.getCurrentTime() >= tl.getEndTime())
				setPlaying(false);
		}
	}

	private void initialize() {
		playing = false;
		setPlaying(playing);
		this.addListener(new ChangeListener() {
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
		this.setStyle(s);
	}

}
