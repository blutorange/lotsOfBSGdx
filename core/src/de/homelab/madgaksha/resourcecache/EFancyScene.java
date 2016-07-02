package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link FreeTypeFontGenerator} resources.
 * 
 * @author madgaksha
 *
 */
public enum EFancyScene implements IResource<EFancyScene, EventFancyScene> {
	OUKA_MUSOUGEKI("cutscene/fancyScene/ougiOukaMusougeki/ougiOukaMusougeki.scene");

	private final static Logger LOG = Logger.getLogger(EFancyScene.class);
	private final static EnumMap<EFancyScene, EventFancyScene> fancySceneCache = new EnumMap<EFancyScene, EventFancyScene>(
			EFancyScene.class);

	private final String path;

	private EFancyScene(String path) {
		this.path = path;
	}

	public static void clearAll() {
		LOG.debug("clearing all fancy scenes");
		for (EFancyScene fs : fancySceneCache.keySet()) {
			fs.clear();
		}
	}

	public static Set<EFancyScene> getMapKeys() {
		return fancySceneCache.keySet();
	}

	@Override
	public EventFancyScene getObject() {
		try {
			FileHandle inputFile = new FileHandle(path);
			Scanner scanner = new Scanner(inputFile.name());
			EventFancyScene scene = EventFancyScene.readNextObject(scanner, inputFile);
			IOUtils.closeQuietly(scanner);
			return scene;
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public Enum<EFancyScene> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_FANCY_SCENE;
	}

	@Override
	public void clear() {
		LOG.debug("clearing event fancy scene: " + String.valueOf(this));
		final EventFancyScene fs = fancySceneCache.get(this);
		if (fs != null)
			fs.reset();
		fancySceneCache.remove(this);
	}

	@Override
	public EnumMap<EFancyScene, EventFancyScene> getMap() {
		return fancySceneCache;
	}

	@Override
	public void clearAllOfThisKind() {
		EFancyScene.clearAll();
	}
}