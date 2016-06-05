package de.homelab.madgaksha.enums;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;

import de.homelab.madgaksha.cutscenesystem.textbox.EFaceSet;
import de.homelab.madgaksha.cutscenesystem.textbox.EFaceVariation;
import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.resourcecache.ETexture;

public enum ESpeaker {
	JOSHUA("Joshua", Color.BLUE, EFaceSet.JOSHUA),
	ESTELLE("Estelle", Color.ORANGE, EFaceSet.ESTELLE);
	
	private String name;
	private Color color;
	private EFaceSet faceSet;
	
	private ESpeaker(String name, Color color, EFaceSet faceSet) {
		this.name = StringUtils.isEmpty(name) ? StringUtils.EMPTY : I18n.character(name);
		this.color = color;
		this.faceSet = faceSet;
	}
	
	public String getName() {
		return name;
	}
	public Color getColor() {
		return color;
	}
	public ETexture getFaceVariation(EFaceVariation variation) {
		return faceSet.getVariation(variation);
	}

	public boolean hasName() {
		return !name.isEmpty();
	}
	public boolean hasFaceSet() {
		return faceSet != null;
	}
	public boolean hasFaceVariation(EFaceVariation variation) {
		return faceSet.hasVariation(variation);
	}
}
