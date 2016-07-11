package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AlphaComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ColorComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ColorFlashEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.FadeEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.QuakeEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class PostEffectSystem extends EntitySystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PostEffectSystem.class);

	private Family familyLeanEffect;
	private Family familyHoverEffect;
	private Family familyColorFlashEffect;
	private Family familyFadeEffect;
	private Family familyQuakeEffect;

	private ImmutableArray<Entity> entitiesLeanEffect;
	private ImmutableArray<Entity> entitiesHoverEffect;
	private ImmutableArray<Entity> entitiesColorFlashEffect;
	private ImmutableArray<Entity> entitiesFadeEffect;
	private ImmutableArray<Entity> entitiesQuakeEffect;

	private final static Color color = new Color();
	private final Vector3 upVector;
	private float a, b;

	public PostEffectSystem() {
		this(DefaultPriority.postEffectSystem);
	}

	@SuppressWarnings("unchecked")
	public PostEffectSystem(int priority) {
		super(priority);
		this.upVector = viewportGame.getCamera().up;
		this.familyLeanEffect = Family.all(ShouldRotationComponent.class, ShouldScaleComponent.class,
				LeanEffectComponent.class, VelocityComponent.class).get();
		this.familyHoverEffect = Family
				.all(TemporalComponent.class, PositionComponent.class, HoverEffectComponent.class).get();
		this.familyColorFlashEffect = Family
				.all(TemporalComponent.class, ColorComponent.class, ColorFlashEffectComponent.class)
				.exclude(InactiveComponent.class).get();
		this.familyFadeEffect = Family.all(AlphaComponent.class, FadeEffectComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class).get();
		this.familyQuakeEffect = Family
				.all(QuakeEffectComponent.class, PositionComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class).get();
	}

	@Override
	public void update(float deltaTime) {
		// Lean effect.
		for (int i = 0; i < entitiesLeanEffect.size(); ++i) {
			final Entity entity = entitiesLeanEffect.get(i);
			final ShouldRotationComponent src = Mapper.shouldRotationComponent.get(entity);
			final ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(entity);
			final LeanEffectComponent lec = Mapper.leanEffectComponent.get(entity);
			final VelocityComponent vc = Mapper.velocityComponent.get(entity);
			a = upVector.x * vc.x + upVector.y * vc.y;
			b = upVector.y * vc.x - upVector.x * vc.y;
			// reset
			ssc.scaleX = ssc.scaleY = 1.0f;
			src.thetaZ = 0.0f;
			// set rotation / scale depending upon velocity in the corresponding
			// direction
			if (Math.abs(a) > Math.abs(b)) {
				if (a > 0) // up
					ssc.scaleX = ssc.scaleY = 1.0f
							- lec.targetScale * (1.0f - (float) Math.exp(-lec.leanFactor * a * a));
				else // down
					ssc.scaleX = ssc.scaleY = 1.0f
							+ lec.targetScale * (1.0f - (float) Math.exp(-lec.leanFactor * a * a));
			} else if (b > 0) // right
				src.thetaZ = -lec.targetAngle * (1.0f - (float) Math.exp(-lec.leanFactor * b * b));
			else // left
				src.thetaZ = lec.targetAngle * (1.0f - (float) Math.exp(-lec.leanFactor * b * b));
		}

		// Hover effect.
		for (int i = 0; i < entitiesHoverEffect.size(); ++i) {
			final Entity entity = entitiesLeanEffect.get(i);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final HoverEffectComponent hec = Mapper.hoverEffectComponent.get(entity);
			final TemporalComponent tc = Mapper.temporalComponent.get(entity);
			a = MathUtils.sin(hec.frequency * tc.totalTime);
			pc.offsetX = hec.amplitude * upVector.x * a;
			pc.offsetY = hec.amplitude * upVector.y * a;
		}

		// Color flash effect.
		for (int i = 0; i < entitiesColorFlashEffect.size(); ++i) {
			final Entity entity = entitiesColorFlashEffect.get(i);
			final TemporalComponent tc = Mapper.temporalComponent.get(entity);
			final ColorFlashEffectComponent cfec = Mapper.colorFlashEffectComponent.get(entity);
			final ColorComponent cc = Mapper.colorComponent.get(entity);
			color.set(cfec.colorStart).lerp(cfec.colorEnd, cfec.interpolator.apply(
					cfec.directionForward ? cfec.totalTime / cfec.duration : (1.0f - cfec.totalTime / cfec.duration)));
			cc.color.set(color);
			cfec.totalTime += tc.deltaTime;
			if (cfec.totalTime >= cfec.duration) {
				cfec.directionForward = !cfec.directionForward;
				++cfec.totalLoops;
				cfec.totalTime = 0.0f;
				if (cfec.totalLoops >= cfec.repetitions) {
					color.set(cfec.colorStart).lerp(cfec.colorEnd,
							cfec.interpolator.apply(cfec.directionForward ? 0.0f : 1.0f));
					cc.color.set(color);
					entity.remove(ColorFlashEffectComponent.class);
				}
			}
		}

		// Fade effect.
		for (int i = 0; i < entitiesFadeEffect.size(); ++i) {
			final Entity entity = entitiesFadeEffect.get(i);
			final FadeEffectComponent fec = Mapper.fadeEffectComponent.get(entity);
			final TemporalComponent tc = Mapper.temporalComponent.get(entity);
			final AlphaComponent ac = Mapper.alphaComponent.get(entity);
			fec.totalTime += tc.deltaTime;
			final float ratio = MathUtils.clamp(fec.totalTime / fec.duration, 0.0f, 1.0f);
			ac.alpha = fec.interpolation.apply(fec.start, fec.end, ratio);
			if (fec.totalTime >= fec.duration) {
				if (fec.callback != null)
					fec.callback.run(entity, fec.customData);
				entity.remove(FadeEffectComponent.class);
			}
		}

		// Quake effect.
		for (int i = 0; i < entitiesQuakeEffect.size(); ++i) {
			final Entity entity = entitiesQuakeEffect.get(i);
			final QuakeEffectComponent qec = Mapper.quakeEffectComponent.get(entity);
			final TemporalComponent tc = Mapper.temporalComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			qec.minusT -= tc.deltaTime * qec.frequency;
			if (qec.minusT <= 0.0f) {
				qec.minusT = 1.0f;
				qec.angle = (qec.angle + MathUtils.random(qec.minAdvanceAngle, qec.maxAdvanceAngle)) % 360.0f;
				float amplitude = MathUtils.random(qec.minAmplitudeRatio * qec.amplitude, qec.amplitude);
				pc.offsetX = amplitude * MathUtils.sinDeg(qec.angle);
				pc.offsetY = amplitude * MathUtils.cosDeg(qec.angle);
			}
		}

	}

	@Override
	public void addedToEngine(Engine engine) {
		entitiesLeanEffect = engine.getEntitiesFor(familyLeanEffect);
		entitiesHoverEffect = engine.getEntitiesFor(familyHoverEffect);
		entitiesColorFlashEffect = engine.getEntitiesFor(familyColorFlashEffect);
		entitiesFadeEffect = engine.getEntitiesFor(familyFadeEffect);
		entitiesQuakeEffect = engine.getEntitiesFor(familyQuakeEffect);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entitiesLeanEffect = null;
		entitiesHoverEffect = null;
		entitiesColorFlashEffect = null;
		entitiesFadeEffect = null;
		entitiesQuakeEffect = null;
	}
}
