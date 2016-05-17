package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETiledMap implements IResource {	
	LEVEL_01("map/Level01.tmx", Type.TMX);

	private static enum Type {
		TMX;
	}
	
	private final static Logger LOG = Logger.getLogger(ETiledMap.class);
	private final static EnumMap<ETiledMap, TiledMap> tiledMapCache = new EnumMap<ETiledMap, TiledMap>(ETiledMap.class);
	private String filename;
	private Type mapType;
	
	private ETiledMap(String f, Type t) {
		filename = f;
		mapType = t;
	}

	public static void clearAll() {
		LOG.debug("clearing all maps");
		for (ETiledMap map : tiledMapCache.keySet()) {
			map.clear();
		}
	}
	@Override
	public TiledMap getObject() {
		switch (mapType) {
		case TMX:
			try {
				return new TmxMapLoader().load(filename);
			} catch (Exception e) {
				LOG.error("could not locate or open resource: " + String.valueOf(this), e);
				return null;
			}
		default:
			return null;
		}
	}

	@Override
	public Enum<?> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_TILED_MAP;
	}

	@Override
	public void clear() {
		LOG.debug("disposing tiled map: " + String.valueOf(this));
		final TiledMap tm = tiledMapCache.get(this);
		if (tm != null)
			tm.dispose();
		tiledMapCache.remove(this);
	}

	@Override
	public EnumMap<ETiledMap, TiledMap> getMap() {
		return tiledMapCache;
	}
}