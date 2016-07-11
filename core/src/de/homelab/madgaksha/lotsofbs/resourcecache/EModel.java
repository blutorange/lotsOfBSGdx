package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.BaseJsonReader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.UBJsonReader;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * For loading, caching and disposing {@link Model} resources.
 * 
 * @author madgaksha
 *
 */
public enum EModel implements IResource<EModel, Model> {
	ITEM_WEAPON_BASIC("model/itemBasicWeapon.g3db"),
	ITEM_WEAPON_MULTI("model/itemMultiWeapon.g3db"),
	ITEM_TOKUGI_OUKAMUSOUGEKI("model/itemOukaMusougekiTokugi.g3db"),;

	private final static Logger LOG = Logger.getLogger(EModel.class);
	private final static EnumMap<EModel, Model> modelCache = new EnumMap<EModel, Model>(EModel.class);

	private String filename;
	private ModelInstance modelInstance = null;

	private EModel(String f) {
		filename = f;
	}

	public static void clearAll() {
		LOG.debug("clearing all models");
		for (EModel mdl : modelCache.keySet()) {
			mdl.clear();
		}
	}

	@Override
	public Model getObject() {
		try {
			FileHandle fileHandle = Gdx.files.internal(filename);
			BaseJsonReader reader = new UBJsonReader();
			G3dModelLoader loader = new G3dModelLoader(reader);
			Model model = loader.loadModel(fileHandle);
			modelInstance = new ModelInstance(model);
			return model;
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public Enum<EModel> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_MODEL;
	}

	@Override
	public void clear() {
		LOG.debug("disposing model: " + String.valueOf(this));
		final Model mdl = modelCache.get(this);
		if (mdl != null)
			mdl.dispose();
		modelCache.remove(this);
		modelInstance = null;
	}

	@Override
	public EnumMap<EModel, Model> getMap() {
		return modelCache;
	}

	public ModelInstance asModelInstance() {
		// Reload model if it has been cleared.
		if (modelInstance == null)
			if (ResourceCache.getModel(this) == null)
				return null;
		return modelInstance;
	}

	@Override
	public void clearAllOfThisKind() {
		EModel.clearAll();
	}
}