package de.homelab.madgaksha.scene2dext.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public abstract class ButtonListener extends ChangeListener {

	private final int pressed = -1;
	private int checked = -1;

	@Override
	public void changed(final ChangeEvent event, final Actor actor) {
		if (!(actor instanceof Button)) return;
		final Button button = (Button)actor;
		if (button.isDisabled()) return;
		if (button.isChecked() && pressed != 1) {
			checked = 1;
			checked(button);
		}
		else if (!button.isChecked() && checked != 0) {
			checked = 0;
			unchecked(button);
		}

		if (button.isPressed()) pressed(button);
	}
	/**
	 * Called when the button is checked. Override this when needed.
	 * @param button The button that got checked.
	 */
	public void checked(final Button button){}
	/**
	 * Called when the button is unchecked. Override this when needed.
	 * @param button That button that got unchecked.
	 */
	public void unchecked(final Button button){}

	/**
	 * Called when the button is pressed.
	 * @param button The button that is pressed.
	 */
	public abstract void pressed(final Button button);

	public static class ButtonAdapter extends ButtonListener {
		@Override
		public void pressed(final Button button) {
		}
	}
}
