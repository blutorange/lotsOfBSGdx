package de.homelab.madgaksha.lotsofbs.entityengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.homelab.madgaksha.lotsofbs.entityengine.component.DisableAllExceptTheseComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public abstract class DisableIteratingSystem extends EntitySystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DisableIteratingSystem.class);

	private final Family familyAll;
	private final Family familyExclude;
	private ImmutableArray<Entity> entitiesAll;
	private ImmutableArray<Entity> entitiesExclude;

	/**
	 * Instantiates a system that will iterate over the entities described by
	 * the Family.
	 *
	 * @param family
	 *            The family of entities iterated over in this System
	 */
	public DisableIteratingSystem(final DisableIteratingSystem.Builder builder) {
		this(builder, 0);
	}

	/**
	 * Instantiates a system that will iterate over the entities described by
	 * the Family, with a specific priority.
	 *
	 * @param family
	 *            The family of entities iterated over in this System
	 * @param priority
	 *            The priority to execute this system with (lower means higher
	 *            priority)
	 */
	public DisableIteratingSystem(final DisableIteratingSystem.Builder builder, final int priority) {
		super(priority);
		this.familyAll = builder.getAll();
		this.familyExclude = builder.getExclude();
	}

	@SafeVarargs
	public static Builder all(final Class<? extends Component>... c) {
		return new Builder().all(c);
	}

	@SafeVarargs
	public static Builder one(final Class<? extends Component>... c) {
		return new Builder().one(c);
	}

	@SafeVarargs
	public static Builder exclude(final Class<? extends Component>... c) {
		return new Builder().exclude(c);
	}

	public static class Builder {
		private final List<Class<? extends Component>> componentTypesAll = new ArrayList<Class<? extends Component>>();
		private final List<Class<? extends Component>> componentTypesOne = new ArrayList<Class<? extends Component>>();
		private final List<Class<? extends Component>> componentTypesExclude = new ArrayList<Class<? extends Component>>();

		@SafeVarargs
		public final Builder all(final Class<? extends Component>... c) {
			componentTypesAll.clear();
			componentTypesAll.addAll(Arrays.asList(c));
			return this;
		}

		@SafeVarargs
		public final Builder exclude(final Class<? extends Component>... c) {
			componentTypesExclude.clear();
			componentTypesExclude.addAll(Arrays.asList(c));
			return this;
		}

		@SafeVarargs
		public final Builder one(final Class<? extends Component>... c) {
			componentTypesOne.clear();
			componentTypesOne.addAll(Arrays.asList(c));
			return this;
		}

		private Family getAll() {
			componentTypesAll.add(DisableAllExceptTheseComponent.class);
			final Family f = get();
			componentTypesAll.remove(componentTypesAll.size() - 1);
			return f;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Family get() {
			Class[] all = new Class[0];
			all = componentTypesAll.toArray(all);
			Class[] one = new Class[0];
			one = componentTypesOne.toArray(one);
			Class[] exclude = new Class[0];
			exclude = componentTypesExclude.toArray(exclude);
			return Family.all(all).one(one).exclude(exclude).get();
		}

		private Family getExclude() {
			componentTypesExclude.add(DisableAllExceptTheseComponent.class);
			final Family f = get();
			componentTypesExclude.remove(componentTypesExclude.size() - 1);
			return f;
		}
	}

	@Override
	public void addedToEngine(final Engine engine) {
		entitiesAll = engine.getEntitiesFor(familyAll);
		entitiesExclude = engine.getEntitiesFor(familyExclude);
	}

	@Override
	public void removedFromEngine(final Engine engine) {
		entitiesAll = null;
		entitiesExclude = null;
	}

	@Override
	public void update(final float deltaTime) {
		if (entitiesAll.size() != 0) {
			for (int i = 0; i < entitiesAll.size(); ++i)
				processEntity(entitiesAll.get(i), deltaTime);
		} else
			for (int i = 0; i < entitiesExclude.size(); ++i)
				processEntity(entitiesExclude.get(i), deltaTime);
	}

	/**
	 * This method is called on every entity on every update call of the
	 * EntitySystem. Override this to implement your system's specific
	 * processing.
	 *
	 * @param entity
	 *            The current Entity being processed
	 * @param deltaTime
	 *            The delta time between the last and current frame
	 */
	protected abstract void processEntity(Entity entity, float deltaTime);
}
