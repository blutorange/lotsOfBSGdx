package de.homelab.madgaksha.entityengine.entity.enemy;

import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.resourcecache.ETexture;


public class SlimeMaker extends EnemyMaker {

	public SlimeMaker(Shape2D shape, ETrigger spawn) {
		super(shape, spawn);
		// TODO Auto-generated method stub
		add(new SpriteComponent(ETexture.ESTELLE_SWINGING));
		
	}

	@Override
	public void reinitializeEntity() {
		super.reinitializeEntity();
	}

}
