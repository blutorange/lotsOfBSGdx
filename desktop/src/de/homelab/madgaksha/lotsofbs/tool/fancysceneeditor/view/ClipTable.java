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
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;
import de.homelab.madgaksha.scene2dext.listener.ButtonListener;
import de.homelab.madgaksha.scene2dext.view.NumericInput.FloatNumericInput;
import de.homelab.madgaksha.scene2dext.view.NumericInput.NumericInputListener;

public class ClipTable extends Table {
	private final static Logger LOG = Logger.getLogger(ClipTable.class);
	private final List<DataRow> dataRowList = new ArrayList<>();
	private boolean ascStartTime, ascEndTime, ascTrack, ascClip;

	public ClipTable(final TimelineProvider timelineProvider, final Skin skin) {
		super(skin);
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(final TimelineGetter getter, final TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
	}

	protected void connectTimeline(final TimelineGetter getter) {
		getter.getTimeline().registerChangeListener(TrackChangeType.CLIP_ATTACHED, new TrackChangeListener() {
			@Override
			public void handle(final ModelTrack track, final TrackChangeType type) {
				rebuildTable(getter.getTimeline());
			}
		});
		rebuildTable(getter.getTimeline());
	}

	protected void rebuildTable(final ModelTimeline timeline) {
		LOG.debug("rebuilding table");
		ascStartTime = ascEndTime = ascTrack = ascClip = false;
		clear();
		dataRowList.clear();
		final Button colStartTime = new TextButton("Start time", getSkin());
		final Button colEndTime = new TextButton("End time", getSkin());
		final Button colTrack = new TextButton("Track", getSkin());
		final Button colClip = new TextButton("Clip", getSkin());
		add(colStartTime);
		add(colEndTime);
		add(colTrack).expandX();
		add(colClip).expandX();
		row().pad(2, 2, 2, 2);

		for (final ModelTrack track : timeline) {
			ButtonListener trackDetailListener = null;
			if (track instanceof DetailsPanel<?>) {
				trackDetailListener = new ButtonListener() {
					@Override
					public void pressed(final Button button) {
						timeline.setSelected((DetailsPanel<?>)track);
					}
				};
			}

			for (final ModelClip clip : track) {
				// Create input fields and buttons
				final FloatNumericInput niStart = new FloatNumericInput(clip.getStartTime(), getSkin());
				final FloatNumericInput niEnd= new FloatNumericInput(clip.getEndTime(), getSkin());
				final TextButton tbTrack = new TextButton(track.getLabel().toString(), getSkin());
				final TextButton tbClip = new TextButton(clip.getClipData().getTitle(), getSkin());
				setMinMax(niStart, niEnd, track, clip);
				niStart.setStep(0.01f);
				niEnd.setStep(0.01f);
				dataRowList.add(new DataRow(niStart, niEnd, tbTrack, tbClip, clip));

				// Add row
				this.add(niStart);
				this.add(niEnd);
				this.add(tbTrack).fill();
				this.add(tbClip).fill();
				row().pad(2, 2, 2, 2);

				// Start time
				niStart.addListener(new NumericInputListener<Float>(){
					@Override
					protected void changed(final Float value, final Actor actor) {
						clip.setStartTime(value);
					}
				});

				// End time
				niEnd.addListener(new NumericInputListener<Float>(){
					@Override
					protected void changed(final Float value, final Actor actor) {
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
					public void pressed(final Button button) {
						timeline.setSelected(clip.getClipData());
					}
				});

				// Show details when clicking track.
				tbTrack.addListener(trackDetailListener);
			}
		}

		// Sort table when a column label is clicked.
		setupSortButtons(colStartTime, colEndTime, colTrack, colClip);
		invalidateHierarchy();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void refillTable() {
		int i;
		final Array<Cell> cellList = getCells();
		for (i = 4; i != cellList.size; ++i) {
			cellList.get(i).clearActor();
		}
		i = 4;
		for (final DataRow row : dataRowList) {
			if (i+3 >= cellList.size) {
				break;
			}
			cellList.get(i).setActor(row.a1);
			cellList.get(i+1).setActor(row.a2);
			cellList.get(i+2).setActor(row.a3);
			cellList.get(i+3).setActor(row.a4);
			i += 4;
		}
		invalidateHierarchy();
	}

	protected static void setMinMax(final FloatNumericInput niStart, final FloatNumericInput niEnd, final ModelTrack track, final ModelClip clip) {
		niStart.setMin(track.getStartTime());
		niStart.setMax(Math.min(track.getEndTime(), clip.getEndTime()));
		niEnd.setMin(Math.max(track.getStartTime(), clip.getStartTime()));
		niEnd.setMax(track.getEndTime());
	}

	private void setupSortButtons(final Button colStartTime, final Button colEndTime, final Button colTrack, final Button colClip) {
		colStartTime.addListener(new ButtonListener() {
			@Override
			public void pressed(final Button button) {
				ascStartTime = !ascStartTime;
				ascEndTime = ascTrack = ascClip = false;
				dataRowList.sort(ascStartTime ? DataRow.START_TIME : Collections.reverseOrder(DataRow.START_TIME));
				refillTable();
			}
		});
		colEndTime.addListener(new ButtonListener() {
			@Override
			public void pressed(final Button button) {
				ascEndTime = !ascEndTime;
				ascStartTime = ascTrack = ascClip = false;
				dataRowList.sort(ascEndTime ? DataRow.END_TIME : Collections.reverseOrder(DataRow.END_TIME));
				refillTable();

			}
		});
		colTrack.addListener(new ButtonListener() {
			@Override
			public void pressed(final Button button) {
				ascTrack = !ascTrack;
				ascEndTime = ascStartTime = ascClip = false;
				dataRowList.sort(ascTrack ? DataRow.TRACK : Collections.reverseOrder(DataRow.TRACK));
				refillTable();

			}
		});
		colClip.addListener(new ButtonListener() {
			@Override
			public void pressed(final Button button) {
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
		public void handle(final ModelClip clip, final ClipChangeType type) {
			final FloatNumericInput niStart = clip.getData("start", FloatNumericInput.class);
			final FloatNumericInput niEnd = clip.getData("end", FloatNumericInput.class);
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

	private static class DataRow {
		private final Actor a1, a2, a3, a4;
		private final ModelClip clip;
		public DataRow(final Actor a1, final Actor a2, final Actor a3, final Actor a4, final ModelClip clip) {
			this.a1 = a1;
			this.a2 = a2;
			this.a3 = a3;
			this.a4 = a4;
			this.clip = clip;
		}
		public final static Comparator<DataRow> START_TIME = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(final DataRow row1, final DataRow row2) {
				final float t1 = row1.clip.getStartTime();
				final float t2 = row2.clip.getStartTime();
				return t1 < t2 ? -1 : t1 == t2 ? 0 : 1;
			}
		};
		public final static Comparator<DataRow> END_TIME = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(final DataRow row1, final DataRow row2) {
				final float t1 = row1.clip.getEndTime();
				final float t2 = row2.clip.getEndTime();
				return t1 < t2 ? -1 : t1 == t2 ? 0 : 1;
			}
		};
		public final static Comparator<DataRow> TRACK = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(final DataRow row1, final DataRow row2) {
				final String s1 = row1.clip.getParentTrack().getLabel();
				final String s2 = row2.clip.getParentTrack().getLabel();
				return s1.compareTo(s2);
			}
		};
		public final static Comparator<DataRow> CLIP = new Comparator<ClipTable.DataRow>() {
			@Override
			public int compare(final DataRow row1, final DataRow row2) {
				final String s1 = row1.clip.getClipData().getTitle();
				final String s2 = row2.clip.getClipData().getTitle();
				return s1.compareTo(s2);
			}
		};
	}

}
