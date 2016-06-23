package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.IBehaving;

/**
 * Contains the callback {@link Entity} implementing {@link IBehaving}.
 * 
 * @author madgaksha
 *
 */
public class ColorComponent implements Component, Poolable {

	private final static Color DEFAULT_COLOR = new Color(Color.WHITE);

	public Color color = DEFAULT_COLOR;

	public ColorComponent() {
	}

	public ColorComponent(Color color) {
		setup(color);
	}

	public void setup(Color color) {
		this.color = color;
	}

	@Override
	public void reset() {
		color = DEFAULT_COLOR;
	}
}
