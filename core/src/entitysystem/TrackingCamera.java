package entitysystem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.Game;

public class TrackingCamera extends Entity {

	public enum Gravity {
		NORTH,
		EAST,
		SOUTH,
		WEST;
	}
		
	// Camera object this entity is attached to.
	private Camera camera;
	
	// Camera position.
	private Vector2 isPositionW = null;
	/**
	 * Current angle of the camera in radians.
	 * Range is within [-pi..pi]. Positive angles
	 * are counter-clockwise, negative angles
	 * clockwise. The positive x-axis has got an
	 * angle of zero (0) radians.
	 */
	private float isAngleW = 0.0f;
	private float isElevationW = 0.0f;
	
	// Camera target position.
	private Vector2 shouldPositionW = null;
	/**
	 * Desired angle of the camera in radians.
	 * Range is within [-pi..pi]. Positive angles
	 * are counter-clockwise, negative angles
	 * clockwise. The positive x-axis has got an
	 * angle of zero (0) radians.
	 */
	private float shouldAngleW = 0.0f;
	private float shouldElevationW = 0.0f;

	// Camera tracking options.
	/**
	 * List of points the camera should focus on.
	 */
	private List<Vector2> focusPoints = new ArrayList<Vector2>();

	/**
	 * When computing the world rectangle the camera should
	 * look at, these points are added to the focus points,
	 * but in player-boss coordinates (the looking direction
	 * of the player and the axis orthogonal to that).
	 * <br>
	 * This is essentially the smallest rectangle in world
	 * coordinates and prevents the camera from zooming-in
	 * too close.
	 * <br>
	 * Should be set along with {@link #playerBossThresholdW}
	 * and is typically about 5-15 times the threshold value.
	 */
	private float adjustmentPointTop = 1.0f;

	/**
	 * @see TrackingCamera#adjustmentPointTop
	 */
	private float adjustmentPointBottom = -1.0f;
	/**
	 * @see TrackingCamera#adjustmentPointTop
	 */
	private float adjustmentPointLeft = -1.0f;
	/**
	 * @see TrackingCamera#adjustmentPointTop
	 */
	private float adjustmentPointRight = 1.0f;

	/**
	 * Point the player should look at; or direction
	 * in which the player should look.
	 * The correct interpretation of this field is determined
	 * by the {@link #absoluteOrientation} flag.
	 */
	private Vector2 bossPoint = new Vector2(0,1);
	/**
	 * The reference point for the calculations.
	 */
	private Vector2 playerPoint = new Vector2(0,0);
	/**
	 * If set to true, bossPoint is interpreted as 
	 * the directional vector for the screen orientation.
	 * 
	 * If set to false, the screen orients itself in the
	 * direction playerPoint-bossPoint.
	 */
	private boolean absoluteOrientation = true;
	/**
	 * Which side of the screen is to be considered 'bottom'.
	 */
	private Gravity gravity = Gravity.SOUTH;

	/** Minimum distance between the player and the
	 *boss for the camera to adjust rotation.
	 * When the distance is below this threshold,
	 * rotation will not be adjusted anymore.
	 */
	private float playerBossThresholdW = 0.1f;

	
	/** Left boundary of world in world coordinates.*/
	private float worldBorderLeftW = 0.0f;
	/** Right boundary of world in world coordinates.*/
	private float worldBorderRightW = 100.0f;
	/** Top boundary of world in world coordinates.*/
	private float worldBorderTopW = 100.0f;
	/** Bottom boundary of world in world coordinates.*/
	private float worldBorderBottomW = 0.0f;
	
	/**
	 * Relative margin to be added to the 
	 * computed camera world rectangle. 
	 */
	private float marginScalingFactor = 1.1f;
	
	public TrackingCamera(Vector2 playerPoint, Vector2 bossPoint, boolean absoluteOrientation) {
			this.playerPoint = playerPoint;
			this.bossPoint = bossPoint;
			this.absoluteOrientation = absoluteOrientation;
	}

