package de.homelab.madgaksha;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.level.StatusScreen;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.util.Clock;
import de.homelab.madgaksha.util.Score;

/**
 * Holds global (static) game data. Objects of which only
 * one instance will be created.
 * 
 * Better than putting it all inside the {@Game} class.
 * 
 * Also useful for sharing data between different entity system etc.
 * 
 * @author madgaksha
 *
 */
public final class GlobalBag {
	private GlobalBag(){}
		
	public static int bitmapFontRasterSize;
	
	public static int currentMonitorWidth;
	public static int currentMonitorHeight;
	/** Maximum supported width in pixels of all monitors. */
	public static int maxMonitorWidth;
	/** Maximum supported height in pixels of all monitors. */
	public static int maxMonitorHeight;
	
	public static Polygon visibleWorld = new Polygon(new float[8]);
	public static Rectangle visibleWorldBoundingBox = new Rectangle();
	
	
	/** The instance of the running game. */
	public static Game game;
	
	/** For playing music. Only one instance should be created. */
	public static MusicPlayer musicPlayer = null;
	/** For playing sound effects. Only one instance should be created. */
	public static SoundPlayer soundPlayer = null;
	
	/** Current level data. */
	public static ALevel level;
	/** Current player data. */
	public static APlayer player;
	
	public static GameViewport viewportGame;	
	public static Viewport viewportPixel;
	public static StatusScreen statusScreen;
	
	/** For drawing the game window. */
	public static SpriteBatch batchGame;
	/** For drawing the background directly to the screen. */
	public static SpriteBatch batchPixel;
	/** For drawing shapes. */
	public static ShapeRenderer shapeRenderer;
	
	
	/** The player entity the player plays */
	public static Entity playerEntity;
		
	/** Entity engine ASHLEY for the main game.*/
	public static Engine gameEntityEngine;

	/** The tracking component of the camera. */
	public static ManyTrackingComponent cameraTrackingComponent;
	
	public static Clock gameClock = new Clock();
	public static Score gameScore = new Score();
	
	static {
		visibleWorld.setPosition(0.0f, 0.0f);
		visibleWorld.setOrigin(0.0f, 0.0f);
		visibleWorld.setRotation(0.0f);
		visibleWorld.setScale(1.0f, 1.0f);
	}
}
