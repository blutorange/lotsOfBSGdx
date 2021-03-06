package de.homelab.madgaksha.lotsofbs.level;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.idEntityMap;

import java.util.Locale;

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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.lotsofbs.DebugMode;
import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.component.IdComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.NpcMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ParticleEffectMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.enemy.SoldierRedMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.enums.Gravity;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;
import de.homelab.madgaksha.lotsofbs.player.consumable.AConsumable;
import de.homelab.madgaksha.lotsofbs.player.consumable.EConsumable;
import de.homelab.madgaksha.lotsofbs.player.tokugi.ATokugi;
import de.homelab.madgaksha.lotsofbs.player.tokugi.ETokugi;
import de.homelab.madgaksha.lotsofbs.player.weapon.AWeapon;
import de.homelab.madgaksha.lotsofbs.player.weapon.EWeapon;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.util.GeoUtil;
import de.homelab.madgaksha.lotsofbs.util.LocaleRootWordUtils;

/**
 * Reads a tiled map and extracts properties for later use. Objects can be added
 * directly via the map editor. All tile layer must have the same tile size.
 * Tile height and width may be different.
 *
 * <br>
 * <br>
 *
 * <h2>Map properties</h2>
 *
 * <ul>
 * <li>baseDirX: X-component of the base direction of the camera when there are
 * no target points.</li>
 * <li>baseDirY: Y-component of the base direction of the camera when there are
 * no target points.</li>
 * <li>gravity: Preferred direction of the screen where the player will be
 * located.</li>
 * <li>minElevation: Minimum distance of the camera above the world.</li>
 * <li>maxElevation: Maximum distance of the camera above the world.</li>
 * <li>playerX: Initial position x of the player in tiles.</li>
 * <li>playerY: Initial position y of the player in tiles.</li>
 * </ul>
 *
 * <h2>Global properties</h2>
 *
 * <ul>
 * <li>type : Object type set in tiled map editor on the top right panel. Case
 * insensitive.</li>
 * <li>guid: An id used to refer to objects in cutscene scripts etc. Should be
 * unique. Undefined behaviour when not unique. Case-insensitive. Must not
 * contain any characters other than letters and number (a-z, A-Z, 0-9). See
 * also {@link IdComponent}.</li>
 * </ul>
 *
 * For convenience, if an event specifies a name (eg. Callback) and there is no
 * event with that GUID, one event with that name is added to the GUID list with
 * its name. <br>
 * Therefore, do not use this if there is more than one event with the same
 * name. In that case, give the events a unique GUID. <br>
 * For example, when you've got excactly one callback with the name
 * "turnbackPlayer" you do not need to specify a GUID separately.
 *
 * <br>
 * <br>
 *
 * The following types and properties are supported:
 *
 * <h3>type: Enemy</h3> <br>
 * Represents an enemy that can engage in combat with the player.
 * <ul>
 * <li>species: The type of a enemy. There must exist a corresponding subclass
 * of {@link EnemyMaker} with the name <code>Enemy&lt;species&gt;</code>.</li>
 * <li>spawn: How and when the enemy should spawn. Possible values are:
 * <ul>
 * <li>startup: When the map has finished loading.
 * <li>screen: When the enemy becomes visible on screen.</li>
 * <li>manual: Can only be triggered via a backing java function from a class of
 * type {@link ALevel}. See type <code>callback</code>.</li>
 * <li>touch: When the bounding box of the enemy is touched.</li>
 * </ul>
 * <li>battleIn: (optional) Distance when enemy starts battling, in tiles
 * (longer side). Default 35</li>
 * <li>battleOut: (optional) Distance when enemy stops battling, in tiles
 * (longer side). Default 45</li>
 * <li>initX: (optional) Displacement of the initial position in x-direction in
 * tiles. If 0, the enemy is placed at the center of the bounding box of the
 * object's shape.</li>
 * <li>initY: (optional) Displacement of the initial position in y-direction in
 * tiles. If 0, the enemy is placed at the center of the bounding box of the
 * object's shape.</li>
 * <li>initDir: (optional) Initial looking direction in degrees. 0 is down, 90
 * is to the right. Default 0.</li>
 * </ul>
 *
 * <h3>type: Callback</h3> <br>
 * An event with a backing callback java function that will be invoked when the
 * event triggers. The callback function must have this signature:
 *
 * <code>public void myCallback(MapProperties properties)</code>
 *
 * <ul>
 * <li>name: Name of the callback function. Must be defined in the implementing
 * {@link ALevel} class.</li>
 * <li>trigger: How and when the event should get triggered. Possible values
 * are:
 * <ul>
 * <li>startup: When the map has finished loading.
 * <li>screen: When the event shape becomes visible on screen.</li>
 * <li>touch: Triggers only when the player touches the even shape.</li>
 * </ul>
 * <li>loop: Number of times the event should repeat after it was first
 * triggered. Use 0 for no loop. Use -1 to keep the event ready to be triggered.
 * Use {@link ComponentUtils#reactivateCallback(String name)} to reactivate the
 * callback.</li>
 * <li>interval: Time in seconds between two loops, see the <code>loop</code>
 * property.
 * </ul>
 *
 *
 * <h3>type: ParticleEffect</h3> Adds a particle effect positioned at the center
 * of the event shape.
 * <ul>
 * <li>name: Name of the particle effect. Must be defined in
 * {@link EParticleEffect}</li>
 * <li>speed: How fast the particle effect should rotate. Useful for flame
 * wheels etc.</li>
 * <li>renderMode: Whether to draw in screen or game coordinates. Position is
 * unaffected, but useful for rotation if particle effect should always point
 * upwards (as seen on the screen).</li>
 * </ul>
 *
 * <h3>type: NPC</h3>
 *
 * <ul>
 * <li>animation: The NPC's {@link EAnimation}.</li>
 * <li>animationList: The NPC's {@link EAnimationList}. Takes priority if
 * animation is given as well.</li>
 * <li>startup: (optional) Whether the NPC should be visible or invisible when
 * the game starts. Possible values are "visible" and "invisible". Default
 * visible.</li>
 * <li>initX: (optional) Displacement of the initial position in x-direction in
 * tiles. If 0, the enemy is placed at the center of the bounding box of the
 * object's shape.</li>
 * <li>initY: (optional) Displacement of the initial position in y-direction in
 * tiles. If 0, the enemy is placed at the center of the bounding box of the
 * object's shape.</li>
 * <li>initDir: (optional) Initial looking direction in degrees. 0 is down, 90
 * is to the right. Default 0.</li>
 * </ul>
 *
 * <h3>type: Item</h3> <br>
 * An collectible item. Items are divided into weapons, specials (special
 * attack) and consumables (recovery).
 *
 * <ul>
 * <li>category: Category of this item. Possible values are <code>weapon</code>,
 * <code>tokugi</code>, and <code>consumable</code></li>
 * <li>name: Name of this item. A corresponding java class must exists for this
 * item. For weapons, the class must be called WeaponXXX, for special attacks it
 * must be called TokugiXXX, and for consumables it must be called
 * ConsumableXXX. Additionally, the class must be located in the same package as
 * {@link AWeapon}, {@link ATokugi}, and {@link AConsumable}.
 * <li>speed: Angular velocity at which the item model should rotate, in
 * degrees/second. Default 45.</li>
 * <li>axisX, axisY, axisZ: (optional) Axis around which the item model should
 * rotate. Does not need be normed. Default (1,1,0)(</li>
 * </ul>
 *
 * @author madgaksha
 *
 */
