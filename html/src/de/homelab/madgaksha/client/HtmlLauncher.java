package de.homelab.madgaksha.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.AGameParameters;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
        		final AGameParameters.Builder params = new AGameParameters.Builder(null);
        		//TODO set params
                return new Game(params.build());
        }
}