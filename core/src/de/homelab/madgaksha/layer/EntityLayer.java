package de.homelab.madgaksha.layer;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerEntity;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InputComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.entityengine.entitysystem.CameraTracingSystem;
import de.homelab.madgaksha.entityengine.entitysystem.DanmakuSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantPositionSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantRotationSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantScaleSystem;
import de.homelab.madgaksha.entityengine.entitysystem.InputVelocitySystem;
import de.homelab.madgaksha.entityengine.entitysystem.MovementSystem;
import de.homelab.madgaksha.entityengine.entitysystem.NewtonianForceSystem;
import de.homelab.madgaksha.entityengine.entitysystem.PostEffectSystem;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteAnimationSystem;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteRenderSystem;
import de.homelab.madgaksha.entityengine.entitysystem.TemporalSystem;
import de.homelab.madgaksha.entityengine.entitysystem.ViewportUpdateSystem;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.enums.Gravity;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.resourcecache.EAnimationList;

/**
 * Creates and renders the main entity engine.
 * 
 * @author madgaksha
 *
 */
public class EntityLayer extends ALayer {

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
		// TODO Auto-generated method stub
		
	}
	
	public void createEngine() {
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
		gameEntityEngine.addSystem(new DanmakuSystem());
		gameEntityEngine.addSystem(new InputVelocitySystem());
		gameEntityEngine.addSystem(new PostEffectSystem());
		gameEntityEngine.addSystem(new TemporalSystem());

		playerEntity = createPlayerEntity();
		
		Entity yourEntity = new Entity();
		SpriteForDirectionComponent sfdc2 = new SpriteForDirectionComponent(EAnimationList.JOSHUA_RUNNING,
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac2 = new SpriteAnimationComponent(sfdc2);
		yourEntity.add(new SpriteComponent(sac2));
		yourEntity.add(new PositionComponent(level.getMapWidthW()/2.0f, 50.0f*32.0f));
		yourEntity.add(sfdc2);
		yourEntity.add(sac2);
		yourEntity.add(new RotationComponent(true));
		yourEntity.add(new BoundingSphereComponent(70.0f));
		yourEntity.add(new DirectionComponent(90.0f));
		yourEntity.add(new TemporalComponent());

		Entity myCamera = new Entity();
		ManyTrackingComponent mtc = new ManyTrackingComponent(level.getMapXW(), level.getMapYW(), level.getMapWidthW(),
				level.getMapHeightW());
		mtc.focusPoints.add(playerEntity);
		mtc.focusPoints.add(yourEntity);
		mtc.playerPoint = playerEntity;
		mtc.bossPoint = yourEntity;
		mtc.gravity = Gravity.SOUTH;
		mtc.trackingOrientationStrategy = TrackingOrientationStrategy.RELATIVE;
		myCamera.add(mtc);
		myCamera.add(new PositionComponent(1920/4,1080/2));
		myCamera.add(new RotationComponent());
		
		myCamera.add(new ShouldPositionComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ViewportComponent(viewportGame));
		myCamera.add(new TemporalComponent());

		
//		Entity myProjectile = new Entity();
//		myProjectile.add(new PositionComponent(1920.0f/4.0f,500.0f));
//		myProjectile.add(new VelocityComponent(60.0f,0.0f));
//		myProjectile.add(new TrajectoryComponent());
//		myProjectile.add(new ForceComponent());
//		myProjectile.add(new SpriteComponent(ETexture.TEST_PROJECTILE));
		
		gameEntityEngine.addEntity(playerEntity);
		gameEntityEngine.addEntity(yourEntity);
		gameEntityEngine.addEntity(myCamera);
//		gameEntityEngine.addEntity(myProjectile);

	}

	private Entity createPlayerEntity() {
		playerEntity = new Entity();

		SpriteForDirectionComponent sfdc = new SpriteForDirectionComponent(player.getAnimationList(),
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac);
		
		playerEntity.add(new LeanEffectComponent(30.0f,0.10f,10.0f));
		playerEntity.add(new HoverEffectComponent(8.0f, 1.0f));
		
		playerEntity.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.1f)));
		playerEntity.add(new ShouldScaleComponent(new ExponentialGrantStrategy(0.1f)));
		
		playerEntity.add(sc);
		playerEntity.add(sac);
		playerEntity.add(sfdc);

		playerEntity.add(new BoundingSphereComponent(player.getBoundingCircle()));
		playerEntity.add(new PositionComponent(level.getPlayerInitialPosition(), true));
		playerEntity.add(new VelocityComponent(0.0f, 0.0f));
		playerEntity.add(new RotationComponent(true));
		playerEntity.add(new ScaleComponent());
		playerEntity.add(new DirectionComponent());

		playerEntity.add(new InputComponent(player.getMovementSpeed()));
		playerEntity.add(new TemporalComponent());
		return playerEntity;
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
