package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.CallbackComponent;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
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
	public void setup(Entity entity, Shape2D shape, ETrigger trigger, Method callback, MapProperties properties, int loops, float duration) {
		super.setup(entity);
		final CallbackComponent cc = new CallbackComponent(callback, properties);
		final PositionComponent pc = new PositionComponent(MakerUtils.makePositionAtCenter(shape));
		final TemporalComponent tec = new TemporalComponent();
		
		final Component tc = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);

		final ABoundingBoxComponent bbcCollision = new BoundingBoxCollisionComponent(
				MakerUtils.makeBoundingBoxCollision(shape, pc));
		final ComponentQueueComponent cqc = new ComponentQueueComponent();

		if (loops < 0) {
			cqc.remove.add(InactiveComponent.class);
			entity.add(new InactiveComponent());
			entity.add(new TimedCallbackComponent(LOOP_CALLBACK_ONCE, 0, -1));
		}
		else {
			final TimedCallbackComponent tcc = new TimedCallbackComponent(LOOP_CALLBACK, duration, loops+1);
			// First activation should occur immediately.
			tcc.totalTime = duration + duration;
			cqc.add.add(tcc);
			cqc.remove.add(tc.getClass());
		}

		entity.add(bbcCollision);
		entity.add(cc);
		entity.add(cqc);
		entity.add(pc);
		entity.add(tc);
		entity.add(tec);
	}

	@Override
	public void callbackTrigger(Entity me, ETrigger trigger) {
		ComponentUtils.applyComponentQueue(me);
	}

	@Override
	public void callbackTouched(Entity me, Entity you) {
		ComponentUtils.applyComponentQueue(me);
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
	
	private final static ITimedCallback LOOP_CALLBACK = new ITimedCallback() {
		@Override
		public void run(Entity entity, Object data) {
			try {
				final CallbackComponent cc = Mapper.callbackComponent.get(entity);
				if (cc != null) cc.callback.invoke(level, cc.properties);
			} catch (ReflectionException ex) {
				LOG.error("could not invoke callback: " + this, ex);
			}			
		}
	};	
	
	public final static ITimedCallback LOOP_CALLBACK_ONCE = new ITimedCallback() {
		@Override
		public void run(Entity entity, Object data) {
			LOOP_CALLBACK.run(entity, data);
			entity.add(gameEntityEngine.createComponent(InactiveComponent.class));
		}
	};
}