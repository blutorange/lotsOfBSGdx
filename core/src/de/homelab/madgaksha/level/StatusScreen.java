package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.gameClock;
import static de.homelab.madgaksha.GlobalBag.gameScore;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.shapeRenderer;

import java.io.IOException;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.entity.EntityEnemy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.util.Layouter;
import de.homelab.madgaksha.util.MiscUtils;

/**
 * The info viewport for highscores etc.
 * 
 * @author madgaksha
 *
 */
public class StatusScreen {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(StatusScreen.class);

	private static final Padding NULL_PADDING = new Padding.Absolute().all(0.0f);
	
	private boolean landscapeMode;

	private final Rectangle screenBounds = new Rectangle();
	
	private final Rectangle uiLevelName = new Rectangle();
	private final Rectangle uiTime = new Rectangle();
	private final Rectangle uiScore = new Rectangle();
	private final Rectangle uiPainBar = new Rectangle();
	private final Rectangle uiWeapon = new Rectangle();
	private final Rectangle uiTokugi = new Rectangle();
	private final Rectangle uiEnemy = new Rectangle();

	private final Rectangle uiImageLevelName = new Rectangle();
	
	private final Rectangle uiColumnIconTime= new Rectangle();
	private final Rectangle uiColumnIconScore= new Rectangle();
	private final Rectangle uiColumnIconPainBar = new Rectangle();
	private final Rectangle uiColumnIconWeapon= new Rectangle();
	private final Rectangle uiColumnIconTokugi= new Rectangle();
	private final Rectangle uiColumnIconEnemy= new Rectangle();
	
	private final Rectangle uiColumnDataTime= new Rectangle();
	private final Rectangle uiColumnDataScore= new Rectangle();
	private final Rectangle uiColumnDataPainBar= new Rectangle();
	private final Rectangle uiColumnDataWeapon= new Rectangle();
	private final Rectangle uiColumnDataTokugi= new Rectangle();
	private final Rectangle uiColumnDataEnemy= new Rectangle();
	
	private final Rectangle uiPainBarMeter = new Rectangle();
	private final Rectangle uiPainBarCounter = new Rectangle();
	
	private final Rectangle uiIconTime = new Rectangle();
	private final Rectangle uiIconScore = new Rectangle();
	private final Rectangle uiIconPainBar = new Rectangle();
	private final Rectangle uiIconWeapon = new Rectangle();
	private final Rectangle uiIconTokugi = new Rectangle();
	private final Rectangle uiIconEnemy = new Rectangle();
	
	private final Rectangle uiImageWeaponMain = new Rectangle();
	private final Rectangle uiImageWeaponSub = new Rectangle();
	
	private final Rectangle uiImageTokugiMain = new Rectangle();
	private final Rectangle uiImageTokugiSub = new Rectangle();

	private final Rectangle uiImageEnemyMain = new Rectangle();
	private final Rectangle uiImageEnemySub = new Rectangle();
	
	private final Rectangle uiCellEnemyImage = new Rectangle();
	private final Rectangle uiCellEnemyPainBar = new Rectangle();

	private final Rectangle uiTimeMinuteTen = new Rectangle();
	private final Rectangle uiTimeMinuteOne = new Rectangle();
	private final Rectangle uiTimeMinuteSecondSeparator = new Rectangle();
	private final Rectangle uiTimeSecondTen = new Rectangle();
	private final Rectangle uiTimeSecondOne = new Rectangle();
	private final Rectangle uiTimeMillisecondHundred = new Rectangle();
	private final Rectangle uiTimeSecondMillisecondSeparator = new Rectangle();
	private final Rectangle uiTimeMillisecondTen = new Rectangle();
	
	private final Rectangle uiPainBarMeterFill= new Rectangle();
	
	private final Rectangle uiCellEnemyPainBarLowMid = new Rectangle();
	private final Rectangle uiCellEnemyPainBarMidHigh = new Rectangle();
	
	private final Rectangle[] uiScoreDigits = new Rectangle[]{
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
	};

	private final Rectangle[] uiPainBarDigits = new Rectangle[]{
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
			new Rectangle(),
	};
	private final Rectangle uiPainBarDigitsSeparatorGM = new Rectangle();
	private final Rectangle uiPainBarDigitsSeparatorMK = new Rectangle();
	private final Rectangle uiPainBarDigitsSeparatorK1 = new Rectangle();
	
	private Sprite textureIconEnemy;
	private Sprite textureIconScore;
	private Sprite textureIconTime;
	private Sprite textureIconPainBar;
	private Sprite textureIconTokugi;
	private Sprite textureIconWeapon;
	private Sprite textureNoEnemyMain;
	private Sprite textureNoEnemyHorizontal;
	private Sprite textureNoEnemyVertical;
	
	private final Sprite spriteNumeral[] = new Sprite[10];
	private Sprite spriteSeparatorTime;

	private NinePatch frameMain;
	private NinePatch frameCell;
	private NinePatch framePainBar;
	private NinePatch framePainBarFill;
	private NinePatch frameData;
	
	private Mode uiImageWeaponMode;
	private Mode uiImageTokugiMode;
	private Mode uiImageEnemyMode;
	
	private Color lerpColor = new Color();
	
	private EntityEnemy targettedEnemy = null;
	
	public StatusScreen(int w, int h) throws IOException {
		computeScreenDimensions(w, h);
		loadResources();
		computeUILayout();
	}

	public void update(int screenWidth, int screenHeight) {
		computeScreenDimensions(screenWidth, screenHeight);
		computeUILayout();
	}
	
	private void loadResources() throws IOException {
		frameMain = ResourceCache.getNinePatch(ENinePatch.STATUS_SCREEN_MAIN_FRAME);
		frameCell = ResourceCache.getNinePatch(ENinePatch.STATUS_SCREEN_CELL_FRAME);
		framePainBar = ResourceCache.getNinePatch(ENinePatch.STATUS_SCREEN_HP_BAR_PLAYER);
		framePainBarFill = ResourceCache.getNinePatch(ENinePatch.STATUS_SCREEN_HP_BAR_FILL);
		frameData = ResourceCache.getNinePatch(ENinePatch.STATUS_SCREEN_DATA_FRAME);
		frameData.setColor(Color.GRAY);
		
		textureIconEnemy = ETexture.STATUS_ICON_ENEMY.asSprite();
		textureIconScore = ETexture.STATUS_ICON_SCORE.asSprite();
		textureIconTime = ETexture.STATUS_ICON_TIME.asSprite();
		textureIconPainBar = ETexture.STATUS_ICON_PAINBAR.asSprite();
		textureIconTokugi = ETexture.STATUS_ICON_TOKUGI.asSprite();
		textureIconWeapon = ETexture.STATUS_ICON_WEAPON.asSprite();
		
		textureNoEnemyMain = ETexture.STATUS_ICON_NO_ENEMY_MAIN.asSprite();
		textureNoEnemyHorizontal = ETexture.STATUS_ICON_NO_ENEMY_SUBH.asSprite();
		textureNoEnemyVertical = ETexture.STATUS_ICON_NO_ENEMY_SUBV.asSprite();
		
		spriteNumeral[0] = ETexture.STATUS_ICON_NUMERAL_0.asSprite();
		spriteNumeral[1] = ETexture.STATUS_ICON_NUMERAL_1.asSprite();
		spriteNumeral[2] = ETexture.STATUS_ICON_NUMERAL_2.asSprite();
		spriteNumeral[3] = ETexture.STATUS_ICON_NUMERAL_3.asSprite();
		spriteNumeral[4] = ETexture.STATUS_ICON_NUMERAL_4.asSprite();
		spriteNumeral[5] = ETexture.STATUS_ICON_NUMERAL_5.asSprite();
		spriteNumeral[6] = ETexture.STATUS_ICON_NUMERAL_6.asSprite();
		spriteNumeral[7] = ETexture.STATUS_ICON_NUMERAL_7.asSprite();
		spriteNumeral[8] = ETexture.STATUS_ICON_NUMERAL_8.asSprite();
		spriteNumeral[9] = ETexture.STATUS_ICON_NUMERAL_9.asSprite();
		spriteSeparatorTime = ETexture.STATUS_ICON_SEPARATOR_TIME.asSprite();
		
		if (textureIconEnemy == null || textureIconScore == null || textureIconTime == null || textureIconPainBar == null || textureIconTokugi == null || textureIconWeapon == null) throw new IOException("failed to load icons");
		if (frameMain == null || frameData == null || framePainBar == null || framePainBarFill == null) throw new IOException("failed to load frame nine patches");
		for (int i = 0; i != 10; ++i) {
			if (spriteNumeral[i] == null) throw new IOException("failed to load digits");
		}
	}
		
