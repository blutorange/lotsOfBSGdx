package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.level;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.CallbackComponent;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.IResource;

public class CallbackMaker extends EntityMaker implements ITrigger, IReceive {
	private final static Logger LOG = Logger.getLogger(EntityMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final CallbackMaker INSTANCE = new CallbackMaker();
	}
	public static CallbackMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}	
	private CallbackMaker () {
		super();
	}
	
	/**
	 * 
	 * @param entity Entity to setup.
	 * @param shape Shape of the callback entity.
	 * @param trigger Type of trigger for the callback.
	 * @param callback Callback to be called. Its signature must be <code>void myCallback(MapProperties)</code> and it must be declared for a subclass of {@link ALevel}.
	 * @param properties Map properties passed to the callback function.
	 * @param loop How many times the callback should loop (be triggered).
	 * @param interval Interval between loops in seconds.
	 */
	public void setup(Entity entity, Shape2D shape, ETrigger trigger, Method callback, MapProperties properties, int loop, float interval) {
		super.setup(entity);
		final CallbackComponent cc = new CallbackComponent(callback, properties);
		final Component tc = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		entity.add(cc);
		entity.add(tc);
	}

	@Override
	public void callbackTrigger(Entity e, ETrigger t) {
		try {
			final CallbackComponent cc = Mapper.callbackComponent.get(e);
			if (cc != null) cc.callback.invoke(level, cc.properties);
		} catch (ReflectionException ex) {
			LOG.error("could not invoke callback: " + this, ex);
		}
	}

	@Override
	public void callbackTouched(Entity me, Entity you) {
		try {
			final CallbackComponent cc = Mapper.callbackComponent.get(me);
			if (cc != null) cc.callback.invoke(level, cc.properties);
		} catch (ReflectionException ex) {
			LOG.error("could not invoke callback: " + this, ex);
		}
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
	
}