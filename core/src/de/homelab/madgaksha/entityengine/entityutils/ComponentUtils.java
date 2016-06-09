package de.homelab.madgaksha.entityengine.entityutils;

import static de.homelab.madgaksha.GlobalBag.cameraEntity;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.QuakeEffectComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.enums.RichterScale;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;

/**
 * Utilities for working with an entity's pain points. Usually processed by an appropriate entity system,
 * this should be used sparingly.
 * @author madgaksha
 *
 */
public class ComponentUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ComponentUtils.class);
	
	public static void applyComponentQueue(Entity e, ComponentQueueComponent cqc) {
		for (Class<? extends Component> c : cqc.remove) e.remove(c);
		for (Component c : cqc.add) e.add(c);
		cqc.add.clear();
		cqc.remove.clear();
	}
	
	/** Applied the component queue of the entity, if it exists.*/
	public static void applyComponentQueue(Entity e) {
		final ComponentQueueComponent cqc = Mapper.componentQueueComponent.get(e);
		if (cqc != null) applyComponentQueue(e, cqc);
	}

	/**
	 * Switches the animation list to the given animation list.
	 * @param entity Entity whose sprite animation list needs to be changed.
	 * @param animationList The new animation list.
	 */
	public static void switchAnimationList(Entity entity, EAnimationList animationList) {
		SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent.get(entity);
		SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		SpriteComponent sc = Mapper.spriteComponent.get(entity);
		if (sfdc != null && sac != null && sc != null) {
			sfdc.setup(animationList, ESpriteDirectionStrategy.ZENITH);
			sac.setup(sfdc);
			sc.setup(sac);
		}
	}
	
	public static void enableScreenQuake(float amplitude, float frequency) {
		final QuakeEffectComponent qec = gameEntityEngine.createComponent(QuakeEffectComponent.class);
		qec.setup(amplitude, frequency);
		cameraEntity.add(qec);
	}
	
	public static void enableScreenQuake(RichterScale quake) {
		enableScreenQuake(quake.amplitude, quake.frequency);
	}
	
	public static void disableScreenQuake() {
		cameraEntity.remove(QuakeEffectComponent.class);
	}
}
