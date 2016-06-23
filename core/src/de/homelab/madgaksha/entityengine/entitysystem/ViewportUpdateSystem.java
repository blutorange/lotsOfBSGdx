package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.visibleWorld;
import static de.homelab.madgaksha.GlobalBag.visibleWorldBoundingBox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates camera and viewport.
 * 
 * @author madgaksha
 *
 */
public class ViewportUpdateSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ViewportUpdateSystem.class);
	private final static float[] vertices = new float[8];

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
		final PerspectiveCamera psc = vc.viewport.getPerspectiveCamera();

		// Set camera position.
		psc.position.x = pc.x + pc.offsetX;
		psc.position.y = pc.y + pc.offsetY;
		psc.position.z = pc.z + pc.offsetZ;

		// Set rotation.
		vc.viewport.setRotation(rc.thetaZ);

		// Set clipping planes appropriately.
		psc.near = 0.5f * pc.z;
		psc.far = 1.5f * pc.z;

		// Update viewport, which updates the camera.
		vc.viewport.apply();

		// Compute intersection of the x-y-plane with the
		// four ray going from the camera through each of
		// the four corner points of the frustum.
		// This defines the area of the world that
		// is currently visible.
		final Vector3[] pp = psc.frustum.planePoints;
		float f;

		// The camera always looks down, so this won't
		// divide by 0. Until I decide to allow other
		// camera angles...
		f = pp[0].z / (pp[4].z - pp[0].z);
		vertices[0] = pp[0].x - f * (pp[4].x - pp[0].x); // bottom left
		vertices[1] = pp[0].y - f * (pp[4].y - pp[0].y); // of the screen

		f = pp[1].z / (pp[5].z - pp[1].z);
		vertices[2] = pp[1].x - f * (pp[5].x - pp[1].x); // bottom right
		vertices[3] = pp[1].y - f * (pp[5].y - pp[1].y); // of the screen

		f = pp[2].z / (pp[6].z - pp[2].z);
		vertices[4] = pp[2].x - f * (pp[6].x - pp[2].x); // top right
		vertices[5] = pp[2].y - f * (pp[6].y - pp[2].y); // of the screen

		f = pp[3].z / (pp[7].z - pp[3].z);
		vertices[6] = pp[3].x - f * (pp[7].x - pp[3].x); // top left
		vertices[7] = pp[3].y - f * (pp[7].y - pp[3].y); // of the screen

		// Width of the viewport in world coordinates
		f = (vertices[0] - vertices[2]) * (vertices[0] - vertices[2])
				+ (vertices[1] - vertices[3]) * (vertices[1] - vertices[3]);
		f = (float) Math.sqrt(f);

		// Tell the viewport which part of the world it is looking at.
		vc.viewport.setWorldWidth(f);
		vc.viewport.setWorldHeight(f * Game.VIEWPORT_GAME_AR_INV);

		// Save visible area.
		visibleWorld.setVertices(vertices);
		visibleWorldBoundingBox = visibleWorld.getBoundingRectangle();

	}
}