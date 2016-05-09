
package de.homelab.madgaksha.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

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

}
