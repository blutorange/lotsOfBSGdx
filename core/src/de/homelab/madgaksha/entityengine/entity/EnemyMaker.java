package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.PositionComponent;


public abstract class EnemyMaker extends AEntityMaker implements IBehaviour {

	
	public EnemyMaker(Shape2D shape, ETrigger trigger) {
		super();
		Component triggerComponent = MakerUtils.makeTrigger(trigger, shape);
		PositionComponent positionComponent = MakerUtils.makePositionAtCenter(shape);
		this.add(triggerComponent);
		this.add(positionComponent);
		//TODO remove component when triggered to activate enemy ?
		// this.add(deactivatedComponent / inactiveComponent);
		//TODO
		// Or add a PropertiesToBeAddedLaterComponent?
		if (triggerComponent != null) this.add(triggerComponent);
}

	@Override
	public void reinitializeEntity() {
	}

	@Override
	public void behave() {
		// TODO Auto-generated method stub
	}	
}
