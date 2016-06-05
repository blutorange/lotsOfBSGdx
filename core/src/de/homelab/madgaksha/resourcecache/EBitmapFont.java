package de.homelab.madgaksha.resourcecache;

import static de.homelab.madgaksha.GlobalBag.bitmapFontRasterSize;

import java.util.EnumMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link BitmapFont} resources.
 * 
 * @author madgaksha
 *
 */
public enum EBitmapFont implements IResource<EBitmapFont,BitmapFont> {
	MAIN_FONT("mainFont");
	private final static Logger LOG = Logger.getLogger(EBitmapFont.class);
	private final static EnumMap<EBitmapFont, BitmapFont> bitmapFontCache = new EnumMap<EBitmapFont, BitmapFont>(EBitmapFont.class);

	private String fontname;

	private EBitmapFont(String f) {
		fontname = f;
	}

	public static void clearAll() {
		LOG.debug("clearing all bitmap fonts");
		for (EBitmapFont bf : bitmapFontCache.keySet()) {
			bf.clear();
		}
	}
	public static Set<EBitmapFont> getMapKeys() {
		return bitmapFontCache.keySet();
	}

	@Override
	public BitmapFont getObject() {
		final FileHandle fileHandle = Gdx.files.internal("font/" + I18n.font(fontname));
		FreeTypeFontGenerator g = null; 
		try {
			g = new FreeTypeFontGenerator(fileHandle);
			FreeTypeFontParameter p = new FreeTypeFontParameter();
			p.size = bitmapFontRasterSize;
			p.characters = I18n.font(fontname + "Chars");
			p.kerning = true;
			p.borderColor = Color.BLACK;
			p.borderWidth = 1;
			p.shadowOffsetX = 2;
			p.shadowOffsetY = 3;
			p.shadowColor = new Color(0f,0f,0f,0.5f);
			p.color = Color.WHITE;
			BitmapFont bf = g.generateFont(p);
			return bf;
		} catch (Exception e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
		finally {
			if (g != null) g.dispose();
		}
	}

	@Override
	public Enum<EBitmapFont> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_BITMAP_FONT;
	}

	@Override
	public void clear() {
		LOG.debug("disposing bitmap font: " + String.valueOf(this));
		final BitmapFont bf = bitmapFontCache.get(this);
		if (bf != null)
			bf.dispose();
		bitmapFontCache.remove(this);
	}

	@Override
	public EnumMap<EBitmapFont, BitmapFont> getMap() {
		return bitmapFontCache;
	}
}