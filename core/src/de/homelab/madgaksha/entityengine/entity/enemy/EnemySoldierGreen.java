package de.homelab.madgaksha.entityengine.entity.enemy;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.entity.EntityEnemy;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;


public class EnemySoldierGreen extends EntityEnemy {
	private final static Logger LOG = Logger.getLogger(EnemySoldierGreen.class);

	@SuppressWarnings("unchecked")
	@Override
	public IResource<? extends Enum<?>,?>[] requestedResources() {
		return new IResource[]{
			EAnimationList.SOLDIER_GREEN_0,
			ESound.HORA_KOCCHI_DA_ZE,
		};
	}
	
	public EnemySoldierGreen(Shape2D shape, ETrigger spawn, Vector2 initialPos, Float initDir) {
		super(shape, spawn,initialPos,initDir);
	
		SpriteForDirectionComponent sfdc = new SpriteForDirectionComponent(EAnimationList.SOLDIER_GREEN_0,
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac);
		
		add(sc);
		add(sac);
		add(sfdc);
		add(new RotationComponent(true));
	}

	@Override
	public void reinitializeEntity() {
		super.reinitializeEntity();
	}

	@Override
	protected void triggeredStartup() {
		triggered();
	}

	@Override
	protected void triggeredTouch(Entity e) {
		triggered();
	}

	@Override
	protected void triggeredScreen() {
		triggered();
	}

	@Override
	protected void triggeredTouched(Entity e) {
		LOG.debug("aasdasd");
		triggered();
	}

	@Override
	protected Rectangle requestedBoundingBox() {
		return new Rectangle(-32.0f,-32.0f,64.0f, 64.0f);
	}
	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f,0.0f,32.0f);
	}
	
	private void triggered() {
		GlobalBag.soundPlayer.play(ESound.HORA_KOCCHI_DA_ZE); 
	}

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.SOLDIER_GREEN_0_MAIN;
	}

	@Override
	protected ETexture requestedIconSubHorizontal() {
		return ETexture.SOLDIER_GREEN_0_SUBV;
	}

	@Override
	protected ETexture requestedIconSubVertical() {
		return ETexture.SOLDIER_GREEN_0_SUBV;
	}

	@Override
	protected int requestedMaxPainPoints() {
		return 10000000;
	}
}
