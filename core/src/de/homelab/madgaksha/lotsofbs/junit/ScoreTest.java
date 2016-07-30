package de.homelab.madgaksha.lotsofbs.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Score;

public class ScoreTest {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ScoreTest.class);

	@Test
	public void testIncreaseBy() {
		Score s = new Score();
		assertEquals(0L, s.getScore());
		for (int i = 0 ; i != 10 ; ++i) {
			s.increaseBy(10000L);
			assertEquals(10000L*(i+1), s.getScore());
		}
	}

	@Test
	public void testDecreaseBy() {
		Score s = new Score();
		s.increaseBy(100L);
		for (int i = 0 ; i != 10 ; ++i) {
			s.decreaseBy(10L);
			assertEquals(100L-10L*(i+1), s.getScore());
		}
	}

	@Test
	public void testNonNegativity() {
		Score s = new Score(Long.MIN_VALUE);
		assertEquals(0L, s.getScore());
		s.increaseBy(5L);
		for (int i = 0 ; i != 10 ; ++i) {
			s.decreaseBy(Long.MAX_VALUE);
			assertEquals(0L, s.getScore());
		}
	}
	
	@Test
	public void testOverflow() {
		Score s = new Score(Long.MAX_VALUE);
		assertEquals(Score.MAX_SCORE, s.getScore());
		s.increaseBy(Long.MAX_VALUE);
		assertEquals(Score.MAX_SCORE, s.getScore());
	}
	
	@Test
	public void testNegativeDeltas() {
		Score s = new Score(Long.MIN_VALUE);
		assertEquals(0L, s.getScore());
		s.increaseBy(42L);
		
		s.increaseBy(Long.MIN_VALUE);
		assertEquals(42L, s.getScore());
		
		s.decreaseBy(Long.MIN_VALUE);
		assertEquals(42L, s.getScore());
	}

	
	@Test
	public void testGetDigit() {
		Score s = new Score(123456789012L);
		for (int i = 0; i != 12; ++i) {
			assertEquals((i+1)%10, s.getDigit(i));
		}
	}
}
