package de.homelab.madgaksha.entityengine.entity.enemy;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;


public class SlimeMaker extends EnemyMaker {
	private final static Logger LOG = Logger.getLogger(SlimeMaker.class);

	public SlimeMaker(Shape2D shape, ETrigger spawn) {
		super(shape, spawn);
		add(new SpriteComponent(ETexture.ESTELLE_SWINGING));
		
	}

	@Override
	public void reinitializeEntity() {
		super.reinitializeEntity();
	}

	@Override
	protected void triggeredStartup() {
		LOG.debug("Slime " + hashCode() + " triggered on startup!!");
	}

	@Override
	protected void triggeredTouch(Entity e) {
		LOG.debug("Slime " + hashCode() + " triggered on touch!!");		
	}

	@Override
	protected void triggeredScreen() {
		LOG.debug("Slime " + hashCode() + " triggered on screen!!");
	}

	@Override
	protected void triggeredTouched(Entity e) {
		LOG.debug("Slime " + hashCode() + " triggered on touched!!");
	}

	@Override
	protected Rectangle requestedBoundingBox() {
		return new Rectangle(-32.0f,-32.0f,64.0f, 64.0f);
	}
	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f,0.0f,32.0f);
	}
}
