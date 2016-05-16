package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.InputComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class InputVelocitySystem extends IteratingSystem {

	private final static Logger LOG = Logger.getLogger(InputVelocitySystem.class);
	private static Vector2 v = new Vector2();
	private static Vector2 w = new Vector2();
	private float lastAngle = 0.0f;
	
	public InputVelocitySystem() {
		this(DefaultPriority.inputVelocitySystem);
	}

	@SuppressWarnings("unchecked")
	public InputVelocitySystem(int priority) {
		super(Family.all(VelocityComponent.class, InputComponent.class,DirectionComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final InputComponent ic = Mapper.inputComponent.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);
		v.set((Gdx.input.isKeyPressed(ic.right)) ? ic.speed : (Gdx.input.isKeyPressed(ic.left)) ? -ic.speed : 0.0f,
		      (Gdx.input.isKeyPressed(ic.up)) ? ic.speed : (Gdx.input.isKeyPressed(ic.down)) ? -ic.speed : 0.0f);
		w.set((Gdx.input.isKeyPressed(ic.directionLeft)) ? 1.0f : (Gdx.input.isKeyPressed(ic.directionRight)) ? -1.0f : 0.0f,
		      (Gdx.input.isKeyPressed(ic.directionUp)) ? 1.0f : (Gdx.input.isKeyPressed(ic.directionDown)) ? -1.0f : 0.0f);
		if (ic.relativeToCamera) v.rotate(-GlobalBag.viewportGame.getRotationUpXY());
		if (ic.orientToScreen) dc.degree = 450.0f+GlobalBag.viewportGame.getRotationUpXY()+ic.screenOrientation;
		else if (w.len2() > 0.5f) dc.degree = 450.0f+GlobalBag.viewportGame.getRotationUpXY()+(lastAngle=w.angle());
		else dc.degree = 450.0f+GlobalBag.viewportGame.getRotationUpXY()+(lastAngle);
		vc.x = v.x;
		vc.y = v.y;
	}
}