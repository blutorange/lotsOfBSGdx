package de.homelab.madgaksha.util;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.dreizak.miniball.model.PointSet;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;

public class ArrayListEntityPointSet extends ArrayList<Entity> implements PointSet {
	private static final long serialVersionUID = 1L;

	@Override
	public int size() {
		return super.size();
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