package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.utils.Pool;

public interface IPooledResource<T, S> {
	public int getInitialCapacity();

	public int getMaximumCapacity();

	public T getObject();

	public Pool<S> getPool();
}
