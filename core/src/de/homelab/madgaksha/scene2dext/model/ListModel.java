package de.homelab.madgaksha.scene2dext.model;

import java.util.List;

public interface ListModel<MODEL extends Model> extends SingleValueModel<List<MODEL>>, Iterable<MODEL> {
	/**
	 * @return A list with the elements. Modifying the returned list does not affect the model.
	 * @deprecated As this returns a copy of the underlying list, {@link #getEntry(int)} and {@link #getSize()}
	 * should be used instead for performance.
	 */
	@Deprecated
	@Override
	public List<MODEL> getValue();
	public int getSize();
	public MODEL getEntry(int position);
}