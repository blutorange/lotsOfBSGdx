package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * The icon and details shown in the status screen when the enemy is targetted.
 * 
 * @author mad_gaksha
 */
public class EnemyIconComponent implements Component, Poolable {
	private final static Sprite DEFAULT_ICON_MAIN = null;
	private final static Sprite DEFAULT_ICON_SUB = null;

	public Sprite iconMain = DEFAULT_ICON_MAIN;
	public Sprite iconSub = DEFAULT_ICON_SUB;

	public EnemyIconComponent() {
	}
	public EnemyIconComponent(Sprite main, Sprite sub) {
		this.iconMain = main;
		this.iconSub = sub;
	}
	
	@Override
	public void reset() {
		iconMain = DEFAULT_ICON_MAIN;
		iconSub = DEFAULT_ICON_SUB;
	}
}
