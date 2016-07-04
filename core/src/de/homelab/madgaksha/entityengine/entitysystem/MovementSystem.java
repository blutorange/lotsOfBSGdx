package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.level;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.level.MapData;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class MovementSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MovementSystem.class);

	private final MapData mapData;
	private final float tileWidth;
	private final float tileHeight;
	private final float tileWidthInverse;
	private final float tileHeightInverse;
	private final float reducedTileWidth;
	private final float reducedTileHeight;

	public MovementSystem() {
		this(DefaultPriority.movementSystem);
	}

	@SuppressWarnings("unchecked")
	public MovementSystem(int priority) {
		super(DisableIteratingSystem.all(PositionComponent.class, VelocityComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class), priority);
		mapData = level.getMapData();
		tileWidth = mapData.getWidthTiles();
		tileHeight = mapData.getHeightTiles();
		tileWidthInverse = 1.0f / mapData.getWidthTiles();
		tileHeightInverse = 1.0f / mapData.getHeightTiles();
		reducedTileHeight = 0.95f * tileHeight;
		reducedTileWidth = 0.95f * tileWidth;
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final BoundingBoxMapComponent bbmc = Mapper.boundingBoxMapComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		// TODO don't check with position but with position +
		// BoundingBoxMapComponent.
		if (pc.limitToMap && bbmc != null) {
			// Move position component to the center of the bounding box.
			float dx = bbmc.minX + 0.5f * (bbmc.maxX - bbmc.minX);
			float dy = bbmc.minY + 0.5f * (bbmc.maxY - bbmc.minY);
			pc.x += dx;
			pc.y += dy;
			// Move on the map, respecting blocking tiles
			// mapMovement(pc, vc.x * deltaTime, vc.y * deltaTime, hh, hw);
			mapMovement(pc, vc, deltaTime);
			// Move position component back to its original position.
			pc.x -= dx;
			pc.y -= dy;
		} else {
			pc.x += vc.x * deltaTime;
			pc.y += vc.y * deltaTime;
		}
		pc.z += vc.z * deltaTime;
	}

	// Still needs some work with bounding boxes.
	@SuppressWarnings("unused")
	@Deprecated
	private void mapMovement(PositionComponent pc, float deltaX, float deltaY, float hw, float hh) {
		// Target position.
		float targetX = pc.x + deltaX;
		float targetY = pc.y + deltaY;
		// Tile coordinates of the current position.
		int tileCurX = (int) (pc.x * tileWidthInverse);
		int tileCurY = (int) (pc.y * tileHeightInverse);
		// Tile coordinates of the current position.
		int tileTargetX = (int) (targetX * tileWidthInverse);
		int tileTargetY = (int) (targetY * tileHeightInverse);
		// Tile coordinates of the top and right edges.
		// int tileRight = (int) ((pc.x + hw) * tileWidthInverse);
		// int tileTop = (int) ((pc.y + hh) * tileHeightInverse);
		// int tileLeft = (int) ((pc.x - hw) * tileWidthInverse);
		// int tileBottom = (int) ((pc.y - hh) * tileHeightInverse);
		// // Check if we are currently not on any blocking tile - may happen
		// due
		// // to limited precision.
		// if (mapData.isTileAreaAnyBlocking(tileLeft, tileRight, tileBottom,
		// tileTop))
		// return;

		// Check direction of movement
		if (deltaX > 0.0f) {
			if (deltaY > 0.0f) {
				// UP-RIGHT

				// Tile coordinates of the top and right edges.
				int tileRight = (int) ((pc.x + hw) * tileWidthInverse);
				int tileTop = (int) ((pc.y + hh) * tileHeightInverse);
				// Check if we are currently not on any blocking tile - may
				// happen due
				// to limited precision.
				if (mapData.isTileAreaAnyBlocking(tileCurX, tileRight, tileCurY, tileTop))
					return;
				while (true) {
					// Distance of the bounding box to the next map tile.
					float distRight = tileWidth * (tileRight + 1) - (pc.x + hw);
					float distTop = tileHeight * (tileTop + 1) - (pc.y + hh);
					// Distance we could move up if we want all the way right,
					// and vice versa.
					float mx = distTop * deltaX;
					float my = distRight * deltaY;
					// No movement possible.
					if (mx == 0.0f && my == 0.0f)
						break;
					// Check if we can go up or right.
					if (mx <= my) {
						// Go all the way up
						++tileCurY;
						++tileTop;
						pc.x += mx / deltaY;
						pc.y = tileTop * tileHeight - hh;
						// Don't go beyond the target.
						if (pc.x >= targetX || pc.y >= targetY) {
							pc.x = targetX;
							pc.y = targetY;
							break;
						}
						// Check if there is a blocking tile in our way.
						int tileRangeMin = (int) ((pc.x - hw) * tileWidthInverse);
						int tileRangeMax = (int) ((pc.x + hw) * tileWidthInverse);
						if (mapData.isTileRangeXAnyBlocking(tileCurY, tileRangeMin, tileRangeMax)) {
							break;
						}
					}
					if (my <= mx) {
						// Go all the way right
						++tileCurX;
						++tileRight;
						pc.x = tileWidth * tileRight - hw;
						pc.y += my / deltaX;
						// Don't go beyond the target.
						if (pc.x >= targetX || pc.y >= targetY) {
							pc.x = targetX;
							pc.y = targetY;
							break;
						}
						// Check if there is a blocking tile in our way.
						int tileRangeMin = (int) ((pc.y - hh) * tileHeightInverse);
						int tileRangeMax = (int) ((pc.y + hh) * tileHeightInverse);
						if (mapData.isTileRangeYAnyBlocking(tileCurX, tileRangeMin, tileRangeMax)) {
							break;
						}
					}
				}
			} else {
				// DOWN-RIGHT

				deltaY = -deltaY;
				// Tile coordinates of the top and right edges.
				int tileRight = (int) ((pc.x + hw) * tileWidthInverse);
				int tileBottom = (int) ((pc.y - hh) * tileHeightInverse);
				// Check if we are currently not on any blocking tile - may
				// happen due
				// to limited precision.
				if (mapData.isTileAreaAnyBlocking(tileCurX, tileRight, tileBottom, tileCurY))
					return;
				while (true) {
					// Distance of the bounding box to the next map tile.
					float distRight = tileWidth * (tileRight + 1) - (pc.x + hw);
					float distBottom = (pc.y - hh) - tileHeight * (tileBottom);
					// Distance we could move up if we want all the way right,
					// and vice versa.
					float mx = distBottom * deltaX;
					float my = distRight * deltaY;
					// No movement possible.
					if (mx == 0.0f && my == 0.0f)
						break;
					// Check if we can go down or right.
					if (mx <= my) {
						// Go all the way down
						pc.x += mx / deltaY;
						pc.y = tileBottom * tileHeight + hh;
						--tileCurY;
						--tileBottom;
						// Don't go beyond the target.
						if (pc.x >= targetX || pc.y <= targetY) {
							pc.x = targetX;
							pc.y = targetY;
							break;
						}
						// Check if there is a blocking tile in our way.
						int tileRangeMin = (int) ((pc.x - hw) * tileWidthInverse);
						int tileRangeMax = (int) ((pc.x + hw) * tileWidthInverse);
						if (mapData.isTileRangeXAnyBlocking(tileCurY, tileRangeMin, tileRangeMax))
							break;
					}
					if (my <= mx) {
						// Go all the way right
						++tileCurX;
						++tileRight;
						pc.x = tileWidth * tileRight - hw;
						pc.y -= my / deltaX;
						// Don't go beyond the target.
						if (pc.x >= targetX || pc.y <= targetY) {
							pc.x = targetX;
							pc.y = targetY;
							break;
						}
						// Check if there is a blocking tile in our way.
						int tileRangeMin = (int) ((pc.y - hh) * tileHeightInverse);
						int tileRangeMax = (int) ((pc.y + hh) * tileHeightInverse);
						if (mapData.isTileRangeYAnyBlocking(tileCurX, tileRangeMin, tileRangeMax)) {
							break;
						}
					}
				}
			}
		} else if (deltaY > 0.0f) {
			// UP-LEFT
			deltaX = -deltaX;
			// Tile coordinates of the top and right edges.
			int tileLeft = (int) ((pc.x - hw) * tileWidthInverse);
			int tileTop = (int) ((pc.y + hh) * tileHeightInverse);
			// Check if we are currently not on any blocking tile - may happen
			// due
			// to limited precision.
			if (mapData.isTileAreaAnyBlocking(tileLeft, tileCurX, tileCurY, tileTop))
				return;
			while (true) {
				// Distance of the bounding box to the next map tile.
				float distLeft = (pc.x - hw) - tileWidth * tileLeft;
				float distTop = tileHeight * (tileTop + 1) - (pc.y + hh);
				// Distance we could move up if we want all the way right,
				// and vice versa.
				float mx = distTop * deltaX;
				float my = distLeft * deltaY;
				// No movement possible.
				if (mx == 0.0f && my == 0.0f)
					break;
				// Check if we can go up or left.
				if (mx <= my) {
					// Go all the way up
					++tileCurY;
					++tileTop;
					pc.x += mx / deltaY;
					pc.y = tileTop * tileHeight - hh;
					// Don't go beyond the target.
					if (pc.x <= targetX || pc.y >= targetY) {
						pc.x = targetX;
						pc.y = targetY;
						break;
					}
					// Check if there is a blocking tile in our way.
					int tileRangeMin = (int) ((pc.x - hw) * tileWidthInverse);
					int tileRangeMax = (int) ((pc.x + hw) * tileWidthInverse);
					if (mapData.isTileRangeXAnyBlocking(tileCurY, tileRangeMin, tileRangeMax)) {
						break;
					}
				}
				if (my <= mx) {
					// Go all the way left
					pc.x = tileWidth * tileLeft + hw;
					pc.y += my / deltaX;
					--tileCurX;
					--tileLeft;
					// Don't go beyond the target.
					if (pc.x <= targetX || pc.y >= targetY) {
						pc.x = targetX;
						pc.y = targetY;
						break;
					}
					// Check if there is a blocking tile in our way.
					int tileRangeMin = (int) ((pc.y - hh) * tileHeightInverse);
					int tileRangeMax = (int) ((pc.y + hh) * tileHeightInverse);
					if (mapData.isTileRangeYAnyBlocking(tileCurX, tileRangeMin, tileRangeMax)) {
						break;
					}
				}
			}
		} else {
			// DOWN-LEFT
			deltaX = -deltaX;
			deltaY = -deltaY;
			// Tile coordinates of the top and right edges.
			int tileLeft = (int) ((pc.x - hw) * tileWidthInverse);
			int tileBottom = (int) ((pc.y - hh) * tileHeightInverse);
			// Check if we are currently not on any blocking tile - may happen
			// due to limited precision.
			if (mapData.isTileAreaAnyBlocking(tileLeft, tileCurX, tileBottom, tileCurY))
				return;
			while (true) {
				// Distance of the bounding box to the next map tile.
				float distLeft = (pc.x - hw) - tileWidth * tileLeft;
				float distBottom = (pc.y - hh) - tileHeight * (tileBottom);
				// Distance we could move up if we want all the way right,
				// and vice versa.
				float mx = distBottom * deltaX;
				float my = distLeft * deltaY;
				// No movement possible.
				if (mx == 0.0f && my == 0.0f)
					break;
				// Check if we can go down or left.
				if (mx <= my) {
					// Go all the way down
					pc.x += mx / deltaY;
					pc.y = tileBottom * tileHeight + hh;
					--tileCurY;
					--tileBottom;
					// Don't go beyond the target.
					if (pc.x <= targetX || pc.y <= targetY) {
						pc.x = targetX;
						pc.y = targetY;
						break;
					}
					// Check if there is a blocking tile in our way.
					int tileRangeMin = (int) ((pc.x - hw) * tileWidthInverse);
					int tileRangeMax = (int) ((pc.x + hw) * tileWidthInverse);
					if (mapData.isTileRangeXAnyBlocking(tileCurY, tileRangeMin, tileRangeMax))
						break;
				}
				if (my <= mx) {
					// Go all the way left
					pc.x = tileWidth * tileLeft + hw;
					pc.y += my / deltaX;
					--tileCurX;
					--tileLeft;
					// Don't go beyond the target.
					if (pc.x <= targetX || pc.y <= targetY) {
						pc.x = targetX;
						pc.y = targetY;
						break;
					}
					// Check if there is a blocking tile in our way.
					int tileRangeMin = (int) ((pc.y - hh) * tileHeightInverse);
					int tileRangeMax = (int) ((pc.y + hh) * tileHeightInverse);
					if (mapData.isTileRangeYAnyBlocking(tileCurX, tileRangeMin, tileRangeMax)) {
						break;
					}
				}
			}
		}
	}

	private void mapMovement(PositionComponent pc, VelocityComponent vc, float deltaTime) {
		// Limit movement speed to avoid clipping.
		float f = 1.0f;
		float dx = Math.abs(vc.x * deltaTime);
		float dy = Math.abs(vc.y * deltaTime);
		if (dx > dy) {
			if (dx > tileWidth * 0.9f) {
				f = reducedTileWidth / dx;
			}
		} else if (dy > tileWidth * 0.9f) {
			f = reducedTileHeight / dy;
		}
		
		// Get new position
		final float newx = pc.x + f * vc.x * deltaTime;
		final float newy = pc.y + f * vc.y * deltaTime;

		int tx = (int) (pc.x * tileWidthInverse);
		int ty = (int) (pc.y * tileHeightInverse);

		int newTileX = (int) (newx * tileWidthInverse);
		int newTileY = (int) (newy * tileWidthInverse);
		
		if (!mapData.isTileBlocking(newx, newy) && (newTileX == tx || newTileY == ty)) {
			// Allow move immediately when we do not move diagonally.
			pc.x = newx;
			pc.y = newy;
		} else {
			// Otherwise, check if we can move vertically / horizontally.
			// Bottom left tile point.
			// Get quadrant of of velocity vector
			if (vc.x > 0)
				++tx;
			if (vc.y > 0)
				++ty;
			// Check which tile this movement would get us in and move only
			// vertically/horizontally.
			if (vc.x * vc.y * (vc.x * (ty * tileHeight - pc.y) - vc.y * (tx * tileWidth - pc.x)) > 0) {
				if (mapData.isTileBlocking(pc.x, newy)) {
					if (!mapData.isTileBlocking(newx, pc.y)) {
						pc.x = newx;
					}
				} else {
					pc.y = newy;
				}
			} else {
				if (mapData.isTileBlocking(newx, pc.y)) {
					if (!mapData.isTileBlocking(pc.x, newy))
						pc.y = newy;
				} else {
					pc.x = newx;
				}
			}
		}
	}

}
