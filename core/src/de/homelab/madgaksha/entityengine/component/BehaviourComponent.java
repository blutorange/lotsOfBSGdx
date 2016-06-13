package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.IBehaving;

/**
 * Contains the callback {@link Entity} implementing {@link IBehaving}. 
 * @author madgaksha
 *
 */
public class BehaviourComponent implements Component, Poolable {

	private final static IBehaving DEFAULT_BRAIN = new IBehaving() {		
		@Override
		public boolean behave(Entity e) {
			return false;
		}
	}; 
	
	public IBehaving brain = DEFAULT_BRAIN;
	public IBehaving cortex = DEFAULT_BRAIN;
	
	public BehaviourComponent(){
	}
	
	public BehaviourComponent(IBehaving brain) {
		setup(brain);
	}
	
	public void setup(IBehaving brain) {
		this.brain = brain;
	}
	
	@Override
	public void reset() {
		brain = DEFAULT_BRAIN;
		cortex = DEFAULT_BRAIN;
	}
}
