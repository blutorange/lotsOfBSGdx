package de.homelab.madgaksha.cutscenesystem.event;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.KeyMap;
import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.textbox.EFaceVariation;
import de.homelab.madgaksha.cutscenesystem.textbox.FancyTextbox;
import de.homelab.madgaksha.enums.ESpeaker;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETextbox;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public class EventTextbox extends ACutsceneEvent implements Poolable {
	private static enum Mode {
		TEXT_APPEARING,
		IDLE,
		WAIT_FOR_EXIT
		;
	}
	
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EventTextbox.class);

	private FancyTextbox textbox;
	private boolean textboxDone = false;
	/** Rate at which glyphs will appear in Hz. */
	private float textSpeed = 1.0f;
	private String lines;
	private float textAdvance = 0.0f;
	private Mode mode = Mode.TEXT_APPEARING;
	private VoicePlayer voicePlayer = null;
	private ESound soundOnTextboxAdvance = ESound.TEXTBOX_ADVANCE;
	private ESound soundOnTextAdvance = ESound.TEXT_ADVANCE;
	private float timeUntilExit;
	
	public EventTextbox() {
		reset();
		this.voicePlayer = new VoicePlayer();
	}

	public boolean setTextbox(FancyTextbox textbox) {
		if (textbox != null) {
			this.textbox = textbox;
			return true;
		}
		else return false;
	}
	public boolean setTextbox(ETextbox textbox) {
		if (textbox != null) return setTextbox(ResourceCache.getTextbox(textbox));
		return false;
	}
	/** @param soundOnTextboxAdvance The sound to play when proceeding to the next textbox. */
	public void setSoundOnTextboxAdvance(ESound soundOnTextboxAdvance) {
		this.soundOnTextboxAdvance = soundOnTextboxAdvance;
	}
	/** @param soundOnTextAdvance The sound to play when the next character appears on-screen. */
	public void setSoundOnTextAdvance(ESound soundOnTextAdvance) {
		this.soundOnTextAdvance = soundOnTextAdvance;
	}
	/**
	 * Sets the rate at which individual characters will appear.
	 * @param textSpeed The rate in Hz.
	 */
	public void setTextSpeed(float textSpeed) {
		this.textSpeed = textSpeed;
	}
	public void setLines(String lines) {
		if (lines != null) this.lines = lines;
	}
	public void setTextColor(Color mainTextColor) {
		if (this.textbox != null) this.textbox.setTextColor(mainTextColor);
	}
	public void setBoxColor(Color boxColor) {
		if (this.textbox != null) this.textbox.setBoxColor(boxColor);
	}
	public void setTextHeightRatio(float textHeightRatio) {
		if (this.textbox != null) this.textbox.setTextHeightRatio(textHeightRatio);
	}
	public void setFont(EFreeTypeFontGenerator freeTypeFontGenerator) {
		if (this.textbox != null) this.textbox.setFont(freeTypeFontGenerator);
	}
	public void setSpeaker(ESpeaker speaker) {
		if (this.textbox != null) this.textbox.setSpeaker(speaker);
	}
	public void setFaceVariation(EFaceVariation faceVariation) {
		if (this.textbox != null) this.textbox.setFaceVariation(faceVariation);
	}
	
	@Override
	public boolean isFinished() {
		return textboxDone;
	}
	
	@Override
	public boolean begin() {
		textboxDone = false;
		textAdvance = 0.0f;
		mode = Mode.TEXT_APPEARING;
		if (this.textbox == null) return false;
		this.textbox.setLines(lines);
		return true;
	}
	
	@Override
	public void render() {
		viewportPixel.apply();
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		batchPixel.begin();
		textbox.render();
		batchPixel.end();
	}

	@Override
	public void update(float deltaTime) {
		switch (mode) {
			case TEXT_APPEARING:
				// Show text incrementally.
				int oldTextAdvance = (int)textAdvance;
				textAdvance += textSpeed * deltaTime;
				if (textAdvance >= textbox.getGlyphCount()) {
					mode = Mode.IDLE;
					textAdvance = textbox.getGlyphCount();
				}
				else if (KeyMap.isTextboxAdvanceJustPressed()) {
					// Fast-forwarding text.
					textAdvance = textbox.getGlyphCount();
				}
				textbox.setRenderedGlyphCount((int)textAdvance);
				if (textbox.getRenderedGlyphCount() != oldTextAdvance && voicePlayer != null)
					voicePlayer.play(soundOnTextAdvance);					
				break;
			case IDLE:
				// Allow next textbox / event.
				if (KeyMap.isTextboxAdvanceJustPressed()) {
					timeUntilExit = 0.0f;
					if (voicePlayer != null && soundOnTextboxAdvance != null) {
						voicePlayer.playUnconditionally(soundOnTextboxAdvance);
						timeUntilExit = soundOnTextboxAdvance.getDuration()*0.25f;
					}
					mode = Mode.WAIT_FOR_EXIT;
				}
				break;
			case WAIT_FOR_EXIT:
				timeUntilExit -= deltaTime;
				if (timeUntilExit <= 0.0f) textboxDone = true;
				break;
		}
	}

	@Override
	public void resize(int width, int height) {
		if (textbox != null) textbox.resize(width, height);
	}

	@Override
	public void reset() {
		this.textbox = null;
		this.voicePlayer = null;
		this.soundOnTextAdvance = ESound.TEXT_ADVANCE;
		this.soundOnTextboxAdvance = ESound.TEXTBOX_ADVANCE;
		this.textboxDone = false;
		this.lines = StringUtils.EMPTY;
		this.textSpeed = Math.max(0.1f, game.params.requestedTextboxSpeed);
		this.textAdvance = 0.0f;
		this.mode = Mode.TEXT_APPEARING;
	}
}
