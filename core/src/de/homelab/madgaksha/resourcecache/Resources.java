package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.homelab.madgaksha.logging.Logger;

public final class Resources {
	
	private final static Logger LOG = Logger.getLogger(Resources.class);
	
	private Resources(){}
	private final static EnumMap<Ebgm, Music> bgmCache = new EnumMap<Ebgm,Music>(Ebgm.class); 

	
	@AnnotationResource
	public enum Ebgm implements IResource {
		TEST_WAV("bgm/testbgm.wav"),
		TEST_ADX_STEREO("bgm/testbgm2_2channels.adx"),
		TEST_ADX_MONO("bgm/testbgm2_1channel.adx");
		private final static int LIMIT_BGM = 5;
		private String filename;
		private Ebgm(String f) {
			filename = f;
		}
		public static void clearAll() {
			LOG.debug("clearing all bgm");
			for (Ebgm bgm : bgmCache.keySet()) {
				bgm.clear();
			}
		}
		
		@Override
		public Object getObject() {
			final FileHandle fileHandle = Gdx.files.internal(filename);
			try {
				return Gdx.audio.newMusic(fileHandle);
			}
			catch (GdxRuntimeException e) {
				LOG.debug("could not locate or open resource: " + String.valueOf(this));
				return null;
			}
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
			LOG.debug("disposing bgm: " + String.valueOf(this));
			final Music m = bgmCache.get(this);
			if (m != null) m.dispose();
			bgmCache.remove(this);
		}
		
		@Override
		public EnumMap getMap() {
			return bgmCache;
		}
	}
}