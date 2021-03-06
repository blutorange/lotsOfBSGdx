package de.homelab.madgaksha.lotsofbs.layer;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.enemyTargetCrossEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.idEntityMap;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;

import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.IdComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.PlayerMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.AccelerationSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.AiSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.AngularMovementSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.AnimationModeSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.CallbackOnReenterSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.CameraTracingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.CollisionSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ConeDistributionSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ForceFieldSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.GrantDirectionSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.GrantPositionSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.GrantRotationSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.GrantScaleSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.InputDesktopSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.LifeSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ModelRenderSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.MovementSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ParticleEffectRenderSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.PostEffectSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ScaleFromDistanceSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.SpriteAnimationSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.SpriteRenderSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.StickySystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.TemporalSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.TimedCallbackSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.VelocityFieldSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ViewportUpdateSystem;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Creates and renders the main entity engine.
 *
 * @author madgaksha
 *
 */
public class EntityLayer extends ALayer {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EntityLayer.class);
	private boolean doUpdate;

	public EntityLayer() throws IOException {
		if (!createEngine())
			throw new IOException("failed to load resources");
	}

	@Override
	public void draw(final float deltaTime) {
		if (doUpdate)
			gameEntityEngine.update(deltaTime);
		else {
			viewportGame.apply();
			final SpriteRenderSystem spriteRenderSystem = gameEntityEngine.getSystem(SpriteRenderSystem.class);
			final ModelRenderSystem modelRenderSystem = gameEntityEngine.getSystem(ModelRenderSystem.class);
			final ParticleEffectRenderSystem particleEffectRenderSystem = gameEntityEngine.getSystem(ParticleEffectRenderSystem.class);
			if (spriteRenderSystem != null)
				spriteRenderSystem.update(deltaTime);
			if (modelRenderSystem != null)
				modelRenderSystem.update(deltaTime);
			if (particleEffectRenderSystem != null)
				particleEffectRenderSystem.update(deltaTime);
		}
		doUpdate = false;
	}

	@Override
	public void update(final float deltaTime) {
		doUpdate = true;
	}

	@Override
	public void removedFromStack() {
		if (gameEntityEngine != null) {
			gameEntityEngine.removeAllEntities();
			for (final EntitySystem es : gameEntityEngine.getSystems()) {
				gameEntityEngine.removeSystem(es);
			}
		}
	}

	@Override
	public void addedToStack() {
		// We need to remove entities while an entity system is being
		// updated, otherwise removal won't be queued and not all
		// entities will be processed.
		// Call all callback entities set to callback on startup.
		final List<Entity> myList = new ArrayList<Entity>(20);
		final Family family = Family.all(TriggerStartupComponent.class).get();
		for (final Entity e : gameEntityEngine.getEntitiesFor(family)) {
			myList.add(e);
		}
		for (final Entity e : myList) {
			final TriggerStartupComponent tsc = Mapper.triggerStartupComponent.get(e);
			tsc.triggerAcceptingObject.callbackTrigger(e, ETrigger.STARTUP);
			e.remove(TriggerStartupComponent.class);
		}
	}

	public boolean createEngine() {
		addSystems();
		if (!addMainEntites())
			return false;
		return true;
	}

	private void addSystems() {
		gameEntityEngine.addSystem(new AccelerationSystem());
		gameEntityEngine.addSystem(new AiSystem());
		gameEntityEngine.addSystem(new AngularMovementSystem());
		gameEntityEngine.addSystem(new BirdsViewSpriteSystem());
		gameEntityEngine.addSystem(new CameraTracingSystem());
		gameEntityEngine.addSystem(new CallbackOnReenterSystem());
		gameEntityEngine.addSystem(new CollisionSystem());
		gameEntityEngine.addSystem(new ConeDistributionSystem());
		gameEntityEngine.addSystem(new DamageSystem());
		gameEntityEngine.addSystem(new ForceFieldSystem());
		gameEntityEngine.addSystem(new GrantPositionSystem());
		gameEntityEngine.addSystem(new GrantRotationSystem());
		gameEntityEngine.addSystem(new GrantScaleSystem());
		gameEntityEngine.addSystem(new GrantDirectionSystem());
		gameEntityEngine.addSystem(new LifeSystem());
		gameEntityEngine.addSystem(new ModelRenderSystem());
		gameEntityEngine.addSystem(new MovementSystem());
		gameEntityEngine.addSystem(new ParticleEffectRenderSystem());
		gameEntityEngine.addSystem(new ScaleFromDistanceSystem());
		gameEntityEngine.addSystem(new SpriteAnimationSystem());
		gameEntityEngine.addSystem(new AnimationModeSystem());
		gameEntityEngine.addSystem(new SpriteRenderSystem());
		gameEntityEngine.addSystem(new StickySystem());
		gameEntityEngine.addSystem(new PostEffectSystem());
		gameEntityEngine.addSystem(new TimedCallbackSystem());
		gameEntityEngine.addSystem(new TemporalSystem());
		gameEntityEngine.addSystem(new VelocityFieldSystem());
		gameEntityEngine.addSystem(new ViewportUpdateSystem());

		switch (Gdx.app.getType()) {
		case Android:
			// TODO
			break;
		case Applet:
			// TODO
			break;
		case Desktop:
			gameEntityEngine.addSystem(new InputDesktopSystem());
			break;
		case HeadlessDesktop:
			// TODO
			break;
		case WebGL:
			// TODO
			break;
		case iOS:
			// TODO
			break;
		default:
			// TODO
			break;
		}
	}

	private boolean addMainEntites() {
		final Entity battleStigma = PlayerMaker.getInstance().makePlayerBattleStigma(playerEntity, player);
		final Entity targetCross = MakerUtils.makeEnemyTargetCross();
		final Entity myCamera = MakerUtils.makeCamera(level, playerEntity);
		if (battleStigma == null || myCamera == null)
			return false;

		gameEntityEngine.addEntity(myCamera);
		gameEntityEngine.addEntity(battleStigma);
		gameEntityEngine.addEntity(playerEntity);
		gameEntityEngine.addEntity(playerHitCircleEntity);
		gameEntityEngine.addEntity(targetCross);

		playerBattleStigmaEntity = battleStigma;
		enemyTargetCrossEntity = targetCross;
		cameraEntity = myCamera;

		return true;
	}

	@Override
	public boolean isBlockDraw() {
		return false;
	}

	@Override
	public boolean isBlockUpdate() {
		return false;
	}

	@Override
	public void resize(final int width, final int height) {
	}

	public static void addEntityListeners() {
		addIdListener();
	}

	private static void addIdListener() {
		// Add entity to idEntityMap when an IdComponent is added or removed.
		gameEntityEngine.addEntityListener(Family.all(IdComponent.class).get(), new EntityListener() {
			@Override
			public void entityAdded(final Entity entity) {
				final IdComponent ic = Mapper.idComponent.get(entity);
				if (ic != null)
					idEntityMap.put(ic.getId(), entity);
			}

			@Override
			public void entityRemoved(final Entity entity) {
				final String id = idEntityMap.inverse().get(entity);
				if (id != null)
					idEntityMap.remove(id);
			}
		});
	}

	public static void addMainEntitiesToMap() {
		idEntityMap.put("player", playerEntity);
		idEntityMap.put("camera", cameraEntity);
	}
}