	@SuppressWarnings("incomplete-switch") // no rotation for 0 degrees...
	private void updateShouldCoordinates() {
		// Determine the direction the player
		// should be looking at.
		Vector2 dir;
		if (absoluteOrientation) {
			dir = bossPoint.cpy();
		}
		else {
			dir = bossPoint.cpy().sub(playerPoint);
		}
		// Check whether the direction is (almost) zero.
		// If it is use, the last known direction.
		// This helps to prevent the camera from 
		// rotating all around when the player is near
		// the enemy.
		if (dir.len2() < playerBossThresholdW) {
			
		}
		// Apply desired gravity.
		switch (gravity) {
		case NORTH:
			dir.rotate(180.0f);
			break;
		case WEST:
			dir.rotate(270.0f);
			break;
		case EAST:
			dir.rotate(90.0f);
			break;
		/*
		case SOUTH:
			dir.rotate(0);
			break;
		 */
		}
		// Normalize, we need a unit vector
		// for the coordinate system.
		// We normalize after rotating the vector,
		// as may add numerical error.
		dir.nor();
		// Vector orthogonal to the looking 
		// direction. We use it to create
		// a rotated coordinate system.
		Vector2 base = dir.cpy().rotate90(-1);
		// Project all focusPoints to new coordinate system
		// and get min / max coordinates to determine
		// the bounding rectangle.
		float vBase, vDir;
		float minx = Float.MAX_VALUE;
		float miny = Float.MAX_VALUE;
		float maxx = -Float.MAX_VALUE;
		float maxy = -Float.MAX_VALUE;
		for (Vector2 v : focusPoints) {
			vDir = v.dot(dir);
			vBase = v.dot(base);
			if (vBase < minx) minx = vDir;
			else if (vBase > maxx) maxx = vBase;
			if (vDir < miny) miny = vBase;
			else if (vDir < maxy) maxx = vDir;
		}
		for (int i = 0; i!= 4; ++i) {
			if (adjustmentPointLeft < minx) minx = adjustmentPointLeft;
			else if (adjustmentPointRight > maxx) maxx = adjustmentPointRight;
			if (adjustmentPointBottom < miny) miny = adjustmentPointBottom;
			else if (adjustmentPointTop > maxy) maxy = adjustmentPointTop;
		}
		// Clip to world boundaries.
		if (maxx-minx < worldBorderRightW-worldBorderLeftW) {
			float dx = 0.0f;
			if (minx < worldBorderLeftW) {
				dx = worldBorderLeftW - minx;
			}
			else if (maxx > worldBorderRightW) {
				dx = worldBorderRightW - maxx;				
			}
			minx += dx;
			maxx += dx;
		}
		else {
			minx = worldBorderLeftW;
			maxx = worldBorderRightW;
		}
		if (maxy-miny < worldBorderTopW-worldBorderBottomW) {
			float dy = 0.0f;
			if (miny < worldBorderBottomW) {
				dy = worldBorderBottomW - minx;
			}
			else if (maxy > worldBorderTopW) {
				dy = worldBorderTopW - maxx;				
			}
			miny += dy;
			maxy += dy;
		}
		else {
			miny = worldBorderBottomW;
			maxy = worldBorderTopW;
		}		
		// Get the center of the rectangle
		float cx = (maxx+minx)*0.5f; // center base coordinate
		float cy = (maxy+miny)*0.5f; // center dir coordinate
		float hw = (maxx-minx)*0.5f; // half width
		float hh = (maxy-miny)*0.5f; // half height
		//Add margin and fit aspect ratio.
		if (hw > Game.VIEWPORT_GAME_AR*hh) {
			// Add requested margin;
			hw *= marginScalingFactor;
			// Increase height.
			hh = hw * Game.VIEWPORT_GAME_AR_INV;
		}
		else {
			// Add requested margin;
			hh *= marginScalingFactor;
			// Increase width.
			hw = hh * Game.VIEWPORT_GAME_AR;
		}
		// Compute the rotation angle of this rectangle.
		shouldAngleW = base.angleRad();
		// Compute the height the camera needs to be located
		// at.
		//TODO
		if (camera instanceof OrthographicCamera) {
			final OrthographicCamera oc = (OrthographicCamera)camera;
			oc.setToOrtho(false, hw*2.0f, hh*2.0f);
		}
		else /*if (camera instanceof PerspectiveCamera)*/ {
			final PerspectiveCamera pc = (PerspectiveCamera)camera;
			// fieldOfView is the double angle in degrees
			// from bottom to top.
			//  ^      /\
			//  |     / -\-------- Angle fieldOfView (fovy)
			//  z    /    \
			//  |   /      \
			//  v  +--------+
			//
			//     <- hh*2 ->
			// 
			// Elevation z, height hh.
			//
			//
			// tan(fovy/2) = hh/z
			shouldElevationW = hh/(float)Math.tan(pc.fieldOfView*0.5f);
		}

		// Compute the position the camera should be located
		// at. We need to convert the coordinates back to the
		// world coordinate system.
		shouldPositionW = new Vector2(cx,cy).rotateRad(-shouldAngleW);
	}

	private void update(float dt) {
		
	}
}
