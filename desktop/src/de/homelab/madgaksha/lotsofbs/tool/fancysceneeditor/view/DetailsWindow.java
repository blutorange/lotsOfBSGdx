package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimeInterval;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimeIntervalListenable;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimeIntervalListenable.TimeIntervalListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;
import de.homelab.madgaksha.scene2dext.util.TableUtils;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;
import de.homelab.madgaksha.scene2dext.widget.NumericInput.NumericInputListener;

public class DetailsWindow extends Table {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DetailsWindow.class);
	private final static String LABEL_START_TIME = "Start time (s)";
	private final static String LABEL_DURATION = "Duration (s)";
	private final static CharSequence LABEL_TIMING = "Timing";
	private final static CharSequence LABEL_DETAILS = "Details";

	private TimeIntervalListenable listenable;
	private Object listenerIdStart, listenerIdEnd;
	private NumericInputListener listenerStart, listenerDuration;
	private final Label heading;
	private final Label description;
	private final ScrollPane scrollPane;
	private final NumericInput niStart;
	private final NumericInput niDuration;
	private final Table table;
	private final TimelineProvider timelineProvider;

	public DetailsWindow (final TimelineProvider timelineProvider, final Skin skin) {
		super(skin);
		this.timelineProvider = timelineProvider;
		heading = new Label(StringUtils.EMPTY, skin);
		description = new Label(StringUtils.EMPTY, skin);
		scrollPane = new ScrollPane(null, skin);
		niStart = new NumericInput(0f, skin);
		niDuration = new NumericInput(0f, skin);
		table = new Table(skin);
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(final TimelineGetter getter, final TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
	}

	private void connectTimeline(final TimelineGetter getter) {
		getter.getTimeline().registerChangeListener(TimelineChangeType.SELECTED, new TimelineChangeListener() {
			@Override
			public void handle(final ModelTimeline timeline, final TimelineChangeType type) {
				setup(getter.getTimeline().getSelected());
			}
		});
		setup(getter.getTimeline().getSelected());
	}

	protected void setup(final DetailsPanel<?> panel) {
		if (panel == null) return;
		final Actor actor = panel.getActor(timelineProvider, getSkin());
		if (actor == null) return;

		final Object parent = panel.getObject();
		final boolean hasTime = parent instanceof TimeIntervalListenable;

		rebuildLayout(hasTime);

		heading.setText(panel.getTitle());
		description.setText(panel.getDescription());
		table.add(actor).fillX().colspan(2);
		niStart.setStep(0.01f);
		niDuration.setStep(0.01f);

		if (hasTime) connectTimeIntervalListenable((TimeIntervalListenable)parent);

		heading.invalidateHierarchy();
		description.invalidateHierarchy();
		scrollPane.invalidateHierarchy();
		table.invalidateHierarchy();
	}

	private void connectTimeIntervalListenable(final TimeIntervalListenable listenable) {
		// Clear old listeners
		if (this.listenable != null && listenerIdStart != null && listenerIdEnd != null) {
			this.listenable.removeStartTimeListener(listenerIdStart);
			this.listenable.removeEndTimeListener(listenerIdEnd);
		}
		if (listenerStart != null) niStart.removeListener(listenerStart);
		if (listenerDuration != null) niDuration.removeListener(listenerDuration);

		// Create new listeners and connect
		final TimeIntervalListener listenerTime = new TimeIntervalListener() {
			@Override
			public void handle(final float startTime, final TimeInterval interval) {
				setTimeInput(listenable);
			}
		};
		listenerStart = new NumericInputListener() {
			@Override
			protected void changed(final float value, final Actor actor) {
				final float endTime = value + listenable.getDuration();
				listenable.setStartTime(value);
				listenable.setEndTime(endTime);
			}
		};
		listenerDuration = new NumericInputListener() {
			@Override
			protected void changed(final float value, final Actor actor) {
				listenable.setEndTime(listenable.getStartTime()+value);
			}
		};

		listenerIdStart = listenable.setStartTimeListener(listenerTime);
		listenerIdEnd = listenable.setEndTimeListener(listenerTime);
		niStart.addListener(listenerStart);
		niDuration.addListener(listenerDuration);
		setTimeInput(listenable);
		this.listenable = listenable;
	}


	private void setTimeInput(final TimeIntervalListenable listenable) {
		niStart.setValue(listenable.getStartTime());
		niStart.setMin(listenable.getMinStartTime());
		niStart.setMax(listenable.getMaxStartTime());
		niDuration.setValue(listenable.getDuration());
		niDuration.setMin(listenable.getMinDuration());
		niDuration.setMax(listenable.getMaxDuration());
	}

	private void rebuildLayout(final boolean hasTime) {
		clearChildren();
		table.clearChildren();
		add(heading).fillX().center().colspan(2);
		row();
		add(description).fillX().center().colspan(2);
		row();
		add(scrollPane).expand().fill();

		scrollPane.setWidget(table);

		if (hasTime) {
			TableUtils.heading(table, LABEL_TIMING, getSkin()).colspan(2);
			table.row();
			TableUtils.labelledActor(table, LABEL_START_TIME, niStart);
			table.row();
			TableUtils.labelledActor(table, LABEL_DURATION, niDuration);
			table.row();
			TableUtils.heading(table, LABEL_DETAILS, getSkin()).colspan(2);
			table.row();
		}

		heading.setAlignment(Align.center);
		heading.setWrap(true);
		heading.setFontScale(2f);
		description.setWrap(true);
		description.setAlignment(Align.left);
		description.setFontScale(1.2f);
	}
}
