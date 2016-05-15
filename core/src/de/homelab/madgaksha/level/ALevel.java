package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
public abstract class ALevel implements Serializable {

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

	protected float mapWidthW = 10.0f;
	protected float mapHeightW = 10.0f;
	protected ETexture backgroundImage = null;
	protected IResource[] requiredResources;
	protected EMusic bgm;
	protected ETiledMap tiledMap;
	
	// =============================
	// Constructor
	// =============================
	public ALevel() {
		mapWidthW = requestedMapWidthW();
		mapHeightW = requestedMapHeightW();
		backgroundImage = requestedBackgroundImage();
		requiredResources = requestedRequiredResources();
		bgm = requestedBgm();
		tiledMap = requestedTiledMap();
	}

	// =============================
	// Abstract methods
	// =============================

	/**
	 * @return The width of the map in world coordinates.
	 */
	protected abstract float requestedMapWidthW();

	/**
	 * @return The height of the map in world coordinates.
	 */
	protected abstract float requestedMapHeightW();

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
	 * @return List of all required resources.
	 */
	protected abstract IResource[] requestedRequiredResources();

	/**
	 * @return Initial music that should be playing. Null if none.
	 */
	public abstract EMusic requestedBgm();

	/**
	 * @return The map to be loaded initially.
	 */
	public abstract ETiledMap requestedTiledMap();
	
	// =============================
	// Implementations
	// =============================

	/**
	 * @return The width of the map in world coordinates.
	 */
	public float getMapWidthW() {
		return mapWidthW;
	}

	/**
	 * @return The height of the map in world coordinates.
	 */
	public float getMapHeightW() {
		return mapHeightW;
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

	public ETiledMap getTiledMap() {
		return tiledMap;
	}
	
	public Texture getBackgroundImage() {
		return ResourceCache.getTexture(backgroundImage);
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

	// TODO
	// change this when making updates and adding more fields
	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
		json.writeValue("mapWidthW", mapHeightW);
		json.writeValue("mapHeightW", mapHeightW);
		json.writeValue("backgroundImage", backgroundImage.toString());

		json.writeObjectStart("requiredResources");
		json.writeValue("length", requiredResources.length);
		json.writeArrayStart("list");
		for (IResource resource : requiredResources) {
			json.writeObjectStart();
			json.writeValue("class", resource.getClass().getCanonicalName());
			json.writeValue("name", resource.toString());
			json.writeObjectEnd();
		}
		json.writeArrayEnd();
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		mapWidthW = jsonData.get("mapWidthW").asFloat();
		mapHeightW = jsonData.get("mapHeightW").asFloat();
		backgroundImage = ETexture.valueOf(jsonData.get("backgroundImage").asString());

		JsonValue rr = jsonData.get("requiredResources");
		int length = rr.get("length").asInt();
		requiredResources = new IResource[length];
		for (int i = 0; i != length; ++i) {
			JsonValue list = rr.get("list");
			JsonValue entry = list.get(i);
			try {
				// Get enum constant from enum class name and constant name.
				IResource r = (IResource) getClass().getClassLoader().loadClass(entry.get("class").asString())
						.getMethod("valueOf", String.class).invoke(null, entry.get("name").asString());
				requiredResources[i] = r;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}