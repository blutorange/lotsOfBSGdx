package de.homelab.madgaksha;

import android.os.Bundle;

import java.util.Locale;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.homelab.madgaksha.Game;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Game(new IGameParameters() {
			
			@Override
			public int getRequestedWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Locale getRequestedLocale() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getRequestedHeight() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public boolean getRequestedFullscreen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getRequestedFps() {
				// TODO Auto-generated method stub
				return 0;
			}
		}), config);
	}
}
