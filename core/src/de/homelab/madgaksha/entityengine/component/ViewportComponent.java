package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.level.GameViewport;

public class ViewportComponent implements Component, Poolable {
	public GameViewport viewport = null;
	
	public ViewportComponent() {}
	public ViewportComponent(GameViewport v) {
		viewport = v;
	}
	@Override
	public void reset() {
		viewport = null;		
	}
	
	
}