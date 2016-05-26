package de.homelab.madgaksha.cutscenesystem;

import static de.homelab.madgaksha.GlobalBag.batchPixel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.resourcecache.EBitmapFont;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public class SpeakerTextbox extends Textbox {
	protected CharSequence speaker;
	protected int offsetSpeakerX;
	protected int offsetSpeakerY;
	protected Color speakerColor = Color.WHITE;

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker) {
		super(lines, bitmapFont, ResourceCache.getNinePatch(ninePatch));
		this.speaker = speaker;
		this.offsetSpeakerX = ninePatch.getOffsetSpeakerX();
		this.offsetSpeakerY = ninePatch.getOffsetSpeakerY();
	}

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker,
			Color textColor) {
		super(lines, bitmapFont, ResourceCache.getNinePatch(ninePatch), textColor);
		this.speaker = speaker;
		this.offsetSpeakerX = ninePatch.getOffsetSpeakerX();
		this.offsetSpeakerY = ninePatch.getOffsetSpeakerY();
	}

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker,
			Color textColor, Color boxColor) {
		super(lines, bitmapFont, ResourceCache.getNinePatch(ninePatch), textColor, boxColor);
		this.speaker = speaker;
		this.offsetSpeakerX = ninePatch.getOffsetSpeakerX();
		this.offsetSpeakerY = ninePatch.getOffsetSpeakerY();
	}

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker,
			Color textColor, Color boxColor, Color speakerColor) {
		super(lines, bitmapFont, ResourceCache.getNinePatch(ninePatch), textColor, boxColor);
		this.speaker = speaker;
		this.offsetSpeakerX = ninePatch.getOffsetSpeakerX();
		this.offsetSpeakerY = ninePatch.getOffsetSpeakerY();
		this.speakerColor = speakerColor;
	}

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker,
			int offsetSpeakerX, int offsetSpeakerY) {
		super(lines, bitmapFont, ninePatch);
		this.speaker = speaker;
		this.offsetSpeakerX = offsetSpeakerX;
		this.offsetSpeakerY = offsetSpeakerY;
	}

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker,
			int offsetSpeakerX, int offsetSpeakerY, Color textColor) {
		super(lines, bitmapFont, ninePatch, textColor);
		this.speaker = speaker;
		this.offsetSpeakerX = offsetSpeakerX;
		this.offsetSpeakerY = offsetSpeakerY;
	}

	public SpeakerTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker,
			int offsetSpeakerX, int offsetSpeakerY, Color textColor, Color boxColor) {
		super(lines, bitmapFont, ninePatch, textColor, boxColor);
		this.speaker = speaker;
		this.offsetSpeakerX = offsetSpeakerX;
		this.offsetSpeakerY = offsetSpeakerY;
	}

	public void setNinePatch(ENinePatch ninePatch) {
		this.ninePatch = ResourceCache.getNinePatch(ninePatch);
		this.offsetSpeakerX = ninePatch.getOffsetSpeakerX();
		this.offsetSpeakerY = ninePatch.getOffsetSpeakerY();
	}

	public void setNinePatch(NinePatch ninePatch) {
		this.ninePatch = ninePatch;
	}

	public void setSpeaker(CharSequence speaker) {
		this.speaker = speaker;
	}

	public void setOffsetSpeakerX(int offsetSpeakerX) {
		this.offsetSpeakerX = offsetSpeakerX;
	}

	public void setOffsetSpeakerY(int offsetSpeakerY) {
		this.offsetSpeakerY = offsetSpeakerY;
	}

	public void setSpeakerColor(Color speakerColor) {
		this.speakerColor = speakerColor;
	}

	@Override
	public void render(float animationFactor) {
		super.render(animationFactor);
		bitmapFont.setColor(speakerColor);
		bitmapFont.draw(batchPixel,
				speaker,
				textx + offsetSpeakerX,
				offsetSpeakerY + boxh - ninePatch.getPadTop() - animationFactor * boxh,
				0,
				speaker.length(),
				targetWidth,
				Align.bottomLeft,
				false,
				emptyString);
	}
}