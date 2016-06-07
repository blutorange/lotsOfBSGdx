package de.homelab.madgaksha.desktop;

public abstract class InputChangeListener {
	/**
	 * Called when the the input field has been emptied does not contain any value anymore.
	 */
	public void inputEmptied() {
		
	}
	/**
	 * Called when the input has changed. The 
	 * @param currentValue The current value of the option.
	 */
	public void inputChanged(float currentValue) {
		
	}
	/**
	 * Called when the input has changed. The 
	 * @param currentValue The current value of the option.
	 */
	public void inputChanged(int currentValue) {
		
	}
	/**
	 * Called when the input has changed. The 
	 * @param currentValue The current value of the option.
	 */
	public void inputChanged(boolean currentValue) {
		
	}
}
