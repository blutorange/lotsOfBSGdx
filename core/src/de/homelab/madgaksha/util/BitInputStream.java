package de.homelab.madgaksha.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wraps an InputStream for reading bits from the stream.
 * 
 * This intentionally violates the contract of the {@link InputStream} interface
 * that {@link InputStream#read()} must return the next byte. It only returns
 * the next bit.
 * 
 * @author mad_gaksha
 *
 */
public class BitInputStream extends InputStream implements IBitInputStream {

	private int currentByte;
	private int bytePos = 8;
	private int currentBit;
	private final InputStream is;
	private boolean eos;

	public BitInputStream(InputStream in) {
		is = in;
		eos = false;
	}

	@Override
	public int readBit() throws IOException {
		if (bytePos == 8) {
			currentByte = is.read();
			eos = currentByte == -1;
			bytePos = 0;
		}
		if (eos)
			return -1;
		currentBit = (currentByte & 0x80) >> 7;
		currentByte <<= 1;
		++bytePos;
		return currentBit;
	}

	@Override
	public int readNBits(int n) throws IOException {
		int result = readBit();
		for (int i = 1; i != n; ++i) {
			result = (result << 1) + readBit();
		}
		return eos ? -1 : result;
	}

	@Override
	public int read() throws IOException {
		return readBit();
	}
}