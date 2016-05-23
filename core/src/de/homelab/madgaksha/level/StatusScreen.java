package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.batchPixel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.IResource;
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
	
	public StatusScreen(int w, int h) {
		computeScreenDimensions(w, h);
		computeUILayout();
	}

	@SuppressWarnings("unchecked")
	public IResource<? extends Enum<?>,?>[] getRequiredResources() {
		return new IResource[] {
			ENinePatch.TEST_PIXEL,	
		};
	}
	
	public void update(int screenWidth, int screenHeight) {
		computeScreenDimensions(screenWidth, screenHeight);
		computeUILayout();
	}

	public void render() {
		// TODO just testing
		NinePatch myNinePatch = ResourceCache.getNinePatch(ENinePatch.TEST_PIXEL);
		myNinePatch.setColor(Color.WHITE);
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
	}

	private void computeUILayoutPortrait() {
		
		
		float paddingLeft, paddingRight, paddingTop, paddingBottom, paddingHorizontal, paddingVertical;
		
		
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
		paddingLeft = PortraitLayout.paddingLeft * screenBounds.width;
		paddingRight = PortraitLayout.paddingRight * screenBounds.width;
		paddingTop = PortraitLayout.paddingTop * screenBounds.height;
		paddingBottom = PortraitLayout.paddingBottom * screenBounds.height;
		paddingHorizontal = PortraitLayout.paddingHorizontal * screenBounds.width;
		paddingVertical = PortraitLayout.paddingVertical * screenBounds.height;
		
		
		
		// Available height for all vertically stacked widgets stacked together.
		float widgetAvailableHeight = screenBounds.height - (paddingBottom + paddingTop + 6 * paddingVertical);		
		
		
		
		// Available width for each main container. They will be stretched as much as possible.
		float widgetAvailableWidth = screenBounds.width - (paddingLeft + paddingRight);

		
		
		// Layout main containers vertically, from bottom to top
		float widgetPosY = paddingBottom + screenBounds.y;

		uiEnemy.y = widgetPosY;
		uiEnemy.height = widgetAvailableHeight * relativeHeightEnemy;
		widgetPosY += uiEnemy.height + paddingVertical;
				
		uiTokugi.y = widgetPosY;
		uiTokugi.height = widgetAvailableHeight * relativeHeightTokugi;
		widgetPosY += uiTokugi.height + paddingVertical;
		
		uiWeapon.y = widgetPosY;
		uiWeapon.height = widgetAvailableHeight * relativeHeightWeapon;
		widgetPosY += uiWeapon.height + paddingVertical;
		
		uiPainBar.y = widgetPosY;
		uiPainBar.height = widgetAvailableHeight * relativeHeightPainBar;
		widgetPosY += uiPainBar.height + paddingVertical;
		
		uiScore.y = widgetPosY;
		uiScore.height = widgetAvailableHeight * relativeHeightScore;
		widgetPosY += uiScore.height + paddingVertical;

		uiTime.y = widgetPosY;
		uiTime.height = widgetAvailableHeight * relativeHeightTime;
		widgetPosY += uiTime.height + paddingVertical;
		
		uiLevelName.y = widgetPosY;
		uiLevelName.height = widgetAvailableHeight * relativeHeightLevelName;
		widgetPosY += uiLevelName.height + paddingVertical;
		
		// Set horizontal position and width.
		float widgetPosX = screenBounds.x + paddingLeft;
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


		
		paddingHorizontal = PortraitLayout.paddingHorizontal2 * widgetAvailableWidth;
		paddingLeft = PortraitLayout.paddingLeft2 * widgetAvailableWidth;
		paddingRight = PortraitLayout.paddingRight2 * widgetAvailableWidth;
		
		
		
		// Relative height for each widget.
		float totalRelativeWidth = 1.0f / (PortraitLayout.columnIconWidth + PortraitLayout.columnDataWidth);
		float relativeWidthColumnIcon = PortraitLayout.columnIconWidth  * totalRelativeWidth;
		float relativeWidthColumnData = PortraitLayout.columnDataWidth  * totalRelativeWidth;
		
		
		
		// Width of the both columns.
		float columnIconWidth = (widgetAvailableWidth - paddingHorizontal - paddingLeft - paddingRight) * relativeWidthColumnIcon;
		float columnDataWidth = (widgetAvailableWidth - paddingHorizontal - paddingLeft - paddingRight) * relativeWidthColumnData ;		
		
		
			
		// Icon column dimensions
		paddingBottom = PortraitLayout.paddingBottom2 * uiEnemy.height;
		paddingTop = PortraitLayout.paddingTop2 * uiEnemy.height;
		uiColumnIconEnemy.x = uiEnemy.x + paddingLeft;
		uiColumnIconEnemy.y = uiEnemy.y + paddingBottom;
		uiColumnIconEnemy.height = uiEnemy.height - paddingBottom - paddingTop;
		uiColumnIconEnemy.width = columnIconWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiTokugi.height;
		paddingTop = PortraitLayout.paddingTop2 * uiTokugi.height;
		uiColumnIconTokugi.x = uiTokugi.x + paddingLeft;
		uiColumnIconTokugi.y = uiTokugi.y + paddingBottom;
		uiColumnIconTokugi.height = uiTokugi.height - paddingBottom - paddingTop;;
		uiColumnIconTokugi.width = columnIconWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiWeapon.height;
		paddingTop = PortraitLayout.paddingTop2 * uiWeapon.height;
		uiColumnIconWeapon.x = uiWeapon.x + paddingLeft;
		uiColumnIconWeapon.y = uiWeapon.y + paddingBottom;
		uiColumnIconWeapon.height = uiWeapon.height - paddingBottom - paddingTop;;
		uiColumnIconWeapon.width = columnIconWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiScore.height;
		paddingTop = PortraitLayout.paddingTop2 * uiScore.height;
		uiColumnIconScore.x = uiScore.x + paddingLeft;
		uiColumnIconScore.y = uiScore.y + paddingBottom;
		uiColumnIconScore.height = uiScore.height - paddingBottom - paddingTop;;
		uiColumnIconScore.width = columnIconWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiTime.height;
		paddingTop = PortraitLayout.paddingTop2 * uiTime.height;
		uiColumnIconTime.x = uiTime.x + paddingLeft;
		uiColumnIconTime.y = uiTime.y + paddingBottom;
		uiColumnIconTime.height = uiTime.height - paddingBottom - paddingTop;;
		uiColumnIconTime.width = columnIconWidth;

		
		
		// Data column dimensions
		paddingBottom = PortraitLayout.paddingBottom2 * uiEnemy.height;
		paddingTop = PortraitLayout.paddingTop2 * uiEnemy.height;
		uiColumnDataEnemy.x = uiEnemy.x + paddingLeft + uiColumnIconEnemy.width + paddingHorizontal;
		uiColumnDataEnemy.y = uiEnemy.y + paddingBottom;
		uiColumnDataEnemy.height = uiEnemy.height - paddingBottom - paddingTop;
		uiColumnDataEnemy.width = columnDataWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiTokugi.height;
		paddingTop = PortraitLayout.paddingTop2 * uiTokugi.height;
		uiColumnDataTokugi.x = uiTokugi.x + paddingLeft + uiColumnIconTokugi.width + paddingHorizontal;
		uiColumnDataTokugi.y = uiTokugi.y + paddingBottom;
		uiColumnDataTokugi.height = uiTokugi.height - paddingBottom - paddingTop;
		uiColumnDataTokugi.width = columnDataWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiWeapon.height;
		paddingTop = PortraitLayout.paddingTop2 * uiWeapon.height;
		uiColumnDataWeapon.x = uiWeapon.x + paddingLeft + uiColumnIconWeapon.width + paddingHorizontal;
		uiColumnDataWeapon.y = uiWeapon.y + paddingBottom;
		uiColumnDataWeapon.height = uiWeapon.height - paddingBottom - paddingTop;
		uiColumnDataWeapon.width = columnDataWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiScore.height;
		paddingTop = PortraitLayout.paddingTop2 * uiScore.height;
		uiColumnDataScore.x = uiScore.x + paddingLeft + uiColumnIconScore.width + paddingHorizontal;
		uiColumnDataScore.y = uiScore.y + paddingBottom;
		uiColumnDataScore.height = uiScore.height - paddingBottom - paddingTop;
		uiColumnDataScore.width = columnDataWidth;
		
		paddingBottom = PortraitLayout.paddingBottom2 * uiTime.height;
		paddingTop = PortraitLayout.paddingTop2 * uiTime.height;
		uiColumnDataTime.x = uiTime.x + paddingLeft + uiColumnIconTime.width + paddingHorizontal;
		uiColumnDataTime.y = uiTime.y + paddingBottom;
		uiColumnDataTime.height = uiTime.height - paddingBottom - paddingTop;;
		uiColumnDataTime.width = columnDataWidth;
		
		
		
		// Level name text/image dimensions ( 1 column)
		paddingBottom = PortraitLayout.paddingBottom2 * uiLevelName.height;
		paddingTop = PortraitLayout.paddingTop2 * uiLevelName.height;
		uiTextLevelName.x = uiLevelName.x + paddingLeft;
		uiTextLevelName.y = uiLevelName.y + paddingBottom;
		uiTextLevelName.height = uiLevelName.height - paddingBottom - paddingTop;
		uiTextLevelName.width = uiLevelName.width - paddingLeft - paddingRight;


		

		
		
		
		// ====================================
		//        Layout hp bar player.
		// ====================================

		paddingHorizontal = PortraitLayout.paddingHorizontal2 * widgetAvailableWidth;
		paddingLeft = PortraitLayout.paddingLeft2 * widgetAvailableWidth;
		paddingRight = PortraitLayout.paddingRight2 * widgetAvailableWidth;
		paddingVertical = PortraitLayout.paddingVertical2 * uiPainBar.height;
		paddingTop = PortraitLayout.paddingTop2 * uiPainBar.height;
		paddingBottom = PortraitLayout.paddingBottom2 * uiPainBar.height;
		
		float painBarTotalHeight = 1.0f / (PortraitLayout.painBarMeterHeight + PortraitLayout.painBarCounterHeight);
		float relativeHeightPainBarMeter = PortraitLayout.painBarMeterHeight * painBarTotalHeight;
		float relativeHeightPainBarCounter = PortraitLayout.painBarCounterHeight * painBarTotalHeight;
		
		float painBarAvailableHeight = uiPainBar.height - paddingTop - paddingBottom - paddingVertical;

		uiPainBarCounter.x = uiPainBar.x + paddingLeft;
		uiPainBarCounter.y = uiPainBar.y + paddingBottom;
		uiPainBarCounter.height = relativeHeightPainBarCounter * painBarAvailableHeight;
		uiPainBarCounter.width = uiPainBar.width - paddingLeft - paddingRight;
		
		uiPainBarMeter.x = uiPainBar.x + paddingLeft;
		uiPainBarMeter.y = uiPainBar.y + paddingBottom + uiPainBarCounter.height + paddingVertical;
		uiPainBarMeter.width = uiPainBar.width - paddingLeft - paddingRight;
		uiPainBarMeter.height = relativeHeightPainBarMeter * painBarAvailableHeight;

		
		
		
		
		
		// ====================================
		//            Layout icons.
		// ====================================

		uiIconTime.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTime, PortraitLayout.padding3, PortraitLayout.iconTimeAspectRatio));
		uiIconScore.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconScore, PortraitLayout.padding3, PortraitLayout.iconTimeAspectRatio));
		uiIconWeapon.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconWeapon, PortraitLayout.padding3, PortraitLayout.iconTimeAspectRatio));
		uiIconTokugi.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTokugi, PortraitLayout.padding3, PortraitLayout.iconTimeAspectRatio));
		uiIconEnemy.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconEnemy, PortraitLayout.padding3, PortraitLayout.iconTimeAspectRatio));
	}

	private void computeUILayoutLandscape() {
		float paddingLeft, paddingRight, paddingTop, paddingBottom, paddingHorizontal, paddingVertical;
		
		
		// ====================================
		//  Layout main containers vertically.
		// ====================================
		
		// Relative width for each widget.
		float totalRelativeWidth = 1.0f / (
				  LandscapeLayout.widgetWeaponWidth
				+ LandscapeLayout.widgetTokugiWidth
				+ LandscapeLayout.widgetEnemyWidth
				+ LandscapeLayout.widgetLevelNameWidth
				+ LandscapeLayout.widgetPainBarWidth
				+ LandscapeLayout.widgetScoreWidth
				+ LandscapeLayout.widgetTimeWidth
				);
		
		float relativeWidthWeapon = LandscapeLayout.widgetWeaponWidth * totalRelativeWidth;
		float relativeWidthTokugi = LandscapeLayout.widgetTokugiWidth * totalRelativeWidth;
		float relativeWidthEnemy = LandscapeLayout.widgetEnemyWidth * totalRelativeWidth;
		float relativeWidthLevelName = LandscapeLayout.widgetLevelNameWidth * totalRelativeWidth;
		float relativeWidthPainBar = LandscapeLayout.widgetPainBarWidth * totalRelativeWidth;
		float relativeWidthScore= LandscapeLayout.widgetScoreWidth * totalRelativeWidth;
		float relativeWidthTime = LandscapeLayout.widgetTimeWidth * totalRelativeWidth;
	
		
		
		// Absolute padding in pixels from relative padding.
		paddingLeft = LandscapeLayout.paddingLeft * screenBounds.width;
		paddingRight = LandscapeLayout.paddingRight * screenBounds.width;
		paddingTop = LandscapeLayout.paddingTop * screenBounds.height;
		paddingBottom = LandscapeLayout.paddingBottom * screenBounds.height;
		paddingHorizontal = LandscapeLayout.paddingHorizontal * screenBounds.width;
		paddingVertical = LandscapeLayout.paddingVertical * screenBounds.height;
		
		
		
		// Available width for all vertically stacked widgets stacked together.
		float widgetAvailableWidth = screenBounds.width - (paddingLeft + paddingRight + 6 * paddingHorizontal);		
		
		
		
		// Available height for each main container. They will be stretched as much as possible.
		float widgetAvailableHeight = screenBounds.height - (paddingTop + paddingBottom);
		
		// Layout main containers horizontally, from left to right
		float widgetPosX = paddingLeft + screenBounds.x;

		uiLevelName.x = widgetPosX;
		uiLevelName.width = widgetAvailableWidth * relativeWidthLevelName;
		widgetPosX += uiLevelName.width + paddingHorizontal;
		
		uiTime.x = widgetPosX;
		uiTime.width = widgetAvailableWidth * relativeWidthTime;
		widgetPosX += uiTime.width + paddingHorizontal;
		
		uiScore.x = widgetPosX;
		uiScore.width = widgetAvailableWidth * relativeWidthScore;
		widgetPosX += uiScore.width + paddingHorizontal;
		
		uiPainBar.x = widgetPosX;
		uiPainBar.width = widgetAvailableWidth * relativeWidthPainBar;
		widgetPosX += uiPainBar.width + paddingHorizontal;
		
		uiWeapon.x = widgetPosX;
		uiWeapon.width = widgetAvailableWidth * relativeWidthWeapon;
		widgetPosX += uiWeapon.width + paddingHorizontal;
		
		uiTokugi.x = widgetPosX;
		uiTokugi.width = widgetAvailableWidth * relativeWidthTokugi;
		widgetPosX += uiTokugi.width + paddingHorizontal;
		
		uiEnemy.x = widgetPosX;
		uiEnemy.width = widgetAvailableWidth * relativeWidthEnemy;
		widgetPosX += uiEnemy.width + paddingHorizontal;
		
		
		// Set vertical position and height.
		float widgetPosY = screenBounds.y + paddingBottom;
		uiEnemy.y = widgetPosY;
		uiEnemy.height = widgetAvailableHeight;

		uiTokugi.y = widgetPosY;
		uiTokugi.height = widgetAvailableHeight;
		
		uiWeapon.y = widgetPosY;
		uiWeapon.height = widgetAvailableHeight;
		
		uiPainBar.y = widgetPosY;
		uiPainBar.height = widgetAvailableHeight;
		
		uiScore.y = widgetPosY;
		uiScore.height= widgetAvailableHeight;

		uiTime.y = widgetPosY;
		uiTime.height = widgetAvailableHeight;
		
		uiLevelName.y = widgetPosY;
		uiLevelName.height = widgetAvailableHeight;
		
		
		// ====================================
		//     Layout icon and data row.
		// ====================================


		
		paddingVertical = LandscapeLayout.paddingVertical2 * widgetAvailableHeight;
		paddingTop = LandscapeLayout.paddingTop2 * widgetAvailableHeight;
		paddingBottom = LandscapeLayout.paddingBottom* widgetAvailableHeight;
		
		
		
		// Relative height for each row.
		float totalRelativeHeight = 1.0f / (LandscapeLayout.rowIconHeight + LandscapeLayout.rowDataHeight);
		float relativeHeightRowIcon = LandscapeLayout.rowIconHeight * totalRelativeHeight;
		float relativeHeightRowData = LandscapeLayout.rowDataHeight  * totalRelativeHeight;
		
		
		
		// Height of both row.
		float rowIconHeight = (widgetAvailableHeight - paddingVertical - paddingTop - paddingBottom) * relativeHeightRowIcon;
		float rowDataHeight = (widgetAvailableHeight - paddingVertical - paddingTop - paddingBottom) * relativeHeightRowData ;		
		
		
			
		// Icon column dimensions
		paddingLeft = LandscapeLayout.paddingLeft2 * uiEnemy.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiEnemy.width;
		uiColumnIconEnemy.x = uiEnemy.x + paddingLeft;
		uiColumnIconEnemy.y = uiEnemy.y + uiEnemy.height - paddingTop - rowIconHeight;
		uiColumnIconEnemy.height = rowIconHeight;
		uiColumnIconEnemy.width = uiEnemy.width - paddingLeft - paddingRight;
		
		paddingLeft = LandscapeLayout.paddingLeft2 * uiTokugi.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiTokugi.width;
		uiColumnIconTokugi.x = uiTokugi.x + paddingLeft;
		uiColumnIconTokugi.y = uiTokugi.y + uiTokugi.height - paddingTop - rowIconHeight;
		uiColumnIconTokugi.height = rowIconHeight;
		uiColumnIconTokugi.width = uiTokugi.width - paddingLeft - paddingRight;

		paddingLeft = LandscapeLayout.paddingLeft2 * uiWeapon.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiWeapon.width;
		uiColumnIconWeapon.x = uiWeapon.x + paddingLeft;
		uiColumnIconWeapon.y = uiWeapon.y + uiWeapon.height - paddingTop - rowIconHeight;
		uiColumnIconWeapon.height = rowIconHeight;
		uiColumnIconWeapon.width = uiWeapon.width - paddingLeft - paddingRight;
		
		paddingLeft = LandscapeLayout.paddingLeft2 * uiScore.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiScore.width;
		uiColumnIconScore.x = uiScore.x + paddingLeft;
		uiColumnIconScore.y = uiScore.y + uiScore.height - paddingTop - rowIconHeight;
		uiColumnIconScore.height = rowIconHeight;
		uiColumnIconScore.width = uiScore.width - paddingLeft - paddingRight;
		
		paddingLeft = LandscapeLayout.paddingLeft2 * uiTime.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiTime.width;
		uiColumnIconTime.x = uiTime.x + paddingLeft;
		uiColumnIconTime.y = uiTime.y + uiTime.height - paddingTop - rowIconHeight;
		uiColumnIconTime.height = rowIconHeight;
		uiColumnIconTime.width = uiTime.width - paddingLeft - paddingRight;

		
		
		// Data row dimensions
		paddingLeft = LandscapeLayout.paddingLeft2 * uiEnemy.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiEnemy.width;
		uiColumnDataEnemy.x = uiEnemy.x + paddingLeft;
		uiColumnDataEnemy.y = uiEnemy.y + paddingBottom;
		uiColumnDataEnemy.width = uiEnemy.width - paddingLeft - paddingRight;
		uiColumnDataEnemy.height = rowDataHeight;
		
		paddingLeft = LandscapeLayout.paddingLeft2 * uiTokugi.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiTokugi.width;
		uiColumnDataTokugi.x = uiTokugi.x + paddingLeft;
		uiColumnDataTokugi.y = uiTokugi.y + paddingBottom;
		uiColumnDataTokugi.width = uiTokugi.width - paddingLeft - paddingRight;
		uiColumnDataTokugi.height = rowDataHeight;
				
		paddingLeft = LandscapeLayout.paddingLeft2 * uiWeapon.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiWeapon.width;
		uiColumnDataWeapon.x = uiWeapon.x + paddingLeft;
		uiColumnDataWeapon.y = uiWeapon.y + paddingBottom;
		uiColumnDataWeapon.width = uiWeapon.width - paddingLeft - paddingRight;
		uiColumnDataWeapon.height = rowDataHeight;
		
		paddingLeft = LandscapeLayout.paddingLeft2 * uiScore.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiScore.width;
		uiColumnDataScore.x = uiScore.x + paddingLeft;
		uiColumnDataScore.y = uiScore.y + paddingBottom;
		uiColumnDataScore.width = uiScore.width - paddingLeft - paddingRight;
		uiColumnDataScore.height = rowDataHeight;
		
		paddingLeft = LandscapeLayout.paddingLeft2 * uiTime.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiTime.width;
		uiColumnDataTime.x = uiTime.x + paddingLeft;
		uiColumnDataTime.y = uiTime.y + paddingBottom;
		uiColumnDataTime.width = uiScore.width - paddingLeft - paddingRight;
		uiColumnDataTime.height = rowDataHeight;
		
		
		
		// Level name text/image dimensions ( 1 column)
		paddingLeft = LandscapeLayout.paddingLeft2 * uiLevelName.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiLevelName.width;
		uiTextLevelName.x = uiLevelName.x + paddingLeft;
		uiTextLevelName.y = uiLevelName.y + paddingBottom;
		uiTextLevelName.height = uiLevelName.height - paddingBottom - paddingTop;
		uiTextLevelName.width = uiLevelName.width - paddingLeft - paddingRight;
		
		
		
		
		
		
		// ====================================
		//        Layout hp bar player.
		// ====================================

		paddingHorizontal = LandscapeLayout.paddingHorizontal2 * uiPainBar.width;
		paddingLeft = LandscapeLayout.paddingLeft2 * uiPainBar.width;
		paddingRight = LandscapeLayout.paddingRight2 * uiPainBar.width;
		paddingVertical = LandscapeLayout.paddingVertical2 * widgetAvailableHeight;
		paddingTop = LandscapeLayout.paddingTop2 * widgetAvailableHeight;
		paddingBottom = LandscapeLayout.paddingBottom2 * widgetAvailableHeight;
		
		float painBarTotalWidth = 1.0f / (LandscapeLayout.painBarMeterWidth + LandscapeLayout.painBarCounterWidth);
		float relativeWidthPainBarMeter = LandscapeLayout.painBarMeterWidth * painBarTotalWidth;
		float relativeWidthPainBarCounter = LandscapeLayout.painBarCounterWidth * painBarTotalWidth;
		
		float painBarAvailableWidth = uiPainBar.width - paddingLeft - paddingRight - paddingHorizontal;

		uiPainBarCounter.x = uiPainBar.x + paddingLeft;
		uiPainBarCounter.y = uiPainBar.y + paddingBottom;
		uiPainBarCounter.height = uiPainBar.height - paddingTop - paddingBottom;
		uiPainBarCounter.width = relativeWidthPainBarCounter * painBarAvailableWidth;
		
		uiPainBarMeter.x = uiPainBar.x + paddingLeft + uiPainBarCounter.width + paddingHorizontal;
		uiPainBarMeter.y = uiPainBar.y + paddingBottom;
		uiPainBarMeter.width = relativeWidthPainBarMeter * painBarAvailableWidth;
		uiPainBarMeter.height = uiPainBar.height - paddingTop - paddingBottom;
		
		

		
		
		
		// ====================================
		//            Layout icons.
		// ====================================

		uiIconTime.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTime, LandscapeLayout.padding3, LandscapeLayout.iconTimeAspectRatio));
		uiIconScore.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconScore, LandscapeLayout.padding3, LandscapeLayout.iconTimeAspectRatio));
		uiIconWeapon.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconWeapon, LandscapeLayout.padding3, LandscapeLayout.iconTimeAspectRatio));
		uiIconTokugi.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconTokugi, LandscapeLayout.padding3, LandscapeLayout.iconTimeAspectRatio));
		uiIconEnemy.set(layoutCenteredInBoxAndKeepAspectRatio(uiColumnIconEnemy, LandscapeLayout.padding3, LandscapeLayout.iconTimeAspectRatio));
	}
	
	private Rectangle layoutCenteredInBoxAndKeepAspectRatio(Rectangle rectangle, Padding padding, float aspectRatio) {
		float paddingLeft = padding.left * rectangle.width;
		float paddingRight = padding.right * rectangle.width;
		float paddingTop = padding.top * rectangle.height;
		float paddingBottom = padding.bottom * rectangle.height;

		
		float availableWidth = rectangle.width - paddingLeft - paddingRight;
		float availableHeight = rectangle.height - paddingTop - paddingBottom;
		float centerX = 0.5f * ((rectangle.x + paddingLeft) + (rectangle.x + rectangle.width - paddingRight));
		float centerY = 0.5f * ((rectangle.y + paddingBottom) + (rectangle.y + rectangle.height - paddingTop));

		float height, width;
		
		if (availableWidth / availableHeight > aspectRatio) {
			height = availableHeight;
			width = height * aspectRatio;
		}
		else {
			width = availableWidth;
			height = width / aspectRatio;
		}

		Rectangle centered = new Rectangle();
		
		centered.x = centerX - width*0.5f;
		centered.y = centerY - height*0.5f;
		centered.width = width;
		centered.height = height;

		return centered;
	}
	
	private final static class PortraitLayout {
		// Level one, box.
		private final static float paddingTop= 0.016f;
		private final static float paddingBottom = 0.016f;
		private final static float paddingRight = 0.016f;
		private final static float paddingLeft = 0.016f;		
		private final static float paddingVertical = 0.016f;
		private final static float paddingHorizontal = 0.016f;

		// Level two, box in a box.
		private final static float paddingTop2= 0.1f;
		private final static float paddingBottom2 = 0.1f;
		private final static float paddingRight2 = 0.016f;
		private final static float paddingLeft2 = 0.016f;		
		private final static float paddingVertical2 = 0.1f;
		private final static float paddingHorizontal2 = 0.016f;
		
		// Level tree, box in a box in a box.
		private final static Padding padding3 = new StatusScreen.Padding(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f);
		
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
		
	}
	
	private final static class LandscapeLayout {
		// Level one, box.
		private final static float paddingTop= 0.016f;
		private final static float paddingBottom = 0.016f;
		private final static float paddingRight = 0.016f;
		private final static float paddingLeft = 0.016f;		
		private final static float paddingVertical = 0.016f;
		private final static float paddingHorizontal = 0.016f;

		// Level two, box in a box.
		private final static float paddingTop2= 0.016f;
		private final static float paddingBottom2 = 0.016f;
		private final static float paddingRight2 = 0.1f;
		private final static float paddingLeft2 = 0.1f;		
		private final static float paddingVertical2 = 0.016f;
		private final static float paddingHorizontal2 = 0.1f;
		
		// Level tree, box in a box in a box.
		private final static Padding padding3 = new StatusScreen.Padding(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f);
		
		private final static float widgetLevelNameWidth = 10;
		private final static float widgetTimeWidth = 10;
		private final static float widgetScoreWidth = 10;
		private final static float widgetPainBarWidth = 15;
		private final static float widgetWeaponWidth = 18;
		private final static float widgetTokugiWidth = 18;
		private final static float widgetEnemyWidth = 20;
		
		private final static float rowIconHeight = 20;
		private final static float rowDataHeight = 80;
		
		private final static float painBarMeterWidth = 65;
		private final static float painBarCounterWidth = 35;
		
		private final static float iconTimeAspectRatio = 1.0f;
		
	}
	
	private final static class Padding {
		public float top, bottom, left, right, vertical, horizontal;
		public Padding(float top, float bottom, float right, float left, float vertical, float horizontal) {
			this.top = top;
			this.bottom = bottom;
			this.left = left;
			this.right = right;
			this.vertical = vertical;
			this.horizontal = horizontal;
		}
	}
}