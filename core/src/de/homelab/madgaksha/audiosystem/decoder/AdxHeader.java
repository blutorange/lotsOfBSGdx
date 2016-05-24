package de.homelab.madgaksha.audiosystem.decoder;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.homelab.madgaksha.util.MoreMathUtils;

public class AdxHeader implements Serializable {
	/**
	 * V1
	 */
	private static final long serialVersionUID = 1L;
	private final EncodingType encodingType;
	private final Version version;
	private final int headerByteCount;
	private final int channelCount;
	private final long sampleRate; // in Hz.
	private final int sampleBitDepth; // in bit
	private final int highPassFrequency; // in Hz
	private final int blockSize; // in bytes
	private final long totalSamples; // number of samples of ONE channel

	private final boolean loopEnabled;
	private final long loopBeginSampleIndex;
	private final long loopBeginByteIndex;
	private final long loopEndSampleIndex;
	private final long loopEndByteIndex;

	@Override
	public String toString() {
		return new StringBuilder().append(this.getClass().getSimpleName())
				.append("<EncondingType=").append(encodingType.toString())
				.append(";BlockSize=").append(blockSize)
				.append(";SampleBitDepth=").append(sampleBitDepth)
				.append(";ChannelCount=").append(channelCount)
				.append(";SampleRate=").append(sampleRate)
				.append(";TotalSamples=").append(totalSamples)
				.append(";HighpassFrequency=").append(highPassFrequency)
				.append(";Version=").append(version)
				.append(";LoopEnabled=").append(loopEnabled)
				.append(";LoopBeginSampleIndex=")
				.append(loopBeginSampleIndex)
				.append(";LoopBeginByteIndex=").append(loopBeginByteIndex)
				.append(";LoopEndSampleIndex=").append(loopEndSampleIndex)
				.append(";LoopEndByteIndex=").append(loopEndByteIndex)
				.append(">").toString();
	}

	public enum Version {
		ADX3_OTHER_DECODER, ADX3, ADX4, ADX4_NO_LOOP;
		public static Version getType(int b)
				throws UnsupportedAudioFileException {
			switch (b) {
			case 0x02:
				return ADX3_OTHER_DECODER;
			case 0x03:
				return ADX3;
			case 0x04:
				return ADX4;
			case 0x05:
				return ADX4_NO_LOOP;
			default:
				throw new UnsupportedAudioFileException(
						"unknown version flag: " + String.valueOf(b));
			}
		}
	}

	public enum EncodingType {
		FIXED_COEFFICIENT_ADPCM, ADX_STANDARD, ADX_EXPONENTIAL, AHX;
		public static EncodingType getType(int b)
				throws UnsupportedAudioFileException {
			switch (b) {
			case 0x02:
				return FIXED_COEFFICIENT_ADPCM;
			case 0x03:
				return ADX_STANDARD;
			case 0x04:
				return ADX_EXPONENTIAL;
			case 0x10:
			case 0x11:
				return AHX;
			default:
				throw new UnsupportedAudioFileException(
						"unknown encoding type: " + String.valueOf(b));
			}
		}
	}

	/**
	 * Constructs a new ADX header from the input stream containing
	 * information about the audio file such as sample rate etc.
	 * 
	 * @param is
	 *            InputStream with the raw adx byte data.
	 * @throws UnsupportedAudioFileException
	 *             When the header contains invalid data.
	 * @throws IOException
	 *             When data could not be read from the input stream.
	 */
	public AdxHeader(final DataInputStream dis)
			throws UnsupportedAudioFileException, IOException {
		final short magic = dis.readShort();
		final int copyRightOffset = MoreMathUtils.signedToUnsigned(dis
				.readShort());
		encodingType = EncodingType.getType(MoreMathUtils.signedToUnsigned(dis
				.readByte()));
		blockSize = MoreMathUtils.signedToUnsigned(dis.readByte());
		sampleBitDepth = MoreMathUtils.signedToUnsigned(dis.readByte());
		channelCount = MoreMathUtils.signedToUnsigned(dis.readByte());
		sampleRate = (int) MoreMathUtils.signedToUnsigned(dis.readInt());
		totalSamples = (int) MoreMathUtils.signedToUnsigned(dis.readInt()); // per channel!!
		highPassFrequency = (int) dis.readShort();
		version = Version
				.getType(MoreMathUtils.signedToUnsigned(dis.readByte()));
		final int flags = MoreMathUtils.signedToUnsigned(dis.readByte());

		headerByteCount = copyRightOffset + 4;
		
		// Now at offset 0x14.
		// Loop info follows next, but it is optionally.
		// If these fields are present is determined by
		// the copyRightOffset, the copyright info starts
		// at (copyRightOffset-2).
		final int copyRightStart = copyRightOffset - 2;
		int bytesToCopyRight = 0;

		if (copyRightStart < 0x38 || version == Version.ADX4_NO_LOOP) {
			// No loop data present.
			if (copyRightStart < 0x14)
				throw new UnsupportedAudioFileException(
						"invalid copyright offset");
			// Default to non-looped.
			loopEnabled = false;
			loopBeginSampleIndex = -1;
			loopBeginByteIndex = -1;
			loopEndSampleIndex = -1;
			loopEndByteIndex = -1;
			// Bytes until the copyright string.
			bytesToCopyRight = copyRightStart - 0x14;
		} else {
			dis.readInt(); // unknown value(s)

			// Now at offset 0x18
			switch (version) {
			case ADX3_OTHER_DECODER:
			case ADX3:
				loopEnabled = dis.readInt() != 0;
				loopBeginSampleIndex = MoreMathUtils.signedToUnsigned(dis
						.readInt());
				loopBeginByteIndex = MoreMathUtils.signedToUnsigned(dis
						.readInt());
				loopEndSampleIndex = MoreMathUtils.signedToUnsigned(dis
						.readInt());
				loopEndByteIndex = MoreMathUtils
						.signedToUnsigned(dis.readInt());
				// Now at 0x2c, skip padding.
				// Copyright string starts at
				// copyRightOffset - 2.
				bytesToCopyRight = copyRightOffset - 2 - 0x2c;
				break;
			case ADX4_NO_LOOP:
			case ADX4:
				dis.skipBytes(4 * 3);
				loopEnabled = dis.readInt() != 0;
				loopBeginSampleIndex = MoreMathUtils.signedToUnsigned(dis
						.readInt());
				loopBeginByteIndex = MoreMathUtils.signedToUnsigned(dis
						.readInt());
				loopEndSampleIndex = MoreMathUtils.signedToUnsigned(dis
						.readInt());
				loopEndByteIndex = MoreMathUtils
						.signedToUnsigned(dis.readInt());
				// Now at 0x38, skip padding.
				// Copyright string starts at
				// copyRightOffset - 2.
				bytesToCopyRight = copyRightOffset - 2 - 0x38;
				break;
			default:
				throw new UnsupportedAudioFileException(
						"unknown adx version");
			}
		}

		if (bytesToCopyRight < 0)
			throw new UnsupportedAudioFileException(
					"invalid copyright offset");

		// Skip bytes so that we are before
		// the copyright string.
		dis.skipBytes(bytesToCopyRight);

		// Skip the copyright string "(c)CRI"
		dis.skipBytes(6);

		// Check if the 4CC is set correctly.
		if (magic != -32768)
			throw new UnsupportedAudioFileException("wrong magic number "
					+ String.valueOf(magic) + " for adx, expected 0x8000");

		// Check we are not dealing with encrypted ADX files.
		if (flags == 0x08)
			throw new UnsupportedAudioFileException(
					"encryption not supported");

		// Check we have got mono or stereo files.
		if (getChannelCount() != 1 && getChannelCount() != 2)
			throw new UnsupportedAudioFileException(
					"only mono and stereo audio files supported");

		// Check loop data for sanity.
		if (loopEnabled)  {
			if (loopEndSampleIndex <= loopBeginSampleIndex) {
				throw new UnsupportedAudioFileException(
						"invalid adx header: loop end before loop begin");
			}
			if (loopBeginSampleIndex < 0) {
				throw new UnsupportedAudioFileException(
						"invalid adx header: loop begin negative");
			}
			if (loopEndSampleIndex > totalSamples) {
				throw new UnsupportedAudioFileException(
						"invalid adx header: loop end beyond eof");
			}
			if (loopEndSampleIndex - loopBeginSampleIndex <= (8 * (blockSize - 2)) / sampleBitDepth)
				throw new UnsupportedAudioFileException(
						"bad adx header: loop period not longer than block sample size");
		}			
	}

	public EncodingType getEncodingType() {
		return encodingType;
	}

	public Version getVersion() {
		return version;
	}

	public int getChannelCount() {
		return channelCount;
	}

	public long getSampleRate() {
		return sampleRate;
	}

	public int getSampleBitDepth() {
		return sampleBitDepth;
	}

	public int getHighPassFrequency() {
		return highPassFrequency;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public long getTotalSamples() {
		return totalSamples;
	}

	public boolean isLoopEnabled() {
		return loopEnabled;
	}

	public long getLoopBeginSampleIndex() {
		return loopBeginSampleIndex;
	}

	public long getLoopEndSampleIndex() {
		return loopEndSampleIndex;
	}

	public long getLoopBeginFrameIndex() {
		return getLoopBeginSampleIndex()/getSamplesInBlock();
	}

	public long getLoopEndFrameIndex() {
		return getLoopEndSampleIndex()/getSamplesInBlock();
	}

	public int getSamplesInBlock() {
		return (8 * (getBlockSize() - 2)) / getSampleBitDepth();
	}

	public long getFrameCount() {
		return getTotalSamples() / getSamplesInBlock();
	}
	
	public int getSamplesInFrame() {
		return getSamplesInBlock()*getChannelCount();
	}
	
	public int getFrameByteCount() {
		return getBlockSize()*getChannelCount();
	}

	/**
	 * Returns the audio format the decoder will deliver.
	 * ADX is decoded to one sample of each channel
	 * per frame. 
	 * @param sampleSizeInBits Sample size of the pcm data. Usually 2.
	 * @return The audio format delivered by the decoder.
	 */
	public AudioFormat getAudioFormat(int sampleSizeInBits) {
		final AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		final AudioFormat af = new AudioFormat(
				encoding,
				getSampleRate(),
				sampleSizeInBits,
				getChannelCount(),
				sampleSizeInBits	* getChannelCount() / 8, getSampleRate(),
				true);
		return af;
	}

	/**
	 * @return The number of bytes of the data section.
	 */
	public long getDataByteCount() {
		return getTotalSamples() * getSampleBitDepth() * getChannelCount() / 8;
	}
	
	public int getHeaderByteCount() {
		return headerByteCount;
	}

	/**
	 * When looping, the loop begin sample may not
	 * lie on the beginning of a new frame.
	 * This returns how many samples it is away from
	 * the beginning of the frame.
	 * @return Number of samples from the new frame to the loop begin sample.
	 */
	public long getLoopBeginFrameOffset() {
		return getLoopBeginSampleIndex() - getLoopBeginFrameIndex()*getSamplesInBlock();
	}
}