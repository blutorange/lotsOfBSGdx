package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.logging.Logger;;

public class CallbackMaker extends AEntityMaker implements ITrigger, IReceive {

	private final static Logger LOG = Logger.getLogger(AEntityMaker.class);

	public CallbackMaker(Shape2D shape, ETrigger trigger, Method method, int loop, float interval) {
		super();
		Component triggerComponent = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		add(triggerComponent);
	}

	@Override
	public void reinitializeEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void callbackStartup() {
		LOG.debug("Callback event triggered on startup!!");
	}

	@Override
	public void callbackTouch(Entity e) {
		LOG.debug("Callback event triggered on touch!!");
	}

	@Override
	public void callbackScreen() {
		LOG.debug("Callback event triggered on screen!!");
	}

	@Override
	public void callbackTouched(Entity e) {
		LOG.debug("Callback event triggered on touched!!");		
	}

}