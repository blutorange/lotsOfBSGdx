package de.homelab.madgaksha.desktop;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.homelab.madgaksha.i18n.I18n;

public class FormComponentUtils {
	/**
	 * Initializes a new input field with a label and adds it to the given
	 * panel. Calls the provided {@link InputChangeListener} when the value
	 * changes.
	 * 
	 * @param i18n
	 *            Key for the label string.
	 * @param listener
	 *            Code to execute when the input value changes.
	 * @param addToPanel
	 *            Panel to add this label and spinner to.
	 * @param val
	 *            Initial value.
	 * @param min
	 *            Minimum value.
	 * @param max
	 *            Maximum value.
	 * @param step
	 *            Value step.
	 * @param isInteger
	 *            If true, val, min, max, step are casted to integers.
	 * @return The JSpinner that was created.
	 */
	public static JSpinner createNumberSpinner(String i18n, final InputChangeListener listener, JPanel addToPanel,
			float val, float min, float max, float step, final boolean isInteger) {
		final JLabel lbl = new JLabel(I18n.main(i18n));
		final JSpinner spn;
		if (isInteger)
			spn = new JSpinner(new SpinnerNumberModel((int) val, (int) min, (int) max, (int) step));
		else
			spn = new JSpinner(new SpinnerNumberModel(val, min, max, step));
		spn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent ev) {
				final Object val = (((JSpinner) ev.getSource()).getValue());
				if (val == null) {
					listener.inputEmptied();
				} else {
					if (isInteger) {
						if (val instanceof Integer)
							listener.inputChanged(((Integer) val).intValue());
						else if (val instanceof Float)
							listener.inputChanged(((Float) val).intValue());
						else if (val instanceof Double)
							listener.inputChanged(((Double) val).intValue());
					} else {
						if (val instanceof Double)
							listener.inputChanged(((Double) val).floatValue());
						else if (val instanceof Float)
							listener.inputChanged(((Float) val).floatValue());
						else if (val instanceof Integer)
							listener.inputChanged(((Integer) val).floatValue());
					}
				}
			}
		});
		spn.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ev) {
				final Object val = (((JSpinner) ev.getSource()).getValue());
				if (val == null) {
					listener.inputEmptied();
				} else {
					if (isInteger) {
						if (val instanceof Integer)
							listener.inputChanged(((Integer) val).intValue());
						else if (val instanceof Float)
							listener.inputChanged(((Float) val).intValue());
						else if (val instanceof Double)
							listener.inputChanged(((Double) val).intValue());
					} else {
						if (val instanceof Double)
							listener.inputChanged(((Double) val).floatValue());
						else if (val instanceof Float)
							listener.inputChanged(((Float) val).floatValue());
						else if (val instanceof Integer)
							listener.inputChanged(((Integer) val).floatValue());
					}
				}
			}
		});
		addToPanel.add(lbl);
		addToPanel.add(spn);
		return spn;
	}

	/**
	 * Initializes a new input field with a label and adds it to the given
	 * panel. Calls the provided {@link InputChangeListener} when the value
	 * changes.
	 * 
	 * @param i18n
	 *            Key for the label string.
	 * @param listener
	 *            Code to execute when the input value changes.
	 * @param addToPanel
	 *            Panel to add this label and spinner to.
	 * @param initiallyChecked
	 *            Initial value.
	 * @return The JCheckbox that was created.
	 */
	public static JCheckBox createBooleanCheckbox(String i18n, final InputChangeListener listener, JPanel addToPanel,
			boolean initiallyChecked) {
		final JLabel lbl = new JLabel(I18n.main(i18n));
		final JCheckBox cb = new JCheckBox();
		cb.setSelected(initiallyChecked);
		cb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ev) {
				listener.inputChanged(((JCheckBox) ev.getSource()).isSelected());
			}
		});
		addToPanel.add(lbl);
		addToPanel.add(cb);
		return cb;
	}
}
