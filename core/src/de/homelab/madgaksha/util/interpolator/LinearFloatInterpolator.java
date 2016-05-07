package de.homelab.madgaksha.util.interpolator;

public class LinearFloatInterpolator extends AInterpolator<Float> {

	public LinearFloatInterpolator() {
		super();
	}
	
	public LinearFloatInterpolator(Float a, Float b, Object o) {
		super(a, b, o);
	}

	private float  m;
	private float n;

	@Override
	protected void doSetup(Object options) {
		n = start;
		m = end-start;
	}
	
	@Override
	protected Float doInterpolate(float x) {
		return n+x*m;
	}
	@Override
	protected Float doInterpolate(double x) {
		return doInterpolate(x);
	}
	
	@Override
	protected Float getDefaultStart() {
		return 0.0f;
	}

	@Override
	protected Float getDefaultEnd() {
		return 1.0f;
	}

}