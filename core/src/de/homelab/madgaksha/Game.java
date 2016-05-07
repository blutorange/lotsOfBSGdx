package de.homelab.madgaksha;

import java.util.Locale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.homelab.madgaksha.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.i18n.i18n;

public class Game implements ApplicationListener {
	
	SpriteBatch batch;
	Texture img;
	
	private int testx = 160;
	private int testy = 160;
	private float test1 = 0.0f;
	private float test2 = 0.0f;
	private float test3 = 0.0f;
	private float test4 = 0.0f;
	private float test5 = 0.0f;
	private float test6 = 0.0f;
	private float test7 = 100.0f;
	
	private PerspectiveCamera testc;
	
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
		
		// Setup audio system.
		AwesomeAudio.initialize();
		
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		testc = new PerspectiveCamera(45,640,480);
		testc.rotate(85, 1, 0, 0);
		testc.rotate(3, 0, 0, 1);
	}

	@Override
	public void render () {
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) testx += 3;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) testx -= 3;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) testy -= 3;
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) testy += 3;
		
		test1 = Gdx.input.isKeyPressed(Input.Keys.Q) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.A) ? -0.5f : 0.0f;
		test2 = Gdx.input.isKeyPressed(Input.Keys.W) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.S) ? -0.5f : 0.0f;
		test3 = Gdx.input.isKeyPressed(Input.Keys.E) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.D) ? -0.5f : 0.0f;
		test4 += Gdx.input.isKeyPressed(Input.Keys.R) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.F) ? -0.5f : 0.0f;
		test5 += Gdx.input.isKeyPressed(Input.Keys.T) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.G) ? -0.5f : 0.0f;
		test6 += Gdx.input.isKeyPressed(Input.Keys.Z) ? 1.2f : Gdx.input.isKeyPressed(Input.Keys.H) ? -1.2f : 0.0f;
		test7 += Gdx.input.isKeyPressed(Input.Keys.O) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.L) ? -0.5f : 0.0f;
		
		testc.position.set(160,120,5);
		testc.far = test7 < 0.1f ? 0.1f : test7;
		
		testc.rotate(test1, 1, 0, 0);
		testc.rotate(test2, 0, 1, 0);
		testc.rotate(test3, 0, 0, 1);
		testc.translate(test4, test5, test6);
		
		testc.update();
		//Gdx.app.log("info", String.valueOf(testa));
		batch.setProjectionMatrix(testc.combined);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, testx, testy);
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
		try {
			
		}
		catch (Exception e) {
			Gdx.app.error(this.getClass().getCanonicalName(),"error during cleanup",e);
		}
	}
}
