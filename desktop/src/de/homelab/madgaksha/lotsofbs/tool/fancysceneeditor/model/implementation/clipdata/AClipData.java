package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.LifeSpanTeller;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;

public abstract class AClipData implements ModelClipData, LifeSpanTeller {
	private ModelClip clip;

	@Override
	public final void clipAttached(ModelClip clip) {
		if (clip != null)
			this.clip = clip;
	}

	@Override
	public final float getStartTime() {
		return clip.getStartTime();
	}

	@Override
	public final float getEndTime() {
		return clip.getEndTime();
	}
	
	public final ModelClip getClip() {
		return clip;
	}

}
