package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import java.util.NoSuchElementException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DrawablePropertyChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DrawablePropertyChangeListener.DrawablePropertyChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty.PropertyEntry;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelInterpolation;
import de.homelab.madgaksha.safemutable.DimensionalValue;
import de.homelab.madgaksha.scene2dext.listener.ButtonListener;
import de.homelab.madgaksha.scene2dext.view.NumericInput;
import de.homelab.madgaksha.scene2dext.view.NumericInput.FloatNumericInput;
import de.homelab.madgaksha.scene2dext.view.NumericInput.NumericInputListener;

public class DrawablePropertyEditor extends SplitPane {
	private final static Logger LOG = Logger.getLogger(DrawablePropertyEditor.class);
	private final static CharSequence LABEL_VALUE = "Value";
	private final static CharSequence LABEL_TIME = "Time";
	private final static String KEY_TIME = "t";
	private final static String KEY_VALUE = "v";
	private static final Actor EMPTY_ACTOR = new Actor();
	private ModelDrawableProperty<? extends DimensionalValue> modelProperty = null;
	private TimeMode timeMode = TimeMode.ABSOLUTE;
	private TextButton buttonAbsRel;
	private Table table;
	private Label labelTime;
	private Label labelValue;

	public DrawablePropertyEditor(final Skin skin) {
		super(null, null, false, skin);
		createElements(skin);
	}

	public void setModel(final ModelDrawableProperty<?> modelProperty) {
		this.modelProperty = modelProperty;
		rebuildTable();
	}

	public ModelDrawableProperty<?> getModel() {
		return modelProperty;
	}

	private void rebuildTable() {
		final Skin skin = table.getSkin();

		// Free unused numeric inputs.
		for (final Cell<?> cell : table.getCells()) {
			final Actor actor = cell.getActor();
			if (actor instanceof NumericInput) {
				FloatNumericInput.free((FloatNumericInput) actor);
			}
		}

		table.clearChildren();

		labelValue.setText(modelProperty.getName());

		table.add(buttonAbsRel).colspan(modelProperty.getDimension() + 2).fillX().expandX();
		table.row();
		table.add(labelTime);
		table.add(labelValue).colspan(modelProperty.getDimension());
		table.add(EMPTY_ACTOR);
		table.row();

		// Register change listener.
		modelProperty.registerChangeListener(drawablePropertyChangeListener, DrawablePropertyChangeType.TIME,
				DrawablePropertyChangeType.VALUE, DrawablePropertyChangeType.ADDED, DrawablePropertyChangeType.REMOVED);

		// Create dynamic values table
		int i = 0;
		final int dims = modelProperty.getDimension();
		final int len = modelProperty.size();
		final float minValue[] = modelProperty.getMinValue().getValues();
		final float maxValue[] = modelProperty.getMaxValue().getValues();
		for (final PropertyEntry<? extends DimensionalValue> entry : modelProperty) {
			// Add time.
			final FloatNumericInput niTime = FloatNumericInput.obtain(skin);
			timeMode.setupEntry(niTime, modelProperty, entry);
			table.add(niTime).fillX().expandX();
			// Add value.
			final float values[] = entry.getValue().getValues();
			final FloatNumericInput niValueArray[] = new FloatNumericInput[dims];
			for (int j = 0; j != dims; ++j) {
				final FloatNumericInput niVal = FloatNumericInput.obtain(skin);
				niVal.setMinMax(minValue[j], maxValue[j]);
				niVal.setValue(values[j]);
				niVal.setStep(0.01f);
				table.add(niVal);
				niValueArray[j] = niVal;
			}
			// Disable first and last time.
			if (i == 0 || i == len - 1) {
				niTime.setDisabled(true);
			} else {
				// Remove buttons.
				final AddDelButton delButton = new AddDelButton(false, skin);
				table.add(delButton).size(24f);
				delButton.addListener(new ButtonListener() {
					@Override
					public void pressed(final Button button) {
						modelProperty.remove(entry);
					}
				});
			}
			table.row();

			// Interpolation
			// Add buttons.
			if (i != len - 1) {
				final ModelInterpolation modelInterpolation = entry.getModelInterpolation();
				final TextButton buttonInterpolation = new TextButton(modelInterpolation.getLabel(), skin);
				final AddDelButton addButton = new AddDelButton(true, skin);
				buttonInterpolation.addListener(new ButtonListener() {
					@Override
					public void pressed(final Button button) {
						//						sbInterpolation.setSelected(InterpolationEntry.findEntry(modelInterpolation, sbInterpolation.getItems()));
						setSecondWidget(modelInterpolation.getActor(skin));
					}
				});
				addButton.addListener(new ButtonListener() {
					@Override
					public void pressed(final Button button) {
						modelProperty.insertAfter(entry);
					}
				});
				table.add(buttonInterpolation).colspan(modelProperty.getDimension() + 1).fillX().expandX();
				table.add(addButton).size(24f);
			}
			table.row();

			// Set data
			entry.setData(KEY_TIME, niTime);
			entry.setData(KEY_VALUE, niValueArray);

			++i;
		}

		table.invalidateHierarchy();
	}

