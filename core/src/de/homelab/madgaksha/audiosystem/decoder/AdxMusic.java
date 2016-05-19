package de.homelab.madgaksha.audiosystem.decoder;


import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.BitInputStream;
import de.homelab.madgaksha.util.IBitInputStream;

public class AdxMusic implements Music, Runnable {

	private final Object lock = new Object();
	
	private final static Logger LOG = Logger.getLogger(AdxMusic.class);
	
	/**
	 * Waiting time in ms the {@link #run()} thread sleeps when not
	 * playing music before checking again. 
	 */
	private final static long IDLE_WAIT = 50;
	
	/**
	 * Number of samples written each loop in the main thread.
	 */
	private final static int WRITE_PER_CYCLE = ((int)IDLE_WAIT*44100)/1000;
	
	/**
	 * The bit depth for the decoded pcm audio. Decoder needs to be changed when
	 * changing this value.
	 */
	public static final int SAMPLE_SIZE_IN_BITS = 16;

	/**
	 * Thread on which the audio decoder is running and writes data to the pcm device.
	 */
	private Thread thread;
	
		
	/**
	 * Decoded data, used for buffered files.
	 */
	private short[] data;
	/**
	 * Handle to the adx file.
	 */
	private FileHandle fileHandle;
	private InputStream adxInputStream;
	private volatile InputStream adxInputStream2 = null;
	private final boolean bufferedAdx;
	public AdxHeader adxHeader;
	
	// Variables for the main thread.
	private volatile int setPosRequested;
	private volatile boolean setPlayingRequested;
	private volatile boolean stopRequested;
	private volatile int position;
	private volatile boolean running;
	private volatile boolean playing;
	private volatile OnCompletionListener onCompletionListener = null;
	private volatile float volume = 1.0f;
	private AudioDevice audioDevice;
	
	
	// Local variables used inside the
	// decoding loop.
	private int __Coefficient1;
	private int __Coefficient2;
	private int __Scale;
	private int __BlockSize;
	private int __BlockSizeM2;
	private int __ChannelCount;
	private int __SamplesInBlock;
	private int __SamplesInFrame;
	private byte[][] __BufferList;
	private AAdxBlock[] __AdxBlockList;
	private int __SamplesToSkip;
	private int __SamplesRead;
	private int __LoopBeginSampleIndex;	
	private int __LoopBeginSampleIndexM1;
	private int __LoopEndSampleIndex;
	private int[] __LoopSampleM1List;
	private int[] __LoopSampleM2List;
	private int __DecodedSample;
	private int __inputPosition;
	private int __outputPosition;

	
	/**
	 * Current position in samples per channel.
	 * This does not include the channel count.
	 * When there are 2 channels and 5 samples
	 * have been read from each channel,
	 * __SamplePosition will be 5, not 10. 
	 */
	private int __SamplePosition;


	

	/**
	 * Represents an adx block from which
	 * data can be decoded to pcm samples.
	 * @author mad_gaksha
	 * @see AdxBlock4Bit
	 * @see AdxBlockNBit
	 */
	private abstract class AAdxBlock {
		protected byte[] buffer; // bytes of one block
		protected final int coeff1; // prediction coefficient 1
		protected final int coeff2; // prediction coefficient 2
		protected int sampleCur; // current sample
		protected int sampleM1; // last sample (Minus1)
		protected int sampleM2; // second to last sample (Minus2)
		protected int scale; // scale factor for samples
		protected int pos; // position in buffer array

		public AAdxBlock(int c1, int c2) {
			coeff1 = c1;
			coeff2 = c2;
			sampleM1 = 0;
			sampleM2 = 0;
		}

		/**
		 * Change the buffer containing the
		 * data to be decoded. It will usually
		 * represent one frame of adx data.
		 * @param b Buffer with the data to be decoded.
		 * @param s Scaling factor for this frame.
		 * @param p Position at which the data begins. The first read byte is b[p].
		 */
		public void swapBuffer(byte[] b, int s, int p) {
			buffer = b;
			pos = p;
			scale = s; // 2 byte unsigned int
		}

