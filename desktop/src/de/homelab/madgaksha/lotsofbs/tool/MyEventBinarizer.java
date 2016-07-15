package de.homelab.madgaksha.lotsofbs.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.LoggerFactory;
import de.homelab.madgaksha.lotsofbs.resourcecache.EFancyScene;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

/**
 * Utility that checks whether all game resources exist.
 * You should run this with the working directory set to the assets directory for the actual game.
 * 
 * @author madgaksha
 */
public class MyEventBinarizer implements ApplicationListener {
	private final static Logger LOG = LoggerFactory.getLogger(MyEventBinarizer.class);

	private final List<EFancyScene> resourceList = new ArrayList<EFancyScene>();
	private boolean success = true;
	private int resourceListIndex;
	private BitmapFont font;
	private Batch batch;
	
	private MyEventBinarizer(List<EFancyScene> resourceList) throws GdxRuntimeException {
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
		
		if (resourceListIndex >= resourceList.size()) {
			batch.begin();
			font.draw(batch, "All done, press enter to exit.", 20,20);
			batch.end();
			if (Gdx.input.isKeyJustPressed(Keys.ENTER)) Gdx.app.exit();
		}
		else {
			EFancyScene resource = resourceList.get(resourceListIndex);
			batch.begin();
			font.draw(batch, String.format("%05d", resourceListIndex) + ": " + resource, 20,20);
			batch.end();
			
			try {
				binarizeCutscene(resource);
			} catch (Exception e) {
				success = false;
				LOG.log(Level.SEVERE, "could not serialize resource: " + resource, e);
			}
			++resourceListIndex;
		}
	}

	private void binarizeCutscene(EFancyScene scene) {
		FileHandle fileHandlePlain = scene.getFileHandle();
		String fileNameBinary = fileHandlePlain.nameWithoutExtension() + ".bin";
		File fileBinary = fileHandlePlain.parent().child(fileNameBinary).file();
		EventFancyScene efs = ResourceCache.getEventFancyScene(scene);
		OutputStream outputStream = null;
		try {
			// deserialize
			Date d1 = new Date();
			byte bytes[] = SerializationUtils.serialize(efs);
			Date d2 = new Date();
			System.out.println("serialization took " + (d2.getTime()-d1.getTime()) + "ms");
			// check if we can serialize it again
			Date d3 = new Date();
			SerializationUtils.deserialize(bytes);
			Date d4 = new Date();
			System.out.println("deserialization took " + (d4.getTime()-d3.getTime()) + "ms");
			// write the deserialized form to file
			outputStream = new FileOutputStream(fileBinary);
			IOUtils.write(bytes, outputStream);
			System.out.println("converted " + fileHandlePlain.path() + " to " + fileBinary.getAbsolutePath());
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "could not serialize resource: " + scene, e);
		}
		finally {
			IOUtils.closeQuietly(outputStream);
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
	
	private static void addAllEvents(List<EFancyScene> eventList) {
		for (EFancyScene fancyScene : EFancyScene.class.getEnumConstants()) {
			if (fancyScene.isPlainText()) eventList.add(fancyScene);
		}
	}
	
	public static void main(String args[]) {
		List<EFancyScene> resourceList = new ArrayList<EFancyScene>();
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