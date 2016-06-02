package de.homelab.madgaksha.entityengine;

/**
 * Global configuration for the default priority of each entity system. This
 * priority can be overridden with the appropriate constructor for an entity
 * system.
 * 
 * The lower the priority, the earlier the system is run.
 * 
 * @author madgaksha
 *
 */
public class DefaultPriority {
	private DefaultPriority() {
	}
	
	public final static int temporalSystem = 0;
	
	// updating
	public final static int lifeSystem = 1;
	public final static int cameraZoomingSystem = 2;
	public final static int aiSystem = 3;
	public final static int stickySystem = 4;
	public final static int scaleFromDistanceSystem = 4;
	public final static int inputPlayerDesktopSystem = 5;
	public final static int newtonianForceSystem = 6;
	public final static int movementSystem = 7;
	public final static int collisionSystem = 8;

	public final static int postEffectSystem = 9;	

	public final static int grantPositionSystem = 10;
	public final static int grantRotationSystem = 11;
	public final static int grantScaleSystem = 12;
	
	public final static int damageSystem = 13;

	// drawing
	public final static int viewportUpdateSystem = 14;
	public final static int birdsViewSpriteSystem = 15;
	public final static int spriteAnimationSystem = 16;
	public final static int spriteRenderSystem = 17;
	public final static int modelRenderSystem = 18;
	public final static int particleEffectRenderSystem = 19;
	
	public final static int timedCallbackSystem = 20;
}
