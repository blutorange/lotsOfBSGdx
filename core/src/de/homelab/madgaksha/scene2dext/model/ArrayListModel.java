package de.homelab.madgaksha.scene2dext.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayListModel<MODEL extends Model> implements ListModel<MODEL> {
	protected final ArrayList<MODEL> list = new ArrayList<MODEL>();
	private List<MODEL> publicList;
	public ArrayListModel() {
	}
	public ArrayListModel(final Collection<MODEL> itemList) {
		list.addAll(itemList);
	}
	public ArrayListModel(final MODEL... itemList) {
		list.ensureCapacity(itemList.length);
		for (int i = 0; i != itemList.length; ++i) {
			list.add(itemList[i]);
		}
	}
	@Override
	public void setValue(final List<MODEL> value) {
		list.clear();
		list.addAll(value);
	}
	@Override
	public List<MODEL> getValue() {
		if (publicList == null) {
			publicList = new ArrayList<MODEL>();
		}
		publicList.clear();
		publicList.addAll(list);
		return publicList;
	}
	@Override
	public Iterator<MODEL> iterator() {
		return list.iterator();
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public MODEL getEntry(final int position) {
		return list.get(position);
	}

	protected int indexOf(final MODEL model) throws NoSuchElementException {
		final int index = list.indexOf(model);
		if (index < 0) throw new NoSuchElementException("List does not contain such an element.");
		return index;
	}
}