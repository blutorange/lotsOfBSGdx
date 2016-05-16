package de.homelab.madgaksha;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.GameViewport;

/**
 * For sharing global variables during one game loop etc.
 * @author madgaksha
 *
 */
public class GlobalBag {
	private GlobalBag(){}
		
	public static float worldVisibleMinX;
	public static float worldVisibleMaxX;
	public static float worldVisibleMinY;
	public static float worldVisibleMaxY;
	
	public static ALevel level;
	
	public static GameViewport viewportGame;
	
	public static Entity playerEntity;
}
