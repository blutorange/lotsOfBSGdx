package de.homelab.madgaksha.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.GameParameters;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
        		final GameParameters.Builder params = new GameParameters.Builder(null);
        		//TODO set params
                return new Game(params.build());
        }
}