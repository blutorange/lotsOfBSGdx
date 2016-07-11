package de.homelab.madgaksha.lotsofbs.util;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class Clock {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Clock.class);

	private int ms = 0;
	private int s = 0;
	private int m = 0;
	private int h = 0;

	public Clock() {
	};

	/**
	 * Updates the clock.
	 * 
	 * @param deltaTime
	 *            Elapsed time since last update in seconds.
	 */
	public void update(float deltaTime) {
		ms += (int) (1000.0f * deltaTime);
		if (ms > 999) {
			s += ms / 1000;
			ms %= 1000;
			if (s > 59) {
				m += s / 60;
				s %= 60;
				if (m > 59) {
					h += m / 60;
					m %= 60;
				}
			}
		}
	}

	public int getMinutesOne() {
		return getMinutes() % 10;
	}

	public int getMinutesTen() {
		return getMinutes() / 10;
	}

	public int getSecondsOne() {
		return s % 10;
	}

	public int getSecondsTen() {
		return s / 10;
	}

	public int getMillisecondsHundred() {
		return ms / 100;
	}

	public int getMillisecondsTen() {
		return (ms % 100) / 10;
	}

	public int getMillisecondsOne() {
		return (ms % 10);
	}

	public int getHours() {
		return h;
	}

	public int getMinutes() {
		return m;
	}

	public int getSeconds() {
		return s;
	}

	public int getMilliseconds() {
		return ms;
	}

	public void reset() {
		ms = s = m = h = 0;
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d:%02d.%03d", h, m, s, ms);
	}

	public static void main(String[] args) {
		Clock c = new Clock();
		for (int i = 0; i != 2 * 60 * 60; ++i) {
			c.update(1.1f);
			System.out.println(c);
		}
	}
}
