package de.homelab.madgaksha.util;

import org.apache.commons.lang3.ArrayUtils;

import de.homelab.madgaksha.logging.Logger;

public final class MiscUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MiscUtils.class);
	
	public MiscUtils() {}
	
	public static <T> T[] reverseCopyArray(T[] array) {
		T[] a = ArrayUtils.clone(array);
		ArrayUtils.reverse(a);
		return a;
	}
}
