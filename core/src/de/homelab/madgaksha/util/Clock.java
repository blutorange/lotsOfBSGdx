package de.homelab.madgaksha.util;

import de.homelab.madgaksha.logging.Logger;

public class Clock {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Clock.class);
	
	private float time = 0.0f;
	
	public Clock(){};
	
	/**
	 * Updates the clock.
	 * @param deltaTime Elapsed time since last update in seconds.
	 */
	public void update(float deltaTime) {
		time += deltaTime;
	}
	
	public int getMinutesOne() {
		return getMinutes() % 10;
	}
	public int getMinutesTen() {
		return getMinutes() / 10;
	}
	public int getSecondsOneClipped() {
		return getSecondsClipped() % 10;
	}
	public int getSecondsTenClipped() {
		return getSecondsClipped() / 10;
	}
	public int getMillisecondsHundred() {
		return ((int)(time*10))%10;
	}
	public int getMillisecondsTen() {
		return ((int)(time*100))%10;
	}
	
	public int getHours() {
		return ((int)time)/3600;
	}
	
	public int getMinutes() {
		return ((int)time)/60;
	}
	public int getMinutesClipped() {
		return (((int)time)%3600)/60;
	}
	
	public int getSeconds() {
		return ((int)time);
	}
	
	public int getSecondsClipped() {
		return ((int)time)%60;
	}
	
	public int getMillisecondsClipped() {
		return ((int)(time*1000))%1000;
	}
	
	public void reset() {
		time = 0.0f;
	}

	@Override
	public String toString(){
		return String.format("%02d:%02d:%02d.%03d", getHours(), getMinutesClipped(), getSecondsClipped(), getMillisecondsClipped());
	}
	
	public static void main(String[] args) {
		Clock c = new Clock();
		for (int i = 0; i != 2*60*60; ++i) {
			c.update(1.1f);
			System.out.println(c);
		}
	}
}
