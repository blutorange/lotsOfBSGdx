package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LifeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.MapItemDataComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ModelComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleFromDistanceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StickyComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.lotsofbs.grantstrategy.SpeedIncreaseGrantStrategy;
import de.homelab.madgaksha.lotsofbs.level.ALevel;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.util.GeoUtil;

public class ItemMaker extends EntityMaker {

	private final static Logger LOG = Logger.getLogger(ItemMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final ItemMaker INSTANCE = new ItemMaker();
	}

	public static ItemMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private ItemMaker() {
		super();
	}

	/**
	 * 
	 * @param entity
	 *            Entity to setup.
	 * @param shape
	 *            Shape of the callback entity.
	 * @param trigger
	 *            Type of trigger for the callback.
	 * @param callback
	 *            Callback to be called. Its signature must be
	 *            <code>void myCallback(MapProperties)</code> and it must be
	 *            declared for a subclass of {@link ALevel}.
	 * @param properties
	 *            Map properties passed to the callback function.
	 * @param loop
	 *            How many times the callback should loop (be triggered).
	 * @param interval
	 *            Interval between loops in seconds.
	 * @return Whether the entity could be setup.
	 */
	public boolean setup(Entity entity, Shape2D shape, MapProperties props, IMapItem mapItem) {

		if (!mapItem.isSupportedByPlayer(player)) {
			LOG.info("player " + player + " does not support item " + mapItem);
			return false;
		}

		super.setup(entity);

		// Apply defaults.
		Vector3 axis = mapItem.getMapAxisOfRotation();
		float angularVelocity = mapItem.getMapAngularVelocity();

		// Check axis is not null vector.
		if (axis.len2() < 0.00001f) {
			axis.x = 1.0f;
			axis.y = 1.0f;
			axis.z = 1.0f;
		}

		// Normalize axis to unit length.
		axis.nor();

		// Create basic components.
		ReceiveTouchComponent rtcCollect = gameEntityEngine.createComponent(ReceiveTouchGroup01Component.class);
		ModelComponent mc = gameEntityEngine.createComponent(ModelComponent.class);
		BoundingBoxRenderComponent bbrc = gameEntityEngine.createComponent(BoundingBoxRenderComponent.class);
		BoundingBoxCollisionComponent bbccCollect = gameEntityEngine
				.createComponent(BoundingBoxCollisionComponent.class);
		PositionComponent pc = gameEntityEngine.createComponent(PositionComponent.class);
		TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);
		RotationComponent rc = gameEntityEngine.createComponent(RotationComponent.class);
		AngularVelocityComponent avc = gameEntityEngine.createComponent(AngularVelocityComponent.class);
		ComponentQueueComponent cqcCollect = gameEntityEngine.createComponent(ComponentQueueComponent.class);
		MapItemDataComponent midc = gameEntityEngine.createComponent(MapItemDataComponent.class);
		ScaleFromDistanceComponent sfdc = gameEntityEngine.createComponent(ScaleFromDistanceComponent.class);
		ShouldScaleComponent ssc = gameEntityEngine.createComponent(ShouldScaleComponent.class);
		ScaleComponent scc = gameEntityEngine.createComponent(ScaleComponent.class);

		// Setup basic components
		rtcCollect.setup(onCollect);
		mc.setup(mapItem.getModel());
		bbrc.setup(mc);
		bbccCollect.setup(bbrc);
		pc.setup(MakerUtils.makePositionAtCenter(shape));
		rc.setup(axis);
		avc.setup(angularVelocity);
		midc.setup(shape, props, mapItem, pc);

		// Enlarge bounding box for the area when the item starts to move
		// towards the player by the desired factor.
		float halfWidth = 0.5f * bbccCollect.maxX - bbccCollect.minX;
		float halfHeight = 0.5f * bbccCollect.maxY - bbccCollect.minY;
		bbccCollect.minX -= halfWidth * mapItem.getActivationAreaScaleFactor();
		bbccCollect.maxX += halfWidth * mapItem.getActivationAreaScaleFactor();
		bbccCollect.minY -= halfHeight * mapItem.getActivationAreaScaleFactor();
		bbccCollect.maxY += halfHeight * mapItem.getActivationAreaScaleFactor();

		// Make item smaller the closer it gets to the player.
		sfdc.setup(playerHitCircleEntity, 0.0f, 1.0f, 0.0f,
				mapItem.getActivationAreaScaleFactor() * Math.min(halfWidth, halfHeight));
		ssc.setup(new ExponentialGrantStrategy(0.5f));

		// Add components.
		entity.add(avc).add(bbrc).add(cqcCollect).add(bbccCollect).add(mc).add(pc).add(tc).add(rtcCollect).add(rc)
				.add(midc).add(sfdc).add(scc).add(ssc);

		// Make item move towards player once he enters the active area.
		BoundingBoxCollisionComponent bbccAcquire = gameEntityEngine
				.createComponent(BoundingBoxCollisionComponent.class);
		ComponentQueueComponent cqcAcquire = gameEntityEngine.createComponent(ComponentQueueComponent.class);
		ReceiveTouchComponent rtcAcquire = gameEntityEngine.createComponent(ReceiveTouchGroup01Component.class);
		ShouldPositionComponent spc = gameEntityEngine.createComponent(ShouldPositionComponent.class);
		StickyComponent sc = gameEntityEngine.createComponent(StickyComponent.class);

		// Setup components above
		bbccAcquire.setup(bbrc);
		rtcAcquire.setup(onAcquire);
		spc.setup(new SpeedIncreaseGrantStrategy(10.0f, 250.0f));
		sc.setup(playerHitCircleEntity);

