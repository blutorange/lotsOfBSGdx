package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.entityengine.ETrigger;


public class CallbackMaker extends AEntityMaker implements IBehaviour{

	public CallbackMaker(Shape2D shape, ETrigger trigger, Method method, int loop, float interval) {
		super();
		Component triggerComponent = MakerUtils.makeTrigger(trigger, shape);
		if (triggerComponent != null) add(triggerComponent);
	}

	@Override
	public void reinitializeEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void behave() {
		// TODO Auto-generated method stub
		
	}
	
}
