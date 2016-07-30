package de.homelab.madgaksha.lotsofbs.util;

public final class MoreMathUtils {
	private MoreMathUtils() {
	}

	public static int signedToUnsigned(final byte b) {
		return b < 0 ? b + 256 : b;
	}

	public static int signedToUnsigned(final short s) {
		return s < 0 ? s + 65536 : s;
	}

	public static long signedToUnsigned(final int s) {
		return s < 0 ? s + 4294967296L : s;
	}

	public static long pow(long x, long n) {
		long y = x;
		for (int i = 1; i != n; ++i)
			y *= x;
		return y;
	}

	public static void main(String[] args) {
		System.out.println(pow(10, 10));
	}
}