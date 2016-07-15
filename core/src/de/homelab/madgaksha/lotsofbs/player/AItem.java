package de.homelab.madgaksha.lotsofbs.player;

import com.badlogic.gdx.math.Vector3;

public abstract class AItem implements IMapItem {
	private Vector3 mapAxisOfRotation = null;
	private Float mapAngularVelocity;
	
	@Override
	public Vector3 getMapAxisOfRotation() {
		return mapAxisOfRotation == null ? getDefaultAxisOfRotation() : mapAxisOfRotation;
	}
	@Override
	public float getMapAngularVelocity() {
		return mapAngularVelocity == null ? getDefaultAngularVelocity() : mapAngularVelocity;
	}	
	public void setMapAxisOfRotation(Vector3 axis) {
		mapAxisOfRotation = axis == null ? null : new Vector3(axis);
	}
	public void setMapAngularVelocity(Float angularVelocity) {
		this.mapAngularVelocity = angularVelocity;
	}
	/**
	 * Can be overridden for other values.
	 * 
	 * @see IMapItem#getActivationAreaRadius()
	 */
	@Override
	public float getActivationAreaRadius() {
		return 32*8;
	}
}
