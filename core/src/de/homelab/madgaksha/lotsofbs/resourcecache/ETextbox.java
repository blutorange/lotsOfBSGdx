package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.util.EnumMap;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.textbox.FancyTextbox;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.textbox.PlainTextbox;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * For loading, caching and disposing {@link FreeTypeFontGenerator} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETextbox implements IResource<ETextbox, PlainTextbox> {
	FC_BLUE(
			ENinePatch.TEXTBOX_BLUE_ALL,
			ENinePatch.TEXTBOX_BLUE_BOTTOM,
			ENinePatch.TEXTBOX_BLUE_TOP,
			ENinePatch.TEXTBOX_BLUE_LEFT,
			ENinePatch.TEXTBOX_BLUE_RIGHT,
			ENinePatch.TEXTBOX_BLUE_BOTTOM_LEFT,
			ENinePatch.TEXTBOX_BLUE_BOTTOM_RIGHT);

	private final static Logger LOG = Logger.getLogger(ETextbox.class);
	private final static EnumMap<ETextbox, PlainTextbox> textboxCache = new EnumMap<ETextbox, PlainTextbox>(
			ETextbox.class);

	private final ENinePatch ninePatchAll;
	private final ENinePatch ninePatchBottom;
	private final ENinePatch ninePatchTop;
	private final ENinePatch ninePatchLeft;
	private final ENinePatch ninePatchRight;
	private final ENinePatch ninePatchBottomLeft;
	private final ENinePatch ninePatchBottomRight;

	private ETextbox(ENinePatch ninePatchAll, ENinePatch ninePatchBottom, ENinePatch ninePatchTop,
			ENinePatch ninePatchLeft, ENinePatch ninePatchRight, ENinePatch ninePatchBottomLeft,
			ENinePatch ninePatchBottomRight) {
		this.ninePatchAll = ninePatchAll;
		this.ninePatchBottom = ninePatchBottom;
		this.ninePatchTop = ninePatchTop;
		this.ninePatchLeft = ninePatchLeft;
		this.ninePatchRight = ninePatchRight;
		this.ninePatchBottomLeft = ninePatchBottomLeft;
		this.ninePatchBottomRight = ninePatchBottomRight;
	}

	public static void clearAll() {
		LOG.debug("clearing all textboxes");
		for (ETextbox tb : textboxCache.keySet()) {
			tb.clear();
		}
	}

	public static Set<ETextbox> getMapKeys() {
		return textboxCache.keySet();
	}

	@Override
	public PlainTextbox getObject() {
		try {
			final NinePatch all = ResourceCache.getNinePatch(ninePatchAll);
			final NinePatch bottom = ResourceCache.getNinePatch(ninePatchBottom);
			final NinePatch top = ResourceCache.getNinePatch(ninePatchTop);
			final NinePatch left = ResourceCache.getNinePatch(ninePatchLeft);
			final NinePatch right = ResourceCache.getNinePatch(ninePatchRight);
			final NinePatch bottomLeft = ResourceCache.getNinePatch(ninePatchBottomLeft);
			final NinePatch bottomRight = ResourceCache.getNinePatch(ninePatchBottomRight);
			if (all == null || bottom == null || top == null || bottomLeft == null || bottomRight == null
					|| left == null || right == null)
				return null;
			return new FancyTextbox(all, bottom, top, left, right, bottomLeft, bottomRight, this);
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public Enum<ETextbox> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_FREE_TYPE_FONT_GENERATOR;
	}

	@Override
	public void clear() {
		LOG.debug("clearing textbox: " + String.valueOf(this));
		final PlainTextbox tb = textboxCache.get(this);
		if (tb != null)
			tb.dispose();
		textboxCache.remove(this);
	}

	@Override
	public EnumMap<ETextbox, PlainTextbox> getMap() {
		return textboxCache;
	}

	@Override
	public void clearAllOfThisKind() {
		ETextbox.clearAll();
	}
}