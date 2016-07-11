package de.homelab.madgaksha.lotsofbs;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import de.homelab.madgaksha.lotsofbs.Game;
import de.homelab.madgaksha.lotsofbs.GameParameters;
import de.homelab.madgaksha.lotsofbs.level.Level01;
import de.homelab.madgaksha.lotsofbs.player.PEstelle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		final GameParameters params = new GameParameters.Builder(new Level01(), new PEstelle())
				// TODO set params
				.build();
		initialize(new Game(params), config);
	}
}
