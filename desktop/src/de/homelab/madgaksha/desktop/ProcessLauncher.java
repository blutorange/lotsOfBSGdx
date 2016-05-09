
package de.homelab.madgaksha.desktop;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.AGameParameters;
import de.homelab.madgaksha.logging.LoggerFactory;

/**
 * Based upon http://stackoverflow.com/a/31120304/3925216
 *  Adding multiple windows in LibGDX?
 *  
 * The game itself consists of one level, the desktop launcher is
 * responsible for selecting a level and launching the lwglj game.
 * Having more than one OpenGL context per thread does not seem to
 * work well, so we need to start each {@link LwjglApplication} as
 * a separate process.
 * 
 * @author madgaksha
 *
 */
public class ProcessLauncher {
	private final static Logger LOG = LoggerFactory.getLogger(Process.class);
	public static void main(String[] args) {
	    try {
			final AGameParameters params = readParams();
			final LwjglApplicationConfiguration config = getConfig(params);
			final Game game = new Game(params);
			final LwjglApplication lwjglApplication = new LwjglApplication(game,config);
			lwjglApplication.addLifecycleListener(new LifecycleListener() {
				
				@Override
				public void resume() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void pause() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void dispose() {
					// TODO Auto-generated method stub
				}
			});
	    }
		catch (Exception e) {
			LOG.log(Level.SEVERE, "failed to launch game", e);
			System.exit(-1);
		}
	    System.exit(0);
	}
	
	private static LwjglApplicationConfiguration getConfig(AGameParameters params) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 5;
		config.foregroundFPS = params.requestedFps;
		config.fullscreen = params.requestedFullscreen;
		config.width = params.requestedWidth;
		config.height = params.requestedHeight;
		config.title = params.requestedWindowTitle;
		config.resizable = true;
		config.x = -1;
		config.y = -1;
		config.forceExit = false;
		config.allowSoftwareMode = false;

		// Set sane values or otherwise 100% CPU is used for the ADX
		// audio decoding thread when calling
		// com.badlogic.gdx.audio.AudioDevice#writeSamples.
		//
		// This is because the OpenALAudioDevice sleeps with a call to
		//
		//   Thread.sleep((long)(1000 * secondsPerBuffer / bufferCount));
		//
		// For the default value (512 and 9), this gives a sleeping time
		// of 0, resulting in 100% CPU usage.
		//
		// This assumes the sampling rate will not be higher than 48000/s
		// and the file will not contain more than 2 channels.
		config.audioDeviceBufferSize = 2048;
		config.audioDeviceBufferCount = 9;
		
		return config;
	}

	private static AGameParameters readParams() {
		final Json json = new Json();
		// Json#fromJson uses UTF-8 encoding, which we used in
		// in IGameParameters#toByteArray to encode the json string.
		return json.fromJson(AGameParameters.class, System.in);
	}
}