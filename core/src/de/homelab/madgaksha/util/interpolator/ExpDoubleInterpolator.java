package de.homelab.madgaksha.util.interpolator;

/**
 * It takes the exponential between x=[x1..x2] and maps its function values to
 * to the range [a..b].
 *
 * {@see ILogInterpolatorOptions}
 * 
 * a... starting value b... ending value x1.. exp map start x1 <= 1 x2.. exp map
 * end 0 < x2 < 1
 *
 * Good values for x1 and x2 are (0,2.71..5).
 *
 * The following function is used for interpolation: f(x) =
 * (exp(x1+(x2-x1)*x)-exp(x1))/(exp(x2)-exp(x1))*(b-a)+a // = k1*exp(k2+k3*x)+k4
 * with k1 = (b-a)/(exp(x2)-exp(x1)) k2 = x1 k3 = (x2-x1) k4 =
 * -exp(x1)/(exp(x2)-exp(x1))*(b-a)+a
 * 
 * @author madgaksha
 *
 */
public class ExpDoubleInterpolator extends AInterpolator<Double> {

	public ExpDoubleInterpolator(ILogExpInterpolatorOptions o) {
		super(o);
	}

	public ExpDoubleInterpolator(Double a, Double b, ILogExpInterpolatorOptions o) {
		super(a, b, o);
	}

	private double k1;
	private double k2;
	private double k3;
	private double k4;

	@Override
	protected void doSetup(Object options) {
		ILogExpInterpolatorOptions o = (ILogExpInterpolatorOptions) options;
		double a = (double) start;
		double b = (double) end;
		double x1 = o.getX1();
		double x2 = o.getX2();
		k1 = (b - a) / (Math.exp(x2) - Math.exp(x1));
		k2 = x1;
		k3 = (x2 - x1);
		k4 = -Math.exp(x1) / (Math.exp(x2) - Math.exp(x1)) * (b - a) + a;
	}

	@Override
	protected Double doInterpolate(double x) {
		return k1 * Math.exp(k2 + k3 * x) + k4;
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