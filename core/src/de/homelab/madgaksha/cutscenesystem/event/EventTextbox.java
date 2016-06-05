package de.homelab.madgaksha.cutscenesystem.event;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.KeyMap;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
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
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EventTextbox.class);

	private FancyTextbox textbox;
	private boolean textboxDone = false;
	private String lines;
	
	public EventTextbox() {
		reset();
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
		if (KeyMap.isTextboxAdvanceJustPressed()) {
			SoundPlayer.getInstance().play(ESound.TEXTBOX_NEXT);
			textboxDone = true;
		}
	}

	@Override
	public void resize(int width, int height) {
		if (textbox != null) textbox.resize(width, height);
	}

	@Override
	public void reset() {
		this.textbox = null;
		this.textboxDone = false;
		this.lines = StringUtils.EMPTY;
	}
}
