package de.homelab.madgaksha.util;

import java.io.IOException;

/**
 * Interface for reading bits. It returns the bits as
 * an Integer, with the lowest n-bits set that were read.
 * 
 * For example, if the three bytes 1, 0, and 1 were read, it would
 * return the integer 0b101 = 5.
 * 
 * @author mad_gaksha
 *
 */
public interface IBitInputStream {
	/**
	 * @return The next bit from the stream, or -1 if the
	 * end of the stream has been reached.
	 * @throws IOException When the stream could not be read.
	 */
	public int readBit() throws IOException;
	/**
	 * 
	 * @param n The number of bits to read.
	 * @return The bits read from the stream as the lowest
	 * bits of an integer. Returns -1 if the end of
	 * the stream has been reached.
	 * @throws IOException When the stream could not be read.
	 */
	public int readNBits(int n) throws IOException;
}
