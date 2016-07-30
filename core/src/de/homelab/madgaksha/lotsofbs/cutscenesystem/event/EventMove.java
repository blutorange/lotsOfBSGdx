package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.idEntityMap;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.CollisionSystem;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.path.APath;
import de.homelab.madgaksha.lotsofbs.path.EPath;

public class EventMove extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventMove.class);
	private final static APath EMPTY_PATH_ARRAY[] = new APath[0];
	private final static Entity EMPTY_ENTITY = new Entity();

	private float totalTime = 0.0f;
	private boolean movementDone = false;
	private boolean collision = false;

	private APath[] pathList = EMPTY_PATH_ARRAY;
	private Entity entityToMove = EMPTY_ENTITY;

	private int currentPathIndex = -1;
	private final Vector2 vector = new Vector2();

	public EventMove() {

	}

	public EventMove(Entity entity, APath pathList[], boolean collision) {
		if (pathList != null)
			this.pathList = pathList;
		if (entity != null)
			this.entityToMove = entity;
		this.collision = collision;
	}

	public EventMove(Entity entity, List<APath> pathList, boolean collision) {
		this(entity, pathList.toArray(EMPTY_PATH_ARRAY), collision);
	}

	@Override
	public void reset() {
		pathList = EMPTY_PATH_ARRAY;
		entityToMove = EMPTY_ENTITY;
		collision = false;
	}

	@Override
	public boolean isFinished() {
		return movementDone;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, boolean isSpedup) {
		if (isSpedup)
			totalTime += 10.0f * deltaTime;
		else
			totalTime += deltaTime;

		// Move to the specified position.
		pathList[currentPathIndex].applyTotal(totalTime, vector);
		PositionComponent pc = Mapper.positionComponent.get(entityToMove);

		if (pc == null)
			return;
		if (totalTime >= pathList[currentPathIndex].getTMax()) {
			pathList[currentPathIndex].applyTotal(pathList[currentPathIndex].getTMax(), vector);
			++currentPathIndex;
			totalTime = 0.0f;
			if (currentPathIndex == pathList.length) {
				currentPathIndex = pathList.length - 1;
				movementDone = true;
			}
			pathList[currentPathIndex].setOrigin(pc.x, pc.y);
		}
		if (!collision || (!pc.limitToMap || !level.getMapData().isTileBlocking(vector.x, vector.y))) {
			pc.x = vector.x;
			pc.y = vector.y;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean begin() {
		currentPathIndex = 0;
		totalTime = 0.0f;
		PositionComponent pc = Mapper.positionComponent.get(entityToMove);
		if (pathList.length == 0 || pc == null) {
			LOG.error("path or position component not found");
			return false;
		}
		if (pathList.length > 0)
			pathList[0].setOrigin(pc.x, pc.y);
		gameEntityEngine.getSystem(CollisionSystem.class).setProcessing(false);
		return true;
	}

	@Override
	public void end() {
		gameEntityEngine.getSystem(CollisionSystem.class).setProcessing(true);
	}

	/**
	 * @param s Scanner from which to read.
	 * @param fh The file handle of the file being used. Should be used only for directories.
	 */
	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		// Read entity name
		String guid = FileCutsceneProvider.readNextGuid(s);
		if (guid == null) {
			LOG.error("expected entity name");
			return null;
		}
		Entity entity = idEntityMap.get(guid);

		if (entity == null) {
			LOG.error("no such entity: " + guid);
			return null;
		}

		// Read list of paths entity should be moved along.
		List<APath> pathList = new ArrayList<APath>(10);
		boolean collision = true;
		while (s.hasNext()) {
			// Read path type, animation time and relative flag.
			String next = s.next();
			if (next.equalsIgnoreCase("Collision")) {
				if (s.hasNextBoolean()) {
					collision = s.nextBoolean();
				} else {
					LOG.error("expected boolean after Collision");
					return null;
				}
			} else {
				String pathType = "PATH_" + next.toUpperCase(Locale.ROOT);
				Float tmax = FileCutsceneProvider.nextNumber(s);
				if (tmax == null) {
					LOG.error("expected time value");
					return null;
				}
				if (!s.hasNext()) {
					LOG.error("expected absolute/relative flag");
					return null;
				}
				boolean relative = s.next().equalsIgnoreCase("R");
				// Try and instantiate path.
				try {
					EPath path = EPath.valueOf(pathType);
					APath newPath = path.readNextObject(tmax, relative, level.getMapData().getWidthTiles(),
							level.getMapData().getHeightTiles(), s);
					if (newPath == null)
						return null;
					pathList.add(newPath);
				} catch (IllegalArgumentException ex) {
					LOG.error("no such path: " + pathType);
					return null;
				}
			}
		}

		return new EventMove(entity, pathList, collision);
	}
}
