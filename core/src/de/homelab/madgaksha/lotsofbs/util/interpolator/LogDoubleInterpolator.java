package de.homelab.madgaksha.lotsofbs.util.interpolator;

/**
 * It takes the logarithm between x=[x1..x2] and maps its function values to to
 * the range [a..b].
 *
 * {@see ILogInterpolatorOptions}
 * 
 * a... starting value b... ending value x1.. log map start x1 <= 1 x2.. log map
 * end 0 < x2 < 1
 *
 * Good values for x1 and x2 are (0.01,1.00).
 *
 * The following function is used for interpolation: f(x) =
 * (log(x1+(x2-x1)*x)-log(x1))/(log(x2)-log(x1))*(b-a)+a // = k1*log(k2+k3*x)+k4
 * with k1 = (b-a)/(log(x2)-log(x1)) k2 = x1 k3 = (x2-x1) k4 =
 * -log(x1)/(log(x2)-log(x1))*(b-a)+a
 * 
 * @author madgaksha
 *
 */
public class LogDoubleInterpolator extends AInterpolator<Double> {

	public LogDoubleInterpolator(ILogExpInterpolatorOptions o) {
		super(o);
	}

	public LogDoubleInterpolator(Double a, Double b, ILogExpInterpolatorOptions o) {
		super(a, b, o);
	}

	private double k1;
	private double k2;
	private double k3;
	private double k4;

	@Override
	protected void doSetup(Object options) {
		ILogExpInterpolatorOptions o = (ILogExpInterpolatorOptions) options;
		double a = start;
		double b = end;
		double x1 = o.getX1();
		double x2 = o.getX2();
		k1 = (b - a) / (Math.log(x2) - Math.log(x1));
		k2 = x1;
		k3 = (x2 - x1);
		k4 = -Math.log(x1) / (Math.log(x2) - Math.log(x1)) * (b - a) + a;
	}

	@Override
	protected Double doInterpolate(double x) {
		return k1 * Math.log(k2 + k3 * x) + k4;
	}

	@Override
	protected Double getDefaultStart() {
		return -10.0d;
	}

	@Override
	protected Double getDefaultEnd() {
		return 0.0d;
	}

}