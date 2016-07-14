package de.homelab.madgaksha.lotsofbs.entityengine.component;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Distributes the components in the {@link #distributionPoints} list along the
 * base of a cone with one component as its apex. <br>
 * <br>
 * The apex of the cone is located at the position of the entity to which this
 * component is attached to, offset by {@link #offsetToApex}. <br>
 * <br>
 * The center of the elliptical base of the cone is located at the position of
 * the apex, offset by {@link ConeDistributionComponent#offsetToBase}.
 * 
 * @author madgaksha
 *
 */
public class ConeDistributionComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ConeDistributionComponent.class);

	private final static float DEFAULT_ANGULAR_VELOCITY = 180.0f;
	private final static float DEFAULT_DEGREES = 0.0f;
	private final static float DEFAULT_RADIUS_1 = 1.0f;
	private final static float DEFAULT_RADIUS_2 = 1.0f;
	private final static int DEFAULT_APEX_POINT = 0;

	/** degrees / second */
	public float angularVelocity = DEFAULT_ANGULAR_VELOCITY;
	/** used internally, current rotation */
	public float degrees = DEFAULT_DEGREES;
	public int apexPoint = DEFAULT_APEX_POINT;
	public float radius1 = DEFAULT_RADIUS_1;
	public float radius2 = DEFAULT_RADIUS_2;
	public final Vector2 offsetToApex = new Vector2();
	public final Vector2 offsetToBase = new Vector2();
	public final List<PositionComponent> distributionPoints = new ArrayList<PositionComponent>(10);

	public ConeDistributionComponent() {
	}

	public ConeDistributionComponent(ConeDistributionParameters param) {
		setup(param);
	}

	public void setup(ConeDistributionParameters param) {
		angularVelocity = param.getAngularVelocity();
		radius1 = param.getRadius1();
		radius2 = param.getRadius2();
		offsetToApex.set(param.getOffsetToApexX(), param.getOffsetToApexY());
		offsetToBase.set(param.getOffsetToBaseX(), param.getOffsetToBaseY());
		degrees = 0.0f;
		apexPoint = 0;
	}

	@Override
	public void reset() {
		angularVelocity = DEFAULT_ANGULAR_VELOCITY;
		apexPoint = DEFAULT_APEX_POINT;
		degrees = DEFAULT_DEGREES;
		radius1 = DEFAULT_RADIUS_1;
		radius2 = DEFAULT_RADIUS_2;
		offsetToBase.setZero();
		offsetToApex.setZero();
		distributionPoints.clear();
	}

	public static interface ConeDistributionParameters {
		public final static ConeDistributionParameters DEFAULT = new ConeDistributionParameters() {
			@Override
			public float getRadius1() {
				return 32 * 3;
			}

			@Override
			public float getRadius2() {
				return 32;
			}

			@Override
			public float getOffsetToBaseX() {
				return 0;
			}

			@Override
			public float getOffsetToBaseY() {
				return -32 * 4;
			}

			@Override
			public float getOffsetToApexX() {
				return 0;
			}

			@Override
			public float getOffsetToApexY() {
				return 32 * 4;
			}

			@Override
			public float getAngularVelocity() {
				return 42;
			}

			@Override
			public float getItemScaleMaxValue() {
				return 0.5f;
			}

			@Override
			public float getItemScaleMaxDistance() {
				return 48f;
			}

			@Override
			public float getItemExponentialGrantFactor() {
				return 0.25f;
			}
		};

		public float getAngularVelocity();

		public float getRadius1();

		public float getRadius2();

		public float getOffsetToApexX();

		public float getOffsetToApexY();

		public float getOffsetToBaseX();

		public float getOffsetToBaseY();

		public float getItemScaleMaxValue();

		public float getItemScaleMaxDistance();

		public float getItemExponentialGrantFactor();
	}
	
	@Override
	public String toString() {
		return "ConeDistributionComponent@" + distributionPoints.size() + "@" + angularVelocity + "degree/sec,";
	}
}
