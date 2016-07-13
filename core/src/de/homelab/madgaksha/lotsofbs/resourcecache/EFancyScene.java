package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * For loading, caching and disposing {@link FreeTypeFontGenerator} resources.
 * 
 * @author madgaksha
 *
 */
public enum EFancyScene implements IResource<EFancyScene, EventFancyScene> {
	OUKA_MUSOUGEKI("cutscene/fancyScene/ougiOukaMusougeki/ougiOukaMusougeki.scene", false),
	OUKA_MUSOUGEKI_BIN("cutscene/fancyScene/ougiOukaMusougeki/ougiOukaMusougeki.bin", true)
	;

	private final static Logger LOG = Logger.getLogger(EFancyScene.class);
	private final static EnumMap<EFancyScene, EventFancyScene> fancySceneCache = new EnumMap<EFancyScene, EventFancyScene>(
			EFancyScene.class);

	private final String path;
	private final boolean binary;
	
	private EFancyScene(String path, boolean isBinary) {
		this.path = path;
		this.binary = isBinary;
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
			FileHandle inputFile = Gdx.files.internal(path);
			if (binary) {
				ObjectInputStream ois = null;
				try {
					InputStream is = inputFile.read();
					ois = new ObjectInputStream(is);
					Object scene = ois.readObject();
					if (scene == null || !(scene instanceof EventFancyScene)) return null;
					return (EventFancyScene) scene;
				}
				catch (Exception e) {
					LOG.error("could not locate or open resource: " + String.valueOf(this), e);
					return null;
				}
				finally {
					IOUtils.closeQuietly(ois);
				}
			}
			else {
				Scanner scanner = null;
				try {
					scanner = new Scanner(inputFile.name());
					scanner.useLocale(Locale.ROOT);
					EventFancyScene scene = EventFancyScene.readNextObject(scanner, inputFile);
					return scene;
				}
				catch (GdxRuntimeException e) {
					LOG.error("could not locate or open resource: " + String.valueOf(this), e);
					return null;
				}
				finally {
					IOUtils.closeQuietly(scanner);
				}
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

	public boolean isPlainText() {
		return !binary;
	}
	public boolean isBinary() {
		return binary;
	}
	public FileHandle getFileHandle() {
		return Gdx.files.internal(path);
	}
}