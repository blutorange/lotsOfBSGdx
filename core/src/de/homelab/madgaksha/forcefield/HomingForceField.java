package de.homelab.madgaksha.forcefield;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.logging.Logger;

public class HomingForceField implements IForceField {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(HomingForceField.class);
	private final static Vector2 v = new Vector2();

	public final PositionComponent target;
	public final float springConstant;

	public HomingForceField(PositionComponent target, float springConstant) {
		this.target = target;
		this.springConstant = springConstant;
	}

	@Override
	public Vector2 apply(Entity e) {
		PositionComponent pc = Mapper.positionComponent.get(e);
		v.set(target.x - pc.x, target.y - pc.y).scl(springConstant);
		return v;
	}
}
