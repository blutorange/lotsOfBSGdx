package de.homelab.madgaksha.scene2dext.model;

import java.util.NoSuchElementException;

public abstract class SelectionModel<T extends Model> extends ArrayListModel<T> {

	private T selectedModel;

	public abstract boolean supportsDuplicates();
	/** @return When false, the model will not be removed. */
	protected abstract void beforeRemoval(T model) throws ModelRuntimeException;
	/** Called when selected model is queried and none has been selected.
	 * Adds the returned model. May return null, in which case {@link getSize()}
	 * can return 0.*/
	protected abstract T getNewModel();

	/**
	 * @param timeline
	 * @throws IllegalArgumentException When this timeline exists already.
	 */
	public void addModel(final T model) throws IllegalArgumentException {
		checkUniqueness(model);
		list.add(model);
		if (selectedModel == null) {
			selectedModel = model;
		}
	}

	public void removeModel(final int index) throws ArrayIndexOutOfBoundsException, ModelRuntimeException {
		final T model = getEntry(index);
		beforeRemoval(model);
		// Remove model
		list.remove(index);
		// Deselect timeline when the selected one has been closed.
		if (model == selectedModel) {
			selectedModel = null;
		}
	}

	public void removeModel(final T model) throws NoSuchElementException {
		removeModel(indexOf(model));
	}

	public void selectModel(final int index) throws ArrayIndexOutOfBoundsException {
		selectedModel = getEntry(index);
	}

	public void selectModel(final T model) throws NoSuchElementException {
		selectModel(indexOf(model));
	}

	private void checkUniqueness(final T model) {
		if (!supportsDuplicates() && list.indexOf(model) >= 0) throw new IllegalAccessError("Object exists already.");
	}

	public final ModelProvider<T> selectedModelProvider = new ModelProvider<T>() {
		@Override
		public T getProvidedObject() {
			if (selectedModel == null) {
				if (getSize() == 0) {
					final T model = getNewModel();
					if (model != null) {
						addModel(model);
						selectModel(0);
					}
				} else {
					selectModel(0);
				}
			}
			return selectedModel;
		}
	};

	public final ModelProvider<ListModel<T>> modelListProvider = new ModelProvider<ListModel<T>>() {
		@Override
		public ListModel<T> getProvidedObject() {
			return SelectionModel.this;
		}
	};
}

