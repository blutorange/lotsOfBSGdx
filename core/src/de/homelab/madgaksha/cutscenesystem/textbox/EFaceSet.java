package de.homelab.madgaksha.cutscenesystem.textbox;

import java.util.EnumMap;

import de.homelab.madgaksha.resourcecache.ETexture;

public enum EFaceSet {
	ESTELLE() {
		@Override
		protected void fillMap() {
			faceMap.put(EFaceVariation.NORMAL, ETexture.FACE_ESTELLE_01);
			faceMap.put(EFaceVariation.LAUGHING, ETexture.FACE_ESTELLE_02);
			faceMap.put(EFaceVariation.SERIOUS, ETexture.FACE_ESTELLE_03);
			faceMap.put(EFaceVariation.EVASIVE, ETexture.FACE_ESTELLE_04);
			faceMap.put(EFaceVariation.SUPRISED, ETexture.FACE_ESTELLE_05);
			faceMap.put(EFaceVariation.ANGRY, ETexture.FACE_ESTELLE_06);
			faceMap.put(EFaceVariation.CONFIDENT, ETexture.FACE_ESTELLE_07);
			faceMap.put(EFaceVariation.SHOU_GA_NAI, ETexture.FACE_ESTELLE_08);
			faceMap.put(EFaceVariation.EMBARRASED, ETexture.FACE_ESTELLE_09);
			faceMap.put(EFaceVariation.SULLEN, ETexture.FACE_ESTELLE_10);
		}
	},
	JOSHUA() {
		@Override
		protected void fillMap() {
			
		}
	};
	
	protected EnumMap<EFaceVariation,ETexture> faceMap;
	protected abstract void fillMap();
	private EFaceSet() {
		faceMap = new EnumMap<EFaceVariation, ETexture>(EFaceVariation.class);
		fillMap();
	}
	public ETexture getVariation(EFaceVariation variation) {
		return faceMap.get(variation);
	}
	public boolean hasVariation(EFaceVariation variation) {
		return faceMap.containsKey(variation);
	}
}
