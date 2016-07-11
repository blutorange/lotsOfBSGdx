package de.homelab.madgaksha.lotsofbs.util.interpolator;

public interface IInterpolatorCallback<T> extends IInterpolatorFinished {
	public void valueUpdated(T val);

	public void finished(T val);
}
