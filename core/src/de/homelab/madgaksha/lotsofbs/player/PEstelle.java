package de.homelab.madgaksha.lotsofbs.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent.ConeDistributionParameters;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.lotsofbs.player.consumable.EConsumable;
import de.homelab.madgaksha.lotsofbs.player.tokugi.ETokugi;
import de.homelab.madgaksha.lotsofbs.player.weapon.EWeapon;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.util.Point;

public class PEstelle extends APlayer {

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource[] { EAnimationList.ESTELLE_STANDING, EAnimationList.ESTELLE_RUNNING,
				EAnimationList.ESTELLE_ON_KNEES, ETexture.OVAL_SHADOW, ESound.ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO,
				ESound.ESTELLE_GYAA, ESound.ESTELLE_UERRGH, ESound.ESTELLE_MINNA_GOMEN,
				ESound.ESTELLE_MADA_MADA_IKERU_WA, ESound.ESTELLE_SAA_IKU_WAYO, ESound.ESTELLE_CHOU_DEKI };
	}

	@Override
	public EAnimationList requestedNormalAnimationList() {
		return EAnimationList.ESTELLE_STANDING;
	}

	@Override
	public EAnimationList requestedBattleAnimationList() {
		return EAnimationList.ESTELLE_RUNNING;
	}

	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f, 0.0f, 36.0f);
	}

	@Override
	protected Vector2 requestedSpriteOrigin() {
		return new Vector2(64.0f, 64.0f);
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
	protected Vector2 requestedOffsetPlayerToHitCircle() {
		return new Vector2(2.0f, 51.0f);
	}

	@Override
	protected Shape2D requestedExactShapeCollision() {
		return new Point(requestedBoundingBoxCollision());
	}

	@Override
	protected int requestedMaxPainPoints() {
		return 420000000;
	}

	@Override
	public void setupShadow(ShadowComponent kc) {
		kc.setup(ETexture.OVAL_SHADOW, 0.0f, -60.0f, 0.0f, -0.010f, 0.5f, 0.0f);
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
		return new EWeapon[] { EWeapon.BASIC, EWeapon.MULTI };
	}

	@Override
	protected ETokugi[] requestedSupportedTokugi() {
		return new ETokugi[] { ETokugi.OUKAMUSOUGEKI };
	}

	@Override
	protected EConsumable[] requestedSupportedConsumable() {
		return new EConsumable[] {
				EConsumable.LOWHEAL,
				EConsumable.MIDHEAL,
				EConsumable.HIGHHEAL,
		};
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
	protected ESound requestedVoiceOnBattleModeStart() {
		return ESound.ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO;
	}

	@Override
	protected ESound requestedVoiceOnBattleModeEnd() {
		return ESound.ESTELLE_MADA_MADA_IKERU_WA;
	}

	@Override
	protected ESound requestedVoiceOnBattleModeFlee() {
		return ESound.ESTELLE_SAA_IKU_WAYO;
	}

	@Override
	protected ESound requestedVoiceOnEnemyKilled() {
		return ESound.ESTELLE_CHOU_DEKI;
	}

	@Override
	protected ESound requestedVoiceOnLightDamage() {
		return ESound.ESTELLE_GYAA;
	}

	@Override
	protected ESound requestedVoiceOnHeavyDamage() {
		return ESound.ESTELLE_UERRGH;
	}

	@Override
	protected ESound requestedVoiceOnDeath() {
		return ESound.ESTELLE_MINNA_GOMEN;
	}

	@Override
	protected Color requestedBattleStigmaColorWhenHit() {
		return Color.RED;
	}

	@Override
	protected EAnimationList requestedDamageAnimationList() {
		return EAnimationList.ESTELLE_HIT;
	}
	
	@Override
	protected EAnimationList requestedDeathAnimationList() {
		return EAnimationList.ESTELLE_ON_KNEES;
	}

	@Override
	protected ConeDistributionParameters requestedItemCircleParamters() {
		return ConeDistributionParameters.DEFAULT;
	}

	@Override
	protected int requestedMaximumHoldableItems() {
		return 3;
	}

	@Override
	protected ESound requestedVoiceOnConsumableUse() {
		return ESound.ESTELLE_OKAY;
	}
	
	@Override
	protected ESound requestedVoiceOnLightHeal() {
		return ESound.ESTELLE_HAA;
	}
	
	@Override
	protected ESound requestedVoiceOnHeavyHeal() {
		return ESound.ESTELLE_HAAAAA;
	}

}
