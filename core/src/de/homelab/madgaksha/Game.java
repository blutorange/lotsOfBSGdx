package de.homelab.madgaksha;

import java.util.Locale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.homelab.madgaksha.i18n.i18n;

public class Game implements ApplicationListener {
	SpriteBatch batch;
	Texture img;
	
	private final IGameParameters params;
	
	/**
	 * @param params Screen size, fps etc. that were requested.
	 */
	public Game(IGameParameters params) {
		this.params = params;
		
		// Set locale if it has not been set yet.
		if (!i18n.isInitiated()) {
			if (params.getRequestedLocale() != null)
				i18n.init(params.getRequestedLocale());
			else i18n.init(Locale.getDefault());
		}
		
	}
	
	@Override
	public void create () {	
			
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
}