public class MapData {

	private final static Logger LOG = Logger.getLogger(MapData.class);
	private final static String ENEMY_PACKAGE = SoldierRedMaker.class.getPackage().getName() + ".";

	private final static Vector2 v1 = new Vector2();

	/**
	 * 0 b 00000000 00000000 00000000 00000000 Byte 4 Byte 3 Byte 2 Byte 1 Byte
	 * 1: Bit 1: is blocking Bit 2: Bit 3: Bit 4: Bit 5: Bit 6: Bit 7: Bit 8:
	 * Byte 2: Byte 3: Byte 4:
	 */
	private final int[][] tileFlags;

	private final Class<? extends ALevel> levelClass;

	/** Number of tiles. */
	private int width, height;
	/** Tile size in pixels. */
	private float widthTiles, heightTiles;
	/** Map size in pixels. */
	private int widthPixel, heightPixel;

	private Vector2 playerInitialPosition;
	private Vector2 baseDirection;
	private float minimumCameraElevation;
	private float maximumCameraElevation;
	private Gravity preferredPlayerLocation;
	private float playerInitialDirection;
	private float widthTilesInverse, heightTilesInverse;

	// ===========================
	// public methods
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
	public boolean isTileBlocking(final float w, final float h) {
		final int x = (int) (w * widthTilesInverse);
		final int y = (int) (h * heightTilesInverse);
		return isTileBlocking(x < 0 ? 0 : x >= width ? width - 1 : x, y < 0 ? 0 : y >= height ? height - 1 : y);
	}

