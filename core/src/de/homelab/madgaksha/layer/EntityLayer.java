package de.homelab.madgaksha.layer;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerEntity;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.entityengine.entitysystem.AngularMovementSystem;
import de.homelab.madgaksha.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.entityengine.entitysystem.CameraTracingSystem;
import de.homelab.madgaksha.entityengine.entitysystem.CollisionSystem;
import de.homelab.madgaksha.entityengine.entitysystem.DanmakuSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantPositionSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantRotationSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantScaleSystem;
import de.homelab.madgaksha.entityengine.entitysystem.InputPlayerDesktopSystem;
import de.homelab.madgaksha.entityengine.entitysystem.MovementSystem;
import de.homelab.madgaksha.entityengine.entitysystem.NewtonianForceSystem;
import de.homelab.madgaksha.entityengine.entitysystem.ParticleEffectRenderSystem;
import de.homelab.madgaksha.entityengine.entitysystem.PostEffectSystem;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteAnimationSystem;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteRenderSystem;
import de.homelab.madgaksha.entityengine.entitysystem.TemporalSystem;
import de.homelab.madgaksha.entityengine.entitysystem.ViewportUpdateSystem;
import de.homelab.madgaksha.logging.Logger;

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
	
	public EntityLayer() {
		createEngine();
	}
	
	@Override
	public void draw(float deltaTime) {
		if (doUpdate) gameEntityEngine.update(deltaTime);
		else {
			final SpriteRenderSystem spriteRenderSystem = gameEntityEngine.getSystem(SpriteRenderSystem.class);
			if (spriteRenderSystem != null) {
				viewportGame.apply();
				spriteRenderSystem.update(deltaTime);
			}
			//gameEntityEngine.getSystem(Draw3dSystem.class).update(deltaTime);
		}
		doUpdate = false;
	}

	@Override
	public void update(float deltaTime) {
		doUpdate = true;

	}
	
	@Override
	public void removedFromStack() {
		if (gameEntityEngine != null) {
			gameEntityEngine.removeAllEntities();
			for (EntitySystem es : gameEntityEngine.getSystems()) {
				gameEntityEngine.removeSystem(es);
			}
		}		
	}

	@Override
	public void addedToStack() {
		// We need to remove entities while an entity system is being
		// updated, otherwise removal won't be queued and not all
		// entities will be processed.
		final List<Entity> myList = new ArrayList<Entity>(20);
		@SuppressWarnings("unchecked")
		final Family family = Family.all(TriggerStartupComponent.class).get();
		for (Entity e : gameEntityEngine.getEntitiesFor(family)) {
			myList.add(e);
		}
		for (Entity e : myList) {
			TriggerStartupComponent tsc = Mapper.triggerStartupComponent.get(e);
			tsc.triggerAcceptingObject.callbackStartup();
			e.remove(TriggerStartupComponent.class);
		}
	}
	
	public void createEngine() {
		gameEntityEngine.addSystem(new ParticleEffectRenderSystem());
		gameEntityEngine.addSystem(new BirdsViewSpriteSystem());
		gameEntityEngine.addSystem(new SpriteAnimationSystem());
		gameEntityEngine.addSystem(new SpriteRenderSystem());
		gameEntityEngine.addSystem(new CameraTracingSystem());
		gameEntityEngine.addSystem(new ViewportUpdateSystem());
		gameEntityEngine.addSystem(new GrantPositionSystem());
		gameEntityEngine.addSystem(new GrantRotationSystem());
		gameEntityEngine.addSystem(new GrantScaleSystem());
		gameEntityEngine.addSystem(new NewtonianForceSystem());
		gameEntityEngine.addSystem(new MovementSystem());
		gameEntityEngine.addSystem(new AngularMovementSystem());
		gameEntityEngine.addSystem(new DanmakuSystem());
		gameEntityEngine.addSystem(new PostEffectSystem());
		gameEntityEngine.addSystem(new TemporalSystem());
		gameEntityEngine.addSystem(new CollisionSystem());

		switch (Gdx.app.getType()) {
		case Android:
			//TODO
			break;
		case Applet:
			//TODO
			break;
		case Desktop:
			gameEntityEngine.addSystem(new InputPlayerDesktopSystem());
			break;
		case HeadlessDesktop:
			//TODO
			break;
		case WebGL:
			//TODO
			break;
		case iOS:
			//TODO
			break;
		default:
			//TODO
			break;
		}
		
		playerEntity = MakerUtils.makePlayer(player);
		Entity myCamera = MakerUtils.makeCamera(level);
		
		gameEntityEngine.addEntity(playerEntity);
		gameEntityEngine.addEntity(myCamera);
		
//		Entity myProjectile = new Entity();
//		myProjectile.add(new PositionComponent(1920.0f/4.0f,500.0f));
//		myProjectile.add(new VelocityComponent(60.0f,0.0f));
//		myProjectile.add(new TrajectoryComponent());
//		myProjectile.add(new ForceComponent());
//		myProjectile.add(new SpriteComponent(ETexture.TEST_PROJECTILE));
//		gameEntityEngine.addEntity(myProjectile);
	}

	@Override
	public boolean isBlockDraw() {
		return false;
	}

	@Override
	public boolean isBlockUpdate() {
		return false;
	}
}
