package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.IMortal;

/**
 * Contains the callback {@link Entity} implementing {@link IMortal}.
 * 
 * @author madgaksha
 *
 */
public class DeathComponent implements Component, Poolable {

	private final static IMortal DEFAULT_KILL = new IMortal() {
		@Override
		public void kill(Entity e) {
		}
	};
	private final static boolean DEFAULT_DEAD = false;

	public IMortal reaper = DEFAULT_KILL;
	public boolean dead = DEFAULT_DEAD;

	public DeathComponent() {
	}

	public DeathComponent(IMortal kill) {
		setup(kill);
	}

	public void setup(IMortal reaper) {
		this.reaper = reaper;
	}

	@Override
	public void reset() {
		reaper = DEFAULT_KILL;
		dead = DEFAULT_DEAD;
	}
}
