package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.resourcecache.Resources.Ebgm;

public final class Resources {
	
	private Resources(){}
	private final static EnumMap<Ebgm, Music> bgmCache = new EnumMap<Ebgm,Music>(Ebgm.class); 

	
	@AnnotationResource
	public static enum Ebgm implements IResource {
		TEST_BGM("bgm/testbgm2_1channel.adx");
		private final static int LIMIT_BGM = 5;
		private String filename;
		private Ebgm(String f) {
			filename = f;
		}
		public static void staticClear() {
			bgmCache.clear();
		}
		@Override
		public Object getObject() {
			final FileHandle fileHandle = Gdx.files.internal(filename);
			if (fileHandle == null) return null;
			else return Gdx.audio.newMusic(fileHandle);
		}
		@Override
		public Enum<?> getEnum() {
			return this;
		}
		@Override
		public int getLimit() {
			return LIMIT_BGM;
		}
		@Override
		public void clear() {
			Ebgm.staticClear();
		}
		@Override
		public EnumMap getMap() {
			return bgmCache;
		}
	}
}