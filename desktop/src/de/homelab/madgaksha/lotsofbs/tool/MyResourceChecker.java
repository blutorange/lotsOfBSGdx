package de.homelab.madgaksha.lotsofbs.tool;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import de.homelab.madgaksha.lotsofbs.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.lotsofbs.i18n.I18n;
import de.homelab.madgaksha.lotsofbs.logging.LoggerFactory;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETextureAtlas;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

/**
 * Utility that checks whether all game resources exist.
 * 
 * @author madgaksha
 */
public class MyResourceChecker implements ApplicationListener {
	private final static Logger LOG = LoggerFactory.getLogger(MyResourceChecker.class);

	private boolean success;
	private int resourceListIndex;
	private final List<IResource<?, ?>> resourceList = new ArrayList<IResource<?, ?>>();
	private BitmapFont font;
	private Batch batch;
	private Locale locale;
	
	public static void main(String args[]) {
		if (args.length != 1) {
			printHelp();
			System.exit(-1);
		}
		List<IResource<?, ?>> resourceList = new ArrayList<IResource<?, ?>>();
		addAllResources(resourceList);
		
		if (resourceList.isEmpty()) {
			LOG.severe("did not find any resources");
			System.exit(-1);
		}
		LOG.fine("found " + resourceList.size() + " number of resources");
		
		Locale locale = Locale.forLanguageTag(args[0]);
		if (!I18n.isLocaleAvailable(locale)) {
			LOG.severe("locale " + locale.getDisplayName() + " does not exist!");
			System.exit(-1);
		}
		
		MyResourceChecker checker = new MyResourceChecker(resourceList, locale);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(checker, config);
	}

	private MyResourceChecker(List<IResource<?, ?>> resourceList, Locale locale) throws GdxRuntimeException {
		this.resourceList.addAll(resourceList);
		this.resourceListIndex = 0;
		this.locale = locale;
	}

	@Override
	public void create() {
		LOG.fine("checking for language: " + locale);
		I18n.init(locale);
		success = true;
		font = new BitmapFont();
		batch = new SpriteBatch();
		AwesomeAudio.initialize();
		ResourcePool.init();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		if (resourceListIndex >= resourceList.size())
			Gdx.app.exit();
		else {
			IResource<?, ?> resource = resourceList.get(resourceListIndex);
			batch.begin();
			
			font.draw(batch, String.format("%05d", resourceListIndex) + ": " + resource, 20,20);
			batch.end();
			try {
				if (!ResourceCache.loadToRam(resource)) {
					LOG.severe("could not load resource: " + resource.toString());
					success = false;
				}
				else { 
					try {
						detailedTesting(resource);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "could not load resource: " + resource.toString(), e);
			}
			++resourceListIndex;
		}
	}

	private void detailedTesting(IResource<?, ?> resource) {
		if (resource instanceof ETexture) {
			Object o = ((ETexture)resource).asSprite();
			if (o == null) {
				LOG.severe("could not create sprite for texture: " + resource.toString());
				success = false;
			}
		}
		else if (resource instanceof ETextureAtlas) {
			for (AtlasRegion ar : ((ETextureAtlas)resource).getObject().getRegions()) {
				if (ar.rotate) {
					LOG.severe("Texture atlas must not contain rotated region: " + resource.toString());
					success = false;
				}
			}
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		ResourceCache.clearAll();
		font.dispose();
		batch.dispose();
		if (success)
			LOG.fine("all resources processed successfully");
		else
			LOG.severe("there were error with some resources, check above");
	}
	
	private static void addAllResources(List<IResource<?, ?>> resourceList) {
		 try {
			ClassPath cp = ClassPath.from(MyResourceChecker.class.getClassLoader());
			for (ClassInfo ci : cp.getTopLevelClasses(ResourceCache.class.getPackage().getName())) {
				Class<?> c = ci.load();
				if (IResource.class.isAssignableFrom(c) && !Modifier.isInterface(c.getModifiers())) {
					IResource<?,?>[] constants = (IResource<?,?>[])c.getEnumConstants();					
					for (IResource<?,?> r: constants) {
						resourceList.add(r);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void printHelp() {
		System.out.println("usage: java -jar MyResourceChecker.jar <languageTagName>");
	}
}