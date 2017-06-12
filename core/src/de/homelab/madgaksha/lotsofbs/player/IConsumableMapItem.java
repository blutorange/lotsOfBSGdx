package de.homelab.madgaksha.lotsofbs.player;


import javax.annotation.Nullable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

/**
 * Used by {@link ItemMaker} for methods common to weapons, tokugis, and
 * consumables.
 *
 * @author madgaksha
 *
 */
public interface IConsumableMapItem extends IMapItem {
	/** Callback for what happens when this consumable is used. */
	public void usedItem();

	/** @return Whether this consumable item can be used right now. */
	public boolean isConsumableNow();

	/** @return Sound player when this item is consumed. */
	@Nullable
	public ESound requestedSoundOnConsumption();

	/**
	 * @return Sound player when this item is used/activated. There may be some
	 *         delay before the item is consumed.
	 */
	@Nullable
	public ESound requestedSoundOnUse();

	/**
	 * Delay between pressing the button and the effect taking place, in
	 * seconds.
	 */
	public float requestedDelayOnConsumption();
}
