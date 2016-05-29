package de.homelab.madgaksha.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.util.Point;

public class PEstelle extends APlayer {

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>,?>[] requestedRequiredResources() {
		return new IResource[] {
				EAnimationList.ESTELLE_STANDING,
				ETexture.ESTELLE_ON_KNEES,
				ETexture.OVAL_SHADOW,
				EMusic.ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO,
				EMusic.ESTELLE_GYAA,
				EMusic.ESTELLE_UERRGH,
				EMusic.ESTELLE_MINNA_GOMEN,
				};
	}
	
	@Override
	public EAnimationList requestedAnimationList() {
		return EAnimationList.ESTELLE_STANDING;
	}

	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f, 0.0f, 36.0f);
	}

	@Override
	protected Vector2 requestedSpriteOrigin() {
		return new Vector2(64.0f,64.0f);
	}
	
	@Override
	protected Rectangle requestedBoundingBoxRender() {
		return new Rectangle(-60, -50.0f, 118.0f, 94.0f);
	}

	@Override
	protected Rectangle requestedBoundingBoxMap() {
		return new Rectangle(-29.0f, -59.0f, 54.0f, 16.0f);
	}

	@Override
	protected Rectangle requestedBoundingBoxCollision() {
		return new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	@Override
	protected Shape2D requestedExactShapeCollision() {
		return new Point(requestedBoundingBoxCollision());
	}
	
	@Override
	protected int requestedMaxPainPoints() {
		// TODO Auto-generated method stub
		return 420000000;
	}
	
	@Override
	public ShadowComponent makeShadow() {
		return new ShadowComponent(ETexture.OVAL_SHADOW, 0.0f,-60.0f,0.0f,-0.010f, 0.5f, 0.0f);
	}

	@Override
	protected float requestedMovementAccelerationFactorLow() {
		return 40.0f;
	}
	
	@Override
	protected float requestedMovementAccelerationFactorHigh() {
		return 80.0f;
	}

	@Override
	protected float requestedMovementFrictionFactor() {
		return 0.8f;
	}

	@Override
	protected EWeapon[] requestedSupportedWeapons() {
		return null;
	}

	@Override
	protected ETokugi[] requestedSupportedTokugi() {
		return null;
	}

	@Override
	protected float requestedBulletAttack() {
		return 1.0f;
	}

	@Override
	protected float requestedBulletResistance() {
		return 1.0f;
	}

	@Override
	protected ETexture requestedHitCircleTexture() {
		return ETexture.HIT_CIRCLE_YELLOW;
	}

	@Override
	protected ETexture requestedBattleStigmaTexture() {
		return ETexture.BATTLE_STIGMA_GREEN;
	}
	
	@Override
	protected float requestedMovementBattleSpeedLow() {
		return 200.0f;
	}

	@Override
	protected float requestedMovementBattleSpeedHigh() {
		return 400.0f;
	}

	@Override
	protected float requestedBattleStigmaAngularVelocity() {
		return 80.0f;
	}

	@Override
	protected EMusic requestedVoiceOnBattleStart() {
		return EMusic.ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO;
	}
	
	@Override
	protected EMusic requestedVoiceOnLightDamage() {
		return EMusic.ESTELLE_GYAA;
	}
	
	@Override
	protected EMusic requestedVoiceOnHeavyDamage() {
		return EMusic.ESTELLE_UERRGH;
	}

	@Override
	protected EMusic requestedVoiceOnDeath() {
		return EMusic.ESTELLE_MINNA_GOMEN;
	}

	@Override
	protected Color requestedBattleStigmaColorWhenHit() {
		return Color.RED;
	}

	@Override
	protected ETexture requestedDeathSprite() {
		return ETexture.ESTELLE_ON_KNEES;
	}
}
