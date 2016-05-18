package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ETiledMap;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * Base class for all the different game levels.
 * 
 * Method and variable names ending with "W" refer to world space coordinates,
 * those ending with "S" to screen space coordinates.
 *
 * @author madgaksha
 */
public abstract class ALevel {

	private final static Logger LOG = Logger.getLogger(ALevel.class);
	
	public final static float WORLD_X = 0.0f;
	public final static float WORLD_Y = 0.0f;

	public final static float CAMERA_GAME_FIELD_OF_VIEW_Y = 30.0f;
	public final static float CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF = (float) Math
			.tan(CAMERA_GAME_FIELD_OF_VIEW_Y * 0.5f * Math.PI / 180.0f);
	public final static float CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF_INV = 1.0f / CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF;

	/**
	 * The info window can work with virtual coordinates [0.0-1.0].
	 */
	public final static float VIEWPORT_INFO_VIRTUAL_WIDTH = 1.0f;
	/**
	 * The info window can work with virtual coordinates [0.0-1.0].
	 */
	public final static float VIEWPORT_INFO_VIRTUAL_HEIGHT = 1.0f;

	/**
	 * The screen window can work with virtual coordinates [0.0..1280.0].
	 */
	public final static float VIEWPORT_SCREEN_VIRTUAL_WIDTH = 1280.0f;
	/**
	 * The screen window can work with virtual coordinates [0.0..720.0].
	 */
	public final static float VIEWPORT_SCREEN_VIRTUAL_HEIGHT = 720.0f;

	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMap loadedTiledMap;
	private MapData mapProperties;
	private final ETexture backgroundImage;
	private final IResource[] requiredResources;
	private final EMusic bgm;
	private final EMusic battleBgm;
	private final ETiledMap tiledMap;
	private final String i18nNameKey;
	/** Initial position of the player in tiles. */
	private Vector2 playerInitialPosition;
	
	// =============================
	// Constructor
	// =============================
	public ALevel() {
		backgroundImage = requestedBackgroundImage();
		requiredResources = requestedRequiredResources();
		bgm = requestedBgm();
		tiledMap = requestedTiledMap();
		playerInitialPosition = requestedPlayerInitialPosition();
		i18nNameKey = requestedI18nNameKey();
		battleBgm = requestedBattleBgm();
	}
	
	public boolean initialize(SpriteBatch batch) {
		loadedTiledMap = ResourceCache.getTiledMap(tiledMap);
		if (loadedTiledMap == null) return false;
		try {
			mapProperties = new MapData(loadedTiledMap, getClass());
		}
		catch (Exception e) {
			LOG.error("failed to load map", e);
			return false;
		}
		mapRenderer = new OrthogonalTiledMapRenderer(loadedTiledMap, 1.0f, batch);
		return true;
	}

	// =============================
	// Abstract methods
	// =============================

	/**
	 * The image to use as a background for those portions of the screen that
	 * would be blank otherwise. May not be seen at all with with common aspect
	 * ratios.
	 * 
	 * @return Path to the background image relative to the assets folder.
	 */
	protected abstract ETexture requestedBackgroundImage();

	/**
	 * This can be overridden to setup the initial viewport and camera for the
	 * game. In particular, this could set: - the initial camera position and
	 * orientation (get the camera with {@link Viewport#getCamera()}) - the
	 * initial world width / height of the viewport with
	 * {@link Viewport#setWorldHeight(float)} and
	 * {@link Viewport#setWorldWidth(float)}
	 * 
	 * @param viewport
	 *            The viewport to setup.
	 */
	protected void setupInitialGameViewport(GameViewport viewport) {
	};

	/**
	 * Must return a list of all resources that the level requires. They will
	 * then be loaded into RAM before the level is started.
	 * 
	 * Does not include the player resources, see {@link APlayer#requestedRequiredResources}.
	 * 
	 * @return List of all required resources.
	 */
	protected abstract IResource[] requestedRequiredResources();

	/**@return Initial music that should be playing. Null if none. */
	protected abstract EMusic requestedBgm();

	/** @return Music while fighting. */
	protected abstract EMusic requestedBattleBgm();

	
	/**
	 * @return The map to be loaded initially.
	 */
	protected abstract ETiledMap requestedTiledMap();
	
	/** Initial position of the player in tiles. */
	protected abstract Vector2 requestedPlayerInitialPosition();
		
	protected abstract String requestedI18nNameKey();
	
	
	// =============================
	//       Implementations
	// =============================


	public MapData getMapProperties() {
		return mapProperties;
	}
	
	/** @return Returns the renderer to render the map. */
	public OrthogonalTiledMapRenderer getMapRenderer() {
		return mapRenderer;
	}
	
	/** @return The width of the map in world coordinates. */
	public float getMapWidthW() {
		return mapProperties.getWidthPixel();
	}

	/** @return The height of the map in world coordinates. */
	public float getMapHeightW() {
		return mapProperties.getHeightPixel();
	}
	
	public float getMapXW() {
		return WORLD_X;
	}

	public float getMapYW() {
		return WORLD_Y;
	}

	public IResource[] getRequiredResources() {
		return requiredResources;
	}
	
	public EMusic getBgm() {
		return bgm;
	}
	public EMusic getBattleBgm() {
		return battleBgm;
	}
	
	public ETiledMap getTiledMap() {
		return tiledMap;
	}
	
	public Texture getBackgroundImage() {
		return ResourceCache.getTexture(backgroundImage);
	}
	
	public String getName() {
		return i18n.game(i18nNameKey);
	}
	
	/** Initial position of the player in pixels. This converts
	 * the position from tiles to pixels.
	 * 
	 * @return Initial position of the player in pixels.
	 */
	public Vector2 getPlayerInitialPosition(){
		return new Vector2(
				playerInitialPosition.x*mapProperties.getWidthTiles(),
				playerInitialPosition.y*mapProperties.getHeightTiles());
	}


	/**
	 * The viewport for the info screen.
	 * 
	 * @see #getWorldViewport()
	 */
	public InfoViewport getInfoViewport(int screenWidth, int screenHeight) {
		return new InfoViewport(screenWidth, screenHeight);
	}

	/**
	 * The viewport for drawing general elements directly in screen coordinates.
	 */
	public Viewport getScreenViewport(int screenWidth, int screenHeight) {
		// OrthographicCamera is created automatically by the FillViewport.
		final Viewport viewport = new FillViewport(VIEWPORT_SCREEN_VIRTUAL_WIDTH, VIEWPORT_SCREEN_VIRTUAL_HEIGHT);
		final Camera camera = viewport.getCamera();
		camera.position.set(VIEWPORT_SCREEN_VIRTUAL_WIDTH * 0.5f, VIEWPORT_SCREEN_VIRTUAL_HEIGHT * 0.5f, 1.0f);
		return viewport;
	}

	/**
	 * The viewport for rendering the world, separate from the
	 * {@link #getInfoViewport()}.
	 * 
	 * @see #getInfoViewport()}
	 * @return The viewport for the world.
	 */
	public GameViewport getGameViewport(int screenWidth, int screenHeight) {
		GameViewport vw = new GameViewport(screenWidth, screenHeight, getMapWidthW(), getMapHeightW(), false);
		setupInitialGameViewport(vw);
		return vw;
	}

}