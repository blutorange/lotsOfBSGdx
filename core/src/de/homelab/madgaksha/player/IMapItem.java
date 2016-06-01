package de.homelab.madgaksha.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.resourcecache.EModel;

/**
 * Used by {@link ItemMaker} for methods common to weapons, tokugis, and consumables.
 * @author madgaksha
 *
 */
public interface IMapItem {
	public EModel getModel();
	public void setup(Entity e);
	public Vector3 getDefaultAxisOfRotation();
	public float getDefaultAngularVelocity();
	public boolean initialize();
	public boolean isSupportedByPlayer(APlayer player);
}
