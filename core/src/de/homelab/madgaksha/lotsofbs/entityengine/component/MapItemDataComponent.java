package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;

public class MapItemDataComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MapItemDataComponent.class);
	private final static Shape2D DEFAULT_SHAPE = new Circle(0f, 0f, 1f);
	
	public Shape2D shape = DEFAULT_SHAPE;
	public final MapProperties props = new MapProperties();
	public IMapItem mapItem = IMapItem.MOCK;
	public final PositionComponent originalPosition = new PositionComponent();
	
	public MapItemDataComponent() {
	}
	
	public MapItemDataComponent(Shape2D shape, MapProperties props, IMapItem mapItem, PositionComponent originalPosition) {
		setup(shape, props, mapItem, originalPosition);
	}
	
	public void setup(Shape2D shape, MapProperties props, IMapItem mapItem, PositionComponent originalPosition) {
		this.props.clear();
		this.props.putAll(props);
		if (shape != null) this.shape = shape;
		if (mapItem != null) this.mapItem = mapItem;
		if (originalPosition != null) this.originalPosition.setup(originalPosition);
	}
	
	@Override
	public void reset() {
		shape = DEFAULT_SHAPE;
		mapItem = IMapItem.MOCK;
		props.clear();
		originalPosition.reset();
	}
}
