package de.homelab.madgaksha.lotsofbs.util;

public class InclusiveRange<T> {
	/** Lower limit of the range (inclusive). */
	public T min;
	/** Upper limit of the range (inclusive). */
	public T max;

	/**
	 * Constructs a new range with the given lower and upper limit (inclusive).
	 * 
	 * @param min
	 * @param max
	 */
	public InclusiveRange(T min, T max) {
		this.min = min;
		this.max = max;
	}
}
