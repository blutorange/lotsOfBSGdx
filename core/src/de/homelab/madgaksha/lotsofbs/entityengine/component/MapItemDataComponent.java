package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;

public class MapItemDataComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MapItemDataComponent.class);
	
	public final MapProperties props = new MapProperties();
	public IMapItem mapItem = IMapItem.MOCK;
	public final PositionComponent originalPosition = new PositionComponent();
	
	public MapItemDataComponent() {
	}
	
	public MapItemDataComponent(PositionComponent originalPosition, MapProperties props, IMapItem mapItem) {
		setup(originalPosition, props, mapItem);
	}
	
	public void setup(PositionComponent originalPosition, MapProperties props, IMapItem mapItem) {
		this.props.clear();
		this.props.putAll(props);
		if (mapItem != null) this.mapItem = mapItem;
		if (originalPosition != null) this.originalPosition.setup(originalPosition);
	}
	
	@Override
	public void reset() {
		mapItem = IMapItem.MOCK;
		props.clear();
		originalPosition.reset();
	}
}
