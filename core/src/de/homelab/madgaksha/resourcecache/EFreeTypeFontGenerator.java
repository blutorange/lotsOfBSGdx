package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.cutscenesystem.textbox.PlainTextbox;
import de.homelab.madgaksha.enums.VerticalAlignPosition;
import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link FreeTypeFontGenerator} resources.
 * 
 * @author madgaksha
 *
 */
public enum EFreeTypeFontGenerator implements IResource<EFreeTypeFontGenerator, FreeTypeFontGenerator> {
	MAIN_FONT("mainFont");

	private final static Logger LOG = Logger.getLogger(EFreeTypeFontGenerator.class);
	private final static EnumMap<EFreeTypeFontGenerator, FreeTypeFontGenerator> freeTypeFontGeneratorCache = new EnumMap<EFreeTypeFontGenerator, FreeTypeFontGenerator>(
			EFreeTypeFontGenerator.class);

	private final String fontName;

	/**
	 * The reference line used for centering text vertically. For Latin, between
	 * base line and x height works well. For Japanese/Chinese, between base
	 * line and cap height works well.
	 */
	private VerticalAlignPosition verticalAlignPosition = null;

	private EFreeTypeFontGenerator(String fontName) {
		this.fontName = fontName;
	}

	public static void clearAll() {
		LOG.debug("clearing all free type font generators");
		for (EFreeTypeFontGenerator ftfg : freeTypeFontGeneratorCache.keySet()) {
			ftfg.clear();
		}
	}

	public static Set<EFreeTypeFontGenerator> getMapKeys() {
		return freeTypeFontGeneratorCache.keySet();
	}

	@Override
	public FreeTypeFontGenerator getObject() {
		final FileHandle fileHandle = Gdx.files.internal("font/" + I18n.font(fontName));
		try {
			return new FreeTypeFontGenerator(fileHandle);
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public Enum<EFreeTypeFontGenerator> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_FREE_TYPE_FONT_GENERATOR;
	}

	@Override
	public void clear() {
		LOG.debug("disposing free type font generator: " + String.valueOf(this));
		final FreeTypeFontGenerator ftfg = freeTypeFontGeneratorCache.get(this);
		if (ftfg != null)
			ftfg.dispose();
		freeTypeFontGeneratorCache.remove(this);
	}

	@Override
	public EnumMap<EFreeTypeFontGenerator, FreeTypeFontGenerator> getMap() {
		return freeTypeFontGeneratorCache;
	}

	/**
	 * Determines characters required for the current text, ie. the set of
	 * unique characters in {@link PlainTextbox#lineList}.
	 * 
	 * @param lineList
	 *            Array of lines.
	 * @param start
	 *            First line to process.
	 * @param nd
	 *            Last line to process (exclusive).
	 */
	public static String getRequiredCharacters(CharSequence[] lineList, int start, int end) {
		// Get characters that need to be rendered.
		final StringBuilder stringBuilder = new StringBuilder();
		final Set<Character> charset = new HashSet<Character>();
		for (int i = start; i != end; ++i) {
			for (int j = lineList[i].length(); j-- > 0;) {
				charset.add(lineList[i].charAt(j));
			}
		}
		for (Character c : charset) {
			stringBuilder.append(c);
		}
		return stringBuilder.toString();
	}

	/**
	 * Determines characters required for the current text, ie. the set of
	 * unique characters in {@link PlainTextbox#lineList}.
	 * 
	 * @param text
	 *            Text to process.
	 */
	public static String getRequiredCharacters(String text) {
		// Get characters that need to be rendered.
		final StringBuilder stringBuilder = new StringBuilder();
		final Set<Character> charset = new HashSet<Character>();
		for (int i = 0; i != text.length(); ++i) {
			charset.add(text.charAt(i));
		}
		for (Character c : charset)
			stringBuilder.append(c);
		return stringBuilder.toString();
	}

	@Override
	public void clearAllOfThisKind() {
		EFreeTypeFontGenerator.clearAll();
	}

	private void retrieveVerticalAlignPosition() {
		String vertAlignKey = fontName + "VerticalAlign";
		VerticalAlignPosition vertAlign = VerticalAlignPosition.BETWEEN_BASE_LINE_AND_X_HEIGHT;
		try {
			if (I18n.isInitiated() && I18n.hasFontKey(vertAlignKey)) {
				vertAlign = VerticalAlignPosition.valueOf(I18n.font(vertAlignKey).toUpperCase(Locale.ROOT));
			} else {
				throw new IllegalArgumentException("i18n key does not exist");
			}
		} catch (IllegalArgumentException e) {
			Logger logger = Logger.getLogger(EFreeTypeFontGenerator.class);
			logger.error(
					"could not read vertical align position for language " + I18n.getShortName() + " and font " + this,
					e);
			logger.error("assure a proper value has been set for the key " + vertAlignKey);
		}
		this.verticalAlignPosition = vertAlign;
	}
	
	public VerticalAlignPosition getVerticalAlignPosition() {
		if (verticalAlignPosition == null) retrieveVerticalAlignPosition();
		return verticalAlignPosition;
	}
}