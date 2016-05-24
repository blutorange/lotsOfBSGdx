package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.soundPlayer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class InputPlayerDesktopSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InputPlayerDesktopSystem.class);
	private static Vector2 v = new Vector2();
	private static Vector2 w = new Vector2();
	private float lastAngle = 180.0f;
	
	public InputPlayerDesktopSystem() {
		this(DefaultPriority.inputPlayerDesktopSystem);
	}

	@SuppressWarnings("unchecked")
	public InputPlayerDesktopSystem(int priority) {
		super(Family.all(VelocityComponent.class, InputDesktopComponent.class,DirectionComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final InputDesktopComponent ic = Mapper.inputDesktopComponent.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);
		
		v.set((Gdx.input.isKeyPressed(ic.right)) ? 1.0f : (Gdx.input.isKeyPressed(ic.left)) ? -1.0f : 0.0f,
		      (Gdx.input.isKeyPressed(ic.up)) ? 1.0f : (Gdx.input.isKeyPressed(ic.down)) ? -1.0f : 0.0f);
		w.set((Gdx.input.isKeyPressed(ic.directionLeft)) ? 1.0f : (Gdx.input.isKeyPressed(ic.directionRight)) ? -1.0f : 0.0f,
		      (Gdx.input.isKeyPressed(ic.directionUp)) ? 1.0f : (Gdx.input.isKeyPressed(ic.directionDown)) ? -1.0f : 0.0f);
		
		if (ic.relativeToCamera) v.rotate(-viewportGame.getRotationUpXY());
		if (ic.orientToScreen) dc.degree = 450.0f+viewportGame.getRotationUpXY()+ic.screenOrientation;
		else if (w.len2() > 0.5f) dc.degree = 450.0f+viewportGame.getRotationUpXY()+(lastAngle=w.angle());
		else dc.degree = 450.0f+viewportGame.getRotationUpXY()+(lastAngle);
		
		float f = Gdx.input.isKeyPressed(ic.speedTrigger) ? ic.accelerationFactorHigh : ic.accelerationFactorLow;
		vc.x = (vc.x+f*v.x)*ic.frictionFactor;
		vc.y = (vc.y+f*v.y)*ic.frictionFactor;
//		g = vc.x*vc.x+vc.y*vc.y;
//		if (g>f*f) {
//			f = f*f/g;
//			vc.x *= f;
//			vc.y *= f;
//		}
		
		if (cameraTrackingComponent.focusPoints.size() > 1) {
			if (Gdx.input.isKeyJustPressed(ic.enemySwitcherPrev)) {
				soundPlayer.play(ESound.ENEMY_SWITCH);
				cameraTrackingComponent.trackedPointIndex++;
				if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
					cameraTrackingComponent.trackedPointIndex = 0;
				}
			} else if (Gdx.input.isKeyJustPressed(ic.enemySwitcherNext)) {
				soundPlayer.play(ESound.ENEMY_SWITCH);
				cameraTrackingComponent.trackedPointIndex--;
				if (cameraTrackingComponent.trackedPointIndex < 0) {
					cameraTrackingComponent.trackedPointIndex = cameraTrackingComponent.focusPoints.size() - 1;
				}
			}
		}
	}
}
