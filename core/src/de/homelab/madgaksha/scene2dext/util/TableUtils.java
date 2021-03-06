package de.homelab.madgaksha.scene2dext.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class TableUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TableUtils.class);
	
	public static void labelledActor(Table table, String label, Actor actor) {
		final Label lb = new Label(label, table.getSkin());
		table.add(lb).uniformX().fillX().pad(2, 2, 2, 5);
		table.add(actor).uniformX().fillX().pad(2, 5, 2, 2);
	}

	public static Cell<? extends Actor> heading(Table table, CharSequence heading, Skin skin) {
		final Label label = new Label(heading, skin);
		label.setAlignment(Align.center);
		label.setFontScale(1.5f);
		return table.add(label).fillX();
	}
}