		public abstract int nextDecodedSample();
		
		public int getSampleM1() {
			return sampleM1;
		}
		public int getSampleM2() {
			return sampleM2;
		}

		public void setSampleM1(int m1) {
			sampleM1 = m1;
		}
		public void setSampleM2(int m2) {
			sampleM2 = m2;
		}
	}

	/**
	 * Class representing an ADX block. Once instance is created for each
	 * channel
	 * 
	 * @author mad_gaksha
	 * 
	 */
	private class AdxBlock4Bit extends AAdxBlock {
		private int error; // undecoded current sample
		private boolean state; // reading the high or low bits of the byte?
		private int prediction;// predicted value for current sample

		public AdxBlock4Bit(int c1, int c2) {
			super(c1, c2);
			state = true;
		}

		@Override
		public void swapBuffer(byte[] b, int scale, int pos) {
			super.swapBuffer(b, scale, pos);
			state = true;
		}

		public int nextDecodedSample() {
			// read 4 bits as unsigned
			if (state) {
				error = ((buffer[pos] & 0xF0) >> 4); //0b11110000
				state = false;
			} else {
				error = buffer[pos] & 0x0F; //0b00001111
				++pos;
				state = true;
			}
			// convert to signed
			error -= 2 * (error & 0x08); //0b1000
			// Computed the current sample by predicting
			// it from the previous two samples
			// and apply the error correction that
			// is stored in the current sample.
			prediction = (coeff1 * sampleM1 + coeff2 * sampleM2) >> 12;
			sampleCur = (int) (prediction) + error * scale;
			// Clamp sample to the range of a short.
			if (sampleCur < -0x8000)
				sampleCur = -0x8000;
			else if (sampleCur > 0x7FFF)
				sampleCur = 0x7FFF;
			// Update previous two samples.
			sampleM2 = sampleM1;
			sampleM1 = sampleCur;
			return sampleCur;
		}
	}

	/**
	 * Class representing an ADX block. Once instance is created for each
	 * channel
	 * 
	 * @author mad_gaksha
	 * 
	 */
	private class AdxBlockNBit extends AAdxBlock {
		private final int bitDepth; // bits per sample
		private int error; // undecoded current sample
		private int prediction; // predicted value for current sample
		private int fullway; // for converting
		private int halfway; // to signed values
		private IBitInputStream bis; // for reading the bits of each sample

		/**
		 * Creates a new adx block reader with the given
		 * bit-depth.
		 * @param c1 Coefficient 1 for the previous sample.
		 * @param c2 Coefficient 2 for the second to last sample.
		 * @param bd Bit-depth. Usually 4, but use {@link AdxBlock4Bit} for that.
		 */
		public AdxBlockNBit(int c1, int c2, int bd) {
			super(c1, c2);
			bitDepth = bd;
			halfway = (1 << (bitDepth - 1));
			fullway = halfway * 2;
		}

		public void swapBuffer(byte[] b, int s, int p) {
			super.swapBuffer(b, s, p);
			bis = new BitInputStream(new ByteArrayInputStream(b));
		}

		public int nextDecodedSample() {
			try {
				// Read n bits from the input stream
				// as an n-bit unsigned integer.
				error = bis.readNBits(bitDepth);
				// Convert to a signed integer.
				if (error >= halfway)
					error -= fullway;
			} catch (IOException e) {
				// Cannot happen as we are reading from
				// a byte array.
				LOG.error("exception occured during read from byte array", e);
			}
			// Computed the current sample by predicting
			// it from the previous two samples
			// and apply the error correction that
			// is stored in the current sample.
			prediction = (coeff1 * sampleM1 + coeff2 * sampleM2) >> 12;
			sampleCur = (int) (prediction) + error * scale;
			// Clamp sample to the range of a short.
			if (sampleCur < -0x8000)
				sampleCur = -0x8000;
			else if (sampleCur > 0x7FFF)
				sampleCur = 0x7FFF;
			// Update previous two samples.
			sampleM2 = sampleM1;
			sampleM1 = sampleCur;
			return sampleCur;
		}
	}
	
