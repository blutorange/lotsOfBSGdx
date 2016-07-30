package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;
import de.homelab.madgaksha.scene2dext.listener.ButtonListener;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;
import de.homelab.madgaksha.scene2dext.widget.NumericInput.NumericInputListener;

public class ClipTable extends Table {
	private final static Logger LOG = Logger.getLogger(ClipTable.class);
	private final TimelineProvider timelineProvider;
	private final List<DataRow> dataRowList = new ArrayList<>();
	private boolean ascStartTime, ascEndTime, ascTrack, ascClip;
	
	public ClipTable(Skin skin, TimelineProvider timelineProvider) {
		super(skin);
		this.timelineProvider = timelineProvider;
		initialize();
	}
	
	private void initialize() {
		timelineProvider.getTimeline().registerChangeListener(TrackChangeType.CLIP_ATTACHED, new TrackChangeListener() {
			@Override
			public void handle(ModelTrack track, TrackChangeType type) {
				rebuildTable();
			}
		});
		rebuildTable();
	}

	private static class DataRow {
		private final Actor a1, a2, a3, a4;
		private final ModelClip clip;
		public DataRow(Actor a1, Actor a2, Actor a3, Actor a4, ModelClip clip) {
			this.a1 = a1;
			this.a2 = a2;
			this.a3 = a3;
			this.a4 = a4;
			this.clip = clip;
		}
		public final static Comparator<DataRow> START_TIME = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(DataRow row1, DataRow row2) {
				final float t1 = row1.clip.getStartTime();
				final float t2 = row2.clip.getStartTime();
				return t1 < t2 ? -1 : t1 == t2 ? 0 : 1; 
			}
		};
		public final static Comparator<DataRow> END_TIME = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(DataRow row1, DataRow row2) {
				final float t1 = row1.clip.getEndTime();
				final float t2 = row2.clip.getEndTime();
				return t1 < t2 ? -1 : t1 == t2 ? 0 : 1; 
			}
		};
		public final static Comparator<DataRow> TRACK = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(DataRow row1, DataRow row2) {
				final String s1 = row1.clip.getParentTrack().getLabel();
				final String s2 = row2.clip.getParentTrack().getLabel();
				return s1.compareTo(s2); 
			}
		};
		public final static Comparator<DataRow> CLIP = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(DataRow row1, DataRow row2) {
				final String s1 = row1.clip.getClipData().getTitle();
				final String s2 = row2.clip.getClipData().getTitle();
				return s1.compareTo(s2); 
			}
		};
	}
	
	protected void rebuildTable() {
		LOG.debug("rebuilding table");
		ascStartTime = ascEndTime = ascTrack = ascClip = false;
		clear();
		dataRowList.clear();
		Button colStartTime = new TextButton("Start time", getSkin());
		Button colEndTime = new TextButton("End time", getSkin());
		Button colTrack = new TextButton("Track", getSkin());
		Button colClip = new TextButton("Clip", getSkin());
		add(colStartTime);
		add(colEndTime);
		add(colTrack).expandX();
		add(colClip).expandX();
		row().pad(2, 2, 2, 2);
		
		for (final ModelTrack track : timelineProvider.getTimeline()) {
			for (final ModelClip clip : track) {
				// Create input fields and buttons
				final NumericInput niStart = new NumericInput(clip.getStartTime(), getSkin());
				final NumericInput niEnd= new NumericInput(clip.getEndTime(), getSkin());
				final TextButton tbTrack = new TextButton(track.getLabel().toString(), getSkin());
				final TextButton tbClip = new TextButton(clip.getClipData().getTitle(), getSkin());				
				setMinMax(niStart, niEnd, track, clip);
				niStart.setQuantizer(0.01f);
				niEnd.setQuantizer(0.01f);
				dataRowList.add(new DataRow(niStart, niEnd, tbTrack, tbClip, clip));
				
				// Add row
				this.add(niStart);
				this.add(niEnd);
				this.add(tbTrack).fill();
				this.add(tbClip).fill();
				this.row().pad(2, 2, 2, 2);				

				// Start time
				niStart.addListener(new NumericInputListener(){
					@Override
					protected void changed(float value, Actor actor) {
						clip.setStartTime(value);
					}
				});
				
				// End time
				niEnd.addListener(new NumericInputListener(){
					@Override
					protected void changed(float value, Actor actor) {
						clip.setEndTime(value);
					}
				});
				
				// Update start and end time when model changed.
				clip.setData("start", niStart);
				clip.setData("end", niEnd);
				clip.registerChangeListener(clipChangeListener, ClipChangeType.START_TIME, ClipChangeType.END_TIME);
				
				// Show details when clicking clip
				tbClip.addListener(new ButtonListener() {
					@Override
					public void pressed(Button button) {
						timelineProvider.getTimeline().setSelected(clip.getClipData());						
					}
				});
			}
		}
		
		// Sort table when a column label is clicked.
		setupSortButtons(colStartTime, colEndTime, colTrack, colClip);
		invalidateHierarchy();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void refillTable() {
		int i;
		Array<Cell> cellList = getCells();
		for (i = 4; i != cellList.size; ++i) {
			cellList.get(i).clearActor();
		}
		i = 4;
		for (DataRow row : dataRowList) {
			if (i+3 >= cellList.size) break;
			cellList.get(i).setActor(row.a1);
			cellList.get(i+1).setActor(row.a2);
			cellList.get(i+2).setActor(row.a3);
			cellList.get(i+3).setActor(row.a4);
			i += 4;
		}
		invalidateHierarchy();
	}

	protected static void setMinMax(NumericInput niStart, NumericInput niEnd, ModelTrack track, ModelClip clip) {
		niStart.setMin(track.getStartTime());
		niStart.setMax(Math.min(track.getEndTime(), clip.getEndTime()));
		niEnd.setMin(Math.max(track.getStartTime(), clip.getStartTime()));
		niEnd.setMax(track.getEndTime());
	}	

	private void setupSortButtons(Button colStartTime, Button colEndTime, Button colTrack, Button colClip) {
		colStartTime.addListener(new ButtonListener() {
			@Override
			public void pressed(Button button) {
				ascStartTime = !ascStartTime;
				ascEndTime = ascTrack = ascClip = false;
				dataRowList.sort(ascStartTime ? DataRow.START_TIME : Collections.reverseOrder(DataRow.START_TIME));
				refillTable();
			}
		});
		colEndTime.addListener(new ButtonListener() {
			@Override
			public void pressed(Button button) {
				ascEndTime = !ascEndTime;
				ascStartTime = ascTrack = ascClip = false;
				dataRowList.sort(ascEndTime ? DataRow.END_TIME : Collections.reverseOrder(DataRow.END_TIME));
				refillTable();

			}			
		});
		colTrack.addListener(new ButtonListener() {
			@Override
			public void pressed(Button button) {
				ascTrack = !ascTrack;
				ascEndTime = ascStartTime = ascClip = false;
				dataRowList.sort(ascTrack ? DataRow.TRACK : Collections.reverseOrder(DataRow.TRACK));
				refillTable();

			}			
		});
		colClip.addListener(new ButtonListener() {
			@Override
			public void pressed(Button button) {
				ascClip = !ascClip;
				ascEndTime = ascTrack = ascStartTime = false;
				dataRowList.sort(ascClip ? DataRow.CLIP : Collections.reverseOrder(DataRow.CLIP));
				refillTable();

			}			
		});
	}
	
	private final static ClipChangeListener clipChangeListener = new ClipChangeListener() {
		@SuppressWarnings("incomplete-switch")
		@Override
		public void handle(ModelClip clip, ClipChangeType type) {
			final NumericInput niStart = clip.getData("start", NumericInput.class);
			final NumericInput niEnd = clip.getData("end", NumericInput.class);
			switch (type) {
			case START_TIME:
				niStart.setValue(clip.getStartTime());
				break;
			case END_TIME:
				niEnd.setValue(clip.getEndTime());
				break;							
			}
			setMinMax(niStart, niEnd, clip.getParentTrack(), clip);
		}
	};
}
