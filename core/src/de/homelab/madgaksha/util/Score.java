package de.homelab.madgaksha.util;

import de.homelab.madgaksha.logging.Logger;

public class Score {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Score.class);
	private final static long scoreMax = 999999999999L;
	private final static int numberOfDigits = 12;
	
	private final int[] digits = new int[numberOfDigits];
	private long score = 0L;
	
	public Score() {}
	
	public void increaseBy(long ds) {
		score += ds;
		if (score > scoreMax) score = scoreMax;
		computeDigits();
	}
	public void decreaseBy(long ds) {
		score -= ds;
		if (score < 0) score = 0L;
		computeDigits();
	}
	
	public int getDigit(int i) {
		return digits[i];
	}
	
	private void computeDigits() {
		long digit = score;
		for (int i = digits.length; i --> 0;) {
			digits[i] = (int)(digit % 10L);
			digit /= 10;
		}
	}
}
