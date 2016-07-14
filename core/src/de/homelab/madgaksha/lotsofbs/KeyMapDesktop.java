package de.homelab.madgaksha.lotsofbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public final class KeyMapDesktop {
	private KeyMapDesktop() {
	}

	public final static boolean isTextboxAdvanceJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.ENTER);
	}

	public final static boolean isTextboxAdvancePressed() {
		return Gdx.input.isKeyPressed(Keys.ENTER);
	}

	public final static boolean isPlayerMoveRightPressed() {
		return Gdx.input.isKeyPressed(Keys.RIGHT);
	}

	public final static boolean isPlayerMoveLeftPressed() {
		return Gdx.input.isKeyPressed(Keys.LEFT);
	}

	public final static boolean isPlayerMoveUpPressed() {
		return Gdx.input.isKeyPressed(Keys.UP);
	}

	public final static boolean isPlayerMoveDownPressed() {
		return Gdx.input.isKeyPressed(Keys.DOWN);
	}

	public static boolean isPauseButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.ESCAPE);
	}

	public static boolean isPauseButtonPressed() {
		return Gdx.input.isKeyPressed(Keys.ESCAPE);
	}

	public static boolean isWeaponSwitchJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.PAGE_UP) || Gdx.input.isKeyJustPressed(Keys.Q);
	}

	public static boolean isTokugiSwitchJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.INSERT) || Gdx.input.isKeyJustPressed(Keys.W);
	}

	public static boolean isSpeedupPressed() {
		return Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
	}

	public static boolean isTokugiFireJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.M) && Gdx.input.isKeyPressed(Keys.X);
	}

	public static boolean isEnemyPrevJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.FORWARD_DEL) || Gdx.input.isKeyJustPressed(Keys.A);
	}

	public static boolean isEnemyNextJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.PAGE_DOWN) || Gdx.input.isKeyJustPressed(Keys.S);
	}

	public static boolean isActiveItemSwitchJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.HOME) || Gdx.input.isKeyJustPressed(Keys.E);
	}

	public static boolean isActiveItemUseJustPressed() {
		return Gdx.input.isKeyJustPressed(Keys.END) || Gdx.input.isKeyJustPressed(Keys.D);
	}
}