	public void render() {
		
		batchPixel.begin();				
		
//		// TODO remove me
//		// for testing: draw rectangles
//		if (false) {
//			NinePatch myNinePatch = ResourceCache.getNinePatch(ENinePatch.STATUS_SCREEN_HP_BAR_FILL);
//
//			myNinePatch.setColor(new Color(255, 255, 255, 0.5f));
//			myNinePatch.draw(batchPixel, uiLevelName.x, uiLevelName.y, uiLevelName.width, uiLevelName.height);
//			myNinePatch.draw(batchPixel, uiTime.x, uiTime.y, uiTime.width, uiTime.height);
//			myNinePatch.draw(batchPixel, uiScore.x, uiScore.y, uiScore.width, uiScore.height);
//			myNinePatch.draw(batchPixel, uiPainBar.x, uiPainBar.y, uiPainBar.width, uiPainBar.height);
//			myNinePatch.draw(batchPixel, uiWeapon.x, uiWeapon.y, uiWeapon.width, uiWeapon.height);
//			myNinePatch.draw(batchPixel, uiTokugi.x, uiTokugi.y, uiTokugi.width, uiTokugi.height);
//			myNinePatch.draw(batchPixel, uiEnemy.x, uiEnemy.y, uiEnemy.width, uiEnemy.height);
//
//			myNinePatch.setColor(new Color(255, 0, 0, 0.5f));
//
//			myNinePatch.draw(batchPixel, uiPainBarMeter.x, uiPainBarMeter.y, uiPainBarMeter.width,
//					uiPainBarMeter.height);
//			myNinePatch.draw(batchPixel, uiPainBarCounter.x, uiPainBarCounter.y, uiPainBarCounter.width,
//					uiPainBarCounter.height);
//
//			myNinePatch.draw(batchPixel, uiColumnIconTime.x, uiColumnIconTime.y, uiColumnIconTime.width,
//					uiColumnIconTime.height);
//			myNinePatch.draw(batchPixel, uiColumnIconScore.x, uiColumnIconScore.y, uiColumnIconScore.width,
//					uiColumnIconScore.height);
//			myNinePatch.draw(batchPixel, uiColumnIconPainBar.x, uiColumnIconPainBar.y, uiColumnIconPainBar.width,
//					uiColumnIconPainBar.height);
//			myNinePatch.draw(batchPixel, uiColumnIconWeapon.x, uiColumnIconWeapon.y, uiColumnIconWeapon.width,
//					uiColumnIconWeapon.height);
//			myNinePatch.draw(batchPixel, uiColumnIconTokugi.x, uiColumnIconTokugi.y, uiColumnIconTokugi.width,
//					uiColumnIconTokugi.height);
//			myNinePatch.draw(batchPixel, uiColumnIconEnemy.x, uiColumnIconEnemy.y, uiColumnIconEnemy.width,
//					uiColumnIconEnemy.height);
//
//			myNinePatch.draw(batchPixel, uiColumnDataTime.x, uiColumnDataTime.y, uiColumnDataTime.width,
//					uiColumnDataTime.height);
//			myNinePatch.draw(batchPixel, uiColumnDataScore.x, uiColumnDataScore.y, uiColumnDataScore.width,
//					uiColumnDataScore.height);
//			myNinePatch.draw(batchPixel, uiColumnDataPainBar.x, uiColumnDataPainBar.y, uiColumnDataPainBar.width,
//					uiColumnDataPainBar.height);
//			myNinePatch.draw(batchPixel, uiColumnDataWeapon.x, uiColumnDataWeapon.y, uiColumnDataWeapon.width,
//					uiColumnDataWeapon.height);
//			myNinePatch.draw(batchPixel, uiColumnDataTokugi.x, uiColumnDataTokugi.y, uiColumnDataTokugi.width,
//					uiColumnDataTokugi.height);
//			myNinePatch.draw(batchPixel, uiColumnDataEnemy.x, uiColumnDataEnemy.y, uiColumnDataEnemy.width,
//					uiColumnDataEnemy.height);
//			myNinePatch.draw(batchPixel, uiImageLevelName.x, uiImageLevelName.y, uiImageLevelName.width,
//					uiImageLevelName.height);
//
//			myNinePatch.setColor(new Color(0, 255, 0, 0.5f));
//			myNinePatch.draw(batchPixel, uiIconTime.x, uiIconTime.y, uiIconTime.width, uiIconTime.height);
//			myNinePatch.draw(batchPixel, uiIconScore.x, uiIconScore.y, uiIconScore.width, uiIconScore.height);
//			myNinePatch.draw(batchPixel, uiIconPainBar.x, uiIconPainBar.y, uiIconPainBar.width, uiIconPainBar.height);
//			myNinePatch.draw(batchPixel, uiIconWeapon.x, uiIconWeapon.y, uiIconWeapon.width, uiIconWeapon.height);
//			myNinePatch.draw(batchPixel, uiIconTokugi.x, uiIconTokugi.y, uiIconTokugi.width, uiIconTokugi.height);
//			myNinePatch.draw(batchPixel, uiIconEnemy.x, uiIconEnemy.y, uiIconEnemy.width, uiIconEnemy.height);
//
//			myNinePatch.draw(batchPixel, uiImageWeaponMain.x, uiImageWeaponMain.y, uiImageWeaponMain.width,
//					uiImageWeaponMain.height);
//			myNinePatch.draw(batchPixel, uiImageTokugiMain.x, uiImageTokugiMain.y, uiImageTokugiMain.width,
//					uiImageTokugiMain.height);
//
//			if (uiImageWeaponMode == Mode.FULL)
//				myNinePatch.draw(batchPixel, uiImageWeaponSub.x, uiImageWeaponSub.y, uiImageWeaponSub.width,
//						uiImageWeaponSub.height);
//			if (uiImageTokugiMode == Mode.FULL)
//				myNinePatch.draw(batchPixel, uiImageTokugiSub.x, uiImageTokugiSub.y,
//						uiImageTokugiSub.width, uiImageTokugiSub.height);
//
//			myNinePatch.draw(batchPixel, uiCellEnemyImage.x, uiCellEnemyImage.y, uiCellEnemyImage.width,
//					uiCellEnemyImage.height);
//			myNinePatch.draw(batchPixel, uiCellEnemyPainBar.x, uiCellEnemyPainBar.y, uiCellEnemyPainBar.width,
//					uiCellEnemyPainBar.height);
//
//			myNinePatch.setColor(new Color(0, 0, 255, 0.5f));
//
//			myNinePatch.draw(batchPixel, uiImageEnemyMain.x, uiImageEnemyMain.y, uiImageEnemyMain.width,
//					uiImageEnemyMain.height);
//			if (uiImageEnemyMode == Mode.FULL)
//				myNinePatch.draw(batchPixel, uiImageEnemySub.x, uiImageEnemySub.y, uiImageEnemySub.width,
//						uiImageEnemySub.height);
//			
//			batchPixel.end();
//			
//			return;
//		}
		
		// Draw main frame containing all ui elements.
		frameMain.draw(batchPixel, screenBounds.x, screenBounds.y, screenBounds.width, screenBounds.height);

		// Draw columns / rows.
		frameCell.draw(batchPixel, uiTime.x, uiTime.y, uiTime.width, uiTime.height);
		frameCell.draw(batchPixel, uiScore.x, uiScore.y, uiScore.width, uiScore.height);
		frameCell.draw(batchPixel, uiPainBar.x, uiPainBar.y, uiPainBar.width, uiPainBar.height);
		frameCell.draw(batchPixel, uiWeapon.x, uiWeapon.y, uiWeapon.width, uiWeapon.height);
		frameCell.draw(batchPixel, uiTokugi.x, uiTokugi.y, uiTokugi.width, uiTokugi.height);
		frameCell.draw(batchPixel, uiEnemy.x, uiEnemy.y, uiEnemy.width, uiEnemy.height);

		// Draw data frame.
//		frameData.draw(batchPixel, uiLevelName.x, uiLevelName.y, uiLevelName.width, uiLevelName.height);
		frameData.draw(batchPixel, uiColumnDataPainBar.x, uiColumnDataPainBar.y, uiColumnDataPainBar.width,
				uiColumnDataPainBar.height);
//		frameData.draw(batchPixel, uiColumnDataWeapon.x, uiColumnDataWeapon.y, uiColumnDataWeapon.width,
//				uiColumnDataWeapon.height);
//		frameData.draw(batchPixel, uiColumnDataTokugi.x, uiColumnDataTokugi.y, uiColumnDataTokugi.width,
//				uiColumnDataTokugi.height);
		frameData.draw(batchPixel, uiColumnDataEnemy.x, uiColumnDataEnemy.y, uiColumnDataEnemy.width,
				uiColumnDataEnemy.height);

		// Draw icons.
		textureIconEnemy.draw(batchPixel);
		textureIconScore.draw(batchPixel);
		textureIconTime.draw(batchPixel);
		textureIconPainBar.draw(batchPixel);
		textureIconTokugi.draw(batchPixel);
		textureIconWeapon.draw(batchPixel);
		
		// Draw time.
		final int mt = gameClock.getMinutesTen();
		final int mo = gameClock.getMinutesOne();
		final int st = gameClock.getSecondsTen();
		final int so = gameClock.getSecondsOne();
		final int msh = gameClock.getMillisecondsHundred();
		final int mst = gameClock.getMillisecondsTen();

		spriteNumeral[mt].setBounds(uiTimeMinuteTen.x, uiTimeMinuteTen.y, uiTimeMinuteTen.width,
				uiTimeMinuteTen.height);
		spriteNumeral[mt].draw(batchPixel);

		spriteNumeral[mo].setBounds(uiTimeMinuteOne.x, uiTimeMinuteOne.y, uiTimeMinuteOne.width,
				uiTimeMinuteOne.height);
		spriteNumeral[mo].draw(batchPixel);

		spriteNumeral[st].setBounds(uiTimeSecondTen.x, uiTimeSecondTen.y, uiTimeSecondTen.width,
				uiTimeSecondTen.height);
		spriteNumeral[st].draw(batchPixel);

		spriteNumeral[so].setBounds(uiTimeSecondOne.x, uiTimeSecondOne.y, uiTimeSecondOne.width,
				uiTimeSecondOne.height);
		spriteNumeral[so].draw(batchPixel);

		spriteNumeral[msh].setBounds(uiTimeMillisecondHundred.x, uiTimeMillisecondHundred.y,
				uiTimeMillisecondHundred.width, uiTimeMillisecondHundred.height);
		spriteNumeral[msh].draw(batchPixel);

		spriteNumeral[mst].setBounds(uiTimeMillisecondTen.x, uiTimeMillisecondTen.y, uiTimeMillisecondTen.width,
				uiTimeMillisecondTen.height);
		spriteNumeral[mst].draw(batchPixel);

		spriteSeparatorTime.setBounds(uiTimeMinuteSecondSeparator.x, uiTimeMinuteSecondSeparator.y,
				uiTimeMinuteSecondSeparator.width, uiTimeMinuteSecondSeparator.height);
		spriteSeparatorTime.draw(batchPixel);

		spriteSeparatorTime.setBounds(uiTimeSecondMillisecondSeparator.x, uiTimeSecondMillisecondSeparator.y,
				uiTimeSecondMillisecondSeparator.width, uiTimeSecondMillisecondSeparator.height);
		spriteSeparatorTime.draw(batchPixel);

		// TODO remove me
		player.takeDamage(91437L * 13);
		gameScore.increaseBy(1);

		// Score.
		for (int i = uiScoreDigits.length; i-- > 0;) {
			spriteNumeral[gameScore.getDigit(i)].setBounds(uiScoreDigits[i].x, uiScoreDigits[i].y,
					uiScoreDigits[i].width, uiScoreDigits[i].height);
			spriteNumeral[gameScore.getDigit(i)].draw(batchPixel);
		}

		// HP meter counter.
		for (int i = uiPainBarDigits.length; i-- > 0;) {
			spriteNumeral[player.getPainPointsDigit(i)].setBounds(uiPainBarDigits[i].x, uiPainBarDigits[i].y,
					uiPainBarDigits[i].width, uiPainBarDigits[i].height);
			spriteNumeral[player.getPainPointsDigit(i)].draw(batchPixel);
		}

		// HP meter player.
		float ratio = player.getPainPointsRatio();
		if (ratio < 0.5)
			framePainBarFill.getColor().set(player.getPainBarColorLow()).lerp(player.getPainBarColorMid(),
					player.getPainPointsRatio() * 2.0f);
		else
			framePainBarFill.getColor().set(player.getPainBarColorMid()).lerp(player.getPainBarColorHigh(),
					(player.getPainPointsRatio() - 0.5f) * 2.0f);

		if (landscapeMode) {
			framePainBarFill.draw(batchPixel, uiPainBarMeterFill.x, uiPainBarMeterFill.y, uiPainBarMeterFill.width,
					uiPainBarMeterFill.height * player.getPainPointsRatio());
		} else {
			framePainBarFill.draw(batchPixel, uiPainBarMeterFill.x, uiPainBarMeterFill.y,
					uiPainBarMeterFill.width * player.getPainPointsRatio(), uiPainBarMeterFill.height);
		}

		batchPixel.end();

		// HP meter enemy.
		shapeRenderer.begin(ShapeType.Filled);
		ratio = targettedEnemy == null ? 0.0f : 1.0f-targettedEnemy.getPainPointsRatio();
		if (ratio > 0.5f) {
			lerpColor.set(level.getEnemyPainBarColorMid()).lerp(level.getEnemyPainBarColorHigh(), ratio);
			if (landscapeMode) {
				shapeRenderer.rect(uiCellEnemyPainBarLowMid.x, uiCellEnemyPainBarLowMid.y,
						uiCellEnemyPainBarLowMid.width, uiCellEnemyPainBarLowMid.height,
						level.getEnemyPainBarColorLow(), level.getEnemyPainBarColorLow(),
						level.getEnemyPainBarColorMid(), level.getEnemyPainBarColorMid());
				shapeRenderer.rect(uiCellEnemyPainBarMidHigh.x, uiCellEnemyPainBarMidHigh.y,
						uiCellEnemyPainBarMidHigh.width, (uiCellEnemyPainBarMidHigh.height * (ratio - 0.5f)) * 2.0f,
						level.getEnemyPainBarColorMid(), level.getEnemyPainBarColorMid(), lerpColor, lerpColor);
			} else {
				shapeRenderer.rect(uiCellEnemyPainBarLowMid.x, uiCellEnemyPainBarLowMid.y,
						uiCellEnemyPainBarLowMid.width, uiCellEnemyPainBarLowMid.height,
						level.getEnemyPainBarColorLow(), level.getEnemyPainBarColorMid(),
						level.getEnemyPainBarColorMid(), level.getEnemyPainBarColorLow());
				shapeRenderer.rect(uiCellEnemyPainBarMidHigh.x, uiCellEnemyPainBarMidHigh.y,
						(uiCellEnemyPainBarMidHigh.width * (ratio - 0.5f)) * 2.0f, uiCellEnemyPainBarMidHigh.height,
						level.getEnemyPainBarColorMid(), lerpColor, lerpColor, level.getEnemyPainBarColorMid());
			}
		} else {
			lerpColor.set(level.getEnemyPainBarColorLow()).lerp(level.getEnemyPainBarColorMid(), ratio * 2.0f);
			if (landscapeMode) {
				shapeRenderer.rect(uiCellEnemyPainBarLowMid.x, uiCellEnemyPainBarLowMid.y,
						uiCellEnemyPainBarLowMid.width, uiCellEnemyPainBarLowMid.height * ratio * 2.0f,
						level.getEnemyPainBarColorLow(), level.getEnemyPainBarColorLow(), lerpColor, lerpColor);
			} else {
				shapeRenderer.rect(uiCellEnemyPainBarLowMid.x, uiCellEnemyPainBarLowMid.y,
						uiCellEnemyPainBarLowMid.width * ratio * 2.0f, uiCellEnemyPainBarLowMid.height,
						level.getEnemyPainBarColorLow(), lerpColor, lerpColor, level.getEnemyPainBarColorLow());
			}
		}

		shapeRenderer.end();
		
		batchPixel.begin();
		
		// Draw images for level name, weapon, tokugi and enemy.
		// We draw them last because they use different texture files.

		player.getWeapon().getIconMain().draw(batchPixel);

		player.getTokugi().getIconMain().draw(batchPixel);

		if (targettedEnemy != null) targettedEnemy.getIconMain().draw(batchPixel);
		else textureNoEnemyMain.draw(batchPixel);

		// Level
		level.getIconHorizontal().draw(batchPixel);

		
		if (landscapeMode) {
			// Weapon
			if (uiImageWeaponMode == Mode.FULL)
				player.getWeapon().getIconSubVertical().draw(batchPixel);
			
			// Tokugi
			if (uiImageTokugiMode == Mode.FULL)
				player.getTokugi().getIconSubVertical().draw(batchPixel);
			
			// Enemy
			if (uiImageEnemyMode == Mode.FULL) {
				if (targettedEnemy != null) targettedEnemy.getIconSubVertical().draw(batchPixel);
				else textureNoEnemyVertical.draw(batchPixel);
			}
		}
		else {
			// Weapon
			if (uiImageWeaponMode == Mode.FULL)
				player.getWeapon().getIconSubHorizontal().draw(batchPixel);
			
			// Tokugi
			if (uiImageTokugiMode == Mode.FULL)
				player.getTokugi().getIconSubHorizontal().draw(batchPixel);
			
			// Enemy
			if (uiImageEnemyMode == Mode.FULL) {
				if (targettedEnemy != null) targettedEnemy.getIconSubHorizontal().draw(batchPixel);
				else textureNoEnemyHorizontal.draw(batchPixel);
			}
		}
		
		// HP bar overlay.
		framePainBar.draw(batchPixel, uiPainBarMeter.x, uiPainBarMeter.y, uiPainBarMeter.width, uiPainBarMeter.height);
		framePainBar.draw(batchPixel, uiCellEnemyPainBar.x, uiCellEnemyPainBar.y, uiCellEnemyPainBar.width,
				uiCellEnemyPainBar.height);

		batchPixel.end();
		
	}	
	