	/**
	 * Decodes the audio data and writes the pcm data to the given short array.
	 * 
	 * @param dis
	 *            Input stream positioned at the beginning of the data section
	 *            of the adx file.
	 * @param adxHeader
	 *            Format info of this adx file.
	 * @param dos
	 *            Output stream to write the data to.
	 * @return
	 */
	private void decodeAdxAudio(DataInput di, short[] output) throws UnsupportedAudioFileException, IOException {
		switch (adxHeader.getEncodingType()) {
		case FIXED_COEFFICIENT_ADPCM:
			throw new UnsupportedAudioFileException("unsupported encoding: "
					+ String.valueOf(adxHeader.getEncodingType()));
		case ADX_EXPONENTIAL:
			// TODO
			// ???
			throw new UnsupportedAudioFileException("unsupported encoding: "
					+ String.valueOf(adxHeader.getEncodingType()));
		case ADX_STANDARD:
			// Setup decoding.
			prepareAdxStandardDecoding();
			// Get decoded pcm byte count.
			int samplesInFrame = adxHeader.getSamplesInFrame();
			final int frameByteCount = adxHeader.getFrameByteCount();
			final int frameCount = (int)adxHeader.getFrameCount();
			//final int framePcmByteCount = adxHeader.getSamplesInFrame()*SAMPLE_SIZE_IN_BITS/8;
			//final int dataPcmByteCount = (int)(adxHeader.getFrameCount()*framePcmByteCount);
			final byte[] input = new byte[frameByteCount];
			// Decode the adx file.
			__outputPosition = -1;
			//for (int i = 0; i != dataPcmByteCount; i += framePcmByteCount) {
			for (int i = 0; i != frameCount; ++i) {
				di.readFully(input, 0, frameByteCount);
				__inputPosition = -1;
				if (samplesInFrame != decodeAdxStandardFrame(input,output))
					throw new UnsupportedAudioFileException("error while reading adx frames");
			}
			break;
		case AHX:
			// TODO
			// mpeg
			throw new UnsupportedAudioFileException("unsupported encoding: "
					+ String.valueOf(adxHeader.getEncodingType()));
		}
	}
	
	/**
	 * Sets up all temporary variables needed for the decoding loop.
	 */
	private void prepareAdxStandardDecoding() {

		computePredictionCoefficients();
		
		__SamplePosition = 0;
		__SamplesToSkip = 0;
		__BlockSize = adxHeader.getBlockSize();
		__BlockSizeM2 = __BlockSize - 2;
		__ChannelCount = adxHeader.getChannelCount();
		__SamplesInBlock = adxHeader.getSamplesInBlock();
		__SamplesInFrame = adxHeader.getSamplesInFrame();
		
		__BufferList = new byte[__ChannelCount][__BlockSizeM2];
		__AdxBlockList = new AAdxBlock[__ChannelCount];

		__LoopSampleM1List = new int[__ChannelCount];
		__LoopSampleM2List = new int[__ChannelCount];
		__LoopBeginSampleIndex = (int)adxHeader.getLoopBeginSampleIndex();
		if (adxHeader.getLoopBeginSampleIndex() == 0) {
			__LoopBeginSampleIndexM1 = 0;
		}
		else {
			__LoopBeginSampleIndexM1 = (int)adxHeader.getLoopBeginSampleIndex() - 1;
		}
		__LoopEndSampleIndex = (int)adxHeader.getLoopEndSampleIndex();
		
		if (adxHeader.getSampleBitDepth() != 4) {
			// TODO
			// May have to add faster implementation for
			// bit depth 8, 16 if those occur often enough.
			LOG.info("adx bit depth is "
					+ String.valueOf(adxHeader.getSampleBitDepth())
					+ ", using slower bitstream fallback");
		}
		
		for (int i = 0; i != __ChannelCount; ++i) {
			__BufferList[i] = new byte[__BlockSizeM2];
			if (adxHeader.getSampleBitDepth() == 4) {
				__AdxBlockList[i] = new AdxBlock4Bit(__Coefficient1, __Coefficient2);
			} else {
				__AdxBlockList[i] = new AdxBlockNBit(__Coefficient1, __Coefficient2,
						adxHeader.getSampleBitDepth());
			}
		}		
	}

	/**
	 * The prediction coefficients for
	 * {@link audiosystem.file.BufferedAdxAudioFile.AdxHeader.EncodingType#ADX_STANDARD}
	 */
	private void computePredictionCoefficients() {
		final double a = Math.sqrt(2.0d)
				- Math.cos(2.0d
						* Math.PI
						* (((double) adxHeader.getHighPassFrequency()) / ((double) adxHeader
								.getSampleRate())));
		final double b = Math.sqrt(2.0d) - 1.0d;
		// (a+b)*(a-b) = a*a-b*b, however the simpler formula loses accuracy in
		// floating point
		final double c = (a - Math.sqrt((a + b) * (a - b))) / b;
		// int trick from http://wiki.multimedia.cx/index.php?title=CRI_ADX_ADPCM
		// fixed point values with 12 fractional bits
		// about 10 % faster
		__Coefficient1 = (int)Math.floor(c*8192);//c * 2.0;
		__Coefficient2 = (int)Math.floor(c*c*-4096);//-(c * c);
	}

	/**
	 * Reads one adx frame and stores the decoded
	 * pcm data in the given byte array, starting at
	 * the given offset.
	 * 
	 * The variables __outputPosition and __inputPosition must be set
	 * to one byte before the actual byte to write/read.
	 * 
	 * @param input Array from which data is read.
	 * @param output Array for storing the decoded pcm samples.
	 * Must be big enough to hold one frame.
	 * @return Number of samples from all channels actually read. 0 when the frame could not be read.
	 */
	private int decodeAdxStandardFrame(byte[] input, short[] output) {
		int j,k;
		// Load one block from each channel into memory.
		for (j = 0; j != __ChannelCount; ++j) {
			// Read scale and interpret as unsigned.
			__Scale = ((input[++__inputPosition] << 8) | (input[++__inputPosition] & 0xff));
			if (__Scale < 0)
				__Scale += 65536;
			// Read one data block.
			__AdxBlockList[j].swapBuffer(input, __Scale, __inputPosition+1);
			__inputPosition += __BlockSizeM2;
		}
		// When looping or seeking, we need to
		// skip some samples to reach the desired
		// stream position.
		for (j = 0; j != __SamplesToSkip; ++j) {
			for (k = 0; k != __ChannelCount; ++k) {
				__AdxBlockList[k].nextDecodedSample();
			}
		}		
		__SamplePosition += __SamplesToSkip;

		// Handle loop end.
		// Write only the number of samples remaining
		// till the end of the loop and return it.
		if (!bufferedAdx && __LoopEndSampleIndex >= __SamplePosition && __LoopEndSampleIndex <__SamplePosition + __SamplesInBlock) {
			int jEnd = __LoopEndSampleIndex - __SamplePosition + 1;
			for (j = 0; j != jEnd; ++j) {
				for (k = 0; k != __ChannelCount; ++k) {
					// We output 16-bit samples big-endian
					// Change this if you change the
					// SAMPLE_SIZE_IN_BITS global constant
					__DecodedSample = __AdxBlockList[k].nextDecodedSample();
					output[++__outputPosition] = (short)__DecodedSample;
				}
				__SamplePosition += 1;
			}
			// Do not loop when reading data into RAM!
			// Reset the state and return the samples
			// decoded thus far.
			if (!rewindToLoopBegin())
				return 0;
			else 
				// Return the number of samples
				// we read from this frame.
				return jEnd * __ChannelCount;
		}
				
		// Write one sample of each channel per frame.
		// Adx contains many samples of each channel
		// per block.
		for (j = __SamplesToSkip; j != __SamplesInBlock; ++j) {
			for (k = 0; k != __ChannelCount; ++k) {
				// We output 16-bit samples big-endian
				// Change this if you change the
				// SAMPLE_SIZE_IN_BITS global constant
				__DecodedSample = __AdxBlockList[k].nextDecodedSample();
				output[++__outputPosition] = (short)__DecodedSample;
			}
			
			// Handle loop begin.
			//
			// The AAdxBlock has already updated the last
			// previous samples, so that sampleM2 now
			// points to the sample we just decoded in
			// this iteration. We therefore save the
			// last two samples one iteration earlier.
			if (__SamplePosition == __LoopBeginSampleIndexM1) {
				// Save last two computed samples so that
				// we can load them later when rewinding.
				for (k = 0; k != __ChannelCount; ++k) {
					__LoopSampleM1List[k] = __AdxBlockList[k].getSampleM1();
					__LoopSampleM2List[k] = __AdxBlockList[k].getSampleM2();
				}
			}
			// Advance sample counter.
			__SamplePosition += 1;
		}

		__SamplesRead = __SamplesInFrame - __SamplesToSkip * __ChannelCount;
		__SamplesToSkip = 0;
		return __SamplesRead;
	}
	
	protected synchronized void setStopRequested(boolean b) {
		stopRequested = b;
		
	}

	protected synchronized void setNextAdxInputStream(InputStream stream) {
		adxInputStream2 = stream;
	}

	/**
	 * Resets the decoder state to the given playback position.
	 * NOTE: This does not reset the previously decoded samples, this
	 * must be handled separately by the method calling this
	 * method, as it is impossible to do so generally without
	 * decoding from the start of the file to this position.
	 * @param frame The frame to seek to.
	 * @param samplesToSkip The samples to skip from the frame.
	 * @return Whether the operation was successful.
	 */
	private boolean setDecoderState(int frame, int samplesToSkip) {
		// Adjust current position in the stream.
		__SamplePosition = frame*adxHeader.getSamplesInBlock();
		// The number of samples to discard from the beginning
		// of the frame we rewind to.
		__SamplesToSkip = samplesToSkip;
		// Perform the seek.
		__inputPosition = adxHeader.getHeaderByteCount() + frame * adxHeader.getFrameByteCount() - 1;
		return true;
	}
	private boolean rewindToBeginning() {
		// Set previous samples for decoding.
		for (int i = 0; i != __ChannelCount; ++i) {
			__AdxBlockList[i].setSampleM1(0);
			__AdxBlockList[i].setSampleM2(0);
		}
		return setDecoderState(0, 0);
	}
	private boolean rewindToLoopBegin() {
		// Set previous samples for decoding.
		for (int i = 0; i != __ChannelCount; ++i) {
			__AdxBlockList[i].setSampleM1(__LoopSampleM1List[i]);
			__AdxBlockList[i].setSampleM2(__LoopSampleM2List[i]);
		}
		return setDecoderState((int)adxHeader.getLoopBeginFrameIndex(), (int)adxHeader.getLoopBeginFrameOffset());
	}
	
	
	//
	// Methods for creating a new instance.
	//
	
