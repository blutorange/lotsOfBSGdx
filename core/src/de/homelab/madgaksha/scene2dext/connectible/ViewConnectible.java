package de.homelab.madgaksha.scene2dext.connectible;

public interface ViewConnectible<T> {
	public void setValue(T value);
	public void onValueChange(ModelConnectibleRequest<T> model);
}