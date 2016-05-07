package de.homelab.madgaksha.util.interpolator;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AInterpolator<T> {
	private final static int DEFAULT_TIMER_INTERVAL = 50;
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
		setRange(a,b);
	}
	
	public AInterpolator(T a, T b, Object o) {
		options = o;
		setRange(a,b);
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
	 * Additional logic for setting up the interpolator, start and end values are already saved.
	 * Does not need to be overwritten when there is no setup to perform.
	 * Start and end values can be accessed from the fields {@see AInterpolator#start} and {@see AInterpolator#end}.
	 * @param options Optional options given to the constructor. May be null.
	 */
	protected void doSetup(Object options) {};
	
	/**
	 * Performs the interpolation.
	 * @param x Position between start and end. Clipped to [0.0..1.0].
	 * @return The interpolated value.
	 */
	protected abstract T doInterpolate(double x);	
	public T interpolate(double x) {
		return doInterpolate(x < 0.0d ? 0.0d : x>1.0d ? 1.0d : x);
	}
	/**
	 * Performs the interpolation.
	 * @param x Position between start and end. Clipped to [0.0..1.0].
	 * @return The interpolated value.
	 */
	protected T doInterpolate(float x) {
		return doInterpolate(x);
	}
	public T interpolate(float x) {
		return doInterpolate(x < 0.0f ? 0.0f : x>1.0f ? 1.0f : x);
	}
	
	private class InterpolatorTimerTask extends TimerTask {
		private double pos = 0.0d;
		private final double step;
		private final IInterpolatorCallback<T> callback;
		public InterpolatorTimerTask(double s, IInterpolatorCallback<T> cb) {
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
			}
			else {
				callback.valueUpdated(val);
			}
		}		
	}
	
	/**
	 * Runs the interpolator in discrete time steps and calls the given callback for each step.
	 * Update time interval is set to the default time interval.
	 * 
	 * @param time Time for the transition from the starting to the ending value.
	 * @param cb Callback that gets called when a new value has been interpolated.
	 */
	public void run(double time, IInterpolatorCallback<T> cb) {
		run(time,DEFAULT_TIMER_INTERVAL,cb);
	}
	
	/**
	 * Runs the interpolator in discrete time steps and calls the given callback for each step.
	 * 
	 * @param time Time for the transition from the starting to the ending value.
	 * @param dt Update time interval in milliseconds.
	 * @param cb Callback that gets called when a new value has been interpolated.
	 */
	public void run(double time, long dt, IInterpolatorCallback<T> cb) {
		double step = (double)dt/time;
		final TimerTask task = new InterpolatorTimerTask(step, cb);
		final Timer timer = new Timer();
		timer.schedule(task,dt,dt);
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append(this.getClass().getSimpleName())
			.append("<")
			.append(start.toString())
			.append("::")
			.append(end.toString())
			.append(">")
			.toString(); 
	}
	
}
