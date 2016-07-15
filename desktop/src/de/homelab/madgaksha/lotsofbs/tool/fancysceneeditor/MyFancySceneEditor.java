package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

/**
 * Utility that checks whether all game resources exist.
 * You should run this with the working directory set to the assets directory for the actual game.
 * 
 * @author madgaksha
 */
public class MyFancySceneEditor implements ApplicationListener {
	private Logger LOG;	

	private MyFancySceneEditor() throws GdxRuntimeException {
	}

	@Override
	public void create() {
		LOG = Logger.getLogger(MyFancySceneEditor.class);
		Logger.setDefaultLevel(Logger.ALL);
		LOG.debug("new editor created");
		ResourcePool.init();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
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
	}
	
	public static void main(String args[]) {
		MyFancySceneEditor editor = new MyFancySceneEditor();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(editor, config);
	}
}