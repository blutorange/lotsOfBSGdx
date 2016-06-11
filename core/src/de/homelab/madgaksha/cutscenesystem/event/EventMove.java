package de.homelab.madgaksha.cutscenesystem.event;

import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.idEntityMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.logging.Logger;
import path.APath;
import path.EPath;

public class EventMove extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventMove.class);
	private final static APath EMPTY_PATH_ARRAY[] = new APath[0];
	private final static Entity EMPTY_ENTITY = new Entity();
	
	private float totalTime = 0.0f;
	private boolean movementDone = false;
	
	private APath[] pathList = EMPTY_PATH_ARRAY;
	private Entity entityToMove = EMPTY_ENTITY;
	
	private int currentPathIndex = -1;
	private final Vector2 vector = new Vector2();
	
	
	public EventMove() {
		
	}
	
	public EventMove(Entity entity, APath pathList[]) {
		if (pathList != null) this.pathList = (APath[])pathList;
		if (entity != null) this.entityToMove = entity;
	}
	
	public EventMove(Entity entity, List<APath> pathList) {
		this(entity, pathList.toArray(EMPTY_PATH_ARRAY));
	}
	
	@Override
	public void reset() {
		pathList = EMPTY_PATH_ARRAY;
		entityToMove = EMPTY_ENTITY;
	}

	@Override
	public boolean isFinished() {
		return movementDone;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime) {
		totalTime += deltaTime;
		
		// Move to the specified position.
		pathList[currentPathIndex].apply(totalTime, vector);
		PositionComponent pc = Mapper.positionComponent.get(entityToMove);
		
		if (pc == null) return;
		if (totalTime >= pathList[currentPathIndex].tmax) {
			pathList[currentPathIndex].apply(pathList[currentPathIndex].tmax, vector);
			++currentPathIndex;
			totalTime = 0.0f;
			if (currentPathIndex == pathList.length) {
				currentPathIndex = pathList.length - 1;
				movementDone = true;
			}
			pathList[currentPathIndex].setOrigin(pc.x, pc.y);
		}
		if (!pc.limitToMap || !level.getMapData().isTileBlocking(vector.x, vector.y)) {
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
		if (pathList.length > 0) pathList[0].setOrigin(pc.x, pc.y);
		return true;
	}

	@Override
	public void end() {
	}
	
	public static ACutsceneEvent readNextObject(Scanner s) {
		if (!s.hasNextLine()) {
			LOG.error("expected entity name");
			return null;
		}
		
		// Read entity name
		String guid = s.nextLine().toLowerCase(Locale.ROOT).trim().replace(" +", "_");
		Entity entity = idEntityMap.get(guid);

		if (entity == null) {
			LOG.error("no such entity: " + guid);
			return null;
		}
				
		// Read list of paths entity should be moved along.
		List<APath> pathList = new ArrayList<APath>(10);
		while (s.hasNext()) {
			// Read path type, animation time and relative flag.
			String pathType = "PATH_" + s.next().toUpperCase();
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
			      APath newPath = path.readNextObject(tmax, relative, s);
			      if (newPath == null) return null;
			     pathList.add(newPath);			      
			}
			catch (IllegalArgumentException ex) {
				LOG.error("no such path: " + pathType);
				return null;
			}
		}
		
		return new EventMove(entity, pathList);
	}
}
