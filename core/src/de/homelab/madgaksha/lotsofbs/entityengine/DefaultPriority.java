package de.homelab.madgaksha.lotsofbs.entityengine;

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
	public final static int forceFieldSystem = 6;
	public final static int accelerationSystem = 7;
	public final static int velocityFieldSystem = 8;
	public final static int movementSystem = 9;
	public final static int collisionSystem = 10;

	public final static int postEffectSystem = 11;
	public final static int coneDistributionSystem = 12;

	public final static int grantPositionSystem = 13;
	public final static int grantRotationSystem = 14;
	public final static int grantScaleSystem = 15;
	public final static int grantDirectionSystem = 16;

	public final static int damageSystem = 17;

	// drawing
	public final static int viewportUpdateSystem = 18;
	public final static int animationModeSystem = 19;
	public final static int birdsViewSpriteSystem = 20;
	public final static int spriteAnimationSystem = 21;
	public final static int spriteRenderSystem = 22;
	public final static int modelRenderSystem = 23;
	public final static int particleEffectRenderSystem = 24;

	public final static int timedCallbackSystem = 25;
	public final static int callbackOnReenterSystem = 26;

}
