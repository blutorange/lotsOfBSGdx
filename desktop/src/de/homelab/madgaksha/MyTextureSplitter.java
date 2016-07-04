package de.homelab.madgaksha;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StringBuilder;

import de.homelab.madgaksha.logging.LoggerFactory;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public class MyTextureSplitter implements ApplicationListener {
	private final static Logger LOG = LoggerFactory.getLogger(MyTexturePacker.class);

	private int animationListListIndex;
	private final EAnimationList[] animationListList;
	private final File outputDirectoryFile;
	private final StringBuilder stringBuilder;
	
	public static void main(String args[]) {
		EAnimationList animationListList[] = null;
		File outputDirectory  = null;
		MyTextureSplitter splitter = null; 
		
		if (args.length < 2) {
			printHelp();
			System.exit(-1);
		}
		
		animationListList = new EAnimationList[args.length-1];
		for (int i = 1; i != args.length; ++i) {
			try {
				EAnimationList animationList = EAnimationList.valueOf(args[i]);
				animationListList[i-1] = animationList;
			}
			catch (IllegalArgumentException e) {
				LOG.log(Level.SEVERE, "no such animation list: " + args[i], e);
				printHelp();
				System.exit(-1);
			}
		}
		
		outputDirectory = new File(args[0]);
		try {
			outputDirectory.mkdirs();
		}
		catch (SecurityException e) {
			LOG.log(Level.SEVERE, "could not create directory", e);
			printHelp();
			System.exit(-1);
		}
		if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
			LOG.warning("no such directory: " + args[1]);
			printHelp();
			System.exit(-1);
		}
		
		try {
			splitter = new MyTextureSplitter(animationListList, outputDirectory);
		}
		catch (GdxRuntimeException e) {
			LOG.warning("error while splitting texture: " + args[1]);
			printHelp();
			System.exit(-1);
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		LwjglApplication lwjglApplication = new LwjglApplication(splitter, config);
	}

	
	private MyTextureSplitter(EAnimationList[] animationListList, File outputDirectoryFile) throws GdxRuntimeException {
		this.animationListList = animationListList;
		this.outputDirectoryFile = outputDirectoryFile;
		this.animationListListIndex = 0;
		this.stringBuilder = new StringBuilder();
	}
	
	@Override
	public void create() {
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		if (animationListListIndex >= animationListList.length) Gdx.app.exit();
		else {
			EAnimationList animationList = animationListList[animationListListIndex];
			LOG.info("processing animation list: " + animationList.toString());
			extractAnimationList(animationList);
			++animationListListIndex;
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
		ResourceCache.clearAllTextureAtlas();
		ResourceCache.clearAllTexture();
		ResourceCache.clearAllAnimation();
		ResourceCache.clearAllAnimationLists();
		System.out.println(stringBuilder.toString());
	}
	
	private static void printHelp() {
		System.out.println("java -jar myTextureSplitter outputDirectory animationList {animationList}");
	}
	
	private void extractAnimationList(EAnimationList animationList) {
		String outputFilePrefix = animationList.toString();
		FileHandle outputDirectory = Gdx.files.absolute(outputDirectoryFile.getAbsolutePath());
		AtlasAnimation[] list = ResourceCache.getAnimationList(animationList);
		stringBuilder.append(animationList.toString()).append("(TextureAtlas.").append(animationList.toString()).append(", \"").append(animationList.toString()).append("\", ").append(list.length).append(")\n");
		for (int mode = 0 ; mode != list.length; ++mode) {
			AtlasAnimation animation = list[mode];
			AtlasRegion[] frames = animation.getKeyFrames();
			for (int frameNumber = 0; frameNumber != frames.length; ++frameNumber) {
				// Get pixel data for the texture
				AtlasRegion frame = frames[frameNumber];
				TextureData data = frame.getTexture().getTextureData();
				if (!data.isPrepared()) data.prepare();
				Pixmap pixmap = data.consumePixmap();
				// Crop to this frame.
				Pixmap cropPixmap = new Pixmap(frame.getRegionWidth(), frame.getRegionHeight(), data.getFormat());
				cropPixmap.drawPixmap(pixmap, -frame.getRegionX(), -frame.getRegionY());
				// Get output file name
				FileHandle outputFile = outputDirectory.child(outputFilePrefix + "_" + mode + "_" + frameNumber + ".png");
				// Write png
				try {
					PixmapIO.writePNG(outputFile, cropPixmap);
					LOG.info("wrote output file: " + outputDirectoryFile.getAbsolutePath() + "/" + outputFile.name());
				}
				catch (GdxRuntimeException e) {
					LOG.log(Level.SEVERE, "could not write output file, aborting", e);
					Gdx.app.exit();
					return;
				}
			}
		}
	}
}
//
//SOLDIER_RED_0(TextureAtlas.SOLDIER_RED, "SOLDIER_RED_0", 8)
//SOLDIER_RED_1(TextureAtlas.SOLDIER_RED, "SOLDIER_RED_1", 8)