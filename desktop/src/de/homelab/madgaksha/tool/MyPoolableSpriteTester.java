package de.homelab.madgaksha.tool;

import java.util.logging.Logger;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.homelab.madgaksha.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.logging.LoggerFactory;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.ResourcePool;

/**
 * Utility that checks whether all game resources exist.
 * 
 * @author madgaksha
 */
public class MyPoolableSpriteTester implements ApplicationListener {
	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(MyPoolableSpriteTester.class);

	private int testIndex = 0;
	private Test[] testList = new Test[0];
	private BitmapFont font;
	private Batch batch;
	
	public static void main(String args[]) {	
		Test[] testList = new Test[] {
				new Test01(),
		};

		MyPoolableSpriteTester checker = new MyPoolableSpriteTester(testList);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		config.fullscreen = false;
		config.backgroundFPS = config.foregroundFPS = 30;
		new LwjglApplication(checker, config);
	}

	private MyPoolableSpriteTester(Test... testList) {
		if (testList != null) this.testList = testList;
	}

	@Override
	public void create() {
		font = new BitmapFont();
		batch = new SpriteBatch();
		batch.enableBlending();
		testIndex = 0; 
		ResourcePool.init();
		if (testList.length > 0) testList[0].findSubject();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (testIndex >= testList.length) {
			Gdx.app.exit();
			return;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && testList[testIndex].isSued()) {
			testList[testIndex].burySubject();
			++testIndex;
			if (testIndex < testList.length)
				testList[testIndex].findSubject();
			else {
				Gdx.app.exit();
				return;
			}
		}
		
		batch.begin();
		testList[testIndex].administerPoison(batch, font);
		font.draw(batch, testList[testIndex].getClass().getSimpleName(), 2, 14);
		batch.end();
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
	}	
	
	private static interface Test {
		public void findSubject();
		public void administerPoison(Batch batch, BitmapFont font);
		public void burySubject();
		public boolean isSued();
	}
	
	private final static class Test01 implements Test {
		private CroppableAtlasSprite sprite;
		private ShapeRenderer shapeRenderer;
		private float centerX = 250;
		private float centerY = 250;
		@Override
		public void findSubject() {
			sprite = new CroppableAtlasSprite(ResourceCache.getTexture(ETexture.TEST_POOLABLE_SPRITE_TEST));
					//ETexture.TEST_POOLABLE_SPRITE_TEST.asSprite();
			shapeRenderer = new ShapeRenderer();
		}
		@Override
		public void administerPoison(Batch batch, BitmapFont font) {		
			batch.end();
			
			shapeRenderer.begin(ShapeType.Filled);
			
			shapeRenderer.rect(centerX-50, centerY-50, 100, 100, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-50+10-2, centerY-50+40-2, 60+4, 40+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX-50+140, centerY-50, 100, 100, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-50+10-2+140, centerY-50+40-2, 60+4, 40+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX-50+240+50, centerY-50, 100, 100, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-50+240+50+10-2, centerY-50+40-2, 60+4, 40+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX-50+140, centerY-50+110, 100, 100, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-50+10-2+140, centerY-50+110+40-2, 60+4, 40+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX+290-50, centerY+110-50, 50, 50, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX+290-50+5-2, centerY+110-50+20-2, 30+4, 20+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX-50+25, centerY-50+102+25, 50, 50, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-50+25+5-2, centerY-50+102+25+20-2, 30+4, 20+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX-152, centerY-50, 50, 50, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-152+5-2, centerY-50+20-2, 30+4, 20+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.rect(centerX-152+50, centerY-50+50, 50, 50, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-152+50+5-2, centerY-50+50+20-2, 30+4, 20+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);			

			shapeRenderer.rect(centerX-50-120, centerY-50+110, 100, 100, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);
			shapeRenderer.rect(centerX-50-120+10-2, centerY-50+110+40-2, 60+4, 40+4, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
			
			shapeRenderer.end();
			
			batch.begin();

			font.draw(batch, "Above: drawing center at position (" + centerX + "," + centerY + ")", centerX-50, centerY-64);
			font.draw(batch, "Below: should be cropped so that blue lines are just visible", centerX-170, centerY+190);
			font.draw(batch, "Yellow: Region of the image including transparenty", 20, 80);
			font.draw(batch, "Blue: Region of the visible image", 20, 60);
			font.draw(batch, "Red: Sprite with whitespace cropped", 20, 40);
			font.draw(batch, "Check that the test sprite is positioned over the blue box and press enter.", 20, 140);
			font.draw(batch, "The test sprite should have a small blue border and be within the yellow box.", 20, 120);
			
			sprite.setScale(1.0f);

			sprite.setBounds(centerX-50, centerY-50, 100, 100);
			sprite.draw(batch);
			
			sprite.setCenter(centerX+140, centerY);
			sprite.draw(batch);

			sprite.setPosition(centerX+240, centerY-50);
			sprite.draw(batch);
			
			sprite.setOriginCenter();
			sprite.setPositionOrigin(centerX+140, centerY+110);
			sprite.draw(batch);
			
			sprite.setCenter(centerX+290, centerY+110);
			sprite.setSize(50, 50);
			sprite.draw(batch);
			sprite.setSize(100, 100);

			sprite.setCenter(centerX, centerY+102);
			sprite.setOriginCenter();
			sprite.setScale(0.5f);
			sprite.draw(batch);

			sprite.setCenter(centerX-102, centerY);
			sprite.setOrigin(0f,0f);
			sprite.setScale(0.5f);
			sprite.draw(batch);

			sprite.setCenter(centerX-102, centerY);
			sprite.setOriginRelative(1f,1f);
			sprite.setScale(0.5f);
			sprite.draw(batch);
			
			sprite.setScale(1.0f);
			sprite.setOrigin(40,60);
			sprite.setBounds(centerX-50-120, centerY-50+110, 100, 100);
			sprite.setCropAbsolute(12f, 12f, 12f, 12f);
			sprite.draw(batch);
			sprite.setCrop(1f,1f,1f,1f);
			
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) centerX+=2.5f;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) centerX-=2.5f;
			if (Gdx.input.isKeyPressed(Keys.UP)) centerY+=2.5f;
			if (Gdx.input.isKeyPressed(Keys.DOWN)) centerY-=2.5f;

		}
		@Override
		public void burySubject() {
			shapeRenderer.dispose();
		}
		@Override
		public boolean isSued() {
			return true;
		}
	}
}