	/** Whether this status screen is landscape (width > height) or portrait-like. */
	public boolean isLandscapeMode() {
		return landscapeMode;
	}
	
	/**
	 * Switches target to the specified enemy and shows the enemy's info in this statusScreen.
	 * @param enemy The enemy to target
	 */
	public void targetEnemy(Entity enemy) {
		if (enemy instanceof EntityEnemy) {
			targettedEnemy = (EntityEnemy)enemy;
		}
		else targettedEnemy = null;
		setEnemyImageBounds();
	}
	
	/** Removes the enemy target and displays "no enemy targetted".*/
	public void untargetEnemy() {
		targettedEnemy = null;
		setEnemyImageBounds();
	}
	
	private void computeScreenDimensions(int screenWidth, int screenHeight) {
		// Compute dimensions of the game window in pixels.
		Vector2 screenDimensions = GameViewport.computeGameViewportDimensions(screenWidth, screenHeight);

		// Set info window to the right / top.
		int gameWidth = (int) screenDimensions.x;
		int gameHeight = (int) screenDimensions.y;
		int infoWidth, infoHeight;
		int infoX, infoY;
		if (screenWidth > screenHeight) {
			// Landscape, status screen to the right 
			infoWidth = screenWidth - gameWidth;
			infoHeight = screenHeight;
			infoX = gameWidth;
			infoY = 0;
		} else {
			// Portrait, status screen to the top
			infoHeight = screenHeight - gameHeight;
			infoWidth = screenWidth;
			infoX = 0;
			infoY = gameHeight;
		}

		landscapeMode = infoWidth > infoHeight;
		
		// Store info.
		screenBounds.set(infoX, infoY, infoWidth, infoHeight);		
	}
	
