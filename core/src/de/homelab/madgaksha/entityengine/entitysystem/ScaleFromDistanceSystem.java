package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ScaleFromDistanceComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Sets an object's scale depending on its position from another entity.
 * 
 * @author madgaksha
 */
public class ScaleFromDistanceSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ScaleFromDistanceSystem.class);

	private final Vector2 v = new Vector2();

	public ScaleFromDistanceSystem() {
		this(DefaultPriority.scaleFromDistanceSystem);
	}

	@SuppressWarnings("unchecked")
	public ScaleFromDistanceSystem(int priority) {
		super(DisableIteratingSystem.all(PositionComponent.class, ShouldScaleComponent.class, ScaleFromDistanceComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(entity);
		final ScaleFromDistanceComponent sfdc = Mapper.scaleFromDistanceComponent.get(entity);
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		if (sfdc.positionComponent != null) {
			v.set(sfdc.positionComponent.x - pc.x, sfdc.positionComponent.y - pc.y);
			ssc.scaleX = ssc.scaleY = ssc.scaleZ = MathUtils.clamp(sfdc.minScale + (sfdc.maxScale - sfdc.minScale)
					* (v.len() - sfdc.minDistance) / (sfdc.maxDistance - sfdc.minDistance), sfdc.minScale,
					sfdc.maxScale);
		}
	}

}
