package de.homelab.madgaksha.scene2dext.model;

import de.homelab.madgaksha.scene2dext.model.Action.ActionWithFixedName;

public interface ActionEnum<T extends Model> {
	public Action<T> getAction();

	// Example of how to create several actions for a model.
	public static enum DemoFloatModelAction implements ActionEnum<NumberModel<Float>> {
		DOUBLE(new ActionWithFixedName<NumberModel<Float>>("Double value"){
			@Override
			public void actOnModel(final NumberModel<Float> model) {
				model.setValue(2f*model.getValue());
			}
		}),
		HALF(new ActionWithFixedName<NumberModel<Float>>("Half value"){
			@Override
			public void actOnModel(final NumberModel<Float> model) {
				model.setValue(0.5f*model.getValue());
			}
		}),
		SQUARE(new ActionWithFixedName<NumberModel<Float>>("Square value"){
			@Override
			public void actOnModel(final NumberModel<Float> model) {
				model.setValue(model.getValue()*model.getValue());
			}
		});
		public final Action<NumberModel<Float>> action;
		private DemoFloatModelAction(final Action<NumberModel<Float>> action) {
			this.action = action;
		}
		@Override
		public Action<NumberModel<Float>> getAction() {
			return action;
		}
	}
}