package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.level.MapProperties;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class MovementSystem extends IteratingSystem {
	private final static Logger LOG = Logger.getLogger(MovementSystem.class);
	private static float newx, newy;

	private final MapProperties mapProperties;
	private final float tileWidth;
	private final float tileHeight;
	private final float tileWidthInverse;
	private final float tileHeightInverse;
	
	public MovementSystem() {
		this(DefaultPriority.movementSystem);
	}

	@SuppressWarnings("unchecked")
	public MovementSystem(int priority) {
		super(Family.all(PositionComponent.class, VelocityComponent.class, TemporalComponent.class).get(), priority);
		mapProperties = GlobalBag.level.getMapProperties();
		tileWidth = mapProperties.getWidthTiles();
		tileHeight = mapProperties.getHeightTiles();
		tileWidthInverse = 1.0f/mapProperties.getWidthTiles();
		tileHeightInverse = 1.0f/mapProperties.getHeightTiles();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		if (pc.limitToMap) {
			newx = pc.x + vc.x * deltaTime;
			newy = pc.y + vc.y * deltaTime;
			if (mapProperties.isTileBlocking(newx, newy)) {
				// Bottom left tile point.
				int tx = (int) (pc.x * tileWidthInverse);
				int ty = (int) (pc.y * tileHeightInverse);
				// Get quadrant of of velocity vector
				if (vc.x > 0)
					++tx;
				if (vc.y > 0)
					++ty;
				// Check which tile this movement would get us in and move only vertically/horizontally.
				if (vc.x*vc.y*(vc.x * (ty * tileHeight - pc.y) -vc.y * (tx * tileWidth - pc.x)) > 0) {
					newy = pc.y + vc.y*deltaTime;
					if (mapProperties.isTileBlocking(pc.x, newy)) {
						newx = pc.x + vc.x*deltaTime;
						if (!mapProperties.isTileBlocking(newx, pc.y)) pc.x = newx;
					}
					else {
						pc.y = newy;
					}
				}
				else {
					newx = pc.x + vc.x*deltaTime;
					if (mapProperties.isTileBlocking(newx, pc.y)) {
						newy = pc.y + vc.y*deltaTime;
						if (!mapProperties.isTileBlocking(pc.x, newy)) pc.y = newy;
					}
					else {
						pc.x = newx;
					}
				}
			} else {
				pc.x = newx;
				pc.y = newy;
			}
		} else {
			pc.x += vc.x * deltaTime;
			pc.y += vc.y * deltaTime;
		}
		pc.z += vc.z * deltaTime;
	}

}
