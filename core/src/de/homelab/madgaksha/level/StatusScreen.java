package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.batchPixel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * The info viewport for highscores etc.
 * 
 * @author madgaksha
 *
 */
public class StatusScreen {
	private final static Logger LOG = Logger.getLogger(StatusScreen.class);
		
	private boolean landscapeMode;

	private final Rectangle screenBounds = new Rectangle();
	
	private final Rectangle uiLevelName = new Rectangle();
	private final Rectangle uiScoreTime = new Rectangle();
	private final Rectangle uiPainBar = new Rectangle();
	private final Rectangle uiArmament = new Rectangle();
	private final Rectangle uiEnemy = new Rectangle();

	private final Rectangle uiArmamentLeft = new Rectangle();
	private final Rectangle uiArmamentRight = new Rectangle();
	private final Rectangle uiEnemyLeft = new Rectangle();
	private final Rectangle uiEnemyRight = new Rectangle();
	private final Rectangle uiScore = new Rectangle();
	private final Rectangle uiTime = new Rectangle();
	
	public StatusScreen(int w, int h) {
		computeScreenDimensions(w, h);
		computeUILayout();
	}

	public void update(int screenWidth, int screenHeight) {
		computeScreenDimensions(screenWidth, screenHeight);
		computeUILayout();
	}

	public void render() {
		if (!landscapeMode) return;
		// TODO just testing
		NinePatch myNinePatch = ResourceCache.getNinePatch(ENinePatch.TEST_PIXEL);
		myNinePatch.setColor(Color.WHITE);
		myNinePatch.draw(batchPixel, uiLevelName.x, uiLevelName.y, uiLevelName.width, uiLevelName.height);
		myNinePatch.draw(batchPixel, uiScoreTime.x, uiScoreTime.y, uiScoreTime.width, uiScoreTime.height);
		myNinePatch.draw(batchPixel, uiPainBar.x, uiPainBar.y, uiPainBar.width, uiPainBar.height);
		myNinePatch.draw(batchPixel, uiArmament.x, uiArmament.y, uiArmament.width, uiArmament.height);
		myNinePatch.draw(batchPixel, uiEnemy.x, uiEnemy.y, uiEnemy.width, uiEnemy.height);
		
		myNinePatch.setColor(new Color(255, 0, 0, 0.5f));
		myNinePatch.draw(batchPixel, uiScore.x, uiScore.y, uiScore.width, uiScore.height);
		myNinePatch.draw(batchPixel, uiTime.x, uiTime.y, uiTime.width, uiTime.height);
		myNinePatch.draw(batchPixel, uiArmamentLeft.x, uiArmamentLeft.y, uiArmamentLeft.width, uiArmamentLeft.height);
		myNinePatch.draw(batchPixel, uiArmamentRight.x, uiArmamentRight.y, uiArmamentRight.width, uiArmamentRight.height);
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
			// Landscape
			landscapeMode = true;
			infoWidth = screenWidth - gameWidth;
			infoHeight = screenHeight;
			infoX = gameWidth;
			infoY = 0;
		} else {
			landscapeMode = false;
			infoHeight = screenHeight - gameWidth;
			infoWidth = screenWidth;
			infoX = 0;
			infoY = gameHeight;
		}

