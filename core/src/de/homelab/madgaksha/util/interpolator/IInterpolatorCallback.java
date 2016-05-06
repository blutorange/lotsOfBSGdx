package de.homelab.madgaksha.util.interpolator;

public interface IInterpolatorCallback<T> {
	public void valueUpdated(T val);
	public void finished(T val);
}
