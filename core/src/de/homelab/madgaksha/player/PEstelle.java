package de.homelab.madgaksha.player;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.IResource;

public class PEstelle extends APlayer {

	@Override
	protected IResource[] requestedRequiredResources() {
		return new IResource[] {EAnimationList.ESTELLE_STANDING};
	}
	
	@Override
	public EAnimationList requestedAnimationList() {
		return EAnimationList.ESTELLE_STANDING;
	}

	@Override
	public float requestedMovementSpeed() {
		return 200.0f;
	}

	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f, 0.0f, 36.0f);
	}

	@Override
	protected Rectangle requestedBoundingBox() {
		return new Rectangle(-18.0f, -18.0f, 36.0f, 36.0f);
	}

}
