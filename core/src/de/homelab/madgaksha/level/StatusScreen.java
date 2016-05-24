package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.gameClock;
import static de.homelab.madgaksha.GlobalBag.gameScore;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * The info viewport for highscores etc.
 * 
 * @author madgaksha
 *
 */
public class StatusScreen {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(StatusScreen.class);
	
	private boolean landscapeMode;

	private final Rectangle screenBounds = new Rectangle();
	
	private final Rectangle uiLevelName = new Rectangle();
	private final Rectangle uiTime = new Rectangle();
	private final Rectangle uiScore = new Rectangle();
	private final Rectangle uiPainBar = new Rectangle();
	private final Rectangle uiWeapon = new Rectangle();
	private final Rectangle uiTokugi = new Rectangle();
	private final Rectangle uiEnemy = new Rectangle();

	private final Rectangle uiTextLevelName = new Rectangle();
	
	private final Rectangle uiColumnIconTime= new Rectangle();
	private final Rectangle uiColumnIconScore= new Rectangle();
	private final Rectangle uiColumnIconWeapon= new Rectangle();
	private final Rectangle uiColumnIconTokugi= new Rectangle();
	private final Rectangle uiColumnIconEnemy= new Rectangle();
	
	private final Rectangle uiColumnDataTime= new Rectangle();
	private final Rectangle uiColumnDataScore= new Rectangle();
	private final Rectangle uiColumnDataWeapon= new Rectangle();
	private final Rectangle uiColumnDataTokugi= new Rectangle();
	private final Rectangle uiColumnDataEnemy= new Rectangle();
	
	private final Rectangle uiPainBarMeter = new Rectangle();
	private final Rectangle uiPainBarCounter = new Rectangle();
	
	private final Rectangle uiIconTime = new Rectangle();
	private final Rectangle uiIconScore = new Rectangle();
	private final Rectangle uiIconWeapon = new Rectangle();
	private final Rectangle uiIconTokugi = new Rectangle();
	private final Rectangle uiIconEnemy = new Rectangle();
	
	private final Rectangle uiImageWeaponMain = new Rectangle();
	private final Rectangle uiImageWeaponSub = new Rectangle();
	
	private final Rectangle uiImageTokugiLeftMain = new Rectangle();
	private final Rectangle uiImageTokugiRightSub = new Rectangle();

	private final Rectangle uiImageEnemyLeftMain = new Rectangle();
	private final Rectangle uiImageEnemyRightSub = new Rectangle();

	
	private Mode uiImageWeaponMode;
	private Mode uiImageTokugiMode;
	private Mode uiImageEnemyMode;
	
	private final Rectangle uiCellEnemyImage = new Rectangle();
	private final Rectangle uiCellEnemyPainBar = new Rectangle();

	private static final Rectangle uiTimeMinuteTen = new Rectangle();
	private static final Rectangle uiTimeMinuteOne = new Rectangle();
	private static final Rectangle uiTimeMinuteSecondSeparator = new Rectangle();
	private static final Rectangle uiTimeSecondTen = new Rectangle();
	private static final Rectangle uiTimeSecondOne = new Rectangle();
	private static final Rectangle uiTimeMillisecondHundred = new Rectangle();
	private static final Rectangle uiTimeSecondMillisecondSeparator = new Rectangle();
	private static final Rectangle uiTimeMillisecondTen = new Rectangle();
	
