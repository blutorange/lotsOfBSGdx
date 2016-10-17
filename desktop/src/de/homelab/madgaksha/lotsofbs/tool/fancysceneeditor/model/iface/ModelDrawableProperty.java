package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import java.util.NoSuchElementException;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DrawablePropertyChangeListener.DrawablePropertyChangeListenable;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty.PropertyEntry;
import de.homelab.madgaksha.safemutable.Clone;
import de.homelab.madgaksha.safemutable.DimensionalValue;

public interface ModelDrawableProperty<T extends DimensionalValue> extends Iterable<PropertyEntry<T>>, TimeIntervalGetter, DrawablePropertyChangeListenable, CustomDataHolder {
	public String getName();
	public int getDimension();
	public int size();
	public T getMinValue();
	public T getMaxValue();

	/**
	 * @param entry The entry to search for.
	 * @return The index of the given entry.
	 * @throw {@link NoSuchElementException} When this property does not contain such an entry.
	 */
	public int getIndex(PropertyEntry<?> entry) throws NoSuchElementException;
	/**
	 * Inserts a new property entry at the given position.
	 * @param position Position at which to insert the new entry. 0 is at the beginning {@link #size()} at the end.
	 * @return The newly inserted entry.
	 * @throws IndexOutOfBoundsException When position is smaller than 0 or greater than {@link #size()}.
	 */
	public PropertyEntry<T> insertPropertyEntry(int position) throws IndexOutOfBoundsException;
	public void removePropertyEntry(int position) throws IndexOutOfBoundsException;
	/**
	 * Inserts a new element after the given entry. When the entry does not exist,
	 * appends a new entry to the end of this property list.
	 * @param entry Entry after which a new entry should be added.
	 */
	default void insertAfter(final PropertyEntry<?> entry) {
		int position;
		try {
			position = getIndex(entry) + 1;
		}
		catch (final NoSuchElementException e) {
			position = size();
		}
		insertPropertyEntry(position);
	}
	default void insertBefore(final PropertyEntry<?> entry) {
		int position;
		try {
			position = getIndex(entry);
		}
		catch (final NoSuchElementException e) {
			position = 0;
		}
		insertPropertyEntry(position);
	}
	/**
	 * Removes the given entry. Does nothing if the entry does not exist.
	 * @param entry The entry to be removed.
	 */
	default void remove(final PropertyEntry<?> entry) {
		try {
			final int position = getIndex(entry);
			removePropertyEntry(position);
		}
		catch (final NoSuchElementException e) {
		}
	}


	public static interface PropertyEntry<T extends DimensionalValue> extends Clone<PropertyEntry<T>>, CustomDataHolder {
		public float getTime();
		public void setTime(float startTime);
		public float getMinTime();
		public float getMaxTime();
		public T getValue();
		public Object getParent();
		public PropertyEntry<T> newObject(Object parent);
		public ModelInterpolation getModelInterpolation();

		@Override
		default PropertyEntry<T> cloneObject() {
			final PropertyEntry<T> entry = newObject(getParent());
			entry.setTime(getTime());
			entry.getValue().setValues(getValue().getValues());
			return entry;
		}
	}
}
