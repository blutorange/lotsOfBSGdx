package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty.PropertyEntry;
import de.homelab.madgaksha.scene2dext.listener.ButtonListener;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;

public class DrawablePropertyEditor extends SplitPane {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DrawablePropertyEditor.class);
	private final static CharSequence LABEL_VALUE = "Value";
	private final static CharSequence LABEL_TIME = "Time";
	private static final Actor EMPTY_ACTOR = new Actor();
	private ModelDrawableProperty<?> modelProperty = null;
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
			if (actor instanceof NumericInput)
				NumericInput.free((NumericInput)actor);
		}

		table.clearChildren();

		labelValue.setText(modelProperty.getName());

		table.add(buttonAbsRel).colspan(modelProperty.getDimension()+2).fillX().expandX();
		table.row();
		table.add(labelTime);
		table.add(labelValue).colspan(modelProperty.getDimension());
		table.add(EMPTY_ACTOR);
		table.row();

		// Create dynamic values table
		int i = 0;
		final int dims = modelProperty.getDimension();
		final int len = modelProperty.size();
		final float minValue[] = modelProperty.getMinValue().getValues();
		final float maxValue[] = modelProperty.getMaxValue().getValues();
		for (final PropertyEntry<?> entry : modelProperty) {
			// Add time.
			final NumericInput niTime = NumericInput.obtain();
			niTime.setSkin(skin);
			timeMode.setupEntry(niTime, modelProperty, entry);
			table.add(niTime).fillX().expandX();
			// Add value.
			final float values[] = entry.getValue().getValues();
			for (int j = 0; j != dims; ++j) {
				final NumericInput niVal = NumericInput.obtain();
				niVal.setSkin(skin);
				niVal.setMinMax(minValue[j], maxValue[j]);
				niVal.setValue(values[j]);
				niVal.setStep(0.01f);
				table.add(niVal);
			}
			// Disable first and last time.
			if (i == 0 || i == len-1) niTime.setDisabled(true);
			else {
				// Remove buttons.
				final AddDelButton delButton = new AddDelButton(false, skin);
				table.add(delButton).size(24f);
			}
			table.row();

			// Interpolation
			// Add buttons.
			if (i != len -1) {
				final TextButton buttonInterpolation = new TextButton("Interpolation", skin);
				final AddDelButton addButton = new AddDelButton(true, skin);
				table.add(buttonInterpolation).colspan(modelProperty.getDimension() + 1).fillX().expandX();
				table.add(addButton).size(24f);
			}
			table.row();

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
			}
		});

		setFirstWidget(table);
		setSecondWidget(new Label("Interpolation Details", skin));
	}

	private static enum TimeMode {
		ABSOLUTE {
			@Override
			public String getText() {
				return LABEL_ABS;
			}

			@Override
			public void setupEntry(final NumericInput niTime, final ModelDrawableProperty<?> modelProperty, final PropertyEntry<?> entry) {
				niTime.setMinMax(modelProperty.getStartTime(), modelProperty.getEndTime());
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
			public void setupEntry(final NumericInput niTime, final ModelDrawableProperty<?> modelProperty, final PropertyEntry<?> entry) {
				niTime.setMinMax(0f, modelProperty.getDuration());
				niTime.setValue(entry.getTime() - modelProperty.getStartTime());
				niTime.setStep(0.01f);
			}
		};
		private final static String LABEL_ABS = "Absolute";
		private final static String LABEL_REL = "Relative";
		public abstract String getText();
		public abstract void setupEntry(NumericInput niTime, ModelDrawableProperty<?> modelProperty, PropertyEntry<?> entry);
	}

}