	private static final Rectangle[] uiScoreDigits = new Rectangle[]{
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
	
	private Sprite textureIconEnemy;
	private Sprite textureIconScore;
	private Sprite textureIconTime;
	private Sprite textureIconTokugi;
	private Sprite textureIconWeapon;

	private final Sprite spriteNumeral[] = new Sprite[10];
	private Sprite spriteSeparatorTime;
	
	public StatusScreen(int w, int h) {
		computeScreenDimensions(w, h);
		loadResources();
		computeUILayout();
	}

	private void loadResources() {
		textureIconEnemy = ETexture.STATUS_ICON_ENEMY.asSprite();
		textureIconScore = ETexture.STATUS_ICON_SCORE.asSprite();
		textureIconTime = ETexture.STATUS_ICON_TIME.asSprite();
		textureIconTokugi = ETexture.STATUS_ICON_TOKUGI.asSprite();
		textureIconWeapon = ETexture.STATUS_ICON_WEAPON.asSprite();
		
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
	}
		
	public void update(int screenWidth, int screenHeight) {
		computeScreenDimensions(screenWidth, screenHeight);
		computeUILayout();
	}

	public void render() {
		// TODO just testing		
		NinePatch myNinePatch = ResourceCache.getNinePatch(ENinePatch.TEST_PIXEL);
		
		myNinePatch.setColor(new Color(255, 255, 0, 0.9f));
		myNinePatch.draw(batchPixel, screenBounds.x, screenBounds.y, screenBounds.width, screenBounds.height);
		
		myNinePatch.setColor(new Color(255, 255, 255, 0.5f));
		myNinePatch.draw(batchPixel, uiLevelName.x, uiLevelName.y, uiLevelName.width, uiLevelName.height);
		myNinePatch.draw(batchPixel, uiTime.x, uiTime.y, uiTime.width, uiTime.height);
		myNinePatch.draw(batchPixel, uiScore.x, uiScore.y, uiScore.width, uiScore.height);
		myNinePatch.draw(batchPixel, uiPainBar.x, uiPainBar.y, uiPainBar.width, uiPainBar.height);
		myNinePatch.draw(batchPixel, uiWeapon.x, uiWeapon.y, uiWeapon.width, uiWeapon.height);
		myNinePatch.draw(batchPixel, uiTokugi.x, uiTokugi.y, uiTokugi.width, uiTokugi.height);
		myNinePatch.draw(batchPixel, uiEnemy.x, uiEnemy.y, uiEnemy.width, uiEnemy.height);

		
		myNinePatch.setColor(new Color(255, 0, 0, 0.5f));
		
		myNinePatch.draw(batchPixel, uiPainBarMeter.x, uiPainBarMeter.y, uiPainBarMeter.width, uiPainBarMeter.height);
		myNinePatch.draw(batchPixel, uiPainBarCounter.x, uiPainBarCounter.y, uiPainBarCounter.width, uiPainBarCounter.height);
		
		myNinePatch.draw(batchPixel, uiColumnIconTime.x, uiColumnIconTime.y, uiColumnIconTime.width, uiColumnIconTime.height);
		myNinePatch.draw(batchPixel, uiColumnIconScore.x, uiColumnIconScore.y, uiColumnIconScore.width, uiColumnIconScore.height);
		myNinePatch.draw(batchPixel, uiColumnIconWeapon.x, uiColumnIconWeapon.y, uiColumnIconWeapon.width, uiColumnIconWeapon.height);
		myNinePatch.draw(batchPixel, uiColumnIconTokugi.x, uiColumnIconTokugi.y, uiColumnIconTokugi.width, uiColumnIconTokugi.height);
		myNinePatch.draw(batchPixel, uiColumnIconEnemy.x, uiColumnIconEnemy.y, uiColumnIconEnemy.width, uiColumnIconEnemy.height);

		myNinePatch.draw(batchPixel, uiColumnDataTime.x, uiColumnDataTime.y, uiColumnDataTime.width, uiColumnDataTime.height);
		myNinePatch.draw(batchPixel, uiColumnDataScore.x, uiColumnDataScore.y, uiColumnDataScore.width, uiColumnDataScore.height);
		myNinePatch.draw(batchPixel, uiColumnDataWeapon.x, uiColumnDataWeapon.y, uiColumnDataWeapon.width, uiColumnDataWeapon.height);
		myNinePatch.draw(batchPixel, uiColumnDataTokugi.x, uiColumnDataTokugi.y, uiColumnDataTokugi.width, uiColumnDataTokugi.height);
		myNinePatch.draw(batchPixel, uiColumnDataEnemy.x, uiColumnDataEnemy.y, uiColumnDataEnemy.width, uiColumnDataEnemy.height);
		myNinePatch.draw(batchPixel, uiTextLevelName.x, uiTextLevelName.y, uiTextLevelName.width, uiTextLevelName.height);

		
		myNinePatch.setColor(new Color(0, 255, 0, 0.5f));
		myNinePatch.draw(batchPixel, uiIconTime.x, uiIconTime.y, uiIconTime.width, uiIconTime.height);		
		myNinePatch.draw(batchPixel, uiIconScore.x, uiIconScore.y, uiIconScore.width, uiIconScore.height);
		myNinePatch.draw(batchPixel, uiIconWeapon.x, uiIconWeapon.y, uiIconWeapon.width, uiIconWeapon.height);
		myNinePatch.draw(batchPixel, uiIconTokugi.x, uiIconTokugi.y, uiIconTokugi.width, uiIconTokugi.height);
		myNinePatch.draw(batchPixel, uiIconEnemy.x, uiIconEnemy.y, uiIconEnemy.width, uiIconEnemy.height);
		
		myNinePatch.draw(batchPixel, uiImageWeaponMain.x, uiImageWeaponMain.y, uiImageWeaponMain.width, uiImageWeaponMain.height);
		myNinePatch.draw(batchPixel, uiImageTokugiLeftMain.x, uiImageTokugiLeftMain.y, uiImageTokugiLeftMain.width, uiImageTokugiLeftMain.height);
				
		if (uiImageWeaponMode == Mode.FULL) myNinePatch.draw(batchPixel, uiImageWeaponSub.x, uiImageWeaponSub.y, uiImageWeaponSub.width, uiImageWeaponSub.height);
		if (uiImageTokugiMode == Mode.FULL) myNinePatch.draw(batchPixel, uiImageTokugiRightSub.x, uiImageTokugiRightSub.y, uiImageTokugiRightSub.width, uiImageTokugiRightSub.height);
		
		myNinePatch.draw(batchPixel, uiCellEnemyImage.x, uiCellEnemyImage.y, uiCellEnemyImage.width, uiCellEnemyImage.height);
		myNinePatch.draw(batchPixel, uiCellEnemyPainBar.x, uiCellEnemyPainBar.y, uiCellEnemyPainBar.width, uiCellEnemyPainBar.height);

		
		myNinePatch.setColor(new Color(0, 0, 255, 0.5f));
		
		myNinePatch.draw(batchPixel, uiImageEnemyLeftMain.x, uiImageEnemyLeftMain.y, uiImageEnemyLeftMain.width, uiImageEnemyLeftMain.height);
		if (uiImageEnemyMode == Mode.FULL) myNinePatch.draw(batchPixel, uiImageEnemyRightSub.x, uiImageEnemyRightSub.y, uiImageEnemyRightSub.width, uiImageEnemyRightSub.height);
		
		
		
		// Draw icons.
		textureIconEnemy.draw(batchPixel);
		textureIconScore.draw(batchPixel);
		textureIconTime.draw(batchPixel);
		textureIconTokugi.draw(batchPixel);
		textureIconWeapon.draw(batchPixel);
		
		// Draw time.
		final int mt = gameClock.getMinutesTen();
		final int mo = gameClock.getMinutesOne();
		final int st = gameClock.getSecondsTenClipped();
		final int so = gameClock.getSecondsOneClipped();
		final int msh = gameClock.getMillisecondsHundred();
		final int mst = gameClock.getMillisecondsTen();
		
		spriteNumeral[mt].setBounds(uiTimeMinuteTen.x, uiTimeMinuteTen.y, uiTimeMinuteTen.width, uiTimeMinuteTen.height);
		spriteNumeral[mt].draw(batchPixel);
		
		spriteNumeral[mo].setBounds(uiTimeMinuteOne.x, uiTimeMinuteOne.y, uiTimeMinuteOne.width, uiTimeMinuteOne.height);
		spriteNumeral[mo].draw(batchPixel);

		spriteNumeral[st].setBounds(uiTimeSecondTen.x, uiTimeSecondTen.y, uiTimeSecondTen.width, uiTimeSecondTen.height);
		spriteNumeral[st].draw(batchPixel);
		
		spriteNumeral[so].setBounds(uiTimeSecondOne.x, uiTimeSecondOne.y, uiTimeSecondOne.width, uiTimeSecondOne.height);
		spriteNumeral[so].draw(batchPixel);

		spriteNumeral[msh].setBounds(uiTimeMillisecondHundred.x, uiTimeMillisecondHundred.y, uiTimeMillisecondHundred.width, uiTimeMillisecondHundred.height);
		spriteNumeral[msh].draw(batchPixel);
		
		spriteNumeral[mst].setBounds(uiTimeMillisecondTen.x, uiTimeMillisecondTen.y, uiTimeMillisecondTen.width, uiTimeMillisecondTen.height);
		spriteNumeral[mst].draw(batchPixel);
		
		spriteSeparatorTime.setBounds(uiTimeMinuteSecondSeparator.x, uiTimeMinuteSecondSeparator.y, uiTimeMinuteSecondSeparator.width, uiTimeMinuteSecondSeparator.height);
		spriteSeparatorTime.draw(batchPixel);
		
		spriteSeparatorTime.setBounds(uiTimeSecondMillisecondSeparator.x, uiTimeSecondMillisecondSeparator.y, uiTimeSecondMillisecondSeparator.width, uiTimeSecondMillisecondSeparator.height);
		spriteSeparatorTime.draw(batchPixel);
		
		// Draw score.
		for (int i = uiScoreDigits.length; i --> 0;) {
			spriteNumeral[gameScore.getDigit(i)].setBounds(uiScoreDigits[i].x,uiScoreDigits[i].y,uiScoreDigits[i].width,uiScoreDigits[i].height);
			spriteNumeral[gameScore.getDigit(i)].draw(batchPixel);
		}
	}
	
	public boolean isLandscapeMode() {
		return landscapeMode;
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
		
		textureIconEnemy.setBounds(uiIconEnemy.x, uiIconEnemy.y, uiIconEnemy.width, uiIconEnemy.height);
		textureIconScore.setBounds(uiIconScore.x, uiIconScore.y, uiIconScore.width, uiIconScore.height);
		textureIconTime.setBounds(uiIconTime.x, uiIconTime.y, uiIconTime.width, uiIconTime.height);
		textureIconTokugi.setBounds(uiIconTokugi.x, uiIconTokugi.y, uiIconTokugi.width, uiIconTokugi.height);
		textureIconWeapon.setBounds(uiIconWeapon.x, uiIconWeapon.y, uiIconWeapon.width, uiIconWeapon.height);
	}

	
	
	private void computeUILayoutPortrait() {

		// ====================================
		//  Layout main containers vertically.
		// ====================================
		
		// Relative height for each widget.
		float totalRelativeHeight = 1.0f / (
				  PortraitLayout.widgetWeaponHeight
				+ PortraitLayout.widgetTokugiHeight
				+ PortraitLayout.widgetEnemyHeight
				+ PortraitLayout.widgetLevelNameHeight
				+ PortraitLayout.widgetPainBarHeight
				+ PortraitLayout.widgetScoreHeight
				+ PortraitLayout.widgetTimeHeight
				);
		float relativeHeightWeapon = PortraitLayout.widgetWeaponHeight * totalRelativeHeight;
		float relativeHeightTokugi = PortraitLayout.widgetTokugiHeight * totalRelativeHeight;
		float relativeHeightEnemy = PortraitLayout.widgetEnemyHeight * totalRelativeHeight;
		float relativeHeightLevelName = PortraitLayout.widgetLevelNameHeight * totalRelativeHeight;
		float relativeHeightPainBar = PortraitLayout.widgetPainBarHeight * totalRelativeHeight;
		float relativeHeightScore= PortraitLayout.widgetScoreHeight * totalRelativeHeight;
		float relativeHeightTime = PortraitLayout.widgetTimeHeight * totalRelativeHeight;
	
		// Absolute padding in pixels from relative padding.
		PortraitLayout.padding1.at(screenBounds, 1, 7);
		
		// Available height for all vertically stacked widgets stacked together.
		float widgetAvailableHeight = screenBounds.height - (PortraitLayout.padding1.bottom() + PortraitLayout.padding1.top() + 6 * PortraitLayout.padding1.vertical());		
				
		// Available width for each main container. They will be stretched as much as possible.
		float widgetAvailableWidth = screenBounds.width - (PortraitLayout.padding1.left() + PortraitLayout.padding1.right());
		
		// Layout main containers vertically, from bottom to top
		float widgetPosY = PortraitLayout.padding1.bottom() + screenBounds.y;

		uiEnemy.y = widgetPosY;
		uiEnemy.height = widgetAvailableHeight * relativeHeightEnemy;
		widgetPosY += uiEnemy.height + PortraitLayout.padding1.vertical();
				
		uiTokugi.y = widgetPosY;
		uiTokugi.height = widgetAvailableHeight * relativeHeightTokugi;
		widgetPosY += uiTokugi.height + PortraitLayout.padding1.vertical();
		
		uiWeapon.y = widgetPosY;
		uiWeapon.height = widgetAvailableHeight * relativeHeightWeapon;
		widgetPosY += uiWeapon.height + PortraitLayout.padding1.vertical();
		
		uiPainBar.y = widgetPosY;
		uiPainBar.height = widgetAvailableHeight * relativeHeightPainBar;
		widgetPosY += uiPainBar.height + PortraitLayout.padding1.vertical();
		
		uiScore.y = widgetPosY;
		uiScore.height = widgetAvailableHeight * relativeHeightScore;
		widgetPosY += uiScore.height + PortraitLayout.padding1.vertical();

		uiTime.y = widgetPosY;
		uiTime.height = widgetAvailableHeight * relativeHeightTime;
		widgetPosY += uiTime.height + PortraitLayout.padding1.vertical();
		
		uiLevelName.y = widgetPosY;
		uiLevelName.height = widgetAvailableHeight * relativeHeightLevelName;
		widgetPosY += uiLevelName.height + PortraitLayout.padding1.vertical();
		
		// Set horizontal position and width.
		float widgetPosX = screenBounds.x + PortraitLayout.padding1.left();
		uiEnemy.x = widgetPosX;
		uiEnemy.width = widgetAvailableWidth;

		uiTokugi.x = widgetPosX;
		uiTokugi.width = widgetAvailableWidth;
		
		uiWeapon.x = widgetPosX;
		uiWeapon.width = widgetAvailableWidth;
		
		uiPainBar.x = widgetPosX;
		uiPainBar.width = widgetAvailableWidth;
		
		uiScore.x = widgetPosX;
		uiScore.width = widgetAvailableWidth;

		uiTime.x = widgetPosX;
		uiTime.width = widgetAvailableWidth;
		
		uiLevelName.x = widgetPosX;
		uiLevelName.width = widgetAvailableWidth;
		
		
		
		// ====================================
		//     Layout icon and data column.
		// ====================================

		layoutLeftRightWithRelativeWidth(uiEnemy, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconEnemy, uiColumnDataEnemy);	

		layoutLeftRightWithRelativeWidth(uiTokugi, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconTokugi, uiColumnDataTokugi);	


		layoutLeftRightWithRelativeWidth(uiWeapon, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconWeapon, uiColumnDataWeapon);
		
		layoutLeftRightWithRelativeWidth(uiScore, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconScore, uiColumnDataScore);

		layoutLeftRightWithRelativeWidth(uiTime, PortraitLayout.padding2,
				PortraitLayout.columnIconWidth, PortraitLayout.columnDataWidth,
				uiColumnIconTime, uiColumnDataTime);
			
		// Level name text/image dimensions (1 column)
		PortraitLayout.padding2.at(uiLevelName);
		uiTextLevelName.x = uiLevelName.x + PortraitLayout.padding2.left();
		uiTextLevelName.y = uiLevelName.y + PortraitLayout.padding2.bottom();
		uiTextLevelName.height = uiLevelName.height - PortraitLayout.padding2.bottom() - PortraitLayout.padding2.top();
		uiTextLevelName.width = uiLevelName.width - PortraitLayout.padding2.left() - PortraitLayout.padding2.right();
		
		// HP bar player (2 rows).
		layoutTopBottomWithRelativeWidth(uiPainBar, PortraitLayout.padding2,
				PortraitLayout.painBarMeterHeight, PortraitLayout.painBarCounterHeight,	uiPainBarMeter, uiPainBarCounter);
	
		
		
		// ====================================
		//            Layout icons.
		// ====================================

		uiIconTime.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTime, PortraitLayout.padding3,
				PortraitLayout.iconTimeAspectRatio));
		uiIconScore.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconScore, PortraitLayout.padding3,
				PortraitLayout.iconTimeAspectRatio));
		uiIconWeapon.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconWeapon, PortraitLayout.padding3,
				PortraitLayout.iconTimeAspectRatio));
		uiIconTokugi.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTokugi, PortraitLayout.padding3,
				PortraitLayout.iconTimeAspectRatio));
		uiIconEnemy.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconEnemy, PortraitLayout.padding3,
				PortraitLayout.iconTimeAspectRatio));				
		
		
		// ====================================
		//         Layout data fields.
		// ====================================

		uiImageWeaponMode = layoutLeftRightWithAspectRatio(uiColumnDataWeapon, PortraitLayout.padding3,
				PortraitLayout.imageWeaponLeftAspectRatio, PortraitLayout.imageWeaponRightAspectRatio, 1.0f, 0.0f,
				uiImageWeaponMain, uiImageWeaponSub);	
		
		uiImageTokugiMode = layoutLeftRightWithAspectRatio(uiColumnDataTokugi, PortraitLayout.padding3,
				PortraitLayout.imageTokugiLeftAspectRatio, PortraitLayout.imageTokugiRightAspectRatio, 1.0f, 0.0f,
				uiImageTokugiLeftMain, uiImageTokugiRightSub);
		
		layoutTopBottomWithRelativeWidth(uiColumnDataEnemy, PortraitLayout.padding3,
				PortraitLayout.cellEnemyImageHeight, PortraitLayout.cellEnemyPainBarHeight,	uiCellEnemyImage, uiCellEnemyPainBar);

		uiImageEnemyMode = layoutLeftRightWithAspectRatio(uiCellEnemyImage, PortraitLayout.padding3,
				PortraitLayout.imageEnemyLeftAspectRatio, PortraitLayout.imageEnemyRightAspectRatio, 1.0f, 0.0f,
				uiImageEnemyLeftMain, uiImageEnemyRightSub);

		// Time digits
		PortraitLayout.paddingTime.at(uiColumnDataTime);
		float fontTimeHeight = uiColumnDataTime.height - PortraitLayout.paddingTime.top() + PortraitLayout.paddingTime.bottom();
		float fontTimeWidth = fontTimeHeight / spriteNumeral[0].getRegionHeight() * spriteNumeral[0].getRegionWidth();
		layoutHorizontallyWithAbsoluteWidth(uiColumnDataTime, PortraitLayout.paddingTime, 0.5f, true, fontTimeWidth, new Rectangle[]{ 
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
		layoutHorizontallyWithAbsoluteWidth(uiColumnDataScore, PortraitLayout.paddingScore, 0.5f, true, fontScoreWidth, uiScoreDigits);
	}

	
	
	
	
	
	private void computeUILayoutLandscape() {
		
		// =====================================
		//  Layout main containers horizontally.
		// =====================================
		
		layoutHorizontallyWithRelativeWidth(screenBounds, LandscapeLayout.padding1, new float[]{
				LandscapeLayout.widgetLevelNameWidth,
				LandscapeLayout.widgetTimeWidth,
				LandscapeLayout.widgetScoreWidth,
				LandscapeLayout.widgetPainBarWidth,
				LandscapeLayout.widgetWeaponWidth,
				LandscapeLayout.widgetTokugiWidth,
				LandscapeLayout.widgetEnemyWidth
			}, new Rectangle[]{
				uiLevelName,
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

		layoutTopBottomWithRelativeWidth(uiEnemy, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconEnemy, uiColumnDataEnemy);	

		layoutTopBottomWithRelativeWidth(uiTokugi, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconTokugi, uiColumnDataTokugi);

		layoutTopBottomWithRelativeWidth(uiWeapon, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconWeapon, uiColumnDataWeapon);
		
		layoutTopBottomWithRelativeWidth(uiScore, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconScore, uiColumnDataScore);

		layoutTopBottomWithRelativeWidth(uiTime, LandscapeLayout.padding2,
				LandscapeLayout.rowIconHeight, LandscapeLayout.rowDataHeight,
				uiColumnIconTime, uiColumnDataTime);
		
		// Level name text/image dimensions (1 column)
		LandscapeLayout.padding2.at(uiLevelName);
		uiTextLevelName.x = uiLevelName.x + LandscapeLayout.padding2.left();
		uiTextLevelName.y = uiLevelName.y + LandscapeLayout.padding2.bottom();
		uiTextLevelName.height = uiLevelName.height - LandscapeLayout.padding2.bottom() - LandscapeLayout.padding2.top();
		uiTextLevelName.width = uiLevelName.width - LandscapeLayout.padding2.left() - LandscapeLayout.padding2.right();
		
		
		// HP bar player (2 columns).
		layoutLeftRightWithRelativeWidth(uiPainBar, LandscapeLayout.padding2,
				LandscapeLayout.painBarMeterWidth, LandscapeLayout.painBarCounterWidth, uiPainBarMeter, uiPainBarCounter);


		
		// ====================================
		//            Layout icons.
		// ====================================

		uiIconTime.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTime, LandscapeLayout.padding3,
				LandscapeLayout.iconTimeAspectRatio));
		uiIconScore.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconScore, LandscapeLayout.padding3,
				LandscapeLayout.iconTimeAspectRatio));
		uiIconWeapon.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconWeapon, LandscapeLayout.padding3,
				LandscapeLayout.iconTimeAspectRatio));
		uiIconTokugi.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTokugi, LandscapeLayout.padding3,
				LandscapeLayout.iconTimeAspectRatio));
		uiIconEnemy.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconEnemy, LandscapeLayout.padding3,
				LandscapeLayout.iconTimeAspectRatio));
		

		
		// ====================================
		//         Layout data fields.
		// ====================================

		uiImageWeaponMode = layoutTopBottomWithAspectRatio(uiColumnDataWeapon, LandscapeLayout.padding3,
				LandscapeLayout.imageWeaponTopInverseAspectRatio, LandscapeLayout.imageWeaponBottomInverseAspectRatio,
				0.0f, 1.0f, uiImageWeaponMain, uiImageWeaponSub);

		uiImageTokugiMode = layoutTopBottomWithAspectRatio(uiColumnDataTokugi, LandscapeLayout.padding3,
				LandscapeLayout.imageTokugiTopInverseAspectRatio, LandscapeLayout.imageTokugiBottomInverseAspectRatio,
				0.0f, 1.0f, uiImageTokugiLeftMain, uiImageTokugiRightSub);

		layoutLeftRightWithRelativeWidth(uiColumnDataEnemy, LandscapeLayout.padding3,
				LandscapeLayout.cellEnemyImageWidth, LandscapeLayout.cellEnemyPainBarWidth, uiCellEnemyImage,
				uiCellEnemyPainBar);

		uiImageEnemyMode = layoutTopBottomWithAspectRatio(uiCellEnemyImage, LandscapeLayout.padding3,
				LandscapeLayout.imageEnemyTopInverseAspectRatio, LandscapeLayout.imageEnemyBottomInverseAspectRatio,
				0.0f, 1.0f, uiImageEnemyLeftMain, uiImageEnemyRightSub);

	}
	
	private Rectangle layoutCenteredInBoxAndKeepAspectRatio(Rectangle rectangle, Padding padding, float aspectRatio) {
		return layoutAlignedInBoxAndKeepAspectRatio(rectangle, padding, aspectRatio, 0.5f, 0.5f);
	}

	private Rectangle layoutAlignedInBoxAndKeepAspectRatio(Rectangle rectangle, Padding padding, float aspectRatio, float alignHorizontal, float alignVertical) {
		
		if (padding == null) padding = new Padding.Absolute();
		padding.at(rectangle);
		
		float availableWidth = rectangle.width - padding.left() - padding.right();
		float availableHeight = rectangle.height - padding.top() - padding.bottom();

		float height, width;
		
		if (availableWidth / availableHeight > aspectRatio) {
			height = availableHeight;
			width = height * aspectRatio;
		}
		else {
			width = availableWidth;
			height = width / aspectRatio;
		}
		
		Rectangle aligned = new Rectangle();
		
		aligned.x = rectangle.x + padding.left() + alignHorizontal * (availableWidth - width);//centerX - width*0.5f;
		aligned.y = rectangle.y + padding.bottom() + alignVertical * (availableHeight - height);//centerX - width*0.5f;

		aligned.width = width;
		aligned.height = height;
		
		return aligned;
	}
	
	private void layoutHorizontallyWithRelativeWidth(Rectangle parent, Padding padding, float[] relativeWidthList, Rectangle... children) {
		if (relativeWidthList.length != children.length) return;
		
		if (padding == null) padding = new Padding.Absolute();
		padding.at(parent, relativeWidthList.length, 1);

		float totalWidth = 0.0f;
		for (float width : relativeWidthList) {
			totalWidth += width;
		}

		float scale = 1.0f/totalWidth;
		float availableWidth = parent.width - padding.left() - padding.right() - (relativeWidthList.length-1) * padding.horizontal();
		float availableHeight = parent.height - padding.bottom() - padding.top();
		
		float posX = parent.x + padding.left();
		float posY = parent.y + padding.bottom();
		for (int i = 0; i != children.length; ++i) {
			float width = relativeWidthList[i] * availableWidth * scale;
			children[i].x = posX;
			children[i].y = posY;
			children[i].width = width;
			children[i].height = availableHeight;
			posX += width + padding.horizontal();
		}
	}

	private void layoutHorizontallyWithAbsoluteWidth(Rectangle parent, Padding padding, float align, boolean keepAspectRatio, float absoluteWidth, Rectangle... children) {
		float[] list = new float[children.length];
		Arrays.fill(list, absoluteWidth);
		layoutHorizontallyWithAbsoluteWidth(parent, padding, align, keepAspectRatio, list, children);
	}
	private void layoutHorizontallyWithAbsoluteWidth(Rectangle parent, Padding padding, float align, boolean keepAspectRatio, float[] absoluteWidthList, Rectangle... children) {
		if (absoluteWidthList.length != children.length) return;

		if (padding == null) padding = new Padding.Absolute();
		padding.at(parent, absoluteWidthList.length, 1);
		
		float totalWidth = 0.0f;
		for (float width : absoluteWidthList) {
			totalWidth += width;
		}
		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.bottom() - padding.top();
				
		// Scale widths if content would not fit otherwise.
		float scaleX = 1.0f;
		float scaleY = 1.0f;
		if (totalWidth + (absoluteWidthList.length-1) * padding.horizontal() > availableWidth) {
			scaleX = (availableWidth -  padding.horizontal() * (absoluteWidthList.length-1)) / totalWidth;
			if (keepAspectRatio) scaleY = scaleX;
			totalWidth *= scaleX;
		}
		
		float posX = parent.x + padding.left() + align * (availableWidth - totalWidth - (absoluteWidthList.length-1) * padding.horizontal());
		float posY = parent.y + padding.bottom() + (1.0f-scaleY)*availableHeight*0.5f;
		for (int i = 0; i != children.length; ++i) {
			float width = absoluteWidthList[i] * scaleX;
			children[i].x = posX;
			children[i].y = posY;
			children[i].width = width;
			children[i].height = availableHeight * scaleY;
			posX += width + padding.horizontal();
		}
	}

	
	private void layoutTopBottomWithRelativeWidth(Rectangle parent, Padding padding, float heightTop,
			float heightBottom, Rectangle top, Rectangle bottom) {
		
		if (padding == null) padding = new Padding.Absolute();
		padding.at(parent, 1, 2);
		
		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.top() - padding.bottom() - padding.vertical();

		float totalHeight = 1.0f / (heightTop + heightBottom);
		float relativeHeightTop = heightTop * totalHeight;
		float relativeHeightBottom = heightBottom * totalHeight;

		bottom.x = parent.x + padding.left();
		bottom.y = parent.y + padding.bottom();
		bottom.width = availableWidth;
		bottom.height = availableHeight * relativeHeightBottom;

		top.x = parent.x + padding.left();
		top.y = bottom.y + bottom.height + padding.vertical();
		top.width = availableWidth;
		top.height = availableHeight * relativeHeightTop;
	}
	
	private void layoutLeftRightWithRelativeWidth(Rectangle parent, Padding padding, float widthLeft,
			float widthRight, Rectangle left, Rectangle right) {

		if (padding == null) padding = new Padding.Absolute();
		padding.at(parent,2,1);
		
		float availableWidth = parent.width - padding.left() - padding.right() - padding.horizontal();
		float availableHeight = parent.height - padding.top() - padding.bottom();

		float totalWidth = 1.0f / (widthLeft + widthRight);
		float relativeWidthLeft = widthLeft * totalWidth;
		float relativeWidthRight = widthRight * totalWidth;

		left.x = parent.x + padding.left();
		left.y = parent.y + padding.bottom();
		left.width = availableWidth * relativeWidthLeft;
		left.height = availableHeight;

		right.x = parent.x + padding.left() + left.width + padding.horizontal();
		right.y = parent.y + padding.bottom();
		right.width = availableWidth * relativeWidthRight;
		right.height = availableHeight;
	}
	
	private Mode layoutTopBottomWithAspectRatio(Rectangle parent, Padding padding, float inverseAspectRatioTop,
			float inverseAspectRatioBottom, float alignTop, float alignBottom, Rectangle top, Rectangle bottom) {
		
		if (padding == null) padding = new Padding.Absolute();
		padding.at(parent,1,2);
		
		float availableWidth = parent.width - padding.left() - padding.right();
		float availableHeight = parent.height - padding.top() - padding.bottom() - padding.vertical();

		Mode mode;

		if (availableHeight / availableWidth < inverseAspectRatioTop + inverseAspectRatioBottom) {
			mode = Mode.COMPACTED;
			Rectangle rectangleTop = new Rectangle();
			rectangleTop.x = parent.x + padding.left();
			rectangleTop.y = parent.y + padding.bottom();
			rectangleTop.width = availableWidth;
			rectangleTop.height = availableHeight + padding.vertical();
			top.set(layoutCenteredInBoxAndKeepAspectRatio(rectangleTop, padding, 1.0f/inverseAspectRatioTop));
		} else {
			mode = Mode.FULL;

			float totalHeight = 1.0f / (inverseAspectRatioTop + inverseAspectRatioBottom);
			float relativeHeightTop = inverseAspectRatioTop * totalHeight;
			float relativeHeightBottom = inverseAspectRatioBottom * totalHeight;

			Rectangle rectangleTop = new Rectangle();
			Rectangle rectangleBottom = new Rectangle();

			rectangleBottom.x = parent.x + padding.left();
			rectangleBottom.y = parent.y + padding.bottom();
			rectangleBottom.width = availableWidth;
			rectangleBottom.height = availableHeight * relativeHeightBottom;

			rectangleTop.x = parent.x + padding.left();
			rectangleTop.y = rectangleBottom.y + rectangleBottom.height + padding.vertical();
			rectangleTop.width = availableWidth;
			rectangleTop.height = availableHeight * relativeHeightTop;

			bottom.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleBottom, null, 1.0f/inverseAspectRatioBottom, 0.5f, alignBottom));
			top.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleTop, null, 1.0f/inverseAspectRatioTop, 0.5f, alignTop));
		}

		return mode;
	}

	
	private Mode layoutLeftRightWithAspectRatio(Rectangle parent, Padding padding, float aspectRatioLeft,
			float aspectRatioRight, float alignLeft, float alignRight, Rectangle left, Rectangle right) {
		
		if (padding == null) padding = new Padding.Absolute();
		padding.at(parent,2,1);
		
		float availableWidth = parent.width - padding.left() - padding.right() - padding.horizontal();
		float availableHeight = parent.height - padding.top() - padding.bottom();

		Mode mode;

		if (availableWidth / availableHeight < aspectRatioLeft + aspectRatioRight) {
			mode = Mode.COMPACTED;
			Rectangle rectangleLeft = new Rectangle();
			rectangleLeft.x = parent.x + padding.left();
			rectangleLeft.y = parent.y + padding.bottom();
			rectangleLeft.width = availableWidth + padding.horizontal();
			rectangleLeft.height = availableHeight;
			left.set(layoutCenteredInBoxAndKeepAspectRatio(rectangleLeft, padding, aspectRatioLeft));
		} else {
			mode = Mode.FULL;

			float totalWidth = 1.0f / (aspectRatioLeft + aspectRatioRight);
			float relativeWidthLeft = aspectRatioLeft * totalWidth;
			float relativeWidthRight = aspectRatioRight * totalWidth;

			Rectangle rectangleLeft = new Rectangle();
			Rectangle rectangleRight = new Rectangle();

			rectangleLeft.x = parent.x + padding.left();
			rectangleLeft.y = parent.y + padding.bottom();
			rectangleLeft.width = availableWidth * relativeWidthLeft;
			rectangleLeft.height = availableHeight;

			rectangleRight.x = parent.x + padding.left() + rectangleLeft.width + padding.horizontal();
			rectangleRight.y = parent.y + padding.bottom();
			rectangleRight.width = availableWidth * relativeWidthRight;
			rectangleRight.height = availableHeight;

			left.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleLeft, null, aspectRatioLeft, alignLeft, 0.5f));
			right.set(layoutAlignedInBoxAndKeepAspectRatio(rectangleRight, null, aspectRatioRight, alignRight, 0.5f));
		}

		return mode;
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
		
		// Level tree, box in a box in a box.
		private final static Padding paddingTime = new Padding.Absolute().all(1.0f);
		private final static Padding paddingScore = new Padding.Absolute().all(1.0f);
		
		private final static float widgetLevelNameHeight = 10;
		private final static float widgetTimeHeight = 10;
		private final static float widgetScoreHeight = 10;
		private final static float widgetPainBarHeight = 15;
		private final static float widgetWeaponHeight = 18;
		private final static float widgetTokugiHeight = 18;
		private final static float widgetEnemyHeight = 20;
		
		private final static float columnIconWidth = 20;
		private final static float columnDataWidth = 80;
		
		private final static float painBarMeterHeight = 65;
		private final static float painBarCounterHeight = 35;
		
		private final static float iconTimeAspectRatio = 1.0f;

		private static final float imageWeaponLeftAspectRatio = 1.0f;
		private static final float imageWeaponRightAspectRatio = 2.0f;
		
		public static final float imageTokugiLeftAspectRatio = 1.0f;
		public static final float imageTokugiRightAspectRatio = 2.0f;
		
		public static final float imageEnemyLeftAspectRatio = 1.0f;
		public static final float imageEnemyRightAspectRatio = 2.0f;

		public static final float cellEnemyImageHeight = 80;
		public static final float cellEnemyPainBarHeight = 20;
	}
	
	private final static class LandscapeLayout {
		public static Padding padding1 = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.right(0.016f).left(0.016f)
				.vertical(0.016f).horizontal(0.016f);

		// Level one, box.
		private final static Padding padding2 = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.1f).right(0.1f)
				.vertical(0.016f).horizontal(0.016f);

		// Level two, box in a box.
		private final static Padding padding3 = new Padding.Relative()
				.top(0.016f).bottom(0.016f)
				.left(0.1f).right(0.1f)
				.vertical(0.016f).horizontal(0.1f);
		
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
		
		private final static float iconTimeAspectRatio = 1.0f;
				
		public static final float imageWeaponTopInverseAspectRatio = 1.0f;
		public static final float imageWeaponBottomInverseAspectRatio = 2.0f;

		public static final float imageTokugiTopInverseAspectRatio = 1.0f;
		public static final float imageTokugiBottomInverseAspectRatio = 2.0f;
		
		public static final float imageEnemyTopInverseAspectRatio = 1.0f;
		public static final float imageEnemyBottomInverseAspectRatio = 2.0f;

		public static final float cellEnemyImageWidth = 80;
		public static final float cellEnemyPainBarWidth = 20;

	}
	
	private static enum Mode {
		COMPACTED,
		FULL;
	}
}