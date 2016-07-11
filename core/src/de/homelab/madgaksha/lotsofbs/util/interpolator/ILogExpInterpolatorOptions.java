package de.homelab.madgaksha.lotsofbs.util.interpolator;

/**
 * It takes the logarithm/exponential between x=[x1..x2] and maps its function
 * values to to the range [a..b].
 *
 * {@see LogDoubleInterpolator}
 *
 * a... starting value b... ending value x1.. log map start x1 <= 1 x2.. log map
 * end 0 < x2 < 1
 *
 *
 * The following function is used for interpolating logarithmically: f(x) =
 * (log(x1+(x2-x1)*x)-log(x1))/(log(x2)-log(x1))*(b-a)+a // = k1*log(k2+k3*x)+k4
 * with k1 = (b-a)/(log(x2)-log(x1)) k2 = x1 k3 = (x2-x1) k4 =
 * -log(x1)/(log(x2)-log(x1))*(b-a)+a
 * 
 * Same function is used for interpolating exponentially, but with <i>log</i>
 * replaced by <i>exp</i>.
 * 
 * @author madgaksha
 *
 */
public interface ILogExpInterpolatorOptions {
	double getX1();

	double getX2();
}