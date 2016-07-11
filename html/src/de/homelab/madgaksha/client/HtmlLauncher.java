package de.homelab.madgaksha.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.homelab.madgaksha.lotsofbs.Game;
import de.homelab.madgaksha.lotsofbs.GameParameters;
import de.homelab.madgaksha.lotsofbs.level.ALevel;
import de.homelab.madgaksha.lotsofbs.level.Level01;
import de.homelab.madgaksha.lotsofbs.player.APlayer;
import de.homelab.madgaksha.lotsofbs.player.PEstelle;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(480, 320);
	}

	@Override
	public ApplicationListener createApplicationListener() {
		APlayer player = new PEstelle();
		ALevel level = new Level01();
		final GameParameters.Builder params = new GameParameters.Builder(level, player);
		// TODO set params
		return new Game(params.build());
	}
}