package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

import de.homelab.madgaksha.graphicssystem.AnimationFamily;

public class SpriteAnimationComponent implements Component {
	public AnimationFamily animationFamily = null;

	public SpriteAnimationComponent(AnimationFamily animationFamily) {
		this.animationFamily = animationFamily;
	}
	
	public SpriteAnimationComponent(Animation animation) {
		//TODO
		this(family);
	}
	
}
