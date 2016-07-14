package de.homelab.madgaksha.lotsofbs.player;

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
	public ESound getSoundOnConsumption();

	/** Delay between pressing the button and the effect taking place, in seconds. */
	public float getDelayOnConsumption();
}
