package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.NumericInput.NumericInputChangeListener;

public class ClipTable extends Table {
	private final static Logger LOG = Logger.getLogger(ClipTable.class);
	private final TimelineProvider timelineProvider;
	
	public ClipTable(Skin skin, TimelineProvider timelineProvider) {
		super(skin);
		this.timelineProvider = timelineProvider;
		initialize();
	}
	
	private void initialize() {
		timelineProvider.getTimeline().registerChangeListener(TrackChangeType.CLIP_ATTACHED, new TrackChangeListener() {
			@Override
			public void handle(ModelTrack track, TrackChangeType type) {
				rebuiltTable();
			}
		});
		rebuiltTable();
	}

	protected void rebuiltTable() {
		LOG.debug("rebuilt table");
		this.clear();
		this.add(new TextButton("Start time", getSkin()));
		this.add(new TextButton("End time", getSkin()));
		this.add(new TextButton("Track", getSkin())).expandX();
		this.add(new TextButton("Clip", getSkin())).expandX();
		this.row().pad(2, 2, 2, 2);
		for (final ModelTrack track : timelineProvider.getTimeline()) {
			for (final ModelClip clip : track) {
				final NumericInput niStart = new NumericInput(clip.getStartTime(), getSkin());
				final NumericInput niEnd= new NumericInput(clip.getEndTime(), getSkin());
				final TextButton tbTrack = new TextButton(track.getLabel().toString(), getSkin());
				final TextButton tbClip = new TextButton(clip.getClipData().getTypeName(), getSkin());
				niStart.addListener(new NumericInputChangeListener(){
					@Override
					protected void changed(float value, Actor actor) {
						clip.setStartTime(value);
					}
				});
				niEnd.addListener(new NumericInputChangeListener(){
					@Override
					protected void changed(float value, Actor actor) {
						clip.setEndTime(value);
					}
				});
				tbClip.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if (actor instanceof Button) {
							final Button button = (Button)actor;
							if (button.isPressed()) {
								timelineProvider.getTimeline().setSelected(clip.getClipData());
							}
						}
					}
				});
				this.add(niStart);
				this.add(niEnd);
				this.add(tbTrack).fill();
				this.add(tbClip).fill();
				this.row().pad(2, 2, 2, 2);
			}
		}
		invalidateHierarchy();
	}
}