		// Store info.
		screenBounds.set(infoX, infoY, infoWidth, infoHeight);		
	}
	
	private void computeUILayout() {
		if (landscapeMode) computeUILayoutLandscape();
		else computeUILayoutPortrait();
	}

	private void computeUILayoutLandscape() {
		
		
		// ====================================
		//  Layout main containers vertically.
		// ====================================
		
		
		
		// Relative height for each widget.
		float totalRelativeHeight = 1.0f / (LandscapeLayout.widgetArmamentHeight + LandscapeLayout.widgetEnemyHeight
				+ LandscapeLayout.widgetLevelNameHeight + LandscapeLayout.widgetPainBarHeight
				+ LandscapeLayout.widgetScoreTimeHeight);
		float relativeHeightArmament = LandscapeLayout.widgetArmamentHeight * totalRelativeHeight;
		float relativeHeightEnemy = LandscapeLayout.widgetEnemyHeight * totalRelativeHeight;
		float relativeHeightLevelName = LandscapeLayout.widgetLevelNameHeight * totalRelativeHeight;
		float relativeHeightPainBar = LandscapeLayout.widgetPainBarHeight * totalRelativeHeight;
		float relativeHeightScoreTime = LandscapeLayout.widgetScoreTimeHeight * totalRelativeHeight;
		
		
		
		// Available height for all vertically stacked widgets stacked together.
		float widgetAvailableHeight = screenBounds.height
				- (LandscapeLayout.paddingBottom + LandscapeLayout.paddingTop + 4 * LandscapeLayout.paddingVertical);		
		
		// Available width for each main container. They will be stretched as much as possible.
		float widgetAvailableWidth = screenBounds.width - (LandscapeLayout.paddingLeft + LandscapeLayout.paddingRight); 

		
		
		// Layout main containers vertically, from bottom to top
		float widgetPosY = LandscapeLayout.paddingBottom;

		uiEnemy.y = widgetPosY;
		uiEnemy.height = widgetAvailableHeight * relativeHeightEnemy;
		widgetPosY += uiEnemy.height + LandscapeLayout.paddingVertical;
				
		uiArmament.y = widgetPosY;
		uiArmament.height = widgetAvailableHeight * relativeHeightArmament;
		widgetPosY += uiArmament.height + LandscapeLayout.paddingVertical;
		
		uiPainBar.y = widgetPosY;
		uiPainBar.height = widgetAvailableHeight * relativeHeightPainBar;
		widgetPosY += uiPainBar.height + LandscapeLayout.paddingVertical;
		
		uiScoreTime.y = widgetPosY;
		uiScoreTime.height = widgetAvailableHeight * relativeHeightScoreTime;
		widgetPosY += uiScoreTime.height + LandscapeLayout.paddingVertical;
		
		uiLevelName.y = widgetPosY;
		uiLevelName.height = widgetAvailableHeight * relativeHeightLevelName;
		widgetPosY += uiLevelName.height + LandscapeLayout.paddingVertical;
		
		
		
		// Set horizontal position and width.
		float widgetPosX = screenBounds.x + LandscapeLayout.paddingLeft;
		uiEnemy.x = widgetPosX;
		uiEnemy.width = widgetAvailableWidth;
		
		uiArmament.x = widgetPosX;
		uiArmament.width = widgetAvailableWidth;
		
		uiPainBar.x = widgetPosX;
		uiPainBar.width = widgetAvailableWidth;
		
		uiScoreTime.x = widgetPosX;
		uiScoreTime.width = widgetAvailableWidth;
		
		uiLevelName.x = widgetPosX;
		uiLevelName.width = widgetAvailableWidth;
		
		
		
		
		
		
		// ====================================
		// Layout score and time horizontally.
		// ====================================
		
		
		
		// Relative width for each widget.
		float totalRelativeWidthScoreTime = 1.0f / (LandscapeLayout.widgetTimeWidth + LandscapeLayout.widgetScoreWidth);
		float relativeWidthScore = LandscapeLayout.widgetScoreWidth * totalRelativeWidthScoreTime;
		float relativeWidthTime = LandscapeLayout.widgetTimeWidth * totalRelativeWidthScoreTime;
		
		float scoreTimeAvailableWidth = uiScoreTime.width - LandscapeLayout.paddingHorizontal;
		float scoreTimePosX = uiScoreTime.x;

		
		
		// Layout horizontally.
		uiScore.x = scoreTimePosX;
		uiScore.width = scoreTimeAvailableWidth * relativeWidthScore;
		scoreTimePosX += uiScore.width + LandscapeLayout.paddingHorizontal;
		
		uiTime.x = scoreTimePosX;
		uiTime.width = scoreTimeAvailableWidth * relativeWidthTime;
		scoreTimePosX += uiTime.width + LandscapeLayout.paddingHorizontal;

		
		
		// Set vertical position and width.
		uiScore.y = uiScoreTime.y;
		uiScore.height = uiScoreTime.height;

		uiTime.y = uiScoreTime.y;
		uiTime.height = uiScoreTime.height;
		
		
		
		
		
		
		// ============================================
		// Layout armament left and right horizontally.
		// ============================================


		
		// Total available width for both widgets.
		float armamentAvailableWidth = uiArmament.width - LandscapeLayout.paddingHorizontal;

		// Compute width of the left widget for desired aspect ratio.
		float armamentLeftDesiredWidth = LandscapeLayout.widgetArmamentLeftAspectRatio * uiArmament.height;
		float armamentLeftWidth = armamentLeftDesiredWidth;
		if (armamentLeftWidth > armamentAvailableWidth - uiArmament.height) armamentLeftWidth = armamentAvailableWidth - uiArmament.height;

		// Give right widget the remaining space.
		float armamentRightWidth = armamentAvailableWidth - armamentLeftWidth;
		
		// Set position and size for the right widget.
		uiArmamentRight.x = uiArmament.x + armamentLeftWidth + LandscapeLayout.paddingHorizontal;
		uiArmamentRight.y = uiArmament.y;
		uiArmamentRight.width = armamentRightWidth;
		uiArmamentRight.height = uiArmament.height;
		
		// Set position and size for the left widget.
		// Adjust height if available space was too small.
		uiArmamentLeft.x = uiArmament.x;
		uiArmamentLeft.width = armamentLeftWidth;
		uiArmamentLeft.y = uiArmament.y;
		uiArmamentLeft.height = armamentLeftWidth / LandscapeLayout.widgetArmamentLeftAspectRatio;
	}

	private void computeUILayoutPortrait() {
		// TODO Auto-generated method stub
		
	}
	
	private final static class LandscapeLayout {
		private final static float paddingTop= 10.0f;
		private final static float paddingBottom = 10.0f;
		private final static float paddingRight = 10.0f;
		private final static float paddingLeft = 10.0f;
		
		private final static float paddingVertical = 10.0f;
		private final static float paddingHorizontal = 10.0f;
		
		private final static float widgetLevelNameHeight = 10;
		private final static float widgetScoreTimeHeight = 10;
		private final static float widgetPainBarHeight = 10;
		private final static float widgetArmamentHeight = 20;
		private final static float widgetEnemyHeight = 20;
		
		private final static float widgetScoreWidth = 60;
		private final static float widgetTimeWidth = 40;
		
		private final static float widgetArmamentLeftAspectRatio = 1.0f;
	}
}