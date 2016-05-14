package de.homelab.madgaksha.entityengine;

import com.badlogic.ashley.core.ComponentMapper;

import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.InverseMassComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;

public class Mapper {
	private Mapper() {
	};

	public final static ComponentMapper<BoundingBoxComponent> boundingBoxComponent = ComponentMapper
			.getFor(BoundingBoxComponent.class);
	public final static ComponentMapper<BoundingSphereComponent> boundingSphereComponent = ComponentMapper
			.getFor(BoundingSphereComponent.class);
	public final static ComponentMapper<DirectionComponent> directionComponent = ComponentMapper
			.getFor(DirectionComponent.class);
	public final static ComponentMapper<ForceComponent> forceComponent = ComponentMapper.getFor(ForceComponent.class);
	public final static ComponentMapper<InverseMassComponent> inverseMassComponent = ComponentMapper
			.getFor(InverseMassComponent.class);
	public final static ComponentMapper<ManyTrackingComponent> manyTrackingComponent = ComponentMapper
			.getFor(ManyTrackingComponent.class);
	public final static ComponentMapper<PositionComponent> positionComponent = ComponentMapper
			.getFor(PositionComponent.class);
	public final static ComponentMapper<RotationComponent> rotationComponent = ComponentMapper
			.getFor(RotationComponent.class);
	public final static ComponentMapper<ShouldPositionComponent> shouldPositionComponent = ComponentMapper
			.getFor(ShouldPositionComponent.class);
	public final static ComponentMapper<ShouldRotationComponent> shouldRotationComponent = ComponentMapper
			.getFor(ShouldRotationComponent.class);
	public final static ComponentMapper<SpriteAnimationComponent> spriteAnimationComponent = ComponentMapper
			.getFor(SpriteAnimationComponent.class);
	public final static ComponentMapper<SpriteComponent> spriteComponent = ComponentMapper
			.getFor(SpriteComponent.class);
	public final static ComponentMapper<SpriteForDirectionComponent> spriteForDirectionComponent = ComponentMapper
			.getFor(SpriteForDirectionComponent.class);
	public final static ComponentMapper<TimeScaleComponent> timeScaleComponent = ComponentMapper
			.getFor(TimeScaleComponent.class);
	public final static ComponentMapper<VelocityComponent> velocityComponent = ComponentMapper
			.getFor(VelocityComponent.class);
	public final static ComponentMapper<ViewportComponent> viewportComponent = ComponentMapper
			.getFor(ViewportComponent.class);
}