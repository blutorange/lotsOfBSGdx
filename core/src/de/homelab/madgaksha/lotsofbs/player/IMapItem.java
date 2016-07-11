package de.homelab.madgaksha.lotsofbs.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

/**
 * Used by {@link ItemMaker} for methods common to weapons, tokugis, and
 * consumables.
 * 
 * @author madgaksha
 *
 */
public interface IMapItem {
	/** @return The model for this item that will be displayed on the map. */
	public EModel getModel();

	/**
	 * Called once when the map is read.
	 * 
	 * @param e
	 *            Entity to be setup.
	 * @param props
	 *            Map properties for this item object.
	 */
	public void setup(Entity e, MapProperties props);

	/** @return The axis around which the item should rotate. */
	public Vector3 getDefaultAxisOfRotation();

	/** @return The angular velocity at which the item should rotate. */
	public float getDefaultAngularVelocity();

	public boolean isSupportedByPlayer(APlayer player);

	/**
	 * Once the player enters the active area of the item, the item starts to
	 * move towards the player, getting smaller the closer it gets. When it
	 * touches the player, the player receives the item. The active area of the
	 * item is the bounding box of the item's model, enlarged by this factor.
	 * 
	 * @return The factor by which the item's model bounding box is multiplied
	 *         to obtain the active area.
	 */
	public float getActivationAreaScaleFactor();

	public boolean initialize();

	/** Called when the player gets this item. */
	public void gotItem();

	/** Resources required by this item. */
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources();
}
