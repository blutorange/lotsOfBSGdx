package de.homelab.madgaksha.util.interpolator;

public class LinearDoubleInterpolator extends AInterpolator<Double> {

	public LinearDoubleInterpolator() {
		super();
	}
	
	public LinearDoubleInterpolator(Double a, Double b, Object o) {
		super(a, b, o);
	}

	private double m;
	private double n;

	@Override
	protected void doSetup(Object options) {
		n = start;
		m = end-start;
	}
	
	@Override
	protected Double doInterpolate(double x) {
		return n+x*m;
	}

	@Override
	protected Double getDefaultStart() {
		return 0.0d;
	}

	@Override
	protected Double getDefaultEnd() {
		return 1.0d;
	}

}