	public int getTileX(final float w) {
		return (int) (w * widthTilesInverse);
	}

	public int getTileY(final float h) {
		return (int) (h * heightTilesInverse);
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
	public boolean isTileBlocking(final int x, final int y) {
		return (tileFlags[x][y] & 1) != 0;
	}

	/**
	 * @param y
	 *            Tile y
	 * @param xMin
	 *            Range lower.
	 * @param xMax
	 *            Range upper.
	 * @return True iff any tile within the range (xMin..xMax,y) is blocking.
	 */
	public boolean isTileRangeXAnyBlocking(final int y, final int xMin, final int xMax) {
		for (int x = xMin; x <= xMax; ++x)
			if ((tileFlags[x][y] & 1) != 0)
				return true;
		return false;
	}

	/**
	 * @param x
	 *            Tile x.
	 * @param yMin
	 *            Range lower.
	 * @param yMax
	 *            Range upper.
	 * @return True iff any tile within the range (x,yMin..yMax) is blocking.
	 */
	public boolean isTileRangeYAnyBlocking(final int x, final int yMin, final int yMax) {
		for (int y = yMin; y <= yMax; ++y)
			if ((tileFlags[x][y] & 1) != 0)
				return true;
		return false;
	}

	public boolean isTileAreaAnyBlocking(final int xMin, final int xMax, final int yMin, final int yMax) {
		for (int x = xMin; x <= xMax; ++x)
			for (int y = yMin; y <= yMax; ++y)
				if ((tileFlags[x][y] & 1) != 0)
					return true;
		return false;
	}

	public MapData(final TiledMap map, final Class<? extends ALevel> levelClass) {
		setMapDimensions(map);
		tileFlags = new int[width][height]; // initialized to 0
		this.levelClass = levelClass;

		processMapProperties(map.getProperties());

		for (int i = 0; i != map.getLayers().getCount(); ++i) {
			final MapLayer mapLayer = map.getLayers().get(i);
			if (mapLayer instanceof TiledMapTileLayer) {
				processTiledMapLayer(((TiledMapTileLayer) map.getLayers().get(i)));
			} else {
				for (final MapObject mapObject : mapLayer.getObjects())
					processMapObject(mapObject);
			}
		}
	}

	// =========================
	// Internal private methods.
	// =========================

	private void setMapDimensions(final TiledMap map) {
		width = height = 0;
		widthTiles = heightTiles = 0.0f;
		// Get map height and width
		for (int i = 0; i != map.getLayers().getCount(); ++i) {
			final MapLayer ml = map.getLayers().get(i);
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

	private void processTiledMapLayer(final TiledMapTileLayer tmtl) {
		final int layerWidth = tmtl.getWidth();
		final int layerHeight = tmtl.getHeight();
		for (int x = 0; x != layerWidth; ++x) {
			for (int y = 0; y != layerHeight; ++y) {
				final Cell c = tmtl.getCell(x, y);
				if (c != null) {
					final com.badlogic.gdx.maps.MapProperties props = c.getTile().getProperties();
					if (props.containsKey("blocking") && props.get("blocking", Boolean.class)) {
						tileFlags[x][y] = 1; // 0b00000001;
					} else {
						tileFlags[x][y] = 0;
					}
				}
			}
		}
	}

	private void processMapObject(final MapObject mapObject) {
		if (!mapObject.isVisible())
			return;

		// Get shape of the object.
		final Shape2D shape = getMapObjectShape(mapObject);
		// Unknown shape.
		if (shape == null)
			return;

		// Read properties
		final MapProperties props = mapObject.getProperties();
		if (props == null) {
			LOG.error("object does not specify map properties: " + mapObject.getName());
			return;
		}

		if (isDebugObject(props)) {
			LOG.debug("skipping map item as it is marked debug-only: " + mapObject.getName());
			return;
		}

		// Try and create a new map object of the specified type.
		final Entity entity = createEntityForMapObject(mapObject, props, shape);
		// Unknown or invalid object.
		if (entity == null) {
			LOG.error("failed to read object: " + mapObject.getName());
			return;
		}

		// Read ID, if available, add to hash map and add id to entity.
		processMapObjectGuid(props, entity);

		// Store our map object for later use.
		gameEntityEngine.addEntity(entity);

		LOG.debug("read map object:  " + mapObject.getName());
	}

	private Entity createEntityForMapObject(final MapObject mapObject, final MapProperties props, final Shape2D shape) {
		Entity entity = null;
		try {
			if (!props.containsKey("type")) {
				LOG.error("no type has been set for map object: " + mapObject.getName());
			} else {
				String type = String.valueOf(props.get("type"));
				type = type.toLowerCase(Locale.ROOT);
				if (type.equals("enemy")) {
					entity = processObjectEnemy(props, shape);
				} else if (type.equals("particleeffect")) {
					entity = processObjectParticleEffect(props, shape);
				} else if (type.equals("callback")) {
					entity = processObjectCallback(props, shape);
				} else if (type.equals("item")) {
					entity = processObjectItem(props, shape);
				} else if (type.equals("npc")) {
					entity = processObjectNpc(props, shape);
				} else {
					LOG.info("unknown map object type: " + type);
				}
			}
		} catch (final Exception e) {
			LOG.error("failed to read map object: " + mapObject.getName(), e);
		}
		return entity;
	}

	private Shape2D getMapObjectShape(final MapObject mapObject) {
		Shape2D shape = null;
		Float rotation = null;

		if (mapObject.getProperties().containsKey("rotation")) {
			float r = mapObject.getProperties().get("rotation", Float.class);
			// shift to positive values
			if (r < 0.0f)
				r -= 360.0f * (int) (r / 360.0f - 1.0f);
			// reduce to 0..360
			r = r % 360.0f;
			// discard small angles
			if (r > 0.01f)
				rotation = r;
		}

		if (mapObject instanceof CircleMapObject) {
			shape = ((CircleMapObject) mapObject).getCircle();
		} else if (mapObject instanceof EllipseMapObject) {
			Ellipse ellipse;
			ellipse = ((EllipseMapObject) mapObject).getEllipse();
			// Convert to circle if it is one.
			if (Math.abs(ellipse.width - ellipse.height) < 1E-3 * Math.max(ellipse.height, ellipse.width)) {
				shape = new Circle(ellipse.x, ellipse.y, 0.5f * (ellipse.height + ellipse.width));
			} else {
				// TODO rotation
				shape = ellipse;
			}
		} else if (mapObject instanceof RectangleMapObject) {
			final Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
			shape = rotation == null ? rect : GeoUtil.getRotatedRectangle(rect, rotation);
		} else if (mapObject instanceof PolylineMapObject) {
			final Polygon poly = ((PolygonMapObject) mapObject).getPolygon();
			GeoUtil.boundingBoxCenter(shape, v1);
			if (rotation != null)
				GeoUtil.rotatePolygon(poly, v1, rotation);
			shape = poly;
		} else if (mapObject instanceof PolygonMapObject) {
			final Polygon poly = ((PolygonMapObject) mapObject).getPolygon();
			GeoUtil.boundingBoxCenter(poly, v1);
			if (rotation != null)
				GeoUtil.rotatePolygon(poly, v1, rotation);
			shape = poly;
		} else {
			LOG.error("unknown shape for map objects");
		}

		return shape;
	}

	/**
	 * Read the id (name) of the map object and adds it to the
	 * {@link GlobalBag#idEntityMap} map. Also adds an appropriate
	 * {@link IdComponent} to the entity.
	 *
	 * @param props
	 *            MapProperties for this map object.
	 * @param entity
	 *            Entity created for this map object.
	 */
	private void processMapObjectGuid(final MapProperties props, final Entity entity) {
		final String id = String.valueOf(props.get("guid"));
		if (props.containsKey("guid") && !id.isEmpty()) {
			final IdComponent ic = new IdComponent(id);
			if (!ic.getId().isEmpty()) {
				// This will add the entity to the idEntityMap automatically.
				entity.add(ic);
			}
		} else if (props.containsKey("name")) {
			final String name = String.valueOf(props.get("name"));
			final IdComponent ic = new IdComponent(name);
			if (!name.isEmpty() && !idEntityMap.containsKey(ic.getId())) {
				// This will add the entity to the idEntityMap automatically.
				entity.add(ic);
			}
		}
	}

	private void processMapProperties(final MapProperties props) {
		// Default data.
		Integer playerX = 0;
		Integer playerY = 0;
		Float minElevation = 500.0f;
		Float maxElevation = 4000.0f;
		Float baseDirX = 0.0f;
		Float baseDirY = 1.0f;
		Gravity gravity = Gravity.SOUTH;
		Float playerDir = 90.0f;

		// Fetch available data.
		if (props.containsKey("playerX"))
			playerX = Integer.valueOf(String.valueOf(props.get("playerX")));
		if (props.containsKey("playerY"))
			playerY = Integer.valueOf(String.valueOf(props.get("playerY")));
		if (props.containsKey("minElevation"))
			minElevation = Float.valueOf(String.valueOf(props.get("minElevation")));
		if (props.containsKey("maxElevation"))
			maxElevation = Float.valueOf(String.valueOf(props.get("maxElevation")));
		if (props.containsKey("baseDirX"))
			baseDirX = Float.valueOf(String.valueOf(props.get("baseDirX")));
		if (props.containsKey("baseDirY"))
			baseDirY = Float.valueOf(String.valueOf(props.get("baseDirY")));
		if (props.containsKey("gravity"))
			gravity = Gravity.valueOf(String.valueOf(props.get("gravity")).toUpperCase(Locale.ROOT));
		if (props.containsKey("playerDir"))
			playerDir = Float.valueOf(String.valueOf(props.get("playerDir")));

		// Save data.
		minimumCameraElevation = minElevation;
		maximumCameraElevation = maxElevation;
		baseDirection = new Vector2(baseDirX, baseDirY);
		playerInitialPosition = new Vector2(playerX * widthTiles, playerY * heightTiles);
		playerInitialDirection = playerDir;
		preferredPlayerLocation = gravity;
	}

	private Entity processObjectEnemy(final MapProperties props, final Shape2D shape) {
		Float initX = 0f;
		Float initY = 0f;
		Float initDir = 0.0f;

		// Fetch parameters.
		final String species = LocaleRootWordUtils.capitalizeFully(props.get("species", String.class)).replace(" ", "");

		final String spawn = props.get("spawn", String.class).toUpperCase(Locale.ROOT);
		if (props.containsKey("initX"))
			initX = Float.valueOf(String.valueOf(props.get("initX")));
		if (props.containsKey("initY"))
			initY = Float.valueOf(String.valueOf(props.get("initY")));
		if (props.containsKey("initDir"))
			initDir = Float.valueOf(String.valueOf(props.get("initDir")));

		// Convert position in tiles to world units (=1 pixel).
		initX *= getWidthTiles();
		initY *= getHeightTiles();

		// Get species class and its constructor with the appropriate
		// constructor.
		Method enemySetupMethod;
		EnemyMaker enemyMaker;
		try {
			@SuppressWarnings("unchecked")
			final
			Class<? extends EnemyMaker> enemyClass = ClassReflection.forName(ENEMY_PACKAGE + species + "Maker");
			enemyMaker = (EnemyMaker) ClassReflection.getDeclaredMethod(enemyClass, "getInstance").invoke(null);
			enemySetupMethod = ClassReflection.getDeclaredMethod(enemyClass, "setup", Entity.class, Shape2D.class,
					MapProperties.class, ETrigger.class, Vector2.class, Float.class, Float.class);
		} catch (final Exception e) {
			LOG.error("no such enemy class with appropriate constructor: " + (ENEMY_PACKAGE + species + "Maker"), e);
			return null;
		}

		// Get spawn type.
		ETrigger spawnEnum;
		try {
			spawnEnum = ETrigger.valueOf(spawn);
		} catch (final Exception e) {
			LOG.error("invalid spawn type for enemy", e);
			return null;
		}

		// Try to initialize a new enemy of the given class.
		try {
			final Entity entity = new Entity();
			final float tileRadius = Math.max(widthTiles, heightTiles);
			enemySetupMethod.invoke(enemyMaker, entity, shape, props, spawnEnum, new Vector2(initX, initY), initDir,
					tileRadius);
			return enemyMaker.isInitializedSuccessfully() ? entity : null;
		} catch (final Exception e) {
			LOG.error("failed to initialize enemy instance: " + species, e);
			return null;
		}
	}

	private Entity processObjectCallback(final MapProperties props, final Shape2D shape) {
		// Fetch parameters.
		final String name = props.get("name", String.class);
		final String trigger = props.get("trigger", String.class).toUpperCase(Locale.ROOT);
		Integer loop = 0;
		Float interval = 1.0f;
		if (props.containsKey("loop"))
			loop = Integer.valueOf(String.valueOf(props.get("loop")));
		if (props.containsKey("interval"))
			interval = Float.valueOf(String.valueOf(props.get("interval")));

		// Search for the method and perform sanity checks.
		Method method;
		try {
			method = ClassReflection.getDeclaredMethod(levelClass, name, MapProperties.class);
			if (method.getReturnType() != Void.TYPE) {
				LOG.error("callback function must return void");
				return null;
			}
			if (!method.isPublic()) {
				LOG.error("callback method must be public");
				return null;
			}
		} catch (final Exception e) {
			LOG.error("cannot instantiate callback map object: no such method: " + name, e);
			return null;
		}

		// Get trigger type.
		ETrigger triggerEnum;
		try {
			triggerEnum = ETrigger.valueOf(trigger);
		} catch (final Exception e) {
			LOG.error("invalid callback trigger type: " + trigger, e);
			return null;
		}

		// Create new object and return it.
		final Entity e = new Entity();
		CallbackMaker.getInstance().setup(e, shape, triggerEnum, method, props, loop, interval);
		return e;
	}

	private Entity processObjectParticleEffect(final MapProperties props, final Shape2D shape) {
		// Fetch parameters.
		final String name = props.get("name", String.class).toUpperCase(Locale.ROOT);
		Float spin = 0.0f;
		String renderMode = "game";
		if (props.containsKey("spin"))
			spin = Float.valueOf(String.valueOf(props.get("spin")));
		if (props.containsKey("renderMode"))
			renderMode = String.valueOf(props.get("renderMode"));

		// Try to get the corresponding enum.
		EParticleEffect particleEffect;
		try {
			particleEffect = EParticleEffect.valueOf(name);
		} catch (final Exception e) {
			LOG.error("no such particle effect: " + name, e);
			return null;
		}

		// Create a new entity for the particle effect.
		final Entity entity = new Entity();
		if (renderMode.equalsIgnoreCase("screen")) {
			ParticleEffectMaker.getInstance().setupScreen(entity, shape, particleEffect, spin);
		} else {
			ParticleEffectMaker.getInstance().setupGame(entity, shape, particleEffect, spin);
		}
		return entity;
	}

	/**
	 * Reads an item object from the map.
	 *
	 * @param props
	 *            Map properties for the item.
	 * @param shape
	 *            Shape of the item.
	 * @return The entity representing the item, or null if an error occurred.
	 */
	private Entity processObjectItem(final MapProperties props, final Shape2D shape) {
		// Fetch parameters.
		final String category = props.get("category", String.class).toLowerCase(Locale.ROOT);
		final String name = props.get("name", String.class).toUpperCase(Locale.ROOT);

		Float angularVelocity = null;
		Vector3 axis = null;
		Float axisX = null;
		Float axisY = null;
		Float axisZ = null;

		if (props.containsKey("speed"))
			angularVelocity = Float.valueOf(String.valueOf(props.get("speed")));
		if (props.containsKey("axisX"))
			axisX = Float.valueOf(String.valueOf(props.get("axisX")));
		if (props.containsKey("axisY"))
			axisY = Float.valueOf(String.valueOf(props.get("axisY")));
		if (props.containsKey("axisZ"))
			axisZ = Float.valueOf(String.valueOf(props.get("axisZ")));

		if (axisX != null || axisY != null || axisZ != null)
			axis = new Vector3(axisX == null ? 0.0f : axisX, axisY == null ? 0.0f : axisY,
					axisZ == null ? 0.0f : axisZ);

		// Try to instantiate new item object.
		IMapItem mapItem = null;
		if (category.equals("weapon")) {
			mapItem = EWeapon.valueOf(name).getWeapon();
			if (mapItem == null) {
				LOG.error("no such weapon: " + name);
				return null;
			}
		} else if (category.equals("tokugi")) {
			mapItem = ETokugi.valueOf(name).getTokugi();
			if (mapItem == null) {
				LOG.error("no such tokugi: " + name);
				return null;
			}
		} else if (category.equals("consumable")) {
			mapItem = EConsumable.valueOf(name).getConsumable();
			if (mapItem == null) {
				LOG.error("no such consumable: " + name);
				return null;
			}
		} else {
			return null;
		}

		final Entity entity = new Entity();
		mapItem.setMapAngularVelocity(angularVelocity);
		mapItem.setMapAxisOfRotation(axis);
		final PositionComponent pcCenter = MakerUtils.makePositionAtCenter(shape);
		if (!ItemMaker.getInstance().setup(entity, pcCenter, props, mapItem))
			return null;
		return entity;
	}

	private Entity processObjectNpc(final MapProperties props, final Shape2D shape) {
		// Defaults
		Float initX = 0.0f;
		Float initY = 0.0f;
		Float initDir = 0.0f;
		String animation = null;
		String animationList = null;

		String startup = "visible";

		// Fetch parameters.
		if (props.containsKey("animation"))
			animation = String.valueOf(props.get("animation"));
		if (props.containsKey("animationList"))
			animationList = String.valueOf(props.get("animationList"));
		if (props.containsKey("startup"))
			startup = String.valueOf(props.get("startup"));
		if (props.containsKey("initX"))
			initX = Float.valueOf(String.valueOf(props.get("initX")));
		if (props.containsKey("initY"))
			initY = Float.valueOf(String.valueOf(props.get("initY")));
		if (props.containsKey("initDir"))
			initDir = Float.valueOf(String.valueOf(props.get("initDir")));

		// Tiled map editor does not support deleting declared properties...
		if (animation != null && animation.isEmpty())
			animation = null;
		if (animationList != null && animationList.isEmpty())
			animationList = null;

		// Get animation and instantiate entity.
		if (animationList != null) {
			try {
				animationList = animationList.toUpperCase(Locale.ROOT).replaceAll(" +", "_");
				final EAnimationList eal = EAnimationList.valueOf(animationList);
				final Entity entity = new Entity();
				NpcMaker.getInstance().setup(entity, shape, eal, startup.equalsIgnoreCase("invisible"),
						new Vector2(initX, initY), initDir);
				return entity;
			} catch (final IllegalArgumentException e) {
				LOG.error("no such animationList: " + animationList, e);
				return null;
			}
		} else if (animation != null) {
			try {
				animation = animation.toUpperCase(Locale.ROOT).replaceAll(" +", "_");
				final EAnimation ea = EAnimation.valueOf(animation);
				final Entity entity = new Entity();
				NpcMaker.getInstance().setup(entity, shape, ea, startup.equalsIgnoreCase("invisible"),
						new Vector2(initX, initY), initDir);
				return entity;
			} catch (final IllegalArgumentException e) {
				LOG.error("no such animation: " + animation, e);
				return null;
			}
		} else {
			LOG.error("either animationList or animation property must be specified for npc: " + props.get("id"));
			return null;
		}
	}

	public Vector2 getBaseDirection() {
		return baseDirection;
	}

	public Vector2 getPlayerInitialPosition() {
		return playerInitialPosition;
	}

	public float getMinimumCameraElevation() {
		return minimumCameraElevation;
	}

	public float getMaximumCameraElevation() {
		return maximumCameraElevation;
	}

	public float getPlayerInitialDirection() {
		return playerInitialDirection;
	}

	public Gravity getPreferredPlayerLocation() {
		return preferredPlayerLocation;
	}

	public static float getNumber(final MapProperties props, final String key, final float defaultValue) {
		final Object o = props.get(key);
		return o == null ? defaultValue : Float.valueOf(String.valueOf(o));
	}

	public static boolean getBoolean(final MapProperties props, final String key, final boolean defaultValue) {
		final Object o = props.get(key);
		return o == null ? defaultValue : Boolean.valueOf(String.valueOf(o));
	}

	private boolean isDebugObject(final MapProperties props) {
		return !DebugMode.activated && props.containsKey("debug") && Boolean.valueOf(String.valueOf(props.get("debug")));
	}
}
