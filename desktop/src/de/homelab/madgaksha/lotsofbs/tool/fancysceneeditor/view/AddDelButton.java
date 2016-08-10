package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AddDelButton extends ImageButton {
	public AddDelButton (final boolean add, final Skin skin) {
		this(skin.get(add ? "add" : "del", AddDelButtonStyle.class));
	}
	public AddDelButton (final AddDelButtonStyle style) {
		super(style);
	}
	static public class AddDelButtonStyle extends ImageButtonStyle {
	}
}
