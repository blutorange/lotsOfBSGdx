package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityCallback;

/**
 * 
 * 
 * @author madgaksha
 *
 */
public class TimedCallbackComponent implements Component, Poolable {
	public final static int UNLIMITED_NUMBER_OF_CALLBACKS = -1;
	private final static float DEFAULT_TOTAL_TIME = 0.0f;
	private final static float DEFAULT_DURATION = 0.0f;
	private final static int DEFAULT_CALLBACKS_LEFT = 1;
	private final static IEntityCallback DEFAULT_TIMED_CALLBACK = IEntityCallback.NOOP;
	private final static Object DEFAULT_CALLBACK_DATA = null;

	public IEntityCallback timedCallback = DEFAULT_TIMED_CALLBACK;

	/** Internal use. */
	public float totalTime = DEFAULT_TOTAL_TIME;

	/** Duration between callback in seconds. */
	public float duration = DEFAULT_DURATION;
	/**
	 * Number of times callbacks will be triggered. If negative, callback will
	 * be triggered indefinitely.
	 */
	public int callbacksLeft = DEFAULT_CALLBACKS_LEFT;
	/** Data that should be passed to the callback method. */
	public Object callbackData = DEFAULT_CALLBACK_DATA;

	public TimedCallbackComponent() {
	}

	public TimedCallbackComponent(IEntityCallback timedCallback) {
		setup(timedCallback);
	}

	public TimedCallbackComponent(IEntityCallback timedCallback, Object callbackData) {
		setup(timedCallback, callbackData);
	}

	public TimedCallbackComponent(IEntityCallback timedCallback, float duration) {
		setup(timedCallback, duration);
	}

	public TimedCallbackComponent(IEntityCallback timedCallback, float duration, int numberOfCallbacks) {
		setup(timedCallback, duration, numberOfCallbacks);
	}

	public TimedCallbackComponent(IEntityCallback timedCallback, Object callbackData, float duration) {
		setup(timedCallback, callbackData, duration);
	}

	public TimedCallbackComponent(IEntityCallback timedCallback, Object callbackData, float duration,
			int numberOfCallbacks) {
		setup(timedCallback, callbackData, duration, numberOfCallbacks);
	}

	public void setup(IEntityCallback timedCallback) {
		setup(timedCallback, DEFAULT_CALLBACK_DATA, DEFAULT_DURATION, DEFAULT_CALLBACKS_LEFT);
	}

	public void setup(IEntityCallback timedCallback, Object callbackData) {
		setup(timedCallback, callbackData, 0.0f, DEFAULT_CALLBACKS_LEFT);
	}

	public void setup(IEntityCallback timedCallback, float duration) {
		setup(timedCallback, DEFAULT_CALLBACK_DATA, duration, DEFAULT_CALLBACKS_LEFT);
	}

	public void setup(IEntityCallback timedCallback, float duration, int numberOfCallbacks) {
		setup(timedCallback, DEFAULT_CALLBACK_DATA, duration, numberOfCallbacks);
	}

	public void setup(IEntityCallback timedCallback, Object callbackData, float duration) {
		setup(timedCallback, callbackData, duration, DEFAULT_CALLBACKS_LEFT);
	}

	public void setup(IEntityCallback timedCallback, Object callbackData, float duration, int numberOfCallbacks) {
		this.totalTime = 0.0f;
		this.timedCallback = timedCallback;
		this.duration = duration;
		this.callbacksLeft = numberOfCallbacks;
		this.callbackData = callbackData;
	}

	@Override
	public void reset() {
		timedCallback = DEFAULT_TIMED_CALLBACK;
		duration = DEFAULT_DURATION;
		totalTime = DEFAULT_DURATION;
		callbacksLeft = DEFAULT_CALLBACKS_LEFT;
	}
}