	private void computeUILayout() {
		if (landscapeMode) computeUILayoutLandscape();
		else computeUILayoutPortrait();
		
		// Position for cell icons.
		textureIconEnemy.setBounds(uiIconEnemy.x, uiIconEnemy.y, uiIconEnemy.width, uiIconEnemy.height);
		textureIconScore.setBounds(uiIconScore.x, uiIconScore.y, uiIconScore.width, uiIconScore.height);
		textureIconPainBar.setBounds(uiIconPainBar.x, uiIconPainBar.y, uiIconPainBar.width, uiIconPainBar.height);
		textureIconTime.setBounds(uiIconTime.x, uiIconTime.y, uiIconTime.width, uiIconTime.height);
		textureIconTokugi.setBounds(uiIconTokugi.x, uiIconTokugi.y, uiIconTokugi.width, uiIconTokugi.height);
		textureIconWeapon.setBounds(uiIconWeapon.x, uiIconWeapon.y, uiIconWeapon.width, uiIconWeapon.height);

		// Level
		level.getIconHorizontal().setBounds(uiImageLevelName.x, uiImageLevelName.y, uiImageLevelName.width, uiImageLevelName.height);
		
		// Position for image icons level, weapon and tokugi.
		player.getWeapon().getIconMain().setBounds(uiImageWeaponMain.x, uiImageWeaponMain.y, uiImageWeaponMain.width,
				uiImageWeaponMain.height);
		player.getTokugi().getIconMain().setBounds(uiImageTokugiMain.x, uiImageTokugiMain.y, uiImageTokugiMain.width,
				uiImageTokugiMain.height);

		if (landscapeMode) {
			// Weapon
			if (uiImageWeaponMode == Mode.FULL)
				player.getWeapon().getIconSubVertical().setBounds(uiImageWeaponSub.x, uiImageWeaponSub.y, uiImageWeaponSub.width, uiImageWeaponSub.height);
			// Tokugi
			if (uiImageTokugiMode == Mode.FULL)
				player.getTokugi().getIconSubVertical().setBounds(uiImageTokugiSub.x, uiImageTokugiSub.y, uiImageTokugiSub.width, uiImageTokugiSub.height);
		}
		else {
			// Weapon
			if (uiImageWeaponMode == Mode.FULL)
				player.getWeapon().getIconSubHorizontal().setBounds(uiImageWeaponSub.x, uiImageWeaponSub.y, uiImageWeaponSub.width, uiImageWeaponSub.height);
			// Tokugi
			if (uiImageTokugiMode == Mode.FULL)
				player.getTokugi().getIconSubHorizontal().setBounds(uiImageTokugiSub.x, uiImageTokugiSub.y, uiImageTokugiSub.width, uiImageTokugiSub.height);
		}
		
		// Image icons for enemy.
		setEnemyImageBounds();
	}

