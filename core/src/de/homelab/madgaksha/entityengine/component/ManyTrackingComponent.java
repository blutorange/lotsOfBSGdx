package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.enums.Gravity;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.util.ArrayListEntityPointSet;

/**
 * Determines how an entity tracks other a collection of other entities and
 * orients itself.
 * 
 * @author madgaksha
 *
 */
public class ManyTrackingComponent implements Component, Poolable {

	private final static float DEFAULT_MINIMUM_ELEVATION_W = 400.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_TOP = 1.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_BOTTOM = -1.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_LEFT = -1.0f;
	private final static float DEFAULT_ADJUSTMENT_POINT_RIGHT = 1.0f;
	/** @see #playerBossThresholdW */
	private final static float DEFAULT_PLAYER_BOSS_THRESHOLD_W = 75.0f*75.0f;
	private final static float DEFAULT_WORLD_BORDER_LEFT_W = 0.0f;
	private final static float DEFAULT_WORLD_BORDER_RIGHT_W = 100.0f;
	private final static float DEFAULT_WORLD_BORDER_TOP_W = 100.0f;
	private final static float DEFAULT_WORLD_BORDER_BOTTOM_W = 0.0f;
	/** @see #marginScalingFactor */
	private final static float DEFAULT_MARGIN_SCALING_FACTOR = 1.1f;

	private final static Entity DEFAULT_PLAYER_POINT = new Entity().add(new PositionComponent());
	private final static Entity DEFAULT_BOSS_POINT = new Entity().add(new PositionComponent(0.0f, 1.0f));

	private final static TrackingOrientationStrategy DEFAULT_TRACKING_ORIENTATION_STRATEGY = TrackingOrientationStrategy.ABSOLUTE;
	private final static Gravity DEFAULT_GRAVITY = Gravity.SOUTH;

	public ManyTrackingComponent(float worldBorderLeftW, float worldBorderBottomW, float worldBorderRightW, float worldBorderTopW) {
		this.worldBorderTopW = worldBorderTopW;
		this.worldBorderBottomW = worldBorderBottomW;
		this.worldBorderLeftW = worldBorderLeftW;
		this.worldBorderRightW = worldBorderRightW;
	}
	
	@Override
	public void reset() {
		adjustmentPointTop = DEFAULT_ADJUSTMENT_POINT_TOP;
		adjustmentPointBottom = DEFAULT_ADJUSTMENT_POINT_BOTTOM;
		adjustmentPointLeft = DEFAULT_ADJUSTMENT_POINT_LEFT;
		adjustmentPointRight = DEFAULT_ADJUSTMENT_POINT_RIGHT;
		playerBossThresholdW = DEFAULT_PLAYER_BOSS_THRESHOLD_W;
		worldBorderTopW = DEFAULT_WORLD_BORDER_TOP_W;
		worldBorderBottomW = DEFAULT_WORLD_BORDER_BOTTOM_W;
		worldBorderLeftW = DEFAULT_WORLD_BORDER_LEFT_W;
		worldBorderRightW = DEFAULT_ADJUSTMENT_POINT_RIGHT;

		bossPoint = DEFAULT_BOSS_POINT;
		playerPoint = DEFAULT_PLAYER_POINT;

		trackingOrientationStrategy = DEFAULT_TRACKING_ORIENTATION_STRATEGY;
		gravity = DEFAULT_GRAVITY;
		
		minimumElevationW = DEFAULT_MINIMUM_ELEVATION_W;
	}

	// Camera tracking options.
	/**
	 * List of points the camera should focus on.
	 */
	public ArrayListEntityPointSet focusPoints = new ArrayListEntityPointSet();

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
	 * Point the player should look at; or direction in which the player should
	 * look. The correct interpretation of this field is determined by the
	 * {@link #absoluteOrientation} flag.
	 */
	public Entity bossPoint = DEFAULT_BOSS_POINT;
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
	public float playerBossThresholdW = DEFAULT_PLAYER_BOSS_THRESHOLD_W;

	/** Left boundary of world in world coordinates. */
	public float worldBorderLeftW = DEFAULT_WORLD_BORDER_LEFT_W;
	/** Right boundary of world in world coordinates. */
	public float worldBorderRightW = DEFAULT_WORLD_BORDER_RIGHT_W;
	/** Top boundary of world in world coordinates. */
	public float worldBorderTopW = DEFAULT_WORLD_BORDER_TOP_W;
	/** Bottom boundary of world in world coordinates. */
	public float worldBorderBottomW = DEFAULT_WORLD_BORDER_BOTTOM_W;

	/** Relative margin to be added to the computed camera world rectangle. */
	public float marginScalingFactor = DEFAULT_MARGIN_SCALING_FACTOR;
	
	/** The minimum height of the camera above the world. */
	public float minimumElevationW = DEFAULT_MINIMUM_ELEVATION_W;

}
