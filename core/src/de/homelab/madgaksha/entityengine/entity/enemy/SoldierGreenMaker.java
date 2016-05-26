package de.homelab.madgaksha.entityengine.entity.enemy;


import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.entityengine.entity.NormalEnemyMaker;
import de.homelab.madgaksha.entityengine.entity.trajectory.LinearMotionTrajectory;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;


public class SoldierGreenMaker extends NormalEnemyMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SoldierGreenMaker.class);

	private final LinearMotionTrajectory linearMotionTrajectory;
	
	// Singleton
	private static class SingletonHolder {
		private static final SoldierGreenMaker INSTANCE = new SoldierGreenMaker();
	}
	public static SoldierGreenMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}
	private SoldierGreenMaker() {
		super();
		linearMotionTrajectory = new LinearMotionTrajectory();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IResource<? extends Enum<?>,?>[] requestedResources() {
		return new IResource[]{
			EAnimationList.SOLDIER_GREEN_0,
			ESound.HORA_KOCCHI_DA_ZE,
		};
	}
	
	@Override
	public void setup(Entity e, Shape2D shape, ETrigger spawn, Vector2 initialPos, Float initDir) {
		super.setup(e, shape, spawn,initialPos,initDir);
	}

	@Override
	protected Rectangle requestedBoundingBox() {
		return new Rectangle(-32.0f,-32.0f,64.0f, 64.0f);
	}
	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f,0.0f,32.0f);
	}
	
	@Override
	protected void spawned(Entity e, ETrigger t) {
		GlobalBag.soundPlayer.play(ESound.HORA_KOCCHI_DA_ZE); 
	}

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.SOLDIER_GREEN_0_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.SOLDIER_GREEN_0_SUB;
	}


	@Override
	protected int requestedMaxPainPoints() {
		return 10000000;
	}
	
	//TODO remove me
	Vector2 v = new Vector2(350.0f,0.0f);
	public void behave(Entity e) {
		final TemporalComponent tc = Mapper.temporalComponent.get(e);
//		timePassed += tc.deltaTime;
//		if (timePassed > 0.2f) {
//			timePassed = 0.0f;
for (int i=0; i!=3; ++i){		
			final PositionComponent pc = Mapper.positionComponent.get(e);
			linearMotionTrajectory.position(pc.x, pc.y);
			v.rotate(MathUtils.random(0.0f,360.0f));
			linearMotionTrajectory.velocity(v.x,v.y);
			Entity bullet =	BulletMaker.makeEntity(BulletShapeMaker.FLOWER_RED, linearMotionTrajectory);
			gameEntityEngine.addEntity(bullet);
}
//		}
	}
	@Override
	protected EAnimationList requestedAnimationList() {
		return EAnimationList.SOLDIER_GREEN_0;
	}
}