	private void setEnemyImageBounds() {
		if (targettedEnemy != null) {
			targettedEnemy.getIconMain().setBounds(uiImageEnemyMain.x,uiImageEnemyMain.y,uiImageEnemyMain.width,uiImageEnemyMain.height);
			if (landscapeMode)
				targettedEnemy.getIconSubVertical().setBounds(uiImageEnemySub.x,uiImageEnemySub.y,uiImageEnemySub.width,uiImageEnemySub.height);
			else 
				targettedEnemy.getIconSubHorizontal().setBounds(uiImageEnemySub.x,uiImageEnemySub.y,uiImageEnemySub.width,uiImageEnemySub.height);
		}
		else {
			textureNoEnemyMain.setBounds(uiImageEnemyMain.x,uiImageEnemyMain.y,uiImageEnemyMain.width,uiImageEnemyMain.height);
			if (landscapeMode)
				textureNoEnemyVertical.setBounds(uiImageEnemySub.x,uiImageEnemySub.y,uiImageEnemySub.width,uiImageEnemySub.height);
			else 
				textureNoEnemyHorizontal.setBounds(uiImageEnemySub.x,uiImageEnemySub.y,uiImageEnemySub.width,uiImageEnemySub.height);
		}
	}
	
	private void computeUILayoutPortrait() {

		// ====================================
		//  Layout main containers vertically.
		// ====================================
		
		Layouter.layoutVerticallyWithRelativeHeight(screenBounds, PortraitLayout.padding1, new float[]{
				PortraitLayout.widgetEnemyHeight,
				PortraitLayout.widgetTokugiHeight,
				PortraitLayout.widgetWeaponHeight,
				PortraitLayout.widgetPainBarHeight,
				PortraitLayout.widgetScoreHeight,
				PortraitLayout.widgetTimeHeight,
				PortraitLayout.widgetLevelNameHeight
			}, new Rectangle[]{
				uiEnemy,
				uiTokugi,
				uiWeapon,
				uiPainBar,
				uiScore,
				uiTime,
				uiLevelName,
		});	
			
		
		// ====================================
		//     Layout icon and data column.
		// ====================================

		Layouter.layoutLeftRightWithRelativeWidth(uiEnemy, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconEnemy, uiColumnDataEnemy);	

		Layouter.layoutLeftRightWithRelativeWidth(uiTokugi, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconTokugi, uiColumnDataTokugi);	

		Layouter.layoutLeftRightWithRelativeWidth(uiWeapon, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconWeapon, uiColumnDataWeapon);

		Layouter.layoutLeftRightWithRelativeWidth(uiPainBar, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconPainBar, uiColumnDataPainBar);
		
		Layouter.layoutLeftRightWithRelativeWidth(uiScore, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconScore, uiColumnDataScore);

		Layouter.layoutLeftRightWithRelativeWidth(uiTime, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconTime, uiColumnDataTime);
			
		// Level name text/image dimensions (1 column)
		uiImageLevelName.set(Layouter.layoutAlignedInBoxAndKeepAspectRatio(uiLevelName, NULL_PADDING,
				PortraitLayout.imageLevelNameAspectRatio, 0.5f, 0.5f));
		
		// HP bar player (2 rows).
		Layouter.layoutTopBottomWithRelativeWidth(uiColumnDataPainBar, PortraitLayout.padding2,
				PortraitLayout.painBarMeterHeight, PortraitLayout.painBarCounterHeight,	uiPainBarMeter, uiPainBarCounter);
		
		uiPainBarMeterFill.x = uiPainBarMeter.x + framePainBar.getPadLeft();
		uiPainBarMeterFill.y = uiPainBarMeter.y + framePainBar.getPadBottom();
		uiPainBarMeterFill.width = uiPainBarMeter.width - framePainBar.getPadLeft() - framePainBar.getPadRight();
		uiPainBarMeterFill.height = uiPainBarMeter.height - framePainBar.getPadTop() - framePainBar.getPadBottom();		
		
		// ====================================
		//            Layout icons.
		// ====================================
		
		uiIconTime.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTime, PortraitLayout.padding3,
				PortraitLayout.iconAspectRatio));
		uiIconScore.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconScore, PortraitLayout.padding3,
				PortraitLayout.iconAspectRatio));
		uiIconPainBar.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconPainBar, PortraitLayout.padding3,
				PortraitLayout.iconAspectRatio));
		uiIconWeapon.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconWeapon, PortraitLayout.padding3,
				PortraitLayout.iconAspectRatio));
		uiIconTokugi.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTokugi, PortraitLayout.padding3,
				PortraitLayout.iconAspectRatio));
		uiIconEnemy.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconEnemy, PortraitLayout.padding3,
				PortraitLayout.iconAspectRatio));				
		
		
		// ====================================
		//         Layout data fields.
		// ====================================

		// Weapon and tokugi cell.
		uiImageWeaponMode = Layouter.layoutLeftRightWithAspectRatio(uiColumnDataWeapon, PortraitLayout.paddingWeaponImage,
				PortraitLayout.imageWeaponLeftAspectRatio, PortraitLayout.imageWeaponRightAspectRatio, 1.0f, 0.0f,
				uiImageWeaponMain, uiImageWeaponSub);	
		
		uiImageTokugiMode = Layouter.layoutLeftRightWithAspectRatio(uiColumnDataTokugi, PortraitLayout.paddingTokugiImage,
				PortraitLayout.imageTokugiLeftAspectRatio, PortraitLayout.imageTokugiRightAspectRatio, 1.0f, 0.0f,
				uiImageTokugiMain, uiImageTokugiSub);
		
		// Enemy cell.
		Layouter.layoutTopBottomWithRelativeWidth(uiColumnDataEnemy, PortraitLayout.paddingEnemyDataPainBar,
				PortraitLayout.cellEnemyImageHeight, PortraitLayout.cellEnemyPainBarHeight,	uiCellEnemyImage, uiCellEnemyPainBar);

		// Enemy pain bar
		uiImageEnemyMode = Layouter.layoutLeftRightWithAspectRatio(uiCellEnemyImage, PortraitLayout.paddingEnemyIcons,
				PortraitLayout.imageEnemyLeftAspectRatio, PortraitLayout.imageEnemyRightAspectRatio, 1.0f, 0.0f,
				uiImageEnemyMain, uiImageEnemySub);

		uiCellEnemyPainBarLowMid.x = uiCellEnemyPainBar.x + framePainBar.getPadLeft();
		uiCellEnemyPainBarLowMid.y = uiCellEnemyPainBar.y + framePainBar.getPadBottom();
		uiCellEnemyPainBarLowMid.width = uiCellEnemyPainBar.width * 0.5f - framePainBar.getPadLeft();
		uiCellEnemyPainBarLowMid.height = uiCellEnemyPainBar.height - framePainBar.getPadTop() - framePainBar.getPadBottom();
		
		uiCellEnemyPainBarMidHigh.x = uiCellEnemyPainBar.x + uiCellEnemyPainBar.width * 0.5f;
		uiCellEnemyPainBarMidHigh.y = uiCellEnemyPainBar.y + framePainBar.getPadBottom();
		uiCellEnemyPainBarMidHigh.width = uiCellEnemyPainBar.width * 0.5f - framePainBar.getPadRight();
		uiCellEnemyPainBarMidHigh.height = uiCellEnemyPainBar.height - framePainBar.getPadTop() - framePainBar.getPadBottom();

		// Time digits
		PortraitLayout.paddingTime.at(uiColumnDataTime);
		float fontTimeHeight = uiColumnDataTime.height - PortraitLayout.paddingTime.top() + PortraitLayout.paddingTime.bottom();
		float fontTimeWidth = fontTimeHeight / spriteNumeral[0].getRegionHeight() * spriteNumeral[0].getRegionWidth();
		Layouter.layoutHorizontallyWithAbsoluteWidth(uiColumnDataTime, PortraitLayout.paddingTime, 0.5f, true, fontTimeWidth, new Rectangle[]{ 
				uiTimeMinuteTen,
				uiTimeMinuteOne,
				uiTimeMinuteSecondSeparator,
				uiTimeSecondTen,
				uiTimeSecondOne,
				uiTimeSecondMillisecondSeparator,
				uiTimeMillisecondHundred,
				uiTimeMillisecondTen
			}
		);
		
		// Score digits
		PortraitLayout.paddingScore.at(uiColumnDataTime);
		float fontScoreHeight = uiColumnDataScore.height - PortraitLayout.paddingScore.top() + PortraitLayout.paddingScore.bottom();
		float fontScoreWidth = fontScoreHeight / spriteNumeral[0].getRegionHeight() * spriteNumeral[0].getRegionWidth();
		Layouter.layoutHorizontallyWithAbsoluteWidth(uiColumnDataScore, PortraitLayout.paddingScore, 0.5f, true, fontScoreWidth, uiScoreDigits);

		// Pain bar digits.
		PortraitLayout.paddingPainBar.at(uiPainBarCounter);
		float fontPainBarHeight = uiPainBarCounter.height - PortraitLayout.paddingPainBar.top() + PortraitLayout.paddingPainBar.bottom();
		float fontPainBarWidth = fontPainBarHeight/ spriteNumeral[0].getRegionHeight() * spriteNumeral[0].getRegionWidth();
		Layouter.layoutHorizontallyWithAbsoluteWidth(uiPainBarCounter, PortraitLayout.paddingPainBar, 0.5f, true, fontPainBarWidth, new Rectangle[]{
				uiPainBarDigits[0],
				uiPainBarDigitsSeparatorGM,
				uiPainBarDigits[1],
				uiPainBarDigits[2],
				uiPainBarDigits[3],
				uiPainBarDigitsSeparatorMK,
				uiPainBarDigits[4],
				uiPainBarDigits[5],
				uiPainBarDigits[6],
				uiPainBarDigitsSeparatorK1,
				uiPainBarDigits[7],
				uiPainBarDigits[8],
				uiPainBarDigits[9]
		});
	}
	
	private void computeUILayoutLandscape() {
		
		// =====================================
		//  Layout main containers horizontally.
		//  and level name vertically above.
		// =====================================
		
		Rectangle topPane = new Rectangle();
		Rectangle bottomPane = new Rectangle();
		Layouter.layoutTopBottomWithRelativeWidth(screenBounds, LandscapeLayout.padding1,
				LandscapeLayout.upperRowHeight, LandscapeLayout.lowerRowHeight, topPane, bottomPane);

		// Level name on top pane.
		uiImageLevelName.set(Layouter.layoutAlignedInBoxAndKeepAspectRatio(topPane, NULL_PADDING,
				LandscapeLayout.imageLevelNameAspectRatio, 0.5f, 0.5f));
		
		// Rest, other than level name on bottom pane.		
		Layouter.layoutHorizontallyWithRelativeWidth(bottomPane, LandscapeLayout.padding1, new float[]{
				LandscapeLayout.widgetTimeWidth,
				LandscapeLayout.widgetScoreWidth,
				LandscapeLayout.widgetPainBarWidth,
				LandscapeLayout.widgetWeaponWidth,
				LandscapeLayout.widgetTokugiWidth,
				LandscapeLayout.widgetEnemyWidth
			}, new Rectangle[]{
				uiTime,
				uiScore,
				uiPainBar,
				uiWeapon,
				uiTokugi,
				uiEnemy
		});	
		
		// ====================================
		//     Layout icon and data row.
		// ====================================

		Layouter.layoutTopBottomWithRelativeWidth(uiEnemy, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconEnemy, uiColumnDataEnemy);	

		Layouter.layoutTopBottomWithRelativeWidth(uiTokugi, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconTokugi, uiColumnDataTokugi);
		
		Layouter.layoutTopBottomWithRelativeWidth(uiPainBar, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconPainBar, uiColumnDataPainBar);

		Layouter.layoutTopBottomWithRelativeWidth(uiWeapon, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconWeapon, uiColumnDataWeapon);
		
		Layouter.layoutTopBottomWithRelativeWidth(uiScore, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconScore, uiColumnDataScore);

		Layouter.layoutTopBottomWithRelativeWidth(uiTime, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconTime, uiColumnDataTime);
		
//		// Level name text/image dimensions (1 column)
//		uiImageLevelName.set(Layouter.layoutAlignedInBoxAndKeepAspectRatio(uiLevelName, NULL_PADDING,
//				LandscapeLayout.imageLevelNameAspectRatio, 0.5f, 0.5f));		
		
		// HP bar player (no counter).
		uiPainBarMeterFill.x = uiColumnDataPainBar.x + framePainBar.getPadLeft();
		uiPainBarMeterFill.y = uiColumnDataPainBar.y + framePainBar.getPadBottom();
		uiPainBarMeterFill.width = uiColumnDataPainBar.width - framePainBar.getPadLeft() - framePainBar.getPadRight();
		uiPainBarMeterFill.height = uiColumnDataPainBar.height - framePainBar.getPadTop() - framePainBar.getPadBottom();		

		
		// ====================================
		//            Layout icons.
		// ====================================

		uiIconTime.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTime, LandscapeLayout.padding3,
				LandscapeLayout.iconAspectRatio));
		uiIconPainBar.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconPainBar, LandscapeLayout.padding3,
				PortraitLayout.iconAspectRatio));
		uiIconScore.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconScore, LandscapeLayout.padding3,
				LandscapeLayout.iconAspectRatio));
		uiIconWeapon.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconWeapon, LandscapeLayout.padding3,
				LandscapeLayout.iconAspectRatio));
		uiIconTokugi.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTokugi, LandscapeLayout.padding3,
				LandscapeLayout.iconAspectRatio));
		uiIconEnemy.set(Layouter.layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconEnemy, LandscapeLayout.padding3,
				LandscapeLayout.iconAspectRatio));
		

		
		// ====================================
		//         Layout data fields.
		// ====================================

		// Weapon and tokugi image.
		uiImageWeaponMode = Layouter.layoutTopBottomWithAspectRatio(uiColumnDataWeapon, LandscapeLayout.paddingWeaponImage,
				LandscapeLayout.imageWeaponTopInverseAspectRatio, LandscapeLayout.imageWeaponBottomInverseAspectRatio,
				0.0f, 1.0f, uiImageWeaponMain, uiImageWeaponSub);

		uiImageTokugiMode = Layouter.layoutTopBottomWithAspectRatio(uiColumnDataTokugi, LandscapeLayout.paddingTokugiImage,
				LandscapeLayout.imageTokugiTopInverseAspectRatio, LandscapeLayout.imageTokugiBottomInverseAspectRatio,
				0.0f, 1.0f, uiImageTokugiMain, uiImageTokugiSub);

		// Enemy pain bar
		Layouter.layoutTopBottomWithRelativeWidth(uiColumnDataEnemy, LandscapeLayout.paddingEnemyIcons,
				LandscapeLayout.cellEnemyImageHeight, LandscapeLayout.cellEnemyPainBarHeight, uiCellEnemyImage,
				uiCellEnemyPainBar);
		
		uiCellEnemyPainBarLowMid.x = uiCellEnemyPainBar.x + framePainBar.getPadLeft();
		uiCellEnemyPainBarLowMid.y = uiCellEnemyPainBar.y + framePainBar.getPadBottom();
		uiCellEnemyPainBarLowMid.width = uiCellEnemyPainBar.width * 0.5f - framePainBar.getPadLeft();
		uiCellEnemyPainBarLowMid.height = uiCellEnemyPainBar.height - framePainBar.getPadBottom();
		
		uiCellEnemyPainBarMidHigh.x = uiCellEnemyPainBar.x + uiCellEnemyPainBar.width* 0.5f;
		uiCellEnemyPainBarMidHigh.y = uiCellEnemyPainBar.y + framePainBar.getPadBottom();
		uiCellEnemyPainBarMidHigh.width = uiCellEnemyPainBar.width * 0.5f - framePainBar.getPadRight();
		uiCellEnemyPainBarMidHigh.height = uiCellEnemyPainBar.height - framePainBar.getPadTop() - framePainBar.getPadBottom();

		// Enemy image.
		uiImageEnemyMode = Layouter.layoutTopBottomWithAspectRatio(uiCellEnemyImage, LandscapeLayout.paddingEnemyDataPainBar,
				LandscapeLayout.imageEnemyTopInverseAspectRatio, LandscapeLayout.imageEnemyBottomInverseAspectRatio,
				0.0f, 1.0f, uiImageEnemyMain, uiImageEnemySub);
		
		// Time digits
		LandscapeLayout.paddingTime.at(uiColumnDataTime);
		float fontTimeWidth = uiColumnDataTime.width - LandscapeLayout.paddingTime.left() + LandscapeLayout.paddingTime.right();
		float fontTimeHeight = fontTimeWidth / spriteNumeral[0].getRegionWidth() * spriteNumeral[0].getRegionHeight();
		Layouter.layoutVerticallyWithAbsoluteHeight(uiColumnDataTime, LandscapeLayout.paddingTime, 0.5f, true, fontTimeHeight, new Rectangle[]{ 
				uiTimeMillisecondTen,
				uiTimeMillisecondHundred,
				uiTimeSecondMillisecondSeparator,
				uiTimeSecondOne,
				uiTimeSecondTen,
				uiTimeMinuteSecondSeparator,
				uiTimeMinuteOne,
				uiTimeMinuteTen,
			}
		);
		
		// Score digits
		LandscapeLayout.paddingScore.at(uiColumnDataTime);
		float fontScoreWidth = uiColumnDataScore.width - LandscapeLayout.paddingScore.right() + LandscapeLayout.paddingScore.left();
		float fontScoreHeight = fontScoreWidth / spriteNumeral[0].getRegionWidth() * spriteNumeral[0].getRegionHeight();
		Layouter.layoutVerticallyWithAbsoluteHeight(uiColumnDataScore, LandscapeLayout.paddingScore, 0.5f, true, fontScoreHeight, MiscUtils.reverseCopyArray(uiScoreDigits));
		
		// Pain bar digits.
//		LandscapeLayout.paddingPainBar.at(uiPainBarCounter);
//		float fontPainBarWidth = uiPainBarCounter.width - LandscapeLayout.paddingPainBar.left() + LandscapeLayout.paddingPainBar.right();
//		float fontPainBarHeight = fontPainBarWidth / spriteNumeral[0].getRegionWidth() * spriteNumeral[0].getRegionHeight();
//		Layouter.layoutVerticallyWithAbsoluteHeight(uiPainBarCounter, LandscapeLayout.paddingPainBar, 0.5f, true, fontPainBarHeight, new Rectangle[]{
//				uiPainBarDigits[9],
//				uiPainBarDigits[8],
//				uiPainBarDigits[7],
//				uiPainBarDigitsSeparatorK1,
//				uiPainBarDigits[6],
//				uiPainBarDigits[5],
//				uiPainBarDigits[4],
//				uiPainBarDigitsSeparatorMK,
//				uiPainBarDigits[3],
//				uiPainBarDigits[2],
//				uiPainBarDigits[1],
//				uiPainBarDigitsSeparatorGM,
//				uiPainBarDigits[0],
//		});

	}
	
	
	private final static class PortraitLayout {
		// Level one, box.
		private final static Padding padding1 = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.016f).right(0.016f)
				.vertical(0.016f).horizontal(0.016f);

		// Level two, box in a box.
		private final static Padding padding2 = new Padding.Relative()
				.top(0.1f).bottom(0.1f)
				.left(0.016f).right(0.016f)
				.vertical(0.1f).horizontal(0.016f);
		
		// Level tree, box in a box in a box.
		private final static Padding padding3 = new Padding.Relative()
				.top(0.1f).bottom(0.1f)
				.left(0.1f).right(0.1f)
				.vertical(0.1f).horizontal(0.1f);
		
		// More paddings.
		public static final Padding paddingEnemyIcons = new Padding.Relative()
				.top(0.0f).bottom(0.0f)
				.left(0.1f).right(0.1f)
				.vertical(0.0f).horizontal(0.03f);
		private final static Padding paddingTime = new Padding.Relative()
				.top(0.3f).bottom(0.3f)
				.left(0.016f).right(0.016f)
				.vertical(0.0f).horizontal(0.0f);
		private final static Padding paddingScore = new Padding.Relative()
				.top(0.3f).bottom(0.3f)
				.left(0.016f).right(0.016f)
				.vertical(0.0f).horizontal(0.0f);
		private final static Padding paddingPainBar = new Padding.Absolute().all(1.0f);
		private static final Padding paddingTokugiImage = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.03f).right(0.03f)
				.vertical(0.03f).horizontal(0.03f);

		private static final Padding paddingWeaponImage = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.03f).right(0.03f)
				.vertical(0.03f).horizontal(0.03f);

		public static final Padding paddingEnemyDataPainBar = new Padding.Relative()
				.top(0.06f).bottom(0.06f)
				.left(0.04f).right(0.04f)
				.horizontal(0.04f).vertical(0.04f);

		
		private final static float widgetLevelNameHeight = 10;
		private final static float widgetTimeHeight = 8;
		private final static float widgetScoreHeight = 8;
		private final static float widgetPainBarHeight = 8;
		private final static float widgetWeaponHeight = 16;
		private final static float widgetTokugiHeight = 16;
		private final static float widgetEnemyHeight = 20;
		
		private final static float columnIconWidth = 20;
		private final static float columnDataWidth = 80;
		
		private final static float painBarMeterHeight = 50;
		private final static float painBarCounterHeight = 50;
		
		private final static float iconAspectRatio = 1.0f;

		private static final float imageLevelNameAspectRatio = 5.0f;
		
		private static final float imageWeaponLeftAspectRatio = 1.0f;
		private static final float imageWeaponRightAspectRatio = 2.5f;
		
		private static final float imageTokugiLeftAspectRatio = 1.0f;
		private static final float imageTokugiRightAspectRatio = 2.5f;
		
		private static final float imageEnemyLeftAspectRatio = 1.0f;
		private static final float imageEnemyRightAspectRatio = 2.5f;

		private static final float cellEnemyImageHeight = 85;
		private static final float cellEnemyPainBarHeight = 15;
	}
	
	private final static class LandscapeLayout {

		public static Padding padding1 = new Padding.Relative()
				.top(0.06f).bottom(0.06f)
				.right(0.016f).left(0.016f)
				.vertical(0.016f).horizontal(0.016f);

		// Level one, box.
		private final static Padding padding2 = new Padding.Relative()
				.top(0.04f).bottom(0.04f)
				.left(0.14f).right(0.14f)
				.vertical(0.016f).horizontal(0.016f);

		// Level two, box in a box.
		private final static Padding padding3 = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.1f).right(0.1f)
				.vertical(0.016f).horizontal(0.1f);

		// More paddings.
		private final static Padding paddingTime = new Padding.Absolute().all(1.0f);
		private final static Padding paddingScore = new Padding.Absolute().all(1.0f);
		private final static Padding paddingPainBar = new Padding.Absolute().all(1.0f);
		private static final Padding paddingEnemyIcons = new Padding.Relative()
				.top(0.1f).bottom(0.1f)
				.left(0.0f).right(0.0f)
				.vertical(0.03f).horizontal(0.0f);
		private static final Padding paddingTokugiImage = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.03f).right(0.03f)
				.vertical(0.03f).horizontal(0.03f);
		private static final Padding paddingWeaponImage = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.03f).right(0.03f)
				.vertical(0.03f).horizontal(0.03f);
		public static final Padding paddingEnemyDataPainBar = new Padding.Relative()
				.top(0.06f).bottom(0.06f)
				.left(0.01f).right(0.01f)
				.horizontal(0.04f).vertical(0.04f);

		public static final float lowerRowHeight = 80;
		public static final float upperRowHeight = 20;
		
		private final static float widgetLevelNameWidth = 10;
		private final static float widgetTimeWidth = 10;
		private final static float widgetScoreWidth = 10;
		private final static float widgetPainBarWidth = 15;
		private final static float widgetWeaponWidth = 15;
		private final static float widgetTokugiWidth = 15;
		private final static float widgetEnemyWidth = 25;
		
		private final static float rowIconHeight = 20;
		private final static float rowDataHeight = 80;
		
		private final static float painBarMeterWidth = 65;
		private final static float painBarCounterWidth = 35;
		
		private final static float iconAspectRatio = 1.0f;
				
		private static final float imageLevelNameAspectRatio = 1.0f/5.0f;
		
		private static final float imageWeaponTopInverseAspectRatio = 1.0f;
		private static final float imageWeaponBottomInverseAspectRatio = 2.5f;

		private static final float imageTokugiTopInverseAspectRatio = 1.0f;
		private static final float imageTokugiBottomInverseAspectRatio = 2.5f;
		
		private static final float imageEnemyTopInverseAspectRatio = 1.0f;
		private static final float imageEnemyBottomInverseAspectRatio = 2.5f;

		private static final float cellEnemyImageHeight = 80;
		private static final float cellEnemyPainBarHeight = 20;

	}
	
	public static enum Mode {
		COMPACTED,
		FULL;
	}
}