package de.homelab.madgaksha.cutscenesystem;

import java.util.Scanner;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.logging.Logger;

public abstract class ACutsceneEvent implements Poolable {
	private final static Logger LOG = Logger.getLogger(ACutsceneEvent.class);
	
	
	/** @return Whether this cutscene event has finished and we may proceed to the next one */
	public abstract boolean isFinished();
	
	/**
	 * Called when this cutscene events needs to be rendered to the screen.
	 * @param deltaTime Time in seconds since the last call to render.
	 */
	public abstract void render();

	/**
	 * Called when this cutscene events needs to be updated.
	 * @param deltaTime Time in seconds since the last call to render.
	 */
	public abstract void update(float deltaTime);

	public abstract void resize(int width, int height);

	/**
	 * Called once when this cutscene starts.
	 * 
	 * @return Whether an error occurred and the cutscene event could not start.
	 *         If it could not start, this cutscene event will be skipped.
	 */
	public abstract boolean begin();
		
	/**
	 * 
	 * @param s Scanner to read from.
	 * @param eventClass Class of the cutscene event.
	 * @return The cutscene object read, or null if it could not be read.
	 */
	public static ACutsceneEvent readNextObject(Scanner s, Class<? extends ACutsceneEvent> eventClass) {
		try {
			Method m = ClassReflection.getDeclaredMethod(eventClass, "readNextObject", Scanner.class);
			ACutsceneEvent event = (ACutsceneEvent)m.invoke(null, s);
			if (event == null) {
				LOG.error("failed to read cutscene event: " + eventClass);
				s.nextLine();
			}
			return event;
		} catch (ReflectionException e) {
			LOG.error("cutscene event does not support configuration files", e);
			return null;
		}
	}
}
