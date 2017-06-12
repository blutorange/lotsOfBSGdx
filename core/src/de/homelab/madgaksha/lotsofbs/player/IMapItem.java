package de.homelab.madgaksha.lotsofbs.player;

import javax.annotation.Nullable;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ComponentQueueComponent;
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
	public final static IMapItem MOCK = new IMapItem() {
		@Override
		public void setup(final Entity e, final MapProperties props, final ComponentQueueComponent queueForAcquisition) {
		}
		@Override
		public void setMapAxisOfRotation(final Vector3 axis) {
		}
		@Override
		public void setMapAngularVelocity(final Float angularVelocity) {
		}
		@Override
		public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
			return null;
		}
		@Override
		public boolean isSupportedByPlayer(final APlayer player) {
			return false;
		}
		@Override
		public boolean initialize() {
			return true;
		}
		@Override
		public void gotItem() {
		}
		@Override
		public EModel getModel() {
			return EModel.ITEM_WEAPON_BASIC;
		}
		@Override
		public Vector3 getMapAxisOfRotation() {
			return Vector3.X;
		}
		@Override
		public float getMapAngularVelocity() {
			return 0;
		}
		@Override
		public Vector3 getDefaultAxisOfRotation() {
			return Vector3.X;
		}

		@Override
		public float getDefaultAngularVelocity() {
			return 0;
		}
		@Override
		public float getActivationAreaRadius() {
			return 1;
		}
	};

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
	public void setup(Entity e, MapProperties props, ComponentQueueComponent queueForAcquisition);

	/** @return The axis around which the item should rotate. */
	public Vector3 getDefaultAxisOfRotation();
	/** @return The axis of rotation as specified by the level's map, or the default when none was specified. */
	public Vector3 getMapAxisOfRotation();

	public void setMapAngularVelocity(@Nullable Float angularVelocity);
	public void setMapAxisOfRotation(@Nullable Vector3 axis);


	/** @return The angular velocity at which the item should rotate. */
	public float getDefaultAngularVelocity();
	/** @return The angular velocity as specified by the level's map, or the default when none was specified. */
	public float getMapAngularVelocity();

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
	public float getActivationAreaRadius();

	public boolean initialize();

	/** Called when the player gets this item. */
	public void gotItem();

	/** Resources required by this item. */
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources();
}
