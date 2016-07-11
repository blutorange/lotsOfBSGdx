package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableSprite;

/**
 * The icon and details shown in the status screen when the enemy is targetted.
 * 
 * @author mad_gaksha
 */
public class EnemyIconComponent implements Component, Poolable {
	private final static CroppableSprite DEFAULT_ICON_MAIN = null;
	private final static CroppableSprite DEFAULT_ICON_SUB = null;

	public CroppableSprite iconMain = DEFAULT_ICON_MAIN;
	public CroppableSprite iconSub = DEFAULT_ICON_SUB;

	public EnemyIconComponent() {
	}

	public EnemyIconComponent(CroppableSprite main, CroppableSprite sub) {
		this.iconMain = main;
		this.iconSub = sub;
	}

	@Override
	public void reset() {
		iconMain = DEFAULT_ICON_MAIN;
		iconSub = DEFAULT_ICON_SUB;
	}
}
