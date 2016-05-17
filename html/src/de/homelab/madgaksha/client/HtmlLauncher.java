package de.homelab.madgaksha.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.GameParameters;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.Level01;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.player.PEstelle;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
        		APlayer player = new PEstelle();
        		ALevel level = new Level01();
        		final GameParameters.Builder params = new GameParameters.Builder(level, player);
        		//TODO set params
                return new Game(params.build());
        }
}