package de.homelab.madgaksha.entityengine.entitysystem;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;

/**
 * Computes the ideal position for the camera.
 * 
 * @author madgaksha
 *
 */
public class CameraTracingSystem extends IntervalIteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CameraTracingSystem.class);
	
	/** In seconds. */
	private final static float DEFAULT_UPDATE_INTERVAL = 0.05f;

	/** Last vector longer than the threshold. */
	private Vector2 lastDirAboveThreshold = new Vector2(0, 1);

	public CameraTracingSystem() {
		this(DEFAULT_UPDATE_INTERVAL, DefaultPriority.cameraZoomingSystem);
	}

	public CameraTracingSystem(int priority) {
		this( DEFAULT_UPDATE_INTERVAL, priority);
	}
	
	@SuppressWarnings("unchecked")
	public CameraTracingSystem(float updateInterval, int priority) {
		super(Family.all(ManyTrackingComponent.class, ShouldPositionComponent.class, ShouldRotationComponent.class)
				.get(), updateInterval, priority);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected void processEntity(Entity entity) {
		final ShouldPositionComponent spc = Mapper.shouldPositionComponent.get(entity);
		final ShouldRotationComponent src = Mapper.shouldRotationComponent.get(entity);
		final ManyTrackingComponent mtc = Mapper.manyTrackingComponent.get(entity);
		final PositionComponent playerPoint = Mapper.positionComponent.get(mtc.playerPoint);
		if (playerPoint == null) return;

		// Determine the direction the player
		// should be looking in.
		Vector2 dir = new Vector2();
		if (mtc.focusPoints.size() > 0 && mtc.trackingOrientationStrategy == TrackingOrientationStrategy.RELATIVE) {
			if (mtc.trackedPointIndex >= mtc.focusPoints.size()) mtc.trackedPointIndex = mtc.focusPoints.size()-1;
			final PositionComponent pc = Mapper.positionComponent.get(mtc.focusPoints.get(mtc.trackedPointIndex));
			dir.set(pc.x, pc.y).sub(playerPoint.x, playerPoint.y);			
		}
		else dir.set(mtc.baseDirection.x, mtc.baseDirection.y);
		
		// Check whether the direction is (almost) zero.
		// If it is use, the last known direction.
		// This helps to prevent the camera from
		// rotating all around when the player is near
		// the enemy.
		if (dir.len2() < mtc.playerBossThreshold) {
			dir.set(lastDirAboveThreshold);
		} else
			lastDirAboveThreshold.set(dir);
		// Apply desired gravity.
		switch (mtc.gravity) {
		case NORTH:
			dir.rotate(270.0f);
			break;
		case WEST:
			dir.rotate(180.0f);
			break;
		/*case EAST:
			dir.rotate(0.0f);
			break;*/
		case SOUTH:
			dir.rotate(90.0f); break;
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
		// Player point is at (0,0) by definition.
		float minx = 0.0f;
		float miny = 0.0f;
		float maxx = 0.0f;
		float maxy = 0.0f;
		final Vector2 v = new Vector2();
		for (Entity e : mtc.focusPoints) {
			final PositionComponent vpc = Mapper.positionComponent.get(e);
			final BoundingSphereComponent bsc = Mapper.boundingSphereComponent.get(e);
			v.set(vpc.x-playerPoint.x, vpc.y-playerPoint.y);
			// Move to logical center of the sprite.
			if (bsc != null) v.add(bsc.centerX,bsc.centerY);
			// Get rotated and translated coordinate system with the
			// ordinate in the looking direction.
			vDir = v.dot(dir);
			vBase = v.dot(base);
			if (vBase < minx)
				minx = vBase - (bsc == null ? 0.0f : bsc.radius);
			else if (vBase > maxx)
				maxx = vBase + (bsc == null ? 0.0f : bsc.radius);
			if (vDir < miny)
				miny = vDir - (bsc == null ? 0.0f : bsc.radius);
			else if (vDir > maxy)
				maxy = vDir + (bsc == null ? 0.0f : bsc.radius);
		}

		// Margin
		if (mtc.adjustmentPointLeft < minx)
			minx = mtc.adjustmentPointLeft;
		else if (mtc.adjustmentPointRight > maxx)
			maxx = mtc.adjustmentPointRight;
		if (mtc.adjustmentPointBottom < miny)
			miny = mtc.adjustmentPointBottom;
		else if (mtc.adjustmentPointTop > maxy)
			maxy = mtc.adjustmentPointTop;
		
		// Get the center of the rectangle
		float cx = (maxx + minx) * 0.5f; // center base coordinate
		float cy = (maxy + miny) * 0.5f; // center dir coordinate
		float hw = (maxx - minx) * 0.5f; // half width
		float hh = (maxy - miny) * 0.5f; // half height
		// Add margin and fit aspect ratio.
		if (hw > Game.VIEWPORT_GAME_AR * hh) {
			// Add requested margin;
			hw *= mtc.marginScalingFactor;
			// Increase height.
			hh = hw * Game.VIEWPORT_GAME_AR_INV;
		} else {
			// Add requested margin;
			hh *= mtc.marginScalingFactor;
			// Increase width.
			hw = hh * Game.VIEWPORT_GAME_AR;
		}
		// Compute the rotation angle of this rectangle.
		src.thetaZ = 360.0f-base.angle();

		// Compute the height the camera needs to be located
		// at.
		if (viewportGame.getCamera() instanceof OrthographicCamera) {
			final OrthographicCamera ocam = (OrthographicCamera) viewportGame.getCamera();
			ocam.setToOrtho(false, hw * 2.0f, hh * 2.0f);
		} else /* if (camera instanceof PerspectiveCamera) */ {
			// fieldOfView is the double angle in degrees
			// from bottom to top.
			// ^ /\
			// | / -\-------- Angle fieldOfView (fovy)
			// z / \
			// | / \
			// v +--------+
			//
			// <- hh*2 ->
			//
			// Elevation z, height hh.
			//
			//
			// tan(fovy/2) = hh/z
			spc.z = hh * ALevel.CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF_INV;
		}
		// Apply minimum elevation.
		if (spc.z < mtc.minimumElevation) spc.z = mtc.minimumElevation;
		// Apply maximum elevation
		else if (spc.z > mtc.maximumElevation) {
			spc.z = mtc.maximumElevation;
			float ratio = spc.z*ALevel.CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF/hh;
			cy *= ratio;
			cx *= ratio;
		}
		// Compute the position the camera should be located
		// at. We need to convert the coordinates back to the
		// world coordinate system.
		spc.x = base.x*cx + dir.x*cy + playerPoint.x;
		spc.y = base.y*cx + dir.y*cy + playerPoint.y;
	}
}
