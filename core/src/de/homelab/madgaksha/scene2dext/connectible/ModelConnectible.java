package de.homelab.madgaksha.scene2dext.connectible;

public interface ModelConnectible<T> extends ModelConnectibleRequest<T> {
	public T getValue();
	public void forView(ViewConnectible<T> view);
	public void removeView(ViewConnectible<T> view);
}