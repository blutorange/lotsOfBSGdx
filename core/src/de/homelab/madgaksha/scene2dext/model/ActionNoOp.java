package de.homelab.madgaksha.scene2dext.model;

import de.homelab.madgaksha.scene2dext.model.Action.ActionWithFixedName;

public class ActionNoOp<T extends Model> extends ActionWithFixedName<T> {
	private final static String NAME = "No action.";
	public ActionNoOp() {
		super(NAME);
	}
	@Override
	public void actOnModel(final T model) {
	}
	public static <K extends Model> ActionNoOp<K> get() {
		return new ActionNoOp<K>();
	}
}