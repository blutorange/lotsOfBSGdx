package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import java.util.Locale;

import org.apache.commons.lang3.text.WordUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.entityengine.entity.ParticleEffectMaker;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EParticleEffect;

/**
 * Reads a tiled map and extracts properties for later use. Properties must be
 * set on the
 *
 * Objects can be added directly via the map editor.
 * 
 * <br><br>
 * 
 * <h2>Global properties</h2>
 * 
 * <ul>
 *   <li>type : Object type set in tiled map editor on the top right panel. Case insensitive.</li>
 * </ul>
 * 
 * The following types and properties are supported: 
 * 
 * <h3>type: Enemy</h3>
 * <br>
 * Represents an enemy that can engage in combat with the player.
 * <ul>
 * <li>species: The type of a enemy. There must exist a corresponding subclass of {@link AEnemy} with the name <code>Enemy&lt;species&gt;</code>.</li>
 * <li>spawn: How and when the enemy should spawn. Possible values are:
 *  <ul>
 *   <li>startup: When the map has finished loading.
 *   <li>screen: When the enemy becomes visible on screen.</li>
 *   <li>manual: can only be triggered via a backing java function from a class of type {@link ALevel}. See type <code>callback</code>.</li>
 *  </ul> 
 * </ul>
 *  
 * <h3>type: Callback</h3>
 * <br>
 * An event with a backing callback java function that will be invoked when the event triggers. The callback function
 * must have this signature:
 * 
 * <code>public void myCallback(AMapObject mapObject)</code>
 * 
 * <ul>
 * <li>name: Name of the callback function. Must be defined in the implementing {@link ALevel} class.</li>
 * <li>trigger: How and when the event should get triggered. Possible values are:
 *  <ul>
 *   <li>startup: When the map has finished loading.
 *   <li>screen: When the event shape becomes visible on screen.</li>
 *   <li>touch: Triggers only when the player touches the even shape.</li>
 *  </ul> 
 * <li> loop: Number of times the event should repeat after it was first triggered. Use 0 for no loop, -1 for infinite loop.</li>
 * <li> interval: Time in seconds between two loops, see the <code>loop</code> property.
 * </ul>
 * 
 * 
 * <h3>type: ParticleEffect</h3>
 * Adds a particle effect positioned at the center of the event shape.
 * <ul>
 *  <li>name: Name of the particle effect. Must be defined in {@link EParticleEffect}</li>
 * </ul>
 * @author madgaksha
 *
 */
public class MapData {

	private final static Logger LOG = Logger.getLogger(MapData.class);
	private final static String  ENEMY_PACKAGE = "de.homelab.madgaksha.entityengine.entity.enemy.";
	
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
	
	private final Class<? extends ALevel> levelClass;
	
	/** Number of tiles. */
	private int width, height;
	/** Tile size in pixels. */
	private float widthTiles, heightTiles;
	/** Map size in pixels. */
	private int widthPixel, heightPixel;

	private float widthTilesInverse, heightTilesInverse;

	// ===========================
	//       public methods
	// ===========================
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
	
	public MapData(TiledMap map, Class<? extends ALevel> levelClass) {
		setMapDimensions(map);
		tileFlags = new int[width][height]; // initialized to 0
		this.levelClass = levelClass;
		
		for (int i = 0; i != map.getLayers().getCount(); ++i) {
			MapLayer mapLayer = map.getLayers().get(i);
			if (mapLayer instanceof TiledMapTileLayer) {
				processTiledMapLayer(((TiledMapTileLayer) map.getLayers().get(i)));
			}
			else {
				for (MapObject mapObject : mapLayer.getObjects())
					processMapObject(mapObject);
			}
		}
	}
	
	// =========================
	// Internal private methods.
	// =========================
	
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

	
	private void processTiledMapLayer(TiledMapTileLayer tmtl) {
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
	
	private void processMapObject(MapObject mapObject) {
		if (!mapObject.isVisible()) return;
		
		// Get shape of the object.
		Shape2D shape = null;
		try {
			if (mapObject instanceof CircleMapObject) {
				shape = ((CircleMapObject)mapObject).getCircle();
			}
			else if (mapObject instanceof EllipseMapObject) {
				shape = ((EllipseMapObject)mapObject).getEllipse();
			}
			else if (mapObject instanceof RectangleMapObject) {
				shape = ((RectangleMapObject)mapObject).getRectangle();
			}
			else if (mapObject instanceof PolylineMapObject) {
				shape = ((PolygonMapObject)mapObject).getPolygon();
			}
			else if (mapObject instanceof PolygonMapObject) {
				shape = ((PolygonMapObject)mapObject).getPolygon();
			}
		}
		catch (Exception e) {
			LOG.error("failed to read shape of map object", e);
			return;
		}
		
		// Unknown shape.
		if (shape == null) {
			LOG.error("map object with unknown shape");
			return;
		}
		
		// Get properties and check if it has got a type.
		MapProperties props = mapObject.getProperties();
		if (!props.containsKey("type")) {
			LOG.error("map object does not specify a type");
			return;
		}

		// Try and create a new map object of the specified type.
		Entity entity = null;
		try {
			String type = props.get("type", String.class);
			if (type == null) return;
			type = type.toLowerCase(Locale.ROOT);
			if (type.equals("enemy")) {
				entity = processObjectEnemy(props, shape);
			}
			else if (type.equals("particleeffect")) {
				entity = processObjectParticleEffect(props, shape);
			}
			else if (type.equals("callback")) {
				entity = processObjectCallback(props, shape);
			}
			else {
				LOG.info("unknown map object type: " + type);
			}
		}
		catch (Exception e) {
			LOG.info("failed to read map object: " + mapObject, e);
			return;
		}
		
		// Unknown or invalid object.
		if (entity == null) {
			LOG.error("failed to read object");
			return;
		}
		
		LOG.debug("read map object to entity " + entity);
		
		// Store our map object for later use.
		gameEntityEngine.addEntity(entity);
	}

	@SuppressWarnings("unchecked")
	private Entity processObjectEnemy(MapProperties props, Shape2D shape) {
		// Fetch parameters.
		String species = WordUtils.capitalizeFully(props.get("species", String.class));
		String spawn = props.get("spawn", String.class).toUpperCase(Locale.ROOT);

		// Get species class and its constructor with the appropriate constructor.
		Constructor enemyConstructor;
		try {
			Class<? extends EnemyMaker> enemyClass = ClassReflection.forName(ENEMY_PACKAGE + species + "Maker");
			enemyConstructor = ClassReflection.getConstructor(enemyClass, Shape2D.class, ETrigger.class);
		}
		catch (Exception e) {
			LOG.error("no such enemy class with appropriate constructor: " + ENEMY_PACKAGE + "Enemy" + species, e);
			return null;
		}
		
		// Get spawn type.
		ETrigger spawnEnum;
		try {
			spawnEnum = ETrigger.valueOf(spawn);
		}
		catch (Exception e) {
			LOG.error("invalid spawn type for enemy", e);
			return null;
		}
		
		// Try to initialize a new enemy of the given class.
		Entity enemy;
		try {
			enemy = (Entity)enemyConstructor.newInstance(shape, spawnEnum);
		} catch (Exception e) {
			LOG.error("failed to initialize enemy instance: " + species, e);
			return null;
		}
		
		return enemy;
	}
	
	private Entity processObjectCallback(MapProperties props, Shape2D shape) {		
		// Fetch parameters.
		String name = props.get("name", String.class);
		String trigger = props.get("trigger", String.class).toUpperCase(Locale.ROOT);
		Integer loop = 0;
		Float interval = 1.0f;
		if (props.containsKey("loop")) loop = props.get("loop", Integer.class);
		if (props.containsKey("interval")) interval = props.get("interval", Float.class);
				
		// Search for the method and perform sanity checks.
		Method method;
		try {
			method = ClassReflection.getDeclaredMethod(levelClass, name, Entity.class);
			if (method.getReturnType() != Void.TYPE) {
				LOG.error("callback function must return void");
				return null;
			}
			if (!method.isPublic()) {
				LOG.error("callback method must be public");
				return null;
			}
		}
		catch (Exception e) {
			LOG.error("cannot instantiate callback map object: no such method: " + name, e);
			return null;
		}
		
		// Get trigger type.
		ETrigger triggerEnum;
		try {
			triggerEnum = ETrigger.valueOf(trigger);
		}
		catch (Exception e) {
			LOG.error("invalid callback trigger type: " + trigger, e);
			return null;
		}
				
		// Create new object and return it.
		return new CallbackMaker(shape, triggerEnum, method, loop, interval);
	}

	private Entity processObjectParticleEffect(MapProperties props, Shape2D shape) {
		// Fetch parameters.
		String name = props.get("name", String.class).toUpperCase(Locale.ROOT);
		
		// Try to get the corresponding enum.
		EParticleEffect particleEffect;
		try {
			particleEffect = EParticleEffect.valueOf(name);
		}
		catch (Exception e) {
			LOG.error("no such particle effect: " + name, e);
			return null;
		}
		
		return new ParticleEffectMaker(shape, particleEffect);
	}	
}
