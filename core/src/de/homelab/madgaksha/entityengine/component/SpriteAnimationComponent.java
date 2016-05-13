package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.EAnimation;

public class SpriteAnimationComponent implements Component, Poolable {
	public Animation animation = null;
	public float stateTime = 0.0f;
	
	public SpriteAnimationComponent(Animation a) {
		animation = a;
	}
	
	public SpriteAnimationComponent(EAnimation ea) {
		animation = ResourceCache.getAnimation(ea);
	}
	
	public SpriteAnimationComponent(SpriteForDirectionComponent sfdc) {
		animation = sfdc.animationList[0];
	}
	
	@Override
	public void reset() {
		this.animation = null;
		this.stateTime = 0.0f;
	}
	
}
