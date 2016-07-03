package de.homelab.madgaksha.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.logging.Logger;

public class HomingForceField implements IForceField {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(HomingForceField.class);
	// private final static Vector2 v = new Vector2();

	public final PositionComponent target;
	public final float springConstant;

	public HomingForceField(PositionComponent target, float springConstant) {
		this.target = target;
		this.springConstant = springConstant;
	}

	@Override
	public void apply(Entity e, ForceComponent fc) {
		PositionComponent pc = Mapper.positionComponent.get(e);
		fc.x = springConstant * (target.x - pc.x);
		fc.y = springConstant * (target.y - pc.y);
	}
}