	private void createElements(final Skin skin) {
		table = new Table(skin);

		labelTime = new Label(LABEL_TIME, skin);
		labelValue = new Label(LABEL_VALUE, skin);

		buttonAbsRel = new TextButton(timeMode.getText(), skin);
		buttonAbsRel.addListener(new ButtonListener() {
			@Override
			public void pressed(final Button button) {
				switch (timeMode) {
				case ABSOLUTE:
					timeMode = TimeMode.RELATIVE;
					break;
				case RELATIVE:
					timeMode = TimeMode.ABSOLUTE;
					break;
				}
				buttonAbsRel.setText(timeMode.getText());
				buttonAbsRel.invalidate();
				rebuildTable();
			}
		});

		setFirstWidget(table);
		setSecondWidget(new Label("No interpolation selected.", skin));
	}



	private final DrawablePropertyChangeListener drawablePropertyChangeListener = new DrawablePropertyChangeListener() {
		@Override
		public void handle(final PropertyEntry<?> entry, final DrawablePropertyChangeType type) {
			switch (type) {
			case TIME:
				try {
					timeMode.setProperties(entry.getData(KEY_TIME, FloatNumericInput.class), modelProperty, entry);
				}
				catch (final NoSuchElementException e) {
					LOG.debug(e);
				}
				break;
			case VALUE:
				final FloatNumericInput[] niValueArray = entry.getData(KEY_VALUE, FloatNumericInput[].class);
				final float values[] = entry.getValue().getValues();
				if (niValueArray.length != values.length) {
					LOG.error("cannot set values to view, dimensions do not match");
					return;
				}
				for (int i = 0; i != niValueArray.length; ++i) {
					niValueArray[i].setValue(values[i]);
				}
				break;
			case ADDED:
			case REMOVED:
				rebuildTable();
				break;
			}
		}
	};

	private static enum TimeMode {
		ABSOLUTE {
			@Override
			public String getText() {
				return LABEL_ABS;
			}

			@Override
			public void setupEntry(final FloatNumericInput niTime, final ModelDrawableProperty<?> modelProperty,
					final PropertyEntry<?> entry) {
				setProperties(niTime, modelProperty, entry);
				niTime.addListener(new NumericInputListener<Float>() {
					@Override
					protected void changed(final Float value, final Actor actor) {
						entry.setTime(value);
					}
				});
			}

			@Override
			public float getTime(final ModelDrawableProperty<?> modelProperty, final PropertyEntry<?> entry) {
				return entry.getTime();
			}

			@Override
			public void setProperties(final FloatNumericInput niTime, final ModelDrawableProperty<?> modelProperty,
					final PropertyEntry<?> entry) {
				niTime.setMinMax(entry.getMinTime(), entry.getMaxTime());
				niTime.setValue(entry.getTime());
				niTime.setStep(0.01f);
			}
		},
		RELATIVE {
			@Override
			public String getText() {
				return LABEL_REL;
			}


			@Override
			public void setProperties(final FloatNumericInput niTime, final ModelDrawableProperty<?> modelProperty,
					final PropertyEntry<?> entry) {
				final float startTime = modelProperty.getStartTime();
				niTime.setMinMax(entry.getMinTime()-startTime, entry.getMaxTime()-startTime);
				niTime.setMinMax(0f, modelProperty.getDuration());
				niTime.setValue(entry.getTime() - startTime);
				niTime.setStep(0.01f);
			}


			@Override
			public void setupEntry(final FloatNumericInput niTime, final ModelDrawableProperty<?> modelProperty,
					final PropertyEntry<?> entry) {
				setProperties(niTime, modelProperty, entry);
				niTime.addListener(new NumericInputListener<Float>() {
					@Override
					protected void changed(final Float value, final Actor actor) {
						entry.setTime(value + modelProperty.getStartTime());
					}
				});
			}

			@Override
			public float getTime(final ModelDrawableProperty<?> modelProperty, final PropertyEntry<?> entry) {
				return entry.getTime() + modelProperty.getStartTime();
			}
		};
		private final static String LABEL_ABS = "Absolute";
		private final static String LABEL_REL = "Relative";

		public abstract String getText();

		public abstract float getTime(ModelDrawableProperty<?> modelProperty, PropertyEntry<?> entry);

		public abstract void setupEntry(FloatNumericInput niTime, ModelDrawableProperty<?> modelProperty,
				PropertyEntry<?> entry);
		public abstract void setProperties(final FloatNumericInput niTime, final ModelDrawableProperty<?> modelProperty,
				final PropertyEntry<?> entry);
	}
}
