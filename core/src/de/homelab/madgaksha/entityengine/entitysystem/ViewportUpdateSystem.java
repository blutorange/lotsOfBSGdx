package de.homelab.madgaksha.entityengine.entitysystem;

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

/**
 * Updates camera and viewport.
 * @author madgaksha
 *
 */
public class ViewportUpdateSystem extends IteratingSystem {
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
	}
}

