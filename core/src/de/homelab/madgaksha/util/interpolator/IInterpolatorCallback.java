package de.homelab.madgaksha.util.interpolator;

public interface IInterpolatorCallback<T> extends IInterpolatorFinished {
	public void valueUpdated(T val);
	public void finished(T val);
}
