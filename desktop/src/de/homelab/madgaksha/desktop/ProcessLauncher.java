
package de.homelab.madgaksha.desktop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

import de.homelab.madgaksha.DebugMode;
import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.GameParameters;
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
	private final static Object lock = new Object();
	private final static Logger LOG = LoggerFactory.getLogger(Process.class);
	public static void main(String[] args) {
		if (DebugMode.activated) {
			try {
				System.setIn(new FileInputStream(DebugMode.debugConfiguration));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return;
			}
		}
	    try {
			final GameParameters params = readParams();
			if (!params.isDeserializedSuccessfully()) System.exit(-1);
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
					// Let the libGdx thread awaken this thread again.
					synchronized (lock) {
						lock.notify();
					}
				}
			});
			// Wait until the game finishes.
			synchronized(lock) {
				lock.wait();
			}
			// Make sure the game has been closed down properly and
			// all cleaning up to be done has been done.
			lwjglApplication.stop();
			// Exit normally.
			System.exit(0);
	    }
		catch (Exception e) {
			LOG.log(Level.SEVERE, "failed to launch game", e);
			System.exit(-1);
		}
	}
	
	private static LwjglApplicationConfiguration getConfig(GameParameters params) {
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

		// Sanitize dimensions and fps.
		if (config.width < 1) config.width = 320;
		if (config.height < 1) config.width = 240;
		if (config.foregroundFPS < 1) config.width = 30;

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

	private static GameParameters readParams() {
		final Json json = new Json();
		// Json#fromJson uses UTF-8 encoding, which we used in
		// in IGameParameters#toByteArray to encode the json string.
		return json.fromJson(GameParameters.class, System.in);
	}	
}