package de.homelab.madgaksha.player;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;

public class PEstelle extends APlayer {

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>,?>[] requestedRequiredResources() {
		return new IResource[] {EAnimationList.ESTELLE_STANDING, ETexture.OVAL_SHADOW};
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
	protected Rectangle requestedBoundingBox() {
		return new Rectangle(-18.0f, -18.0f, 36.0f, 36.0f);
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

}
