package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityCallback;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.CallbackOnReenterSystem;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Contains a callback that gets activated (called) when the position component
 * of the entity to which this component is attached reenters the given shape.
 * If the position component of an entity is inside the shape at the time that
 * entity is added the family as defined by the {@link CallbackOnReenterSystem}
 * (any entity with a {@link PositionComponent} and a
 * {@link CallbackOnReenterComponent}), the position component must first leave
 * the shape and then enter it again for the callback to be activated. When it
 * is already outside the shape, it only needs to enter the shape.
 * <br><br>
 * The callback will be called the given number of times. After the callback has
 * been called, the position component needs to reenter the shape for the callback
 * to be activated again.
 * <br><br>
 * Specify any number smaller than zero for the number of callbacks for an
 * unlimited number of callbacks.
 * @author madgaksha
 */
public class CallbackOnReenterComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CallbackOnReenterComponent.class);

	public final static int UNLIMITED_NUMBER_OF_CALLBACKS = -1;
	private final static Object DEFAULT_CALLBACK_DATA = new Object();
	private final static IEntityCallback DEFAULT_ON_REENTER = IEntityCallback.NOOP;
	private final static Shape2D DEFAULT_SHAPE = new Circle(0f, 0f, 1f);
	private final static boolean DEFAULT_HAS_LEFT_SHAPE = false;
	private final static int DEFAULT_REMAINING_NUMBER_OF_REPETITIONS = 0;

	public int remainingNumberOfRepetitions = DEFAULT_REMAINING_NUMBER_OF_REPETITIONS;
	public Object callbackData = DEFAULT_CALLBACK_DATA;
	public IEntityCallback onReenter = DEFAULT_ON_REENTER;
	public Shape2D shape = DEFAULT_SHAPE;
	public boolean hasLeftShape = DEFAULT_HAS_LEFT_SHAPE;

	public CallbackOnReenterComponent() {
	}

	public CallbackOnReenterComponent(Shape2D shape, IEntityCallback onReenter) {
		setup(shape, DEFAULT_REMAINING_NUMBER_OF_REPETITIONS, onReenter, DEFAULT_CALLBACK_DATA);
	}

	public CallbackOnReenterComponent(Shape2D shape, IEntityCallback onReenter, int numberOfRepetitions) {
		setup(shape, numberOfRepetitions, onReenter, DEFAULT_CALLBACK_DATA);
	}

	public CallbackOnReenterComponent(Shape2D shape, int numberOfRepetitions, IEntityCallback onReenter,
			Object callbackData) {
		setup(shape, numberOfRepetitions, onReenter, callbackData);
	}

	public void setup(Shape2D shape, IEntityCallback onReenter) {
		setup(shape, DEFAULT_REMAINING_NUMBER_OF_REPETITIONS, onReenter, DEFAULT_CALLBACK_DATA);
	}

	public void setup(Shape2D shape, IEntityCallback onReenter, int numberOfRepetitions) {
		setup(shape, numberOfRepetitions, onReenter, DEFAULT_CALLBACK_DATA);
	}

	public void setup(Shape2D shape, int numberOfRepetitions, IEntityCallback onReenter, Object callbackData) {
		this.shape = shape;
		this.remainingNumberOfRepetitions = numberOfRepetitions;
		this.onReenter = onReenter;
		this.callbackData = callbackData;
		this.hasLeftShape = false;
	}

	@Override
	public void reset() {
		remainingNumberOfRepetitions = DEFAULT_REMAINING_NUMBER_OF_REPETITIONS;
		callbackData = DEFAULT_CALLBACK_DATA;
		shape = DEFAULT_SHAPE;
		hasLeftShape = DEFAULT_HAS_LEFT_SHAPE;
		onReenter = DEFAULT_ON_REENTER;
	}
}