package de.homelab.madgaksha.entityengine;

import com.badlogic.ashley.core.ComponentMapper;

import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InputComponent;
import de.homelab.madgaksha.entityengine.component.InverseMassComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.TrajectoryComponent;
import de.homelab.madgaksha.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;

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
	public final static ComponentMapper<HoverEffectComponent> hoverEffectComponent = ComponentMapper.getFor(HoverEffectComponent.class);
	public final static ComponentMapper<InverseMassComponent> inverseMassComponent = ComponentMapper
			.getFor(InverseMassComponent.class);
	public final static ComponentMapper<InputComponent> inputComponent = ComponentMapper
			.getFor(InputComponent.class);	
	public final static ComponentMapper<LeanEffectComponent> leanEffectComponent = ComponentMapper
			.getFor(LeanEffectComponent.class);	
	public final static ComponentMapper<ManyTrackingComponent> manyTrackingComponent = ComponentMapper
			.getFor(ManyTrackingComponent.class);
	public final static ComponentMapper<ParticleEffectComponent> particleEffectComponent = ComponentMapper
			.getFor(ParticleEffectComponent.class);
	public final static ComponentMapper<PositionComponent> positionComponent = ComponentMapper
			.getFor(PositionComponent.class);
	public final static ComponentMapper<RotationComponent> rotationComponent = ComponentMapper
			.getFor(RotationComponent.class);
	public final static ComponentMapper<ScaleComponent> scaleComponent = ComponentMapper
			.getFor(ScaleComponent.class);
	public final static ComponentMapper<ShouldPositionComponent> shouldPositionComponent = ComponentMapper
			.getFor(ShouldPositionComponent.class);
	public final static ComponentMapper<ShouldRotationComponent> shouldRotationComponent = ComponentMapper
			.getFor(ShouldRotationComponent.class);
	public final static ComponentMapper<ShouldScaleComponent> shouldScaleComponent = ComponentMapper
			.getFor(ShouldScaleComponent.class);
	public final static ComponentMapper<SpriteAnimationComponent> spriteAnimationComponent = ComponentMapper
			.getFor(SpriteAnimationComponent.class);
	public final static ComponentMapper<SpriteComponent> spriteComponent = ComponentMapper
			.getFor(SpriteComponent.class);
	public final static ComponentMapper<SpriteForDirectionComponent> spriteForDirectionComponent = ComponentMapper
			.getFor(SpriteForDirectionComponent.class);
	public final static ComponentMapper<TemporalComponent> temporalComponent = ComponentMapper
			.getFor(TemporalComponent.class);
	public final static ComponentMapper<TimeScaleComponent> timeScaleComponent = ComponentMapper
			.getFor(TimeScaleComponent.class);
	public final static ComponentMapper<TriggerScreenComponent> triggerScreenComponent = ComponentMapper
			.getFor(TriggerScreenComponent.class);
	public final static ComponentMapper<TriggerStartupComponent> triggerStartupComponent = ComponentMapper
			.getFor(TriggerStartupComponent.class);
	public final static ComponentMapper<TriggerTouchComponent> triggerTouchComponent = ComponentMapper
			.getFor(TriggerTouchComponent.class);

	public final static ComponentMapper<TrajectoryComponent> trajectoryComponent = ComponentMapper
			.getFor(TrajectoryComponent.class);
	public final static ComponentMapper<VelocityComponent> velocityComponent = ComponentMapper
			.getFor(VelocityComponent.class);
	public final static ComponentMapper<ViewportComponent> viewportComponent = ComponentMapper
			.getFor(ViewportComponent.class);
}