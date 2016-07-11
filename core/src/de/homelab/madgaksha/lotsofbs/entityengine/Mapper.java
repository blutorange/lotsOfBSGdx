package de.homelab.madgaksha.lotsofbs.entityengine;

import com.badlogic.ashley.core.ComponentMapper;

import de.homelab.madgaksha.lotsofbs.entityengine.component.AlphaComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnyChildComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BattleDistanceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BulletStatusComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.CallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ColorComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ColorFlashEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DeathComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DisableAllExceptTheseComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.EnemyIconComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.FadeEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceFieldComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.IdComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LifeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ModelComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParentComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectGameComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.QuakeEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleFromDistanceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SiblingComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StickyComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityFieldComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup02Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup03Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup04Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup05Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder0Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder1Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder3Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder4Component;

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

	public final static ComponentMapper<ZOrder0Component> zOrder0Component = ComponentMapper
			.getFor(ZOrder0Component.class);
	public final static ComponentMapper<ZOrder1Component> zOrder1Component = ComponentMapper
			.getFor(ZOrder1Component.class);
	public final static ComponentMapper<ZOrder2Component> zOrder2Component = ComponentMapper
			.getFor(ZOrder2Component.class);
	public final static ComponentMapper<ZOrder3Component> zOrder3Component = ComponentMapper
			.getFor(ZOrder3Component.class);
	public final static ComponentMapper<ZOrder4Component> zOrder4Component = ComponentMapper
			.getFor(ZOrder4Component.class);

	public final static ComponentMapper<AlphaComponent> alphaComponent = ComponentMapper.getFor(AlphaComponent.class);
	public final static ComponentMapper<AngularVelocityComponent> angularVelocityComponent = ComponentMapper
			.getFor(AngularVelocityComponent.class);
	public final static ComponentMapper<AnimationForDirectionComponent> animationForDirectionComponent = ComponentMapper
			.getFor(AnimationForDirectionComponent.class);
	public final static ComponentMapper<AnimationModeListComponent> animationModeListComponent = ComponentMapper
			.getFor(AnimationModeListComponent.class);
	public final static ComponentMapper<AnimationModeQueueComponent> animationModeQueueComponent = ComponentMapper
			.getFor(AnimationModeQueueComponent.class);
	public final static ComponentMapper<AnyChildComponent> anyChildComponent = ComponentMapper
			.getFor(AnyChildComponent.class);
	public final static ComponentMapper<BattleDistanceComponent> battleDistanceComponent = ComponentMapper
			.getFor(BattleDistanceComponent.class);
	public final static ComponentMapper<BehaviourComponent> behaviourComponent = ComponentMapper
			.getFor(BehaviourComponent.class);
	public final static ComponentMapper<BoundingBoxCollisionComponent> boundingBoxCollisionComponent = ComponentMapper
			.getFor(BoundingBoxCollisionComponent.class);
	public final static ComponentMapper<BoundingBoxMapComponent> boundingBoxMapComponent = ComponentMapper
			.getFor(BoundingBoxMapComponent.class);
	public final static ComponentMapper<BoundingBoxRenderComponent> boundingBoxRenderComponent = ComponentMapper
			.getFor(BoundingBoxRenderComponent.class);
	public final static ComponentMapper<BoundingSphereComponent> boundingSphereComponent = ComponentMapper
			.getFor(BoundingSphereComponent.class);
	public final static ComponentMapper<BulletStatusComponent> bulletStatusComponent = ComponentMapper
			.getFor(BulletStatusComponent.class);
	public final static ComponentMapper<CameraFocusComponent> cameraFocusComponent = ComponentMapper
			.getFor(CameraFocusComponent.class);
	public final static ComponentMapper<CallbackComponent> callbackComponent = ComponentMapper
			.getFor(CallbackComponent.class);
	public final static ComponentMapper<ColorComponent> colorComponent = ComponentMapper.getFor(ColorComponent.class);
	public final static ComponentMapper<ColorFlashEffectComponent> colorFlashEffectComponent = ComponentMapper
			.getFor(ColorFlashEffectComponent.class);
	public final static ComponentMapper<ComponentQueueComponent> componentQueueComponent = ComponentMapper
			.getFor(ComponentQueueComponent.class);
	public final static ComponentMapper<DamageQueueComponent> damageQueueComponent = ComponentMapper
			.getFor(DamageQueueComponent.class);
	public final static ComponentMapper<DeathComponent> deathComponent = ComponentMapper.getFor(DeathComponent.class);
	public final static ComponentMapper<DirectionComponent> directionComponent = ComponentMapper
			.getFor(DirectionComponent.class);
	public final static ComponentMapper<DisableAllExceptTheseComponent> disableAllExceptTheseComponent = ComponentMapper
			.getFor(DisableAllExceptTheseComponent.class);
	public final static ComponentMapper<EnemyIconComponent> enemyIconComponent = ComponentMapper
			.getFor(EnemyIconComponent.class);
	public final static ComponentMapper<FadeEffectComponent> fadeEffectComponent = ComponentMapper
			.getFor(FadeEffectComponent.class);
	public final static ComponentMapper<ForceComponent> forceComponent = ComponentMapper.getFor(ForceComponent.class);
	public final static ComponentMapper<ForceFieldComponent> forceFieldComponent = ComponentMapper
			.getFor(ForceFieldComponent.class);
	public final static ComponentMapper<GetHitComponent> getHitComponent = ComponentMapper
			.getFor(GetHitComponent.class);
	public final static ComponentMapper<HoverEffectComponent> hoverEffectComponent = ComponentMapper
			.getFor(HoverEffectComponent.class);
	public final static ComponentMapper<IdComponent> idComponent = ComponentMapper.getFor(IdComponent.class);
	public final static ComponentMapper<InactiveComponent> inactiveComponent = ComponentMapper
			.getFor(InactiveComponent.class);
	public final static ComponentMapper<InputDesktopComponent> inputDesktopComponent = ComponentMapper
			.getFor(InputDesktopComponent.class);
	public final static ComponentMapper<InvisibleComponent> invisibleComponent = ComponentMapper
			.getFor(InvisibleComponent.class);
	public final static ComponentMapper<LifeComponent> lifeComponent = ComponentMapper.getFor(LifeComponent.class);
	public final static ComponentMapper<LeanEffectComponent> leanEffectComponent = ComponentMapper
			.getFor(LeanEffectComponent.class);
	public final static ComponentMapper<ManyTrackingComponent> manyTrackingComponent = ComponentMapper
			.getFor(ManyTrackingComponent.class);
	public final static ComponentMapper<ModelComponent> modelComponent = ComponentMapper.getFor(ModelComponent.class);
	public final static ComponentMapper<PainPointsComponent> painPointsComponent = ComponentMapper
			.getFor(PainPointsComponent.class);
	public final static ComponentMapper<ParentComponent> parentComponent = ComponentMapper
			.getFor(ParentComponent.class);
	public final static ComponentMapper<ParticleEffectScreenComponent> particleEffectScreenComponent = ComponentMapper
			.getFor(ParticleEffectScreenComponent.class);
	public final static ComponentMapper<ParticleEffectGameComponent> particleEffectGameComponent = ComponentMapper
			.getFor(ParticleEffectGameComponent.class);
	public final static ComponentMapper<PositionComponent> positionComponent = ComponentMapper
			.getFor(PositionComponent.class);
	public final static ComponentMapper<QuakeEffectComponent> quakeEffectComponent = ComponentMapper
			.getFor(QuakeEffectComponent.class);
	public final static ComponentMapper<RotationComponent> rotationComponent = ComponentMapper
			.getFor(RotationComponent.class);
	public final static ComponentMapper<ScaleComponent> scaleComponent = ComponentMapper.getFor(ScaleComponent.class);
	public final static ComponentMapper<ScaleFromDistanceComponent> scaleFromDistanceComponent = ComponentMapper
			.getFor(ScaleFromDistanceComponent.class);
	public final static ComponentMapper<ShadowComponent> shadowComponent = ComponentMapper
			.getFor(ShadowComponent.class);
	public final static ComponentMapper<ShapeComponent> shapeComponent = ComponentMapper.getFor(ShapeComponent.class);
	public final static ComponentMapper<ShouldDirectionComponent> shouldDirectionComponent = ComponentMapper
			.getFor(ShouldDirectionComponent.class);
	public final static ComponentMapper<ShouldPositionComponent> shouldPositionComponent = ComponentMapper
			.getFor(ShouldPositionComponent.class);
	public final static ComponentMapper<ShouldRotationComponent> shouldRotationComponent = ComponentMapper
			.getFor(ShouldRotationComponent.class);
	public final static ComponentMapper<ShouldScaleComponent> shouldScaleComponent = ComponentMapper
			.getFor(ShouldScaleComponent.class);
	public final static ComponentMapper<SiblingComponent> siblingComponent = ComponentMapper
			.getFor(SiblingComponent.class);
	public final static ComponentMapper<SpriteAnimationComponent> spriteAnimationComponent = ComponentMapper
			.getFor(SpriteAnimationComponent.class);
	public final static ComponentMapper<SpriteComponent> spriteComponent = ComponentMapper
			.getFor(SpriteComponent.class);
	public final static ComponentMapper<StatusValuesComponent> statusValuesComponent = ComponentMapper
			.getFor(StatusValuesComponent.class);
	public final static ComponentMapper<StickyComponent> stickyComponent = ComponentMapper
			.getFor(StickyComponent.class);
	public final static ComponentMapper<TemporalComponent> temporalComponent = ComponentMapper
			.getFor(TemporalComponent.class);
	public final static ComponentMapper<TimedCallbackComponent> timedCallbackComponent = ComponentMapper
			.getFor(TimedCallbackComponent.class);
	public final static ComponentMapper<TimeScaleComponent> timeScaleComponent = ComponentMapper
			.getFor(TimeScaleComponent.class);
	public final static ComponentMapper<TriggerScreenComponent> triggerScreenComponent = ComponentMapper
			.getFor(TriggerScreenComponent.class);
	public final static ComponentMapper<TriggerStartupComponent> triggerStartupComponent = ComponentMapper
			.getFor(TriggerStartupComponent.class);
	public final static ComponentMapper<TriggerTouchComponent> triggerTouchComponent = ComponentMapper
			.getFor(TriggerTouchComponent.class);
	public final static ComponentMapper<VelocityComponent> velocityComponent = ComponentMapper
			.getFor(VelocityComponent.class);
	public final static ComponentMapper<VelocityFieldComponent> velocityFieldComponent = ComponentMapper
			.getFor(VelocityFieldComponent.class);
	public final static ComponentMapper<ViewportComponent> viewportComponent = ComponentMapper
			.getFor(ViewportComponent.class);
	public final static ComponentMapper<VoiceComponent> voiceComponent = ComponentMapper.getFor(VoiceComponent.class);
}