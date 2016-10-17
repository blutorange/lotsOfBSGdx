package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DrawablePropertyChangeListener.DrawablePropertyChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty.PropertyEntry;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelInterpolation;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.ACustomDataHolder;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.interpolation.InterpolationNone;
import de.homelab.madgaksha.safemutable.DimensionalValue;

public abstract class APropertyEntry<T extends DimensionalValue> extends ACustomDataHolder implements PropertyEntry<T> {
	private final AProperty<T> parent;
	private float time;
	private final ModelInterpolation modelInterpolation;
	@SuppressWarnings("unchecked")
	public APropertyEntry(final float time, final Object parent) {
		this.time = time;
		this.parent = (AProperty<T>)parent;
		this.modelInterpolation = new InterpolationNone();
	}
	@Override
	public AProperty<T> getParent() {
		return parent;
	}
	@Override
	public abstract APropertyEntry<T> newObject(Object parent);
	@Override
	public final float getTime() {
		return time;
	}
	@Override
	public final void setTime(float time) {
		time = MathUtils.clamp(time, getMinTime(), getMaxTime());
		final boolean hasChanged = time != this.time;
		this.time = time;
		if (hasChanged && parent != null) {
			parent.fireChangeEvent(DrawablePropertyChangeType.TIME, this);
		}
	}
	@Override
	public float getMinTime() {
		return parent.getMinTimeOf(this);
	}
	@Override
	public float getMaxTime() {
		return parent.getMaxTimeOf(this);
	}

	@Override
	public ModelInterpolation getModelInterpolation() {
		return modelInterpolation;
	}
}
