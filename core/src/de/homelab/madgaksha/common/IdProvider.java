package de.homelab.madgaksha.common;

import javax.naming.InsufficientResourcesException;

public class IdProvider implements IdProvidable {
	private long currentId;
	private final String prefix;

	public IdProvider() {
		this("id");
	}

	public IdProvider(final String prefix) {
		if (prefix == null)
			throw new NullPointerException("prefix cannot be null");
		this.prefix = prefix;
	}

	@Override
	public String uniqueId() throws InsufficientResourcesException {
		if (currentId == Long.MAX_VALUE)
			throw new InsufficientResourcesException("too many IDs, cannot generate more");
		return prefix + Long.toString(currentId++, Character.MAX_RADIX);
	}
}
