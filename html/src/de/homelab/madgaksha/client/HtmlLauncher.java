package de.homelab.madgaksha.client;

import java.util.Locale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.IGameParameters;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Game(new IGameParameters() {
					
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
				});
        }
}