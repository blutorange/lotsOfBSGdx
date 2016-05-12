package de.homelab.madgaksha.entityengine.entitysystem;

import javax.swing.text.View;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;

public class ViewportUpdateSystem extends IteratingSystem {
	public ViewportUpdateSystem() {
		this(DefaultPriority.viewportUpdateSystem);
	}

	@SuppressWarnings("unchecked")
	public ViewportUpdateSystem(int priority) {
		super(Family.all(ViewportComponent.class, PositionComponent.class, RotationComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final ViewportComponent vc = Mapper.viewportComponent.get(entity);
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		// TODO
	}

	// private void update(float dt) {
	// Update viewport
	// viewport.setWorldWidth(2*hw);
	// viewport.setWorldHeight(2*hh);
	// set clipping plane
	// }

}
