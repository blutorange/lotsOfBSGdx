package de.homelab.madgaksha.textboxsystem;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import de.homelab.madgaksha.resourcecache.EBitmapFont;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public class FaceTextbox extends SpeakerTextbox {
	
	protected Texture faceTexture;
	protected int offsetFaceX;
	protected int offsetFaceY;
	protected int faceWidth;
	protected int faceHeight;
	
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, ETexture faceTexture) {
		super(lines, bitmapFont, ninePatch, speaker);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceY = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, ETexture faceTexture, Color textColor) {
		super(lines, bitmapFont, ninePatch, speaker, textColor);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceY = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, ETexture faceTexture, Color textColor, Color boxColor) {
		super(lines, bitmapFont, ninePatch, speaker, textColor, boxColor);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceY = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}	
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, ETexture faceTexture, Color textColor, Color boxColor, Color speakerColor) {
		super(lines, bitmapFont, ninePatch, speaker, textColor, boxColor, speakerColor);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceY = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}	
	public FaceTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker, ETexture faceTexture, int offsetSpeakerX, int offsetSpeakerY, int offsetFaceX, int offsetFaceY, int faceWidth, int faceHeight) {
		super(lines, bitmapFont, ninePatch, speaker, offsetSpeakerX, offsetSpeakerY);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = offsetFaceX;
		this.offsetFaceY = offsetFaceY;
		this.faceWidth = faceWidth;
		this.faceHeight = faceHeight;
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker, ETexture faceTexture, int offsetSpeakerX, int offsetSpeakerY, int offsetFaceX, int offsetFaceY, int faceWidth, int faceHeight, Color textColor) {
		super(lines, bitmapFont, ninePatch, speaker, offsetSpeakerX, offsetSpeakerY, textColor);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = offsetFaceX;
		this.offsetFaceY = offsetFaceY;
		this.faceWidth = faceWidth;
		this.faceHeight = faceHeight;
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker, ETexture faceTexture, int offsetSpeakerX, int offsetSpeakerY, int offsetFaceX, int offsetFaceY, int faceWidth, int faceHeight, Color textColor, Color boxColor) {
		super(lines, bitmapFont, ninePatch, speaker, offsetSpeakerX, offsetSpeakerY, textColor, boxColor);
		this.faceTexture = ResourceCache.getTexture(faceTexture);
		this.offsetFaceX = offsetFaceX;
		this.offsetFaceY = offsetFaceY;
		this.faceWidth = faceWidth;
		this.faceHeight = faceHeight;
	}
	
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, Texture faceTexture) {
		super(lines, bitmapFont, ninePatch, speaker);
		this.faceTexture = faceTexture;
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceY = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, Texture faceTexture, Color textColor) {
		super(lines, bitmapFont, ninePatch, speaker, textColor);
		this.faceTexture = faceTexture;
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceY = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, Texture faceTexture, Color textColor, Color boxColor) {
		super(lines, bitmapFont, ninePatch, speaker, textColor, boxColor);
		this.faceTexture = faceTexture;
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceX = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}	
	public FaceTextbox(String lines, EBitmapFont bitmapFont, ENinePatch ninePatch, CharSequence speaker, Texture faceTexture, Color textColor, Color boxColor, Color speakerColor) {
		super(lines, bitmapFont, ninePatch, speaker, textColor, boxColor, speakerColor);
		this.faceTexture = faceTexture;
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceX = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}	
	public FaceTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker, Texture faceTexture, int offsetSpeakerX, int offsetSpeakerY, int offsetFaceX, int offsetFaceY, int faceWidth, int faceHeight) {
		super(lines, bitmapFont, ninePatch, speaker, offsetSpeakerX, offsetSpeakerY);
		this.faceTexture = faceTexture;
		this.offsetFaceX = offsetFaceX;
		this.offsetFaceY = offsetFaceY;
		this.faceWidth = faceWidth;
		this.faceHeight = faceHeight;
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker, Texture faceTexture, int offsetSpeakerX, int offsetSpeakerY, int offsetFaceX, int offsetFaceY, int faceWidth, int faceHeight, Color textColor) {
		super(lines, bitmapFont, ninePatch, speaker, offsetSpeakerX, offsetSpeakerY, textColor);
		this.faceTexture = faceTexture;
		this.offsetFaceX = offsetFaceX;
		this.offsetFaceY = offsetFaceY;
		this.faceWidth = faceWidth;
		this.faceHeight = faceHeight;
	}
	public FaceTextbox(String lines, EBitmapFont bitmapFont, NinePatch ninePatch, CharSequence speaker, Texture faceTexture, int offsetSpeakerX, int offsetSpeakerY, int offsetFaceX, int offsetFaceY, int faceWidth, int faceHeight, Color textColor, Color boxColor) {
		super(lines, bitmapFont, ninePatch, speaker, offsetSpeakerX, offsetSpeakerY, textColor, boxColor);
		this.faceTexture = faceTexture;
		this.offsetFaceX = offsetFaceX;
		this.offsetFaceY = offsetFaceY;
		this.faceWidth = faceWidth;
		this.faceHeight = faceHeight;
	}

	public void setNinePatch(ENinePatch ninePatch) {
		super.setNinePatch(ninePatch);
		this.offsetFaceX = ninePatch.getOffsetFaceX();
		this.offsetFaceX = ninePatch.getOffsetFaceY();
		this.faceWidth = ninePatch.getFaceWidth();
		this.faceHeight = ninePatch.getFaceWidth();
	}
	public void setFaceTexture(ETexture faceTexture) {
		this.faceTexture = ResourceCache.getTexture(faceTexture);
	}
	public void setFaceTexture(Texture faceTexture) {
		this.faceTexture = faceTexture;
	}
	public void setOffsetFaceX(int offsetFaceX) {
		this.offsetFaceX = offsetFaceX;
	}
	public void setOffsetFaceY(int offsetFaceY) {
		this.offsetFaceY = offsetFaceY;
	}
	public void setFaceWidth(int faceWidth) {
		this.faceWidth = faceWidth;
	}
	public void setFaceHeight(int faceHeight) {
		this.faceHeight = faceHeight;
	}
	
	@Override
	public void render(float animationFactor) {
		super.render(animationFactor);
		if (viewportGame.getScreenWidth() > 459) {
			final int faceX = offsetFaceX + viewportGame.getScreenWidth() - (int) ninePatch.getPadRight() - viewportPixel.getScreenWidth()/2;
			final int faceY = offsetFaceY + ((int)ninePatch.getPadBottom())- viewportPixel.getScreenHeight()/2;		
			batchPixel.draw(faceTexture, faceX, faceY-animationFactor*boxh, faceWidth, faceHeight);
		}
	}
}