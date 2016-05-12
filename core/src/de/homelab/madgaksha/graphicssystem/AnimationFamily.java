package de.homelab.madgaksha.graphicssystem;

import com.badlogic.gdx.graphics.g2d.Animation;

import de.homelab.madgaksha.entityengine.entitysystem.BirdsViewSpriteSystem;

/**
 * Works similar to the {@link Animation} class, but provides an additional
 * mode parameter that can be used to retrieve a certain animation.
 * 
 * This is used by the {@link BirdsViewSpriteSystem} which uses a different
 * set of animation frames for each direction the sprite is looking at.
 * 
 * @author madgaksha
 *
 */
public class AnimationFamily extends Animation {
	private int mode = 0; 
	public void setMode(int mode) {
		
	}
}
