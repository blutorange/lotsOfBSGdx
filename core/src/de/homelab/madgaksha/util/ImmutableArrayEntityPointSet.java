package de.homelab.madgaksha.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.dreizak.miniball.model.PointSet;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;

/**
 * Extending ImmutableArray with two methods to use it with miniball. 
 * @author madgaksha
 *
 */
public class ImmutableArrayEntityPointSet extends ImmutableArray<Entity> implements PointSet {
	public ImmutableArrayEntityPointSet(Array<Entity> array) {
		super(array);
	}

	@Override
	public int dimension() {
		return 2;
	}

	@Override
	public double coord(int i, int j) {
		final PositionComponent pc = Mapper.positionComponent.get(super.get(i));
		return (pc == null) ? 0.0d : (i == 0) ? pc.x : pc.y;
	}
}