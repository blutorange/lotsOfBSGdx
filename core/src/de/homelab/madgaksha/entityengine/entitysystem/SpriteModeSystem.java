package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionListComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionListComponent.SpriteDirection;
import de.homelab.madgaksha.logging.Logger;

/**
 * Sets the spriteForDirectionComponent for the current mode.
 * 
 * @author madgaksha
 * 
 */
public class SpriteModeSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpriteModeSystem.class);

	public SpriteModeSystem() {
		this(DefaultPriority.spriteModeSystem);
	}

	@SuppressWarnings("unchecked")
	public SpriteModeSystem(int priority) {
		super(Family.all(SpriteForDirectionComponent.class, SpriteForDirectionListComponent.class).exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent.get(entity);
		final SpriteForDirectionListComponent sfdlc = Mapper.spriteForDirectionListComponent.get(entity);
		SpriteDirection sd = sfdlc.mode.getSpriteDirection(sfdlc);
		if (sd == null) sd = sfdlc.dfault;
		sfdc.animationList = sd.animationList;
		sfdc.spriteDirectionStrategy = sd.spriteDirectionStrategy;
	}
}