		// Change bounding box once the item activates.
		cqcCollect.remove.add(BoundingBoxCollisionComponent.class);
		cqcCollect.remove.add(ReceiveTouchGroup01Component.class);
		cqcCollect.add.add(bbccAcquire);
		cqcCollect.add.add(rtcAcquire);

		// Make item move towards the player once it activates.
		cqcCollect.add.add(sc);
		cqcCollect.add.add(spc);

		// Switch component queue.
		cqcCollect.add.add(cqcAcquire);

		// Queue once player gets the item.
		ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectScreenComponent.class);
		LifeComponent lc = gameEntityEngine.createComponent(LifeComponent.class);
		TimedCallbackComponent tcc = gameEntityEngine.createComponent(TimedCallbackComponent.class);

		// Setup components above.
		pec.setup(EParticleEffect.ALL_MY_ITEM_ARE_BELONG_TO_ME);
		float duration = 0.0f;
		for (ParticleEmitter e : pec.particleEffect.getEmitters()) {
			float d = e.getDuration().getLowMax();
			if (d > duration)
				duration = d;
		}
		lc.setup(duration / 1000.0f, onDeath);
		tcc.setup(onGet, mapItem);

		cqcAcquire.remove.add(ReceiveTouchGroup01Component.class);
		cqcAcquire.remove.add(ScaleFromDistanceComponent.class);
		cqcAcquire.remove.add(ModelComponent.class);
		cqcAcquire.add.add(lc);
		cqcAcquire.add.add(pec);
		cqcAcquire.add.add(tcc);

		// Proceed with item-specific setup.
		mapItem.setup(entity, props, cqcAcquire);

		return true;
	}

	public void setupCirclingItem(Entity entity, IMapItem mapItem, ConeDistributionComponent cdc,
			PositionComponent currentPc) {
		// Create new components.
		final AngularVelocityComponent avc = gameEntityEngine.createComponent(AngularVelocityComponent.class);
		final ModelComponent mc = gameEntityEngine.createComponent(ModelComponent.class);
		final PositionComponent pc = gameEntityEngine.createComponent(PositionComponent.class);
		final RotationComponent rc = gameEntityEngine.createComponent(RotationComponent.class);
		final ScaleComponent sc = gameEntityEngine.createComponent(ScaleComponent.class);
		final ScaleFromDistanceComponent sfdc = gameEntityEngine.createComponent(ScaleFromDistanceComponent.class);
		final ShouldPositionComponent spc = gameEntityEngine.createComponent(ShouldPositionComponent.class);
		final ShouldScaleComponent ssc = gameEntityEngine.createComponent(ShouldScaleComponent.class);
		final TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);

		final float maxScale = player.getItemCircleParameters().getItemScaleMaxValue();
		final float maxDistance = player.getItemCircleParameters().getItemScaleMaxDistance();
		final float exponentialGrantFactor = player.getItemCircleParameters().getItemExponentialGrantFactor();

		// Setup components.
		avc.setup(mapItem.getMapAngularVelocity());
		mc.setup(mapItem.getModel());
		pc.setup(currentPc);
		rc.setup(mapItem.getMapAxisOfRotation());
		sc.setup(0.0f);
		sfdc.setup(playerHitCircleEntity, 0f, maxScale, 0f, maxDistance);
		spc.setup(new ExponentialGrantStrategy(exponentialGrantFactor));

		// Add all components.
		entity.add(avc).add(mc).add(pc).add(rc).add(sc).add(sfdc).add(spc).add(ssc).add(tc);

		// Add item to cone distribution component.
		cdc.distributionPoints.add(spc);
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return new IResource<?, ?>[] { ESound.ACTIVATE_ITEM, };
	}

	private final static IReceive onCollect;
	private final static IReceive onAcquire;
	private final static IMortal onDeath;
	private final static IEntityCallback onGet;

	static {
		onCollect = new IReceive() {
			@Override
			public void callbackTouched(Entity me, Entity you) {
				if (ComponentUtils.applyComponentQueue(me))
					SoundPlayer.getInstance().play(ESound.ACTIVATE_ITEM);
			}
		};

		onAcquire = new IReceive() {
			@Override
			public void callbackTouched(Entity me, Entity you) {
				if (ComponentUtils.applyComponentQueue(me)) {
					Mapper.scaleComponent.get(me).setup(0.0f);
				} else {
					// reset item to waiting state
					resetItemToWaitingState(me);
				}
			}
		};
		onDeath = new IMortal() {
			@Override
			public void kill(Entity e) {
				final ParticleEffectComponent pec = Mapper.particleEffectScreenComponent.get(e);
				if (pec == null || pec.particleEffect.isComplete())
					gameEntityEngine.removeEntity(e);
				else {
					final LifeComponent lc = Mapper.lifeComponent.get(e);
					lc.remainingLife = 0.2f;
				}
			}
		};
		onGet = new IEntityCallback() {
			@Override
			public void run(Entity entity, Object data) {
				((IMapItem) data).gotItem();
			}
		};
	}

	public static void resetItemToWaitingState(Entity item) {
		// TODO
		LOG.error("resetting item to waiting state, REMEMBER TO SWITCH SETUP TO ENGINE_CREATE_COMPONENT!");
		final MapItemDataComponent midc = Mapper.mapItemDataComponent.get(item);
		if (midc != null) {
			final PositionComponent pcNew = Mapper.positionComponent.get(item);
			final Entity newItem = gameEntityEngine.createEntity();
			GeoUtil.translateShape(midc.shape, pcNew.x - midc.originalPosition.x, pcNew.y - midc.originalPosition.y);
			if (ItemMaker.getInstance().setup(newItem, midc.shape, midc.props, midc.mapItem)) {
				gameEntityEngine.addEntity(newItem);
			}
		}
		gameEntityEngine.removeEntity(item);

	}

}