	/**
	 * 
	 * @param file Adx file to open.
	 * @param buffered Whether it should be buffered or streamed.
	 * @throws GdxRuntimeException When the file could not be found.
	 * @throws UnsupportedAudioFileException When the file contains invalid data.
	 * @throws IOException When an error occurred while reading the file.
	 */
	private AdxMusic(FileHandle file, boolean buffered) throws GdxRuntimeException, UnsupportedAudioFileException, IOException {
		// Default values.
		thread = null;
		setPlayingRequested = false;
		setPosRequested = -1;
		stopRequested = false;
		position = 0;
		running = false;
		playing = false;
		onCompletionListener = null;
		volume = 1.0f;
		
		bufferedAdx = buffered;
		fileHandle = file;
		
		InputStream is = null;
		try {
			is = file.read();
			adxHeader = new AdxHeader(new DataInputStream(is));
		}
		finally {
			if (is != null) is.close();
		}
		
		// Setup a new audio device for playback.
		audioDevice = Gdx.audio.newAudioDevice((int) adxHeader.getSampleRate(),adxHeader.getChannelCount() == 1);
		
		// Start the main thread for playing the audio. 
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Static factory method for creating a new instance.
	 * @param file The file to open.
	 * @param buffered Whether the file should be buffered (= preloaded to RAM) or streamed.
	 * @return A reference to the opened file, or null if it could not be opened.
	 */
	public static AdxMusic newAdxMusic(FileHandle file, boolean buffered) {
		try {
			return new AdxMusic(file, buffered);
		}
		catch (Exception e) {
			LOG.error("could not open adx file " + file.toString(), e);
			return null;
		}
	}
	
	/**
	 * Prepares the decoding so that it can be played.
	 */
	public void prepareDecoding() throws IOException, UnsupportedAudioFileException {
		if (bufferedAdx) {
			// Decode all data and write to array.
			//data = new short[(int)adxHeader.getTotalSamples() * adxHeader.getChannelCount() * SAMPLE_SIZE_IN_BITS / 8];
			InputStream is = null;
			DataInput dis = null;
			try {
				is = fileHandle.read();
				// Seek to the audio data.
				is.skip(adxHeader.getHeaderByteCount());
				dis = new DataInputStream(is);
				decodeAdxAudio(dis, data);
			}
			finally {
				if (is != null) is.close();
			}
		}
		else {
			// Open file in random access mode and prepare for streaming.
			adxInputStream = null;
			try {
				adxInputStream = fileHandle.read();
				// Load file to RAM.
				adxInputStream.skip(adxHeader.getHeaderByteCount());
				// Setup decoding.
				prepareAdxStandardDecoding();
			}
			catch (Exception e) {
				LOG.error("failed to seek to adx data position start", e);
				adxInputStream = null;
			}
		}
	}
	
	public void run() {	
		
		LOG.debug("creating new thread for adx");
		
		running = true;
					
		try {
			// Setup decoding, temporary variables etc.

			// For buffered files, we decode the entire audio to 2 byte
			// pcm samples and load it to RAM.
			// We decode in another thread so that playback can
			// start as soon as possible.
			if (bufferedAdx) {
				data = new short[(int)adxHeader.getTotalSamples() * adxHeader.getChannelCount() * SAMPLE_SIZE_IN_BITS / 8];
				new Thread(new Runnable() {				
					@Override
					public void run() {
						try {
							prepareDecoding();
						} catch (Exception e) {
							dispose(); // stop play back
							LOG.error("could not decode adx stream",e);
						}
					}
				}).start();
				// Wait a split second to give the decoding thread
				// some time to write the first samples.
				// When we cannot get samples in time, the playback
				// thread will play back silence - the data array gets
				// initialized to 0.
				synchronized (lock) {
					lock.wait(33);
				}
			}
			// For streamed adx files, we read and decode audio data on
			// the fly. First we need to open the input stream and skip
			// to the initial position where the audio data starts.
			else {
				prepareDecoding();
				//TODO check if adxInputStream is null
			}
			
			// Play the audio.
			if (bufferedAdx) {
				int inc;
				final int channelCount = adxHeader.getChannelCount();
				//final int samples = adxHeader.getSamplesInFrame()*adxHeader.getChannelCount();
				final int totalSamples = (int)adxHeader.getTotalSamples()*adxHeader.getChannelCount();
				final int loopStart = adxHeader.isLoopEnabled() ? (int)adxHeader.getLoopBeginSampleIndex() * adxHeader.getChannelCount() : -1;
				final int loopEnd = adxHeader.isLoopEnabled() ? (int)adxHeader.getLoopEndSampleIndex() * adxHeader.getChannelCount() : totalSamples + 1;
				//final int remSamplesInFrame = ((int)adxHeader.getLoopBeginFrameIndex() + 1) * adxHeader.getSamplesInFrame() - (int)adxHeader.getLoopBeginSampleIndex();
				
				position = 0;
				while (!stopRequested && position < totalSamples) {
					// write some pcm data to the device when active
					if (playing) {
						// how many samples we can write, less when the loop point is near
						inc = WRITE_PER_CYCLE * channelCount; 
						if (position + inc > loopEnd) inc = loopEnd - position;
						if (position + inc >= totalSamples) inc = totalSamples - position - channelCount;
						// write the actual audio samples
						audioDevice.writeSamples(data, position, inc);
						position += inc;
						// check if we are at the end of the loop
						if (position == loopEnd) {
							LOG.debug("adx looping");
							position = loopStart;
						}
						playing = setPlayingRequested;
					}
					// sleep when we are not required
					else {
						LOG.debug("adx thread going to sleep");
						synchronized(lock) {
							while (!setPlayingRequested && !stopRequested) lock.wait(IDLE_WAIT);
						}
						if (setPlayingRequested) playing = true;
						LOG.debug("adx thread awakened");
					}
					// seek to the requested position when asked to
					if (setPosRequested != -1) {
						position = setPosRequested;
						setPosRequested = -1;
					}
				}
			} else if (adxInputStream != null) {
				//Streaming mode.
				int inc;
				final int frameByteCount = adxHeader.getFrameByteCount();
				final int framesPerCycle = 350;
				final byte[] input = new byte [frameByteCount];
				final short[] output = new short[__SamplesInFrame*framesPerCycle];
				
				final int channelCount = adxHeader.getChannelCount();
				final int totalSamples = (int)adxHeader.getTotalSamples()*adxHeader.getChannelCount();
				final int loopStart = adxHeader.isLoopEnabled() ? (int)adxHeader.getLoopBeginSampleIndex() * adxHeader.getChannelCount() : -1;
				final int loopEnd = adxHeader.isLoopEnabled() ? (int)adxHeader.getLoopEndSampleIndex() * adxHeader.getChannelCount() : totalSamples + 1;

				while (!stopRequested && position < totalSamples) {
					// write some pcm data to the device when active
					if (playing) {
						// how many samples we can write, less when the loop point is near
						inc = __SamplesInFrame*framesPerCycle; 
						// prepare new input stream for looping
						if (position<= loopStart && position+inc>loopStart) {
							LOG.debug("preparing new adx file handle for looping");
							// Prepare new input stream for next loop.
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										// Open new file handle.
										final InputStream stream = fileHandle.read();
										// Skip header.
										stream.skip(adxHeader.getHeaderByteCount());
										// Skip to loop begin.
										stream.skip(adxHeader.getLoopBeginFrameIndex()*adxHeader.getFrameByteCount());
										setNextAdxInputStream(stream);
										LOG.debug("new adx file for looping prepared");
										}
									catch (IOException e) {
										LOG.error("failed to open new adx stream, stop looping", e);
										setStopRequested(true);
									}
								}
							}).start();
						}
						if (position + inc > loopEnd) inc = loopEnd - position;
						if (position + inc >= totalSamples) inc = totalSamples - position - channelCount;
						// read some samples...
						// ...decode some samples...
						__outputPosition = -1;
						for (int i=0; i!= framesPerCycle; ++i) {
							__inputPosition = -1;
							adxInputStream.read(input, 0, frameByteCount);
							decodeAdxStandardFrame(input, output);
						}
						// ...and output them
						audioDevice.writeSamples(output, 0, inc);
						position += inc;
						// check if we are at the end of the loop
						if (position == loopEnd) {						
							LOG.debug("adx looping");
							// Close old file handle as it does not
							// support random access.
							StreamUtils.closeQuietly(adxInputStream);

							// The new input stream should be ready by a long time
							// now. If it is not, grant a short grace period,
							// and if it still is not available, kill it.
							if (adxInputStream2 == null) {
								synchronized(lock) {
									lock.wait(200L);
								}
							}
							adxInputStream = adxInputStream2;
							adxInputStream2 = null;
							if (adxInputStream != null) {
								position = loopStart;
							}
							else {
								LOG.error("could not seek to the adx loop start position");
								stopRequested = true;
							}
						}
						playing = setPlayingRequested;
					}
					// sleep when we are not required
					else {
						LOG.debug("adx thread going to sleep");
						synchronized(lock) {
							while (!setPlayingRequested && !stopRequested) lock.wait(IDLE_WAIT);
						}
						if (setPlayingRequested) playing = true;
						LOG.debug("adx thread awakened");
					}
					// seek to the requested position when asked to
					if (setPosRequested != -1) {
						LOG.error("seek unsupported on non-buffered adx streams");
						setPosRequested = -1;
					}
				}
				
			}
		} catch (Exception e) {
			LOG.error("error while playing back adx", e);
		}
		
		// Cleanup.
		running = false;
		playing = false;
		LOG.debug("disposing audio device");
		audioDevice.dispose();
		audioDevice = null;
		synchronized (lock) {
			if (adxInputStream != null) StreamUtils.closeQuietly(adxInputStream);
			if (adxInputStream2 != null) StreamUtils.closeQuietly(adxInputStream2);			
		}
		if (onCompletionListener != null) onCompletionListener.onCompletion(this);
		
		LOG.debug("thread for adx finished");
	}
	
	//
	// Methods for the Music interface
	//
	
	@Override
	public synchronized void play() {
		setPlayingRequested = true;
	}

	@Override
	public synchronized void pause() {
		setPlayingRequested = false;
	}

	@Override
	public synchronized void stop() {
		pause();
		setPosition(0.0f);
	}

	@Override
	public boolean isPlaying() {
		return playing && running;
	}

	@Override
	public void setLooping(boolean isLooping) {
		// TODO Easy to implement, not needed now.
		LOG.info("setLooping not supported for adx music");
	}

	@Override
	public boolean isLooping() {
		return adxHeader.isLoopEnabled();
	}

	@Override
	public synchronized void setVolume(float volume) {
		if (audioDevice != null) audioDevice.setVolume(this.volume = volume);
	}

	@Override
	public synchronized float getVolume() {
		return (audioDevice == null) ? 0.0f : volume;
	}

	@Override
	public void setPan(float pan, float volume) {
		// TODO Unsupported
		LOG.info("setPan not supported for adx music");
	}

	@Override
	public synchronized void setPosition(float position) {
		if (position == 0.0f) setPosRequested = 0;
		else setPosRequested = (int)(position*adxHeader.getSampleRate()*adxHeader.getChannelCount());
	}

	@Override
	public float getPosition() {
		return (float)position/((float)adxHeader.getChannelCount()*(float)position);
	}

	@Override
	public synchronized void dispose() {
		LOG.debug("disposing adx");
		pause();
		stopRequested = true;
		try {
			if (thread != null) {
				thread.join(100);
				if (thread.isAlive()) {
					LOG.error("could not join adx thread after 100ms wait");
				}
			}
		} catch (InterruptedException e) {
			LOG.error("could not join adx thread", e);
		}
		synchronized (lock) {
			if (adxInputStream != null) StreamUtils.closeQuietly(adxInputStream);
			if (adxInputStream2 != null) StreamUtils.closeQuietly(adxInputStream2);			
		}
		// Make GC easier.
		thread = null;
		data = null;
		fileHandle = null;		
	}
	
	@Override
	public synchronized void setOnCompletionListener(OnCompletionListener listener) {
		onCompletionListener = listener;
	}

}