package de.homelab.madgaksha.lotsofbs.level;

import org.apache.commons.lang3.StringUtils;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EMusic;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETiledMap;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

public class LevelMock extends ALevel {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LevelMock.class);

	@Override
	protected ETexture requestedBackgroundImage() {
		return ETexture.DEFAULT;
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return null;
	}

	@Override
	protected EMusic requestedBgm() {
		return null;
	}

	@Override
	protected EMusic requestedBattleBgm() {
		return null;
	}

	@Override
	protected EMusic requestedGameOverBgm() {
		return null;
	}

	@Override
	protected ETiledMap requestedTiledMap() {
		return ETiledMap.LEVEL_MOCK;
	}

	@Override
	protected String requestedI18nNameKey() {
		return StringUtils.EMPTY;
	}

	@Override
	protected String requestedI18nDescriptionKey() {
		return StringUtils.EMPTY;
	}

	@Override
	protected ETexture requestedIcon() {
		return ETexture.DEFAULT;
	}

	@Override
	protected float requestedEnemyTargetCrossAngularVelocity() {
		return 0;
	}

	@Override
	protected ETexture requestedEnemyTargetCrossTexture() {
		return ETexture.DEFAULT;
	}

	@Override
	protected ESound requestedSoundOnBattleWin() {
		return null;
	}
}
