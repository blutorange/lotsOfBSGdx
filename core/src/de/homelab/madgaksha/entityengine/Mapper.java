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
import de.homelab.madgaksha.entityengine.component.ShapeComponent;
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
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup05Component;

public final class Mapper {
	private Mapper() {
	};

	public final static ComponentMapper<TriggerTouchGroup01Component> triggerTouchGroup01Component = ComponentMapper
			.getFor(TriggerTouchGroup01Component.class);
	public final static ComponentMapper<TriggerTouchGroup02Component> triggerTouchGroup02Component = ComponentMapper
			.getFor(TriggerTouchGroup02Component.class);
	public final static ComponentMapper<TriggerTouchGroup03Component> triggerTouchGroup03Component = ComponentMapper
			.getFor(TriggerTouchGroup03Component.class);
	public final static ComponentMapper<TriggerTouchGroup04Component> triggerTouchGroup04Component = ComponentMapper
			.getFor(TriggerTouchGroup04Component.class);
	public final static ComponentMapper<TriggerTouchGroup05Component> triggerTouchGroup05Component = ComponentMapper
			.getFor(TriggerTouchGroup05Component.class);
	
	public final static ComponentMapper<ReceiveTouchGroup01Component> receiveTouchGroup01Component = ComponentMapper
			.getFor(ReceiveTouchGroup01Component.class);
	public final static ComponentMapper<ReceiveTouchGroup02Component> receiveTouchGroup02Component = ComponentMapper
			.getFor(ReceiveTouchGroup02Component.class);
	public final static ComponentMapper<ReceiveTouchGroup03Component> receiveTouchGroup03Component = ComponentMapper
			.getFor(ReceiveTouchGroup03Component.class);
	public final static ComponentMapper<ReceiveTouchGroup04Component> receiveTouchGroup04Component = ComponentMapper
			.getFor(ReceiveTouchGroup04Component.class);
	public final static ComponentMapper<ReceiveTouchGroup05Component> receiveTouchGroup05Component = ComponentMapper
			.getFor(ReceiveTouchGroup05Component.class);
	
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
	public final static ComponentMapper<ShapeComponent> shapeComponent = ComponentMapper
			.getFor(ShapeComponent.class);
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