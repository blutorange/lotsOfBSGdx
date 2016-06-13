package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.LifeComponent;
import de.homelab.madgaksha.entityengine.component.ModelComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ScaleFromDistanceComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.grantstrategy.ImmediateGrantStrategy;
import de.homelab.madgaksha.grantstrategy.SpeedIncreaseGrantStrategy;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.IMapItem;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcepool.EParticleEffect;

public class ItemMaker extends EntityMaker {

	private final static Logger LOG = Logger.getLogger(EntityMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final ItemMaker INSTANCE = new ItemMaker();
	}
	public static ItemMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}	
	private ItemMaker () {
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
	 * @return Whether the entity could be setup.
	 */
	public boolean setup(Entity entity, Shape2D shape, MapProperties props, IMapItem mapItem, Float angularVelocity, Vector3 axis) {
		
		if (!mapItem.isSupportedByPlayer(player)) {
			LOG.info("player " + player + " does not support weapon " + mapItem);
			return false;
		}
		
		super.setup(entity);
		
		// Apply defaults.
		if (axis == null) axis = mapItem.getDefaultAxisOfRotation();
		if (angularVelocity == null) angularVelocity = mapItem.getDefaultAngularVelocity();
		
		// Check axis is not null vector.
		if (axis.len2() < 0.00001f) {
			axis.x = 1.0f;
			axis.y = 1.0f;
			axis.z = 1.0f;
		}
		
		// Normalize axis to unit length.
		axis.nor();
		
		// Create basic components.
		ReceiveTouchComponent rtcCollect = new ReceiveTouchGroup01Component(onCollect);
		ModelComponent mc = new ModelComponent(mapItem.getModel());
		BoundingBoxRenderComponent bbrc = new BoundingBoxRenderComponent(mc);
		BoundingBoxCollisionComponent bbccCollect = new BoundingBoxCollisionComponent(bbrc);		
		PositionComponent pc = new PositionComponent(MakerUtils.makePositionAtCenter(shape));
		TemporalComponent tc = new TemporalComponent();
		RotationComponent rc = new RotationComponent(axis);
		AngularVelocityComponent avc = new AngularVelocityComponent(angularVelocity);
		ComponentQueueComponent cqcCollect = new ComponentQueueComponent();
		
		// Add components.
		entity.add(avc)
			.add(bbrc)
			.add(cqcCollect)
			.add(bbccCollect)
			.add(mc)
			.add(pc)
			.add(tc)
			.add(rtcCollect)
			.add(rc);
		

		// Enlarge bounding box for the area when the item starts to move towards the player
		// by the desired factor.
		float halfWidth = 0.5f * bbccCollect.maxX-bbccCollect.minX;
		float halfHeight = 0.5f * bbccCollect.maxY-bbccCollect.minY;
		bbccCollect.minX -= halfWidth * mapItem.getActivationAreaScaleFactor();
		bbccCollect.maxX += halfWidth * mapItem.getActivationAreaScaleFactor();
		bbccCollect.minY -= halfHeight * mapItem.getActivationAreaScaleFactor();
		bbccCollect.maxY += halfHeight * mapItem.getActivationAreaScaleFactor();
		
		//
		// Queue once player enters the active area.
		//
		BoundingBoxCollisionComponent bbccAcquire = new BoundingBoxCollisionComponent(bbrc);
		ComponentQueueComponent cqcAcquire = new ComponentQueueComponent();
		ReceiveTouchComponent rtcAcquire = new ReceiveTouchGroup01Component(onAcquire); 
		ScaleComponent scc = new ScaleComponent();
		ScaleFromDistanceComponent sfdc = new ScaleFromDistanceComponent(playerHitCircleEntity, 0.0f, 1.0f, 0.0f,
				0.9f * mapItem.getActivationAreaScaleFactor()
						* (float) Math.sqrt(halfWidth * halfWidth + halfHeight * halfHeight));
		ShouldPositionComponent spc = new ShouldPositionComponent(new SpeedIncreaseGrantStrategy(10.0f,250.0f));
		ShouldScaleComponent ssc = new ShouldScaleComponent(new ImmediateGrantStrategy());
		StickyComponent sc = new StickyComponent(playerHitCircleEntity);

		// Change bounding box once the item activates.
		cqcCollect.remove.add(BoundingBoxCollisionComponent.class);
		cqcCollect.remove.add(ReceiveTouchGroup01Component.class);
		cqcCollect.add.add(bbccAcquire);
		cqcCollect.add.add(rtcAcquire);
		
		// Make item move towards the player once it activates.
		cqcCollect.add.add(sc);
		cqcCollect.add.add(spc);
			
		// Make item smaller the closer it gets to the player.
		cqcCollect.add.add(sfdc);
		cqcCollect.add.add(scc);
		cqcCollect.add.add(ssc);
		
		// Switch component queue.
		cqcCollect.add.add(cqcAcquire);
	
		
		//
		// Queue once player gets the item.
		//
		ParticleEffectComponent pec = new ParticleEffectScreenComponent(EParticleEffect.ALL_MY_ITEM_ARE_BELONG_TO_ME);
		float duration = 0.0f;
		for (ParticleEmitter e : pec.particleEffect.getEmitters()) {
			float d = e.getDuration().getLowMax();
			if (d > duration) duration = d;
		}
		LifeComponent lc = new LifeComponent(duration/1000.0f, onDeath);
		TimedCallbackComponent tcc = new TimedCallbackComponent(onGet, mapItem);
		cqcAcquire.remove.add(ReceiveTouchGroup01Component.class);
		cqcAcquire.remove.add(ScaleFromDistanceComponent.class);
		cqcAcquire.add.add(lc);
		cqcAcquire.add.add(pec);
		cqcAcquire.add.add(tcc);
		
		// Proceed with item-specific setup.
		mapItem.setup(entity, props);
		
		return true;
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return new IResource<?,?>[]{
			ESound.ACTIVATE_ITEM,
		};
	}
	
	private final static IReceive onCollect;
	private final static IReceive onAcquire;
	private final static IMortal onDeath;
	private final static ITimedCallback onGet;
	
	static {
		onCollect = new IReceive() {
			@Override
			public void callbackTouched(Entity me, Entity you) {
				ComponentUtils.applyComponentQueue(me);
				SoundPlayer.getInstance().play(ESound.ACTIVATE_ITEM);
			}
		};
		
		onAcquire = new IReceive() {
			@Override
			public void callbackTouched(Entity me, Entity you) {
				ComponentUtils.applyComponentQueue(me);
				Mapper.scaleComponent.get(me).setup(0.0f);
				// Acquire the item.
			}
		};
		onDeath = new IMortal() {
			@Override
			public void kill(Entity e) {
				final ParticleEffectComponent pec = Mapper.particleEffectScreenComponent.get(e);
				if (pec == null || pec.particleEffect.isComplete()) gameEntityEngine.removeEntity(e);
				else {
					final LifeComponent lc = Mapper.lifeComponent.get(e);
					lc.remainingLife = 0.2f;
				}
			}
		};
		onGet = new ITimedCallback() {
			@Override
			public void run(Entity entity, Object data) {
				((IMapItem)data).gotItem();
			}
		};
	}
	
}