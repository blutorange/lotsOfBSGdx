package de.homelab.madgaksha.scene2dext.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public abstract class ButtonListener extends ChangeListener {

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		if (!(actor instanceof Button)) return;
		final Button button = (Button)actor;
		if (button.isDisabled()) return;
		if (button.isChecked()) checked(button);
		if (button.isPressed()) pressed(button);
	}
	/**
	 * Called when the button is checked. Override this when needed.
	 * @param button The button that is checked.
	 */
	public void checked(Button button){}
	/**
	 * Called when the button is pressed.
	 * @param button The button that is pressed.
	 */
	public abstract void pressed(Button button);
}
