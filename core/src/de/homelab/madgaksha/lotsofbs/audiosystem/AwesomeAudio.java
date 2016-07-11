package de.homelab.madgaksha.lotsofbs.audiosystem;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StreamUtils;

import de.homelab.madgaksha.lotsofbs.audiosystem.decoder.AdxMusic;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Contains logic for registering decoders for other audio file formats than
 * those supported by libGdx. The {@link #newMusic(FileHandle)} checks for the
 * file type and calls the appropriate decoder.
 * 
 * In order to use this, it must first be setup with the {@link #initialize()}
 * method, which will overwrite the value of #{@link Gdx#audio} and save a
 * reference of the old value for internal use. After that, a call to
 * <code>Gdx.audio.newMusic("myMusic.adx")</code> will do all the magic.
 * 
 * @author madgaksha
 *
 */
public class AwesomeAudio implements Audio {

	private final static Logger LOG = Logger.getLogger(AwesomeAudio.class);
	private static Audio oldAudio = null;

	private AwesomeAudio() {
	}

	public static void initialize() {
		if (oldAudio != null)
			return; // we only need to initialize once
		oldAudio = Gdx.audio;
		Gdx.audio = new AwesomeAudio();
	}

	@Override
	public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
		return oldAudio.newAudioDevice(samplingRate, isMono);
	}

	@Override
	public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
		return oldAudio.newAudioRecorder(samplingRate, isMono);
	}

	@Override
	public Sound newSound(FileHandle fileHandle) {
		return oldAudio.newSound(fileHandle);
	}

	@Override
	public Music newMusic(FileHandle file) {
		// Need to extract adx files to local storage for streaming playback.
		try {
			if (file.extension().equalsIgnoreCase("adx")) {
				FileHandle dest = Gdx.files.local("tempadx" + File.separator + file.name());
				if (!dest.exists()) {
					LOG.debug("copying adx to local storage: " + file.name());
					OutputStream os = null;
					InputStream is = null;
					try {
						os = dest.write(false);
						is = file.read();
						StreamUtils.copyStream(is, os);
					} finally {
						if (os != null)
							StreamUtils.closeQuietly(os);
						if (is != null)
							StreamUtils.closeQuietly(is);
					}
				}
				return AdxMusic.newAdxMusic(dest, true);
			} else
				return oldAudio.newMusic(file);
		} catch (Exception e) {
			LOG.error("could not open music file", e);
			return null;
		}
	}
}
