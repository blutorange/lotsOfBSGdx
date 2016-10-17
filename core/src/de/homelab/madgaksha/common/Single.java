package de.homelab.madgaksha.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;

public class Single<T> implements Collection<T> {
	private final Iterator<T> emptyIterator = new EmptyIterator<T>();
	private final ArrayList<T> converter = new ArrayList<T>(1);
	private T object;
	private boolean hasObject;

	public Single() {
	}

	public Single(final T shake) {
		this();
		add(shake);
	}

	@Override
	public int size() {
		return hasObject ? 1 : 0;
	}

	@Override
	public boolean isEmpty() {
		return !hasObject;
	}

	@Override
	public boolean contains(final Object paramObject) {
		if (!hasObject) return false;
		return paramObject == null ? object == null : object.equals(paramObject);
	}

	@Override
	public Iterator<T> iterator() {
		return isEmpty() ? emptyIterator : new SingleIterator<T>(object);
	}

	@Override
	public Object[] toArray() {
		return hasObject ? new Object[]{object} : ArrayUtils.EMPTY_OBJECT_ARRAY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S[] toArray(final S[] paramArrayOfT) {
		if (!hasObject) {
			if (paramArrayOfT.length>0) {
				paramArrayOfT[0] = null;
			}
			return paramArrayOfT;
		}
		if (paramArrayOfT.length == 0) {
			converter.clear();
			converter.add(object);
			return converter.toArray(paramArrayOfT);
		}
		try {
			paramArrayOfT[0] = (S)object;
		}
		catch (final ClassCastException e) {
			throw new ArrayStoreException(e.getMessage());
		}
		if (paramArrayOfT.length > 1) {
			paramArrayOfT[0] = null;
		}
		return paramArrayOfT;
	}

	@Override
	public boolean add(final T paramE) {
		if (contains(paramE)) return false;
		if (hasObject) throw new UnsupportedOperationException("Single cannot contain more than a single object.");
		object = paramE;
		hasObject = true;
		return true;
	}

	@Override
	public boolean remove(final Object paramObject) {
		if (!contains(paramObject)) return false;
		hasObject = false;
		object = null;
		return true;
	}

	@Override
	public boolean containsAll(final Collection<?> paramCollection) {
		for (final Object o : paramCollection) {
			if (!contains(o)) return false;
		}
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends T> paramCollection) {
		boolean changed = false;
		for (final T o : paramCollection) {
			changed = add(o) || changed;
		}
		return changed;
	}

	@Override
	public boolean removeAll(final Collection<?> paramCollection) {
		boolean changed = false;
		for (final Object o : paramCollection) {
			changed = remove(o) || changed;
		}
		return changed;
	}

	@Override
	public boolean retainAll(final Collection<?> paramCollection) {
		boolean retain = false;
		for (final Object o : paramCollection) {
			retain = retain || contains(o);
		}
		return retain ? false : remove(object);
	}

	@Override
	public void clear() {
		remove(object);
	}
	private static class SingleIterator<T> implements Iterator<T> {
		private final T object;
		private boolean queried;
		public SingleIterator(final T object) {
			this.object = object;
		}
		@Override
		public boolean hasNext() {
			return queried;
		}
		@Override
		public T next() {
			queried = true;
			return object;
		}
	}
}