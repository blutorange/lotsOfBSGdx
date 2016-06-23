package de.homelab.madgaksha.entityengine.entityutils;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Utilities for working with an entity's pain points. Usually processed by an
 * appropriate entity system, this should be used sparingly.
 * 
 * @author madgaksha
 *
 */
public class PainBarUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PainBarUtils.class);

	/**
	 * @param e
	 *            Entity to check for pain points
	 * @return Current pain points, or -1 if it does not possess pain points.
	 */
	public static long getPainPoints(Entity e) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(e);
		return ppc == null ? -1L : ppc.painPoints;
	}

	/**
	 * @param e
	 *            Entity to check for pain points ratio.
	 * @return Current pain points ratio, or -1.0f if it does not possess pain
	 *         points.
	 */
	public static float getPainPointsRatio(Entity e) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(e);
		return ppc == null ? -1.0f : ppc.painPointsRatio;
	}
}
