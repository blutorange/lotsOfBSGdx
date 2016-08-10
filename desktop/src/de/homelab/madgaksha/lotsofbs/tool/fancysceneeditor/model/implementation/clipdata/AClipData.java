package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata;

import javax.naming.InsufficientResourcesException;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.IdProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimeInterval;

public abstract class AClipData implements ModelClipData, TimeInterval {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AClipData.class);
	private final ModelClip clip;

	protected AClipData(final ModelClip clip) {
		if (clip == null) throw new NullPointerException("clip cannot be null");
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

	@Override
	public ModelClip getObject() {
		return clip;
	}

	@Override
	public void setStartTime(final float startTime) {
		clip.setStartTime(startTime);
	}

	@Override
	public void setEndTime(final float endTime) {
		clip.setEndTime(endTime);
	}

	@Override
	public float getMinTime() {
		return clip.getMinTime();
	}

	@Override
	public float getMaxTime() {
		return clip.getMaxTime();
	}

	@Override
	public final void renderEvent(final StringBuilder builder, final IdProvider idProvider) throws InsufficientResourcesException {
		performRenderEvent(builder, idProvider);
	}

	@Override
	public final ModelClip getClip() {
		return clip;
	}

	protected abstract void performRenderEvent(StringBuilder builder, IdProvider idProvider) throws InsufficientResourcesException;
}
