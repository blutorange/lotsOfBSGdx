package de.homelab.madgaksha.util;

public final class SignUtils {
	private SignUtils(){};
	
	public static int signedToUnsigned(final byte b) {
		return b < 0 ? b+256 : b;
	}

	public static int signedToUnsigned(final short s) {
		return s < 0 ? s+65536 : s;
	}
	
	public static long signedToUnsigned(final int s) {
		return s < 0 ? s+4294967296L : s;
	}
}