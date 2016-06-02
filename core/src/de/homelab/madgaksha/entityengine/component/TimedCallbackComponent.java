package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.ITimedCallback;

/**
 * Component for hierarchical (tree) structure.
 * @author madgaksha
 *
 */
public class TimedCallbackComponent implements Component, Poolable {
	private final static float DEFAULT_TOTAL_TIME = 0.0f;
	private final static float DEFAULT_DURATION = 0.0f;
	private final static int DEFAULT_CALLBACKS_LEFT = 1;
	private final static ITimedCallback DEFAULT_TIMED_CALLBACK = new ITimedCallback() {
		@Override
		public void run(Entity entity, Object data) {}
	};
	private final static Object DEFAULT_CALLBACK_DATA = null;
	
	public ITimedCallback timedCallback = DEFAULT_TIMED_CALLBACK;
	public float totalTime = DEFAULT_TOTAL_TIME;
	public float duration = DEFAULT_DURATION;
	public int callbacksLeft = DEFAULT_CALLBACKS_LEFT;
	public Object callbackData = DEFAULT_CALLBACK_DATA;
	
	public TimedCallbackComponent(){
	}
	
	public TimedCallbackComponent(ITimedCallback timedCallback) {
		setup(timedCallback);
	}
	public TimedCallbackComponent(ITimedCallback timedCallback, Object callbackData) {
		setup(timedCallback, callbackData);
	}
	public TimedCallbackComponent(ITimedCallback timedCallback, float duration) {
		setup(timedCallback, duration);
	}
	public TimedCallbackComponent(ITimedCallback timedCallback, float duration, int numberOfCallbacks) {
		setup(timedCallback, duration, numberOfCallbacks);
	}
	public TimedCallbackComponent(ITimedCallback timedCallback, Object callbackData, float duration) {
		setup(timedCallback, callbackData, duration);
	}
	public TimedCallbackComponent(ITimedCallback timedCallback, Object callbackData, float duration, int numberOfCallbacks) {
		setup(timedCallback, callbackData, duration, numberOfCallbacks);
	}
	
	public void setup(ITimedCallback timedCallback) {
		setup(timedCallback, DEFAULT_CALLBACK_DATA, DEFAULT_DURATION, DEFAULT_CALLBACKS_LEFT);
	}
	public void setup(ITimedCallback timedCallback, Object callbackData) {
		setup(timedCallback, callbackData, 0.0f, DEFAULT_CALLBACKS_LEFT);
	}
	public void setup(ITimedCallback timedCallback, float duration) {
		setup(timedCallback, DEFAULT_CALLBACK_DATA, duration, DEFAULT_CALLBACKS_LEFT);
	}
	public void setup(ITimedCallback timedCallback, float duration, int numberOfCallbacks) {
		setup(timedCallback, DEFAULT_CALLBACK_DATA, duration, numberOfCallbacks);
	}
	public void setup(ITimedCallback timedCallback, Object callbackData, float duration) {
		setup(timedCallback, callbackData, duration, DEFAULT_CALLBACKS_LEFT);
	}
	public void setup(ITimedCallback timedCallback, Object callbackData, float duration, int numberOfCallbacks) {
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
	}}
