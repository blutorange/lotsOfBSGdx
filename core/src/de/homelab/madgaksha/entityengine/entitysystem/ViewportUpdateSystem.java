package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.worldVisibleMaxX;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMaxY;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMinX;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMinY;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates camera and viewport.
 * @author madgaksha
 *
 */
public class ViewportUpdateSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ViewportUpdateSystem.class);
	private static float upx,upy,otx,oty, hh, hw;
	private static float tmp, mapMinX, mapMinY, mapMaxX, mapMaxY;
	
	public ViewportUpdateSystem() {
		this(DefaultPriority.viewportUpdateSystem);
	}

	@SuppressWarnings("unchecked")
	public ViewportUpdateSystem(int priority) {
		super(Family.all(ViewportComponent.class, PositionComponent.class, RotationComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final ViewportComponent vc = Mapper.viewportComponent.get(entity);
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		
		final float viewportWidth = 2.0f * vc.viewport.getTanFovyH() * pc.z;
		final PerspectiveCamera psc = vc.viewport.getPerspectiveCamera();
		
		// Set camera position.
		psc.position.x = pc.x;
		psc.position.y = pc.y;
		psc.position.z = pc.z;
		
		// Set rotation.
		vc.viewport.setRotation(rc.thetaZ);

		// Tell the viewport which part of the world it is looking at.
		vc.viewport.setWorldWidth(viewportWidth);
		vc.viewport.setWorldHeight(viewportWidth*Game.VIEWPORT_GAME_AR_INV);
		
		// Set clipping planes appropriately.
		psc.near = 0.5f * pc.z;
		psc.far  = 1.5f * pc.z;
		
		// Update viewport, which updates the camera.
		vc.viewport.apply();
		
		// TODO
		// Compute the frustum. Compute the four rays for each corner
		// and intersect with the xy-plane.
		// 
		// Compute visible boundaries of the xy-plane.
		//  - pc is the center of the rectangle.
		//  - up is the vector along the vertical screen axis
		//  - ort = up x z = (up_y, -up_x) is the vector orthogonal to to the up vector
		//  -  hh and hw are half the width / height of the rectangle.
		// The four corner of the visible area in world coordinates are then:
		//   pc+ot*hw
		//   pc-ot*hw
		//   pc+up*hh
		//   pc-up*hh
		upx = psc.up.x;
		upy = psc.up.y;
		otx = upy;
		oty = -upx;
		hh = 2.0f*ALevel.CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF * pc.z;
		hw = hh * Game.VIEWPORT_GAME_AR;
		
		mapMinX = pc.x+otx*hw;
		mapMinY = pc.y+oty*hw;
		mapMaxX = mapMinX;
		mapMaxY = mapMinY;
		
		tmp = pc.x-otx*hw;
		if (tmp < mapMinX) mapMinX = tmp;
		else if (tmp > mapMaxX) mapMaxX = tmp;
		
		tmp = pc.x+upx*hh;
		if (tmp < mapMinX) mapMinX = tmp;
		else if (tmp > mapMaxX) mapMaxX = tmp;
			
		tmp = pc.x-upx*hh;
		if (tmp < mapMinX) mapMinX = tmp;
		else if (tmp > mapMaxX) mapMaxX = tmp;

		tmp = pc.y-oty*hw;
		if (tmp < mapMinY) mapMinY = tmp;
		else if (tmp > mapMaxY) mapMaxY = tmp;
		
		tmp = pc.y+upy*hh;
		if (tmp < mapMinY) mapMinY = tmp;
		else if (tmp > mapMaxY) mapMaxY = tmp;
			
		tmp = pc.y-upy*hh;
		if (tmp < mapMinY) mapMinY = tmp;
		else if (tmp > mapMaxY) mapMaxY = tmp;
		
		worldVisibleMinX = mapMinX;
		worldVisibleMaxX = mapMaxX;
		worldVisibleMinY = mapMinY;
		worldVisibleMaxY = mapMaxY;
	}
}

