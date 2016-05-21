package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.level;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.logging.Logger;
public class CallbackMaker extends AEntityMaker implements ITrigger, IReceive {

	private final static Logger LOG = Logger.getLogger(AEntityMaker.class);
	private Method callback;
	private MapProperties properties;
	
	public CallbackMaker(Shape2D shape, ETrigger trigger, Method callback, MapProperties properties, int loop, float interval) {
		super();
		this.callback = callback;
		this.properties = properties;
		Component triggerComponent = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		add(triggerComponent);
	}

	@Override
	public void reinitializeEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void callbackStartup() {
		try {
			callback.invoke(level, properties);
		} catch (ReflectionException e) {
			LOG.error("could not invoke callback: " + this, e);
		}
	}

	@Override
	public void callbackTouch(Entity e) {
		try {
			callback.invoke(level, properties);
		} catch (ReflectionException ex) {
			LOG.error("could not invoke callback: " + this, ex);
		}
	}

	@Override
	public void callbackScreen() {
		try {
			callback.invoke(level, properties);
		} catch (ReflectionException e) {
			LOG.error("could not invoke callback: " + this, e);
		}
	}

	@Override
	public void callbackTouched(Entity e) {
		try {
			callback.invoke(level, properties);
		} catch (ReflectionException ex) {
			LOG.error("could not invoke callback: " + this, ex);
		}
	}

}