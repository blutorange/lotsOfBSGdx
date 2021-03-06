package de.homelab.madgaksha.lotsofbs.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.maps.MapProperties;

import de.homelab.madgaksha.lotsofbs.DebugMode;
import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityCallback;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.tokugi.ETokugi;
import de.homelab.madgaksha.lotsofbs.player.weapon.EWeapon;
import de.homelab.madgaksha.lotsofbs.resourcecache.EMusic;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETiledMap;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

/**
 * Only for testing purposes.
 * 
 * @author madgaksha
 */
public class Level01 extends ALevel {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Level01.class);

	@Override
	protected ETexture requestedBackgroundImage() {
		return ETexture.MAIN_BACKGROUND;
	}

	private int counterDangerNorth = 0;
	private boolean joshuaAppearsTriggered = false;

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource[] { EMusic.ROCK_ON_THE_ROAD, EMusic.SOPHISTICATED_FIGHT, EMusic.SILVER_WILL,
				EMusic.FADING_STAR, ETiledMap.LEVEL_01 };
	}

	@Override
	protected ETiledMap requestedTiledMap() {
		return ETiledMap.LEVEL_01;
	}

	@Override
	protected EMusic requestedBgm() {
		return EMusic.ROCK_ON_THE_ROAD;
	}

	@Override
	protected EMusic requestedBattleBgm() {
		return EMusic.SOPHISTICATED_FIGHT;
	}

	@Override
	protected EMusic requestedGameOverBgm() {
		return EMusic.FADING_STAR;
	}

	@Override
	protected String requestedI18nNameKey() {
		return "level.01.name";
	}

	@Override
	protected String requestedI18nDescriptionKey() {
		return "level.01.description";
	}

	@Override
	protected void setupInitialGameViewport(GameViewport viewport) {
		viewport.getCamera().position.x = getMapData().getPlayerInitialPosition().x;
		viewport.getCamera().position.y = -50.0f * getMapData().getHeightTiles();
		viewport.getCamera().position.z = 1000.0f;
		viewport.getCamera().up.x = 0;
		viewport.getCamera().up.y = 1;
	}

	@Override
	public ETexture requestedIcon() {
		return ETexture.LEVEL_01_ICON;
	}

	@Override
	public int getComponentPoolInitialSize() {
		return 1000;
	}

	@Override
	public int getComponentPoolMaxSize() {
		return 40000;
	}

	@Override
	public int getEntityPoolInitialSize() {
		return 100;
	}

	@Override
	public int getEntityPoolPoolMaxSize() {
		return 10000;
	}

	@Override
	public void setupEnvironment(Environment environment) {
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0.0f, -1.0f));
	}

	@Override
	protected float requestedEnemyTargetCrossAngularVelocity() {
		return -40.0f;
	}

	@Override
	protected ETexture requestedEnemyTargetCrossTexture() {
		return ETexture.TARGET_SELECT_CIRCLE_WHEEL;
	}

	@Override
	protected ESound requestedSoundOnBattleWin() {
		return ESound.POSAUNEN_CHORUS;
	}

	// =========================
	// Callback methods
	// =========================

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void initialDialog(MapProperties properties) {
		if (!DebugMode.activated)
			pushCutsceneLayer("cutscene/level01.initialDialog");
		else {
			MakerUtils.addTimedRunnable(2f, new IEntityCallback() {				
				@Override
				public void run(Entity entity, Object data) {
					GlobalBag.gameScore.increaseBy(1000000L);
					GlobalBag.player.obtainWeapon(EWeapon.BASIC.getWeapon());
					GlobalBag.player.obtainWeapon(EWeapon.MULTI.getWeapon());
					GlobalBag.player.learnTokugi(ETokugi.OUKAMUSOUGEKI.getTokugi());
					ComponentUtils.dealDamage(GlobalBag.playerHitCircleEntity, GlobalBag.playerHitCircleEntity, 10000000000L, true);
				}
			});
		}
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void joshuaAppears(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.joshuaAppears");
		joshuaAppearsTriggered = true;
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void signKikiRight(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.signKikiRight");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void signRiverFloat(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.signRiverFloat");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void signPoisonFlower(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.signPoisonFlower");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void signIsekaiGate(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.signIsekaiGate");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void signDangerNorth(MapProperties properties) {
		++counterDangerNorth;
		if (counterDangerNorth == 1)
			pushCutsceneLayer("cutscene/level01.signDangerNorth");
		else
			pushCutsceneLayer("cutscene/level01.signDangerNorth2");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void monologueForSign(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.monologueForSign");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void turnBackJoshua(MapProperties properties) {
		if (!joshuaAppearsTriggered)
			pushCutsceneLayer("cutscene/level01.turnBackJoshua");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void investigateTent(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.investigateTent");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void weaponTutorial(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.weaponTutorial");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void ourAmbush(MapProperties properties) {
		pushCutsceneLayer("cutscene/level01.ourAmbush");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void demoEnd(MapProperties properties) {
		if (GlobalBag.enemyKillCount >= 35)
			pushCutsceneLayer("cutscene/level01.demoEnd");
		else
			pushCutsceneLayer("cutscene/level01.demoNotEnd");
	}

	/** @param properties Map properties, can be used for setting options on the map. Must check for sanity */
	public void changeBattleBgm(MapProperties properties) {
		switchBattleBgm(EMusic.SILVER_WILL);
	}

}
