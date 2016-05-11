package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.dreizak.miniball.highdim.Miniball;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;

/**
 * Computes the ideal position for the camera.
 * @author madgaksha
 *
 */
public class CameraTracingSystem extends IntervalIteratingSystem {
	
	/** In seconds. */
	private final static float SHOULD_POSITION_UPDATE_INTERVAL = 0.2f;

	private Vector2 lastDirAboveThreshold = new Vector2(0,1);

	public CameraTracingSystem() {
		this(DefaultPriority.cameraZoomingSystem);
	}
	
	@SuppressWarnings("unchecked")
	public CameraTracingSystem(int priority) {
		super(Family.all(
				ManyTrackingComponent.class,
				ShouldPositionComponent.class,
				ShouldRotationComponent.class
				).get(), SHOULD_POSITION_UPDATE_INTERVAL, priority);
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void processEntity(Entity entity) {
		final ShouldPositionComponent spc = Mapper.shouldPositionComponent.get(entity);		
		final ShouldRotationComponent src = Mapper.shouldRotationComponent.get(entity);
		final ManyTrackingComponent mtc = Mapper.manyTrackingComponent.get(entity);
		final PositionComponent playerPoint = Mapper.positionComponent.get(mtc.playerPoint);
		final PositionComponent bossPoint = Mapper.positionComponent.get(mtc.bossPoint);

		if (playerPoint == null) return;
		
		// Determine the direction the player
		// should be looking at.
		Vector2 dir = new Vector2();
		switch (mtc.trackingOrientationStrategy) {
		case MINIBALL:
			final double[] center = new Miniball(mtc.focusPoints).center();
			dir.set((float)(center[0]-playerPoint.x),(float)(center[1]-playerPoint.y));
			break;
		case ABSOLUTE:
			if (bossPoint == null) return;
			dir.set(bossPoint.x,bossPoint.y);
			break;
		case RELATIVE:
			if (bossPoint == null) return;
			dir.set(bossPoint.x,bossPoint.y).sub(playerPoint.x,playerPoint.y);
			break;
		default:
			dir.set(lastDirAboveThreshold);
		}
		// Check whether the direction is (almost) zero.
		// If it is use, the last known direction.
		// This helps to prevent the camera from 
		// rotating all around when the player is near
		// the enemy.
		if (dir.len2() < mtc.playerBossThresholdW) {
			dir.set(lastDirAboveThreshold);
		}
		else lastDirAboveThreshold.set(dir);
		// Apply desired gravity.
		switch (mtc.gravity) {
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
		final Vector2 v = new Vector2();
		for (Entity e : mtc.focusPoints) {
			final PositionComponent vpc = Mapper.positionComponent.get(e);
			v.set(vpc.x,vpc.y);
			vDir = v.dot(dir);
			vBase = v.dot(base);
			if (vBase < minx) minx = vDir;
			else if (vBase > maxx) maxx = vBase;
			if (vDir < miny) miny = vBase;
			else if (vDir < maxy) maxx = vDir;
		}
		for (int i = 0; i!= 4; ++i) {
			if (mtc.adjustmentPointLeft < minx) minx = mtc.adjustmentPointLeft;
			else if (mtc.adjustmentPointRight > maxx) maxx = mtc.adjustmentPointRight;
			if (mtc.adjustmentPointBottom < miny) miny = mtc.adjustmentPointBottom;
			else if (mtc.adjustmentPointTop > maxy) maxy = mtc.adjustmentPointTop;
		}
		// Clip to world boundaries.
		if (maxx-minx < mtc.worldBorderRightW - mtc.worldBorderLeftW) {
			float dx = 0.0f;
			if (minx < mtc.worldBorderLeftW) {
				dx = mtc.worldBorderLeftW - minx;
			}
			else if (maxx > mtc.worldBorderRightW) {
				dx = mtc.worldBorderRightW - maxx;				
			}
			minx += dx;
			maxx += dx;
		}
		else {
			minx = mtc.worldBorderLeftW;
			maxx = mtc.worldBorderRightW;
		}
		if (maxy-miny < mtc.worldBorderTopW - mtc.worldBorderBottomW) {
			float dy = 0.0f;
			if (miny < mtc.worldBorderBottomW) {
				dy = mtc.worldBorderBottomW - minx;
			}
			else if (maxy > mtc.worldBorderTopW) {
				dy = mtc.worldBorderTopW - maxx;				
			}
			miny += dy;
			maxy += dy;
		}
		else {
			miny = mtc.worldBorderBottomW;
			maxy = mtc.worldBorderTopW;
		}		
		// Get the center of the rectangle
		float cx = (maxx+minx)*0.5f; // center base coordinate
		float cy = (maxy+miny)*0.5f; // center dir coordinate
		float hw = (maxx-minx)*0.5f; // half width
		float hh = (maxy-miny)*0.5f; // half height
		//Add margin and fit aspect ratio.
		if (hw > Game.VIEWPORT_GAME_AR*hh) {
			// Add requested margin;
			hw *= mtc.marginScalingFactor;
			// Increase height.
			hh = hw * Game.VIEWPORT_GAME_AR_INV;
		}
		else {
			// Add requested margin;
			hh *= mtc.marginScalingFactor;
			// Increase width.
			hw = hh * Game.VIEWPORT_GAME_AR;
		}
		// Compute the rotation angle of this rectangle.
		src.thetaZ = base.angleRad();
		// Compute the height the camera needs to be located
		// at.
		if (Game.viewportGame.getCamera() instanceof OrthographicCamera) {
			final OrthographicCamera ocam = (OrthographicCamera)Game.viewportGame.getCamera();
			ocam.setToOrtho(false, hw*2.0f, hh*2.0f);
		}
		else /*if (camera instanceof PerspectiveCamera)*/ {
			final PerspectiveCamera pcam = (PerspectiveCamera)Game.viewportGame.getCamera();
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
			spc.z = hh/(float)Math.tan(pcam.fieldOfView*0.5f);
		}
		
		// Compute the position the camera should be located
		// at. We need to convert the coordinates back to the
		// world coordinate system.
		dir.set(cx,cy).rotateRad(-src.thetaZ);
		spc.x = dir.x;
		spc.y = dir.y;
	}
}
