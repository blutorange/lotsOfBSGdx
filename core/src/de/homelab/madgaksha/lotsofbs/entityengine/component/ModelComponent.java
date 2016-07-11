package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;

public class ModelComponent implements Component, Poolable {

	public ModelInstance modelInstance;

	/**
	 * Creates a new sprite component. Used mainly for pooling.
	 */
	public ModelComponent() {
	}

	/**
	 * Loads the sprite with texture from the given sprite.
	 * 
	 * @param sprite
	 *            The sprite with the texture to use.
	 */
	public ModelComponent(ModelInstance modelInstance) {
		setup(modelInstance);
	}

	/**
	 * Loads the sprite with the given texture.
	 * 
	 * @param texture
	 *            Texture for the sprite.
	 */
	public ModelComponent(EModel model) {
		setup(model);
	}

	public void setup(ModelInstance modelInstance) {
		this.modelInstance = modelInstance;
	}

	public void setup(EModel model) {
		modelInstance = model.asModelInstance();
	}

	@Override
	public void reset() {
		modelInstance = null;
	}
}
