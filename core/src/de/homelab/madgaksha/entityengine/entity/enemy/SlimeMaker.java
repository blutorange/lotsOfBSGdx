package de.homelab.madgaksha.entityengine.entity.enemy;

import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.resourcecache.ETexture;


public class SlimeMaker extends EnemyMaker {

	public SlimeMaker(Shape2D shape, ETrigger spawn) {
		super(shape, spawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeEnemy() {
		// TODO Auto-generated method stub
		add(new PositionComponent(30*32,30*32));
		add(new SpriteComponent(ETexture.ESTELLE_SWINGING));
	}

}
