package de.homelab.madgaksha.common;

import javax.naming.InsufficientResourcesException;

public interface IdProvidable {
	public String uniqueId() throws InsufficientResourcesException;
}
