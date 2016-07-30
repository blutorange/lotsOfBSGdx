package de.homelab.madgaksha.lotsofbs.util.interpolator;

import com.badlogic.gdx.utils.Timer;

public abstract class AInterpolator<T> {
	private final static float DEFAULT_TIMER_INTERVAL = 0.050f;
	protected T start;
	protected T end;
	protected final Object options;

	public AInterpolator() {
		options = null;
		setRange(getDefaultStart(), getDefaultStart());
	}

	public AInterpolator(Object o) {
		options = o;
		setRange(getDefaultStart(), getDefaultStart());
	}

	public AInterpolator(T a, T b) {
		options = null;
		setRange(a, b);
	}

	public AInterpolator(T a, T b, Object o) {
		options = o;
		setRange(a, b);
	}

	public void setRange(T a, T b) {
		start = a;
		end = b;
		doSetup(options);
	}

	/**
	 * @return Default value to be used for the starting value.
	 */
	protected abstract T getDefaultStart();

	/**
	 * @return Default value to be used for the ending value.
	 */
	protected abstract T getDefaultEnd();

	/**
	 * Additional logic for setting up the interpolator, start and end values
	 * are already saved. Does not need to be overwritten when there is no setup
	 * to perform. Start and end values can be accessed from the fields
	 * {@see AInterpolator#start} and {@see AInterpolator#end}.
	 * 
	 * @param options
	 *            Optional options given to the constructor. May be null.
	 */
	protected void doSetup(Object options) {
	}

	/**
	 * Performs the interpolation.
	 * 
	 * @param x
	 *            Position between start and end. Clipped to [0.0..1.0].
	 * @return The interpolated value.
	 */
	protected abstract T doInterpolate(double x);

	public T interpolate(double x) {
		return doInterpolate(x < 0.0d ? 0.0d : x > 1.0d ? 1.0d : x);
	}

	/**
	 * Performs the interpolation.
	 * 
	 * @param x
	 *            Position between start and end. Clipped to [0.0..1.0].
	 * @return The interpolated value.
	 */
	protected T doInterpolate(float x) {
		return doInterpolate(x);
	}

	public T interpolate(float x) {
		return doInterpolate(x < 0.0f ? 0.0f : x > 1.0f ? 1.0f : x);
	}

	private class InterpolatorTimerTask extends Timer.Task {
		private float pos = 0.0f;
		private final float step;
		private final IInterpolatorCallback<T> callback;

		public InterpolatorTimerTask(float s, IInterpolatorCallback<T> cb) {
			step = s;
			callback = cb;
		}

		@Override
		public void run() {
			pos += step;
			T val = interpolate(pos);
			if (pos >= 1.0f) {
				callback.finished(end);
				cancel();
			} else {
				callback.valueUpdated(val);
			}
		}
	}

	/**
	 * Runs the interpolator in discrete time steps and calls the given callback
	 * for each step. Update time interval is set to the default time interval.
	 * 
	 * @param time
	 *            Time for the transition from the starting to the ending value.
	 * @param cb
	 *            Callback that gets called when a new value has been
	 *            interpolated.
	 */
	public Timer.Task run(float time, IInterpolatorCallback<T> cb) {
		return run(time, DEFAULT_TIMER_INTERVAL, cb);
	}

	public Timer.Task run(float time, float dt, IInterpolatorCallback<T> cb) {
		final Timer timer = new Timer();
		return run(timer, time, dt, cb);
	}

	/**
	 * Runs the interpolator in discrete time steps and calls the given callback
	 * for each step.
	 * 
	 * @param time
	 *            Time in seconds for the transition from the starting to the
	 *            ending value.
	 * @param dt
	 *            Update time interval in seconds.
	 * @param cb
	 *            Callback that gets called when a new value has been
	 *            interpolated.
	 * @return The timer used for calling the callback.
	 */
	public Timer.Task run(Timer timer, float time, float dt, IInterpolatorCallback<T> cb) {
		float step = dt / time;
		final Timer.Task task = new InterpolatorTimerTask(step, cb);
		timer.scheduleTask(task, dt, dt, (int) (time / dt) * 2); // Make sure it
																	// won't
																	// keep
																	// going
																	// forever.
		return task;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(this.getClass().getSimpleName()).append("<").append(start.toString())
				.append("::").append(end.toString()).append(">").toString();
	}

}
