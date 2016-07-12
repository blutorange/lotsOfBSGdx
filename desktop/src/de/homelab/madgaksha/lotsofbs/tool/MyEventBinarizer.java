package de.homelab.madgaksha.lotsofbs.tool;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.logging.LoggerFactory;
import de.homelab.madgaksha.lotsofbs.resourcecache.EFancyScene;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

/**
 * Utility that checks whether all game resources exist.
 * 
 * @author madgaksha
 */
public class MyEventBinarizer implements ApplicationListener {
	private final static Logger LOG = LoggerFactory.getLogger(MyEventBinarizer.class);

	private final List<IResource<?,?>> resourceList = new ArrayList<IResource<?,?>>();
	private boolean success = true;
	private int resourceListIndex;
	private BitmapFont font;
	private Batch batch;
	
	private MyEventBinarizer(List<IResource<?,?>> resourceList) throws GdxRuntimeException {
		this.resourceList.addAll(resourceList);
		this.resourceListIndex = 0;
	}

	@Override
	public void create() {
		success = true;
		font = new BitmapFont();
		batch = new SpriteBatch();
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
			IResource<?,?> resource = resourceList.get(resourceListIndex);
			batch.begin();
			font.draw(batch, String.format("%05d", resourceListIndex) + ": " + resource, 20,20);
			batch.end();
			
			try {
				Object loadedObject = resource.getObject();
//				byte[] b = SerializationUtils.serialize((Serializable)loadedObject);
//				Object o = SerializationUtils.deserialize(b);
//				System.out.println(o);
				
				FileOutputStream fos = new FileOutputStream("/tmp/ougiOukaMusougeki.bin");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(loadedObject);
				IOUtils.closeQuietly(oos);
				resource.clear();
			} catch (Exception e) {
				success = false;
				LOG.log(Level.SEVERE, "could not serialize resource: " + resource, e);
			}
			++resourceListIndex;
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
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
	
	private static void addAllEvents(List<IResource<?,?>> eventList) {
		 eventList.add(EFancyScene.OUKA_MUSOUGEKI);
	}
	
	public static void main(String args[]) {
		List<IResource<?,?>> resourceList = new ArrayList<IResource<?,?>>();
		addAllEvents(resourceList);
		
		if (resourceList.isEmpty()) {
			LOG.severe("did not find any events");
			System.exit(-1);
		}
		LOG.fine("found " + resourceList.size() + " number of events");
			
		MyEventBinarizer binarizer = new MyEventBinarizer(resourceList);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(binarizer, config);
	}
}