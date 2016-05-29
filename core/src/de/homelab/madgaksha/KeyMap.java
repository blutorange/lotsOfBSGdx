package de.homelab.madgaksha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public final class KeyMap {
	private KeyMap(){}
	
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
	
}
