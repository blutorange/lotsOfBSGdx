package de.homelab.madgaksha.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.logging.Logger;

/**
 * Reads a tiled map and extracts properties for later use. Properties must be
 * set on the
 * 
 * @author madgaksha
 *
 */
public class MapProperties {

	private final static Logger LOG = Logger.getLogger(MapProperties.class);

	/**
	 * 0 b 00000000 00000000 00000000 00000000 
	 *      Byte 4   Byte 3   Byte 2    Byte 1
	 * Byte 1:
	 *   Bit 1: is blocking
	 *   Bit 2:
	 *   Bit 3:
	 *   Bit 4:
	 *   Bit 5:
	 *   Bit 6:
	 *   Bit 7:
	 *   Bit 8:
	 * Byte 2:
	 * Byte 3:
	 * Byte 4:
	 */
	private int[][] tileFlags;
	/** Number of tiles. */
	private int width, height;
	/** Tile size in pixels. */
	private float widthTiles, heightTiles;
	/** Map size in pixels. */
	private int widthPixel, heightPixel;

	private float widthTilesInverse, heightTilesInverse;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/** @return Tile width in pixels. */
	public float getWidthTiles() {
		return widthTiles;
	}

	/** @return Tile height in pixels. */
	public float getHeightTiles() {
		return heightTiles;
	}

	/** @return Map width in pixels. */
	public int getWidthPixel() {
		return widthPixel;
	}

	/** @return Map height in pixels. */
	public int getHeightPixel() {
		return heightPixel;
	}

	private void setMapDimensions(TiledMap map) {
		width = height = 0;
		widthTiles = heightTiles = 0.0f;
		// Get map height and width
		for (int i = 0; i != map.getLayers().getCount(); ++i) {
			MapLayer ml = map.getLayers().get(i);
			if (ml instanceof TiledMapTileLayer) {
				final TiledMapTileLayer tmtl = ((TiledMapTileLayer) map.getLayers().get(i));
				width = Math.max(width, tmtl.getWidth());
				height = Math.max(height, tmtl.getHeight());
				widthTiles = Math.max(widthTiles, tmtl.getTileWidth());
				heightTiles = Math.max(heightTiles, tmtl.getTileHeight());
			}
		}
		widthTilesInverse = 1.0f / widthTiles;
		heightTilesInverse = 1.0f / heightTiles;
		widthPixel = (int) (width * widthTiles);
		heightPixel = (int) (height * heightTiles);
	}

	public MapProperties(TiledMap map) {
		setMapDimensions(map);
		tileFlags = new int[width][height]; // initialized to 0
		
		for (int i = 0; i != map.getLayers().getCount(); ++i) {
			MapLayer ml = map.getLayers().get(i);
			if (ml instanceof TiledMapTileLayer) {
				final TiledMapTileLayer tmtl = ((TiledMapTileLayer) map.getLayers().get(i));
				int layerWidth = tmtl.getWidth();
				int layerHeight = tmtl.getHeight();
				for (int x = 0; x != layerWidth ; ++x) {
					for (int y = 0; y != layerHeight; ++y) {
						final Cell c = tmtl.getCell(x, y);
						if (c != null) {
							com.badlogic.gdx.maps.MapProperties props = c.getTile().getProperties();
							if (props.containsKey("blocking") && Boolean.valueOf(props.get("blocking",String.class))) {
								tileFlags[x][y] |= 1; // 0b00000001;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This performs sanity checks.
	 * 
	 * @param x
	 *            Position (width) on the map in pixels.
	 * @param y
	 *            Position (height) on the map in pixels.
	 * @return Whether the tile is blocking.
	 */
	public boolean isTileBlocking(float w, float h) {
		int x = (int) (w * widthTilesInverse);
		int y = (int) (h * heightTilesInverse);
		return isTileBlocking(x < 0 ? 0 : x >= width ? width - 1 : x, y < 0 ? 0 : y >= height ? height - 1 : y);
	}

	/**
	 * This will not perform sanity checks.
	 * 
	 * @param x
	 *            Position (width) on the map in tiles.
	 * @param y
	 *            Position (height) on the map in tiles.
	 * @return Whether the tile is blocking.
	 */
	public boolean isTileBlocking(int x, int y) {
		return (tileFlags[x][y] & 1) != 0;
	}
}
