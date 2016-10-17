package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.interpolation;

import java.io.IOException;
import java.lang.reflect.Modifier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelInterpolation;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.DrawablePropertyEditor;
import de.homelab.madgaksha.scene2dext.util.TableUtils;

public abstract class AInterpolation implements ModelInterpolation {
	/**
	 * A list of all available interpolations.
	 */
	private final static Array<InterpolationEntry> interpolationArray;
	static {
		interpolationArray = new Array<>();
		try {
			final ClassPath cp = ClassPath.from(DrawablePropertyEditor.class.getClassLoader());
			for (final ClassInfo ci : cp.getTopLevelClasses(InterpolationNone.class.getPackage().getName())) {
				final Class<?> c = ci.load();
				final int mods = c.getModifiers();
				if (ModelInterpolation.class.isAssignableFrom(c) && !Modifier.isInterface(mods) && !Modifier.isAbstract(mods)) {
					@SuppressWarnings("unchecked")
					final Class<? extends ModelInterpolation> cmi = (Class<? extends ModelInterpolation>)c;
					interpolationArray.add(new InterpolationEntry(cmi));
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private Table table;
	private SelectBox<InterpolationEntry> sbInterpolation;
	private Cell<?> cellInterpolation;

	@Override
	public final Actor getActor(final Skin skin) {
		return table != null ? table : createActor(skin);
	}

	private Table createActor(final Skin skin) {
		table = new Table(skin);

		sbInterpolation = new SelectBox<InterpolationEntry>(skin);
		sbInterpolation.setItems(interpolationArray);

		TableUtils.heading(table, "Interpolation", skin);
		table.row();
		table.add(sbInterpolation).expandX().fillX();
		table.row();
		TableUtils.heading(table, "Details", skin);
		table.row();
		cellInterpolation = table.add().expand().fill();

		sbInterpolation.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if (!(actor instanceof SelectBox)) return;
				final SelectBox<?> sbObject = (SelectBox<?>)actor;
				final Object selectedObject = sbObject.getSelected();
				if (!(selectedObject instanceof InterpolationEntry)) return;
				final InterpolationEntry selectedEntry = (InterpolationEntry)selectedObject;

			}
		});
		return table;
	}

	private static class InterpolationEntry {
		private final Class<? extends ModelInterpolation> clazz;
		private final String simpleName;
		public InterpolationEntry(final Class<? extends ModelInterpolation> clazz) {
			this.clazz = clazz;
			simpleName = clazz.getSimpleName();
		}
		@Override
		public String toString() {
			return simpleName;
		}
		public static InterpolationEntry findEntry(final ModelInterpolation ipol, final Array<InterpolationEntry> array) {
			final Class<? extends ModelInterpolation> clazz = ipol.getClass();
			for (final InterpolationEntry entry : array) {
				if (entry.clazz.equals(clazz))
					return entry;
			}
			return null;
		}
	}
}
