package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Component for sprites that are pseudo 3D, ie. with a different sprite for
 * each of the eight directions.
 * 
 * Contains the mapping
 * 
 * @author madgaksha
 *
 */
public class BirdsViewSpriteComponent implements Component {
	public Sprite sprite = null;

	public BirdsViewSpriteComponent(Sprite sprite) {
		this.sprite = sprite;
	}
}
