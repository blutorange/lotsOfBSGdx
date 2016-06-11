package de.homelab.madgaksha.entityengine.entity;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;


/**
 * Maker for most enemies with normal and common properties sprites, direction, movement etc.
 * @author madgaksha
 *
 */
public abstract class NormalEnemyMaker extends EnemyMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(NormalEnemyMaker.class);

	protected NormalEnemyMaker() {
		super();
	}
	
	
	@Override
	public void setup(Entity e, Shape2D shape, ETrigger spawn, Vector2 initialPos, Float initDir) {
		super.setup(e, shape, spawn,initialPos,initDir);
		
		BehaviourComponent bc = new BehaviourComponent(this);
		RotationComponent rc = new RotationComponent(true);
		//TemporalComponent tc = new TemporalComponent();
		SpriteForDirectionComponent sfdc = new SpriteForDirectionComponent(requestedAnimationList(),
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac);

		e.add(bc);
		//e.add(tc);
		e.add(sc);
		e.add(sac);
		e.add(sfdc);
		e.add(rc);
	}
		
	protected abstract EAnimationList requestedAnimationList();
}