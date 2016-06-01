package de.homelab.madgaksha.player.consumable;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.player.EConsumable;
import de.homelab.madgaksha.player.IMapItem;

public abstract class AConsumable implements IMapItem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AConsumable.class);
	
	private EConsumable type = null;
	
	public AConsumable() {
	}
	
	public void setType(EConsumable type) {
		this.type = type;
	}	

	@Override
	public boolean initialize() {
		return true;
	}
	
	@Override
	public void setup(Entity e) {			
	}

	/**
	 * Can be overridden for other values.
	 * @return The default axis of rotation around which the item model rotates.
	 */
	@Override
	public Vector3 getDefaultAxisOfRotation() {
		return new Vector3(1.0f,1.0f,0.0f);
	}

	/** Can be overridden for other values.
	 * @return The angular velocity at which the item model rotates.
	 */
	@Override
	public float getDefaultAngularVelocity() {
		return 45.0f;
	}
	
	@Override
	public boolean isSupportedByPlayer(APlayer player) {
		return type == null ? true : player.supportsConsumable(type);
	}
}