package de.homelab.madgaksha.lotsofbs.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.level.ALevel;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class Score {
	private final static Logger LOG = Logger.getLogger(Score.class);
	private final static long scoreMax = 999999999999L;
	private final static int numberOfDigits = 12;

	private final int[] digits = new int[numberOfDigits];
	private long score = 0L;

	public Score() {
	}

	public void increaseBy(long ds) {
		score += ds;
		if (score > scoreMax)
			score = scoreMax;
		computeDigits();
	}

	public void decreaseBy(long ds) {
		score -= ds;
		if (score < 0)
			score = 0L;
		computeDigits();
	}

	public int getDigit(int i) {
		return digits[i];
	}

	public long getScore() {
		return score;
	}

	private void computeDigits() {
		long digit = score;
		for (int i = digits.length; i-- > 0;) {
			digits[i] = (int) (digit % 10L);
			digit /= 10;
		}
	}

	/**
	 * Writes the current score for the current level to the highscore file.
	 * Does nothing if the current score is lower than the highscore.
	 */
	public void writeScore(InputStream scoreInputStream, OutputStream scoreOutputStream) {
		writeScore(scoreInputStream, scoreOutputStream, GlobalBag.level);
	}

	/**
	 * Writes the current score for the given level to the highscore file. Does
	 * nothing if the current score is lower than the highscore.
	 * 
	 * @param level
	 *            Level to which this score belongs.
	 */
	public void writeScore(InputStream scoreInputStream, OutputStream scoreOutputStream, ALevel level) {
		// Merge current score with existing scores.
		Map<Class<? extends ALevel>, Long> scoreMap = Score.readScore(scoreInputStream);
		Class<? extends ALevel> levelClass = level.getClass();
		if (scoreMap.containsKey(levelClass)) {
			Long oldScore = scoreMap.get(levelClass);
			if (score > oldScore) {
				scoreMap.put(levelClass, score);
			}
		} else
			scoreMap.put(levelClass, score);
		// Write scores to file.
		OutputStream os = null;
		try {
			Json json = new Json();
			StringWriter sw = new StringWriter();
			json.setWriter(sw);
			json.writeObjectStart();
			for (Class<? extends ALevel> key : scoreMap.keySet()) {
				json.writeValue(key.getCanonicalName(), scoreMap.get(key));
			}
			json.writeObjectEnd();
			String serialized = sw.toString();
			scoreOutputStream.write(serialized.getBytes(Charset.forName("UTF-8")));
		} catch (Exception e) {
			LOG.error("could not write score", e);
		} finally {
			if (os != null)
				IOUtils.closeQuietly(os);
		}
	}

	/**
	 * Reads the score file and returns is as a map with the level class as the
	 * key and the score as the value.
	 * 
	 * @return The score for each level. Empty map when file not found or if it
	 *         could not be read.
	 */
	public static Map<Class<? extends ALevel>, Long> readScore(InputStream scoreInputStream) {
		Map<Class<? extends ALevel>, Long> scoreMap = new HashMap<Class<? extends ALevel>, Long>();
		if (scoreInputStream != null) {
			try {
				// Open file.
				JsonReader reader = new JsonReader();
				JsonValue val = reader.parse(scoreInputStream).child();
				// Parse each entry with the level class and score.
				do {
					String levelName = val.name();
					long score = val.asLong();
					try {
						@SuppressWarnings("unchecked")
						Class<? extends ALevel> levelClass = (Class<? extends ALevel>) ClassReflection
								.forName(levelName);
						scoreMap.put(levelClass, score);
					} catch (ReflectionException e) {
						LOG.error("no such level: " + levelName);
					}
				} while ((val = val.next()) != null);
			} catch (Exception e) {
				LOG.error("could not read score file", e);
			}
		}
		return scoreMap;
	}
}
