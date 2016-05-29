package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ColorComponent;
import de.homelab.madgaksha.entityengine.component.ColorFlashEffectComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.StickyEffectComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.logging.Logger;

public class PostEffectSystem extends EntitySystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PostEffectSystem.class);

	private Family familyLeanEffect;
	private Family familyHoverEffect;
	private Family familyStickyEffect;
	private Family familyColorFlashEffect;
	
	private ImmutableArray<Entity> entitiesLeanEffect;
	private ImmutableArray<Entity> entitiesHoverEffect;
	private ImmutableArray<Entity> entitiesStickyEffect;
	private ImmutableArray<Entity> entitiesColorFlashEffect;

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
		this.familyLeanEffect = Family.all(ShouldRotationComponent.class, ShouldScaleComponent.class, LeanEffectComponent.class, VelocityComponent.class).get();
		this.familyHoverEffect = Family.all(TemporalComponent.class, PositionComponent.class, HoverEffectComponent.class).get();
		this.familyStickyEffect = Family.all(PositionComponent.class, StickyEffectComponent.class).exclude(InactiveComponent.class).get();
		this.familyColorFlashEffect = Family.all(ColorComponent.class, ColorFlashEffectComponent.class).exclude(InactiveComponent.class).get();
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
			a = upVector.x*vc.x+upVector.y*vc.y;
			b = upVector.y*vc.x-upVector.x*vc.y;
			// reset
			ssc.scaleX = ssc.scaleY = 1.0f;
			src.thetaZ = 0.0f;
			// set rotation / scale depending upon velocity in the corresponding direction
			if (Math.abs(a) > Math.abs(b)) {
				if (a>0) // up
					ssc.scaleX = ssc.scaleY = 1.0f - lec.targetScale * (1.0f-(float)Math.exp(-lec.leanFactor*a*a));
				else // down
					ssc.scaleX = ssc.scaleY = 1.0f + lec.targetScale * (1.0f-(float)Math.exp(-lec.leanFactor*a*a));
			}
			else if (b>0) //right
				src.thetaZ = -lec.targetAngle * (1.0f-(float)Math.exp(-lec.leanFactor*b*b));
			else // left
				src.thetaZ = lec.targetAngle * (1.0f-(float)Math.exp(-lec.leanFactor*b*b));
		}
		
		// Hover effect.
		for (int i = 0; i < entitiesHoverEffect.size(); ++i) {
			final Entity entity = entitiesLeanEffect.get(i);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final HoverEffectComponent hec = Mapper.hoverEffectComponent.get(entity);
			final TemporalComponent tc = Mapper.temporalComponent.get(entity);
			a = MathUtils.sin(hec.frequency*tc.totalTime);
			pc.offsetX = hec.amplitude*upVector.x*a;
			pc.offsetY = hec.amplitude*upVector.y*a;	
		}
		
		// Sticky effect.
		for (int i = 0; i < entitiesStickyEffect.size(); ++i) {
			final Entity entity = entitiesStickyEffect.get(i);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final StickyEffectComponent sec = Mapper.stickyEffectComponent.get(entity);
			if (sec.offsetRelativeToCamera) {
				pc.x = sec.stickToPositionComponent.x + sec.offsetY * upVector.x + sec.offsetX * upVector.y;
				pc.y = sec.stickToPositionComponent.y + sec.offsetY * upVector.y - sec.offsetX * upVector.x;
			}
			else {
				pc.x = sec.stickToPositionComponent.x + sec.offsetX;
				pc.y = sec.stickToPositionComponent.y + sec.offsetY;
			}
			if (!sec.ignoreTrackOffset) {
				pc.offsetX = sec.stickToPositionComponent.offsetX;
				pc.offsetY = sec.stickToPositionComponent.offsetY;
			}
		}
		
		// Color flash effect.
		for (int i = 0; i < entitiesColorFlashEffect.size(); ++i) {
			final Entity entity = entitiesColorFlashEffect.get(i);
			ColorFlashEffectComponent cfec = Mapper.colorFlashEffectComponent.get(entity);
			ColorComponent cc = Mapper.colorComponent.get(entity);
			color.set(cfec.colorStart).lerp(cfec.colorEnd, cfec.interpolator.apply(cfec.directionForward ? cfec.totalTime / cfec.duration : (1.0f-cfec.totalTime/cfec.duration)));
			cc.color.set(color);
			cfec.totalTime += deltaTime;
			if (cfec.totalTime >= cfec.duration) {
				cfec.directionForward = !cfec.directionForward;
				++cfec.totalLoops;
				cfec.totalTime = 0.0f;
				if (cfec.totalLoops >= cfec.repetitions) {
					color.set(cfec.colorStart).lerp(cfec.colorEnd, cfec.interpolator.apply(cfec.directionForward ? 0.0f : 1.0f));
					cc.color.set(color);
					entity.remove(ColorFlashEffectComponent.class);
				}
			}
		}

	}

	@Override
	public void addedToEngine(Engine engine) {
		entitiesLeanEffect = engine.getEntitiesFor(familyLeanEffect);
		entitiesHoverEffect = engine.getEntitiesFor(familyHoverEffect);
		entitiesStickyEffect = engine.getEntitiesFor(familyStickyEffect);
		entitiesColorFlashEffect = engine.getEntitiesFor(familyColorFlashEffect);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entitiesLeanEffect = null;
		entitiesHoverEffect = null;
		entitiesStickyEffect = null;
		entitiesColorFlashEffect = null;
	}
}
