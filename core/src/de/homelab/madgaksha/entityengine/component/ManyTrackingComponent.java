package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.enums.Gravity;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
/**
 * Determines how an entity tracks other a collection of other entities and
 * orients itself.
 * 
 * @author madgaksha
 *
 */
public class ManyTrackingComponent implements Component, Poolable {

	private final static float DEFAULT_MINIMUM_ELEVATION = 400.0f;
	private final static float DEFAULT_MAXIMUM_ELEVATION = 2000.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_TOP = 64.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_BOTTOM = -64.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_LEFT = -64.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_RIGHT = 64.0f;

	private final static float DEFAULT_PLAYER_BOSS_THRESHOLD = 75.0f*75.0f;
	private final static float DEFAULTORLD_BORDER_LEFT = 0.0f;
	private final static float DEFAULTORLD_BORDER_RIGHT = 100.0f;
	private final static float DEFAULTORLD_BORDER_TOP = 100.0f;
	private final static float DEFAULTORLD_BORDER_BOTTOM = 0.0f;
	
	private final static float DEFAULT_MARGIN_SCALING_FACTOR = 1.1f;

	private final static Entity DEFAULT_PLAYER_POINT = new Entity().add(new PositionComponent());
	private final static Vector2 DEFAULT_BASE_DIRECTION = new Vector2(0.0f, 1.0f);

	private final static int DEFAULT_TRACKED_POINT_INDEX = 0;
	
	private final static TrackingOrientationStrategy DEFAULT_TRACKING_ORIENTATION_STRATEGY = TrackingOrientationStrategy.ABSOLUTE;
	private final static Gravity DEFAULT_GRAVITY = Gravity.SOUTH;

	public ManyTrackingComponent(float worldBorderLeftW, float worldBorderBottomW, float worldBorderRightW, float worldBorderTopW) {
		this.worldBorderTop = worldBorderTopW;
		this.worldBorderBottom = worldBorderBottomW;
		this.worldBorderLeft = worldBorderLeftW;
		this.worldBorderRight = worldBorderRightW;
	}
	
	@Override
	public void reset() {
		adjustmentPointTop = DEFAULT_ADJUSTMENT_POINT_TOP;
		adjustmentPointBottom = DEFAULT_ADJUSTMENT_POINT_BOTTOM;
		adjustmentPointLeft = DEFAULT_ADJUSTMENT_POINT_LEFT;
		adjustmentPointRight = DEFAULT_ADJUSTMENT_POINT_RIGHT;
		playerBossThreshold = DEFAULT_PLAYER_BOSS_THRESHOLD;
		worldBorderTop = DEFAULTORLD_BORDER_TOP;
		worldBorderBottom = DEFAULTORLD_BORDER_BOTTOM;
		worldBorderLeft = DEFAULTORLD_BORDER_LEFT;
		worldBorderRight = DEFAULT_ADJUSTMENT_POINT_RIGHT;

		trackedPointIndex = DEFAULT_TRACKED_POINT_INDEX;
		
		baseDirection = DEFAULT_BASE_DIRECTION;
		playerPoint = DEFAULT_PLAYER_POINT;

		trackingOrientationStrategy = DEFAULT_TRACKING_ORIENTATION_STRATEGY;
		gravity = DEFAULT_GRAVITY;
		
		minimumElevation = DEFAULT_MINIMUM_ELEVATION;
		maximumElevation = DEFAULT_MAXIMUM_ELEVATION;
	}

	// Camera tracking options.
	/**
	 * List of points the camera should focus on.
	 */
	public ImmutableArray<Entity> focusPoints = new ImmutableArray<Entity>(new Array<Entity>());

	/** Index of the point currently tracked. */
	public int trackedPointIndex = DEFAULT_TRACKED_POINT_INDEX;
	
	/**
	 * When computing the world rectangle the camera should look at, these
	 * points are added to the focus points, but in rotated player-boss 
	 * world coordinates (the looking direction of the player and the axis
	 * orthogonal to that).
	 * <br>
	 * This is essentially the smallest rectangle in world coordinates and
	 * prevents the camera from zooming-in too close. <br>
	 * Should be set along with {@link #playerBossThresholdW} and is typically
	 * about 5-15 times the threshold value.
	 */
	public float adjustmentPointTop = DEFAULT_ADJUSTMENT_POINT_TOP;

	/**
	 * @see TrackingCamera#adjustmentPointTop
	 */
	public float adjustmentPointBottom = DEFAULT_ADJUSTMENT_POINT_BOTTOM;
	/**
	 * @see TrackingCamera#adjustmentPointTop
	 */
	public float adjustmentPointLeft = DEFAULT_ADJUSTMENT_POINT_LEFT;
	/**
	 * @see TrackingCamera#adjustmentPointTop
	 */
	public float adjustmentPointRight = DEFAULT_ADJUSTMENT_POINT_RIGHT;

	/**
	 * Default direction the player should look into when there
	 * are no other points to track.
	 */
	public Vector2 baseDirection = DEFAULT_BASE_DIRECTION;
	/**
	 * The reference point for the calculations.
	 */
	public Entity playerPoint = DEFAULT_PLAYER_POINT;
	/**
	 * 
	 * How to determine the direction in which we are looking, ie. the
	 * orientation of the screen.
	 * <br><br>
	 * ABSOLUTE: {@link #bossPoint} is interpreted as the directional vector for
	 * the screen orientation.
	 * <br><br>
	 * RELATIVE: The screen orients itself in the direction of the vector
	 * {@link #playerPoint}-{@link #bossPoint}.
	 * <br><br>
	 * MINIBALL: We compute center C of the smallest enclosing circle of all
	 * focusPoints and look in the direction of {@link #playerPoint}-C. This
	 * uses the miniball library.
	 * <br><br>
	 * <a href="https://github.com/hbf/miniball">Miniball library on github.</a>
	 * 
	 */
	public TrackingOrientationStrategy trackingOrientationStrategy = DEFAULT_TRACKING_ORIENTATION_STRATEGY;
	/**
	 * Which side of the screen is to be considered 'bottom'.
	 */
	public Gravity gravity = DEFAULT_GRAVITY;

	/**
	 * Minimum distance between the player and the boss for the camera to adjust
	 * rotation. When the distance is below this threshold, rotation will not be
	 * adjusted anymore.
	 * 
	 * This is the square of the distance.
	 */
	public float playerBossThreshold = DEFAULT_PLAYER_BOSS_THRESHOLD;

	/** Left boundary of world in world coordinates. */
	public float worldBorderLeft = DEFAULTORLD_BORDER_LEFT;
	/** Right boundary of world in world coordinates. */
	public float worldBorderRight = DEFAULTORLD_BORDER_RIGHT;
	/** Top boundary of world in world coordinates. */
	public float worldBorderTop = DEFAULTORLD_BORDER_TOP;
	/** Bottom boundary of world in world coordinates. */
	public float worldBorderBottom = DEFAULTORLD_BORDER_BOTTOM;

	/** Relative margin to be added to the computed camera world rectangle. */
	public float marginScalingFactor = DEFAULT_MARGIN_SCALING_FACTOR;
	
	/** The minimum height of the camera above the world. */
	public float minimumElevation = DEFAULT_MINIMUM_ELEVATION;
	/** The maximum height of the camera above the world. */
	public float maximumElevation = DEFAULT_MAXIMUM_ELEVATION;
	

}
