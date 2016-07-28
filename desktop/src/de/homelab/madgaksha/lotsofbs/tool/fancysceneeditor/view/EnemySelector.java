package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.enemy.SoldierRedMaker;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.EnemySelector.EnemyOption;

public class EnemySelector extends SelectBox<EnemyOption> {

	private final static Logger LOG = Logger.getLogger(EnemySelector.class);

	public EnemySelector(Skin skin) {
		this(skin.get(SelectBoxStyle.class));
	}

	public EnemySelector(Skin skin, String styleName) {
		this(skin.get(styleName, SelectBoxStyle.class));
	}

	public EnemySelector(SelectBoxStyle style) {
		super(style);
		init();
	}

	private void init() {
		setItems(getAvailableEnemies());
		loadSelectedEnemyOrDefault(SoldierRedMaker.getInstance());
		addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor instanceof EnemySelector) {
					final EnemySelector es = (EnemySelector)actor;
					es.loadSelectedEnemyOrDefault(SoldierRedMaker.getInstance());
				}
			}
		});
	}

	private void loadSelectedEnemyOrDefault(EnemyMaker defaultEnemy) {
		final EnemyOption opt = getSelected();
		final Entity enemy = new Entity();
		EnemyMaker enemyMaker;
		try {
			enemyMaker = opt.getInstance();
		}
		catch (NoSuchMethodException e) {
			LOG.debug("Failed to load enemy", e);
			setSelected(new EnemyOption(defaultEnemy.getClass()));
			enemyMaker = defaultEnemy;
		}
		enemyMaker.setup(enemy, new Rectangle(0f, 0f, 1f, 1f), new MapProperties(), ETrigger.MANUAL,
				Vector2.Zero, 0f, 32f);
		enemyMaker.callbackTrigger(enemy, ETrigger.MANUAL);
		cameraTrackingComponent.focusPoints = new ImmutableArray<Entity>(new Array<Entity>(new Entity[] { enemy }));
	}

	@SuppressWarnings("unchecked")
	private Array<EnemyOption> getAvailableEnemies() {
		final Array<EnemyOption> array = new Array<EnemyOption>();
		try {
			ClassPath cp = ClassPath.from(EnemySelector.class.getClassLoader());
			for (ClassInfo ci : cp.getTopLevelClasses(SoldierRedMaker.class.getPackage().getName())) {
				Class<?> c = ci.load();
				if (EnemyMaker.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
					array.add(new EnemyOption((Class<EnemyMaker>) c));
				}
			}
		} catch (IOException e) {
			LOG.debug("failed to read list of enemies", e);
		}
		return array;
	}

	public static class EnemyOption {
		private final Class<? extends EnemyMaker> enemyClass;

		public EnemyOption(Class<? extends EnemyMaker> enemyClass) {
			if (enemyClass == null)
				throw new NullPointerException("enemyClass cannot be null");
			this.enemyClass = enemyClass;
		}

		public EnemyMaker getInstance() throws NoSuchMethodException {
			Method m;
			try {
				m = enemyClass.getMethod("getInstance");
			} catch (SecurityException e) {
				throw new NoSuchMethodException("Could not get instance as security manager does not allow it.");
			}
			Object enemy;
			try {
				enemy = m.invoke(null);
			} catch (IllegalAccessException e) {
				throw new NoSuchMethodException("Failed to getInstance, enemy class does not specify correct method.");
			} catch (IllegalArgumentException e) {
				throw new NoSuchMethodException("Failed to getInstance, enemy class does not specify correct method.");
			} catch (InvocationTargetException e) {
				throw new NoSuchMethodException("Failed to getInstance, enemy class does not specify correct method.");
			}
			return (EnemyMaker)enemy;
		}

		@Override
		public String toString() {
			final String name = enemyClass.getSimpleName();
			return name.substring(0, Math.max(0, name.length()-5));
		}
	}

}
