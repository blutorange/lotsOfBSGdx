package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleFromDistanceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

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

	public ScaleFromDistanceSystem(final int priority) {
		super(DisableIteratingSystem
				.all(PositionComponent.class, ShouldScaleComponent.class, ScaleFromDistanceComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(entity);
		final ScaleFromDistanceComponent sfdc = Mapper.scaleFromDistanceComponent.get(entity);
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		if (sfdc.positionComponent != null) {
			v.set(sfdc.positionComponent.x - pc.x, sfdc.positionComponent.y - pc.y);
			final float len2 = v.len2();
			if (len2 > sfdc.maxDistance * sfdc.maxDistance)
				ssc.scaleX = ssc.scaleY = ssc.scaleZ = sfdc.maxScale;
			else {
				ssc.scaleX = ssc.scaleY = ssc.scaleZ = MathUtils.clamp(sfdc.minScale + (sfdc.maxScale - sfdc.minScale)
						* ((float) Math.sqrt(len2) - sfdc.minDistance) / (sfdc.maxDistance - sfdc.minDistance),
						sfdc.minScale, sfdc.maxScale);
			}
		}
	}
}