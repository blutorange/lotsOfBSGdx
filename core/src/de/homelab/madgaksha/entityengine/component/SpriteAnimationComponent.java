package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.EAnimation;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public class SpriteAnimationComponent implements Component, Poolable {
	public AtlasAnimation animation = null;
	public float stateTime = 0.0f;

	public SpriteAnimationComponent() {

	}

	public SpriteAnimationComponent(AtlasAnimation a) {
		animation = a;
	}

	public SpriteAnimationComponent(EAnimation ea) {
		setup(ea);
	}

	public SpriteAnimationComponent(SpriteForDirectionComponent sfdc) {
		setup(sfdc);
	}

	public void setup(EAnimation ea) {
		animation = ResourceCache.getAnimation(ea);
	}
	
	public void setup(AtlasAnimation a) {
		animation = a;
	}

	public void setup(SpriteForDirectionComponent sfdc) {
		if (sfdc.animationList.length != 0) animation = sfdc.animationList[0];
	}

	@Override
	public void reset() {
		this.animation = null;
		this.stateTime = 0.0f;
	}

}
