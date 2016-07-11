package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public class SpriteAnimationComponent implements Component, Poolable {
	private final static float DEFAULT_START_TIME = 0.0f;

	public AtlasAnimation animation = null;
	/** Entity's local time when the animation started. */
	public float startTime = DEFAULT_START_TIME;

	public SpriteAnimationComponent() {

	}

	public SpriteAnimationComponent(AtlasAnimation a) {
		animation = a;
	}

	public SpriteAnimationComponent(EAnimation ea) {
		setup(ea);
	}

	public SpriteAnimationComponent(AnimationForDirectionComponent sfdc) {
		setup(sfdc);
	}

	public void setup(EAnimation ea) {
		animation = ResourceCache.getAnimation(ea);
	}

	public void setup(AtlasAnimation a) {
		animation = a;
	}

	public void setup(AnimationForDirectionComponent sfdc) {
		if (sfdc.animationList.length != 0)
			animation = sfdc.animationList[0];
	}

	@Override
	public void reset() {
		animation = null;
		startTime = DEFAULT_START_TIME;
	}

}
