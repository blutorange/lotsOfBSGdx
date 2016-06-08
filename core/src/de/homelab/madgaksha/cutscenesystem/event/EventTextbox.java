package de.homelab.madgaksha.cutscenesystem.event;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.DebugMode;
import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.KeyMapDesktop;
import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventTextbox.Setter;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.cutscenesystem.textbox.EFaceVariation;
import de.homelab.madgaksha.cutscenesystem.textbox.FancyTextbox;
import de.homelab.madgaksha.cutscenesystem.textbox.PlainTextbox;
import de.homelab.madgaksha.enums.ESpeaker;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETextbox;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.EventTextboxPool;

public class EventTextbox extends ACutsceneEvent {
	private static enum Mode {
		TRANSITION_IN,
		TEXT_APPEARING,
		IDLE,
		TRANSITION_OUT,
		WAIT_FOR_EXIT, 
		;
	}
	
	/**
	 * Used for changing settings while the textbox is active.
	 * @see EventTextbox#hotswapSetting(Setter)
	 * @author madgaksha
	 */
	public static interface Setter {
		public void set(EventTextbox textbox);
	}
	
	private final static Logger LOG = Logger.getLogger(EventTextbox.class);

	/** Speed at which textboxes appear immediately without animation. */
	private final static float IMMEDIATE_TRANSITION_SPEED = 2.0f/Game.MIN_DELTA_TIME;
	
	/** @see #interpolationIn */
	private final static Interpolation DEFAULT_INTERPOLATION_IN = Interpolation.sineIn;
	/** @see #interpolationOut */
	private final static Interpolation DEFAULT_INTERPOLATION_OUT = Interpolation.sineOut;
	
	/** @see #transitionInSpeed */
	private final static float NICE_TRANSITION_IN_SPEED = 3.0f;
	/** @see #transitionOutSpeed */
	private final static float NICE_TRANSITION_OUT_SPEED = 3.0f;
	
	/** The textbox used for rendering. */
	private FancyTextbox textbox;
	
	/** 
	 * Whether this textbox is currently active, ie. rendered on-screen. 
	 * Modifications via set methods are allowed only when it is not active,
	 * otherwise an {@link IllegalStateException} is thrown, unless {@link #strictMode} is deactivated. 
	 * <br><br>
	 * This textbox becomes active when {@link #begin()} is called and ends
	 * once the user presses the textbox advance button and this textbox
	 * has finished its out-transition.
	 */
	private boolean textboxDone = true;
	
	/** Whether modifications are disallowed while the textbox is active. */
	private boolean strictMode = true;
	
	/** The lines this textbox will render. */
	private String lines;

	/** Current state of the state machine for this textbox. */
	private Mode mode = Mode.TEXT_APPEARING;
	
	/** How much of the text is visible, for animating text in. */
	private float textAdvance = 0.0f;

	/** Rate at which glyphs will appear, in Hz. */
	private float textSpeed = 1.0f;
	
	/** Animation factor for transition in and out. 0.0 is textbox away, 1.0 is is normal textbox. */
	private float animationFactor = 1.0f;
		
	/** Speed at which textbox appears. In {@link #animationFactor}/s. Take the reciprocal to get the duration in seconds. */
	private float transitionInSpeed = NICE_TRANSITION_IN_SPEED;
	/** Speed at which textbox disappears. In {@link #animationFactor}/s. Take the reciprocal to get the duration in seconds. */
	private float transitionOutSpeed = NICE_TRANSITION_OUT_SPEED;
	
	/** Easing function when textbox appears. */
	private Interpolation interpolationIn = DEFAULT_INTERPOLATION_IN;
	/** Easing function when textbox disappears. */
	private Interpolation interpolationOut = DEFAULT_INTERPOLATION_OUT; 
	
	/** Player for playing sound effect at a certain maximum rate. */
	private VoicePlayer voicePlayer = null;
	
	/** Sound when pressing enter and textbox disappears. */
	private ESound soundOnTextboxAdvance = ESound.TEXTBOX_ADVANCE;
	
	/** Sound played as the text keeps unrevealing from left to right, top to bottom. */
	private ESound soundOnTextAdvance = ESound.TEXT_ADVANCE;
	
	/** Final delay until textbox disappears. */
	private float timeUntilExit = 0.0f;
	
	private Builder queuedBuilder = null;
	
	public EventTextbox() {
		reset();
		this.voicePlayer = new VoicePlayer();
	}

	/**
	 * Sets the textbox layout and design used for rendering.
	 * <br><br>
	 * Must be called to provide a valid value before calling any other setters.
	 * @param textbox The textbox to use.
	 * @return Whether the textbox has been set successfully.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public boolean setTextbox(FancyTextbox textbox) {
		if (enforceModificationsPolicy()) return false;
		if (textbox != null) {
			this.textbox = textbox;
			return true;
		}
		else return false;
	}
	/**
	 * Sets the textbox layout and design used for rendering.
	 * @param textbox The textbox to use.
	 * @return Whether the textbox has been set successfully.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public boolean setTextbox(ETextbox textbox) {
		if (textbox != null) return setTextbox(ResourceCache.getTextbox(textbox));
		return false;
	}
	/**
	 * @param soundOnTextboxAdvance The sound to play when proceeding to the next textbox.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setSoundOnTextboxAdvance(ESound soundOnTextboxAdvance) {
		if (enforceModificationsPolicy()) return;
		this.soundOnTextboxAdvance = soundOnTextboxAdvance;
	}
	/**
	 * @param soundOnTextAdvance The sound to play when the next character appears on-screen. 
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setSoundOnTextAdvance(ESound soundOnTextAdvance) {
		if (enforceModificationsPolicy()) return;
		this.soundOnTextAdvance = soundOnTextAdvance;
	}
	/**
	 * Sets the rate at which individual characters will appear.
	 * @param textSpeed The rate in Hz.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setTextSpeed(float textSpeed) {
		if (enforceModificationsPolicy()) return;
		this.textSpeed = textSpeed;
	}
	/**
	 * The text which will be rendered. May contain multiple lines.
	 * @param lines The lines to render. Line break must be <code>\n</code> (0x0A).
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setLines(String lines) {
		if (enforceModificationsPolicy()) return;
		if (lines != null) this.lines = lines;
		if (!textboxDone) textbox.setLines(lines);
	}
	/**
	 * Sets the text color for the text as set by {@link #setLines(String)}. Default is {@link Color#WHITE}.
	 * @param mainTextColor The text color.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setTextColor(Color mainTextColor) {
		if (enforceModificationsPolicy()) return;
		if (this.textbox != null) this.textbox.setTextColor(mainTextColor);
	}
	/**
	 * Sets the tint of the textbox. Default is {@link Color#WHITE}, ie. the original colors.
	 * @param boxColor The box color (tint).
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setBoxColor(Color boxColor) {
		if (enforceModificationsPolicy()) return;
		if (this.textbox != null) this.textbox.setBoxColor(boxColor);
	}
	/** 
	 * Sets the font size, relative to the game screen height. The aspect ration of the game screen will always be {@link Game#VIEWPORT_GAME_AR}.
	 * @param textHeightRatio The font size.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setTextHeightRatio(float textHeightRatio) {
		if (enforceModificationsPolicy()) return;
		if (this.textbox != null) this.textbox.setTextHeightRatio(textHeightRatio);
	}
	/**
	 * Sets the font to use for rendering.
	 * @param freeTypeFontGenerator The font.
	 * @see EFreeTypeFontGenerator
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setFont(EFreeTypeFontGenerator freeTypeFontGenerator) {
		if (enforceModificationsPolicy()) return;
		if (this.textbox != null) this.textbox.setFont(freeTypeFontGenerator);
	}
	/**
	 * Sets the speaker associated with this box.
	 * @param speaker The speaker, or {@link <code>null</code>} to unset the speaker.
	 * @see ESpeaker
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setSpeaker(ESpeaker speaker) {
		if (enforceModificationsPolicy()) return;
		this.textbox.setSpeaker(speaker);
	}
	/**
	 * The face which will be displayed to the right of this textbox.
	 * @param faceVariation The face, or {@link <code>null</code>} to unset the face.
	 * @see EFaceVariation
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void setFaceVariation(EFaceVariation faceVariation) {
		if (enforceModificationsPolicy()) return;
		this.textbox.setFaceVariation(faceVariation);
	}
	/**
	 * Textbox will be animated when appearing, using some default speed.
	 * @see #enableTransitionIn(float)
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void enableTransitionIn() {
		enableTransitionIn(NICE_TRANSITION_IN_SPEED, DEFAULT_INTERPOLATION_IN);
	}
	/**
	 * Textbox will be animated when appearing.
	 * @param speed Speed of the animation in 1/s.
	 * @see #transitionInSpeed
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void enableTransitionIn(float speed) {
		enableTransitionIn();
	}
	/**
	 * Textbox will be animated when appearing.
	 * @param speed Speed of the animation in 1/s.
	 * @param interpolationIn Easing type. 
	 * @see #transitionInSpeed
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void enableTransitionIn(float speed, Interpolation interpolationIn) {
		if (enforceModificationsPolicy()) return;
		this.transitionInSpeed = MathUtils.clamp(speed, 0.01f, 2.0f/Game.MIN_DELTA_TIME);
		this.interpolationIn = interpolationIn;
	}
	
	/**
	 *  Disables the animation for an appearing textbox.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void disableTransitionIn() {
		if (enforceModificationsPolicy()) return;
		this.transitionInSpeed = IMMEDIATE_TRANSITION_SPEED;
	}
	/**
	 * Textbox will be animated when disappearing, using some default speed.
	 * @see #enableTransitionOut(float)
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void enableTransitionOut() {
		enableTransitionOut(NICE_TRANSITION_OUT_SPEED, DEFAULT_INTERPOLATION_OUT);
	}
	/**
	 * Textbox will be animated when disappearing.
	 * @param speed Speed of the animation in 1/s.
	 * @see #transitionOutSpeed
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void enableTransitionOut(float speed) {
		enableTransitionOut(speed, DEFAULT_INTERPOLATION_OUT);
	}
	/**
	 * Textbox will be animated when disappearing.
	 * @param speed Speed of the animation in 1/s.
	 * @param interpolationOut Easing type. 
	 * @see #transitionOutSpeed
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen).
	 */
	public void enableTransitionOut(float speed, Interpolation interpolationOut) {
		if (enforceModificationsPolicy()) return;
		this.transitionOutSpeed = MathUtils.clamp(speed, 0.01f, 2.0f/Game.MIN_DELTA_TIME);
		this.interpolationOut = interpolationOut;
	}
	/** Disables the animation for a disappearing textbox.
	 * @throws IllegalStateException When the textbox is currently active (rendered on screen). 
	 */
	public void disableTransitionOut() {
		if (enforceModificationsPolicy()) return;
		this.transitionOutSpeed = IMMEDIATE_TRANSITION_SPEED;
	}
	
	/**
	 * Throws an error when textbox parameters are modified while it is active.
	 * Only logs an error in production mode.
	 * @return True iff modifications are not allowed.
	 * @throws When this textbox is active.
	 */
	private boolean enforceModificationsPolicy() {
		if (!textboxDone && strictMode) {
			if (DebugMode.activated) throw new IllegalStateException("EventTextbox parameters may not be modified while the textbox is active.");
			else LOG.error("EventTextbox parameters may not be modified while the textbox is active.");
			return true;
		}
		return false;
	}
	
	/**
	 * Allow changing parameters of this textbox while it is active. Use with
	 * care.
	 * 
	 * @param setter
	 *            Settings to be applied. Use the {@link EventTextbox} passed to
	 *            the {@link Setter#set(EventTextbox)} function to change the
	 *            settings.
	 */
	public void hotswapSettings(Setter setter) {
		strictMode = false;
		try {
			setter.set(this);
		}
		finally {
			strictMode = true;
		}
	}
	
	public void queueBuilder(Builder builder) {
		enforceModificationsPolicy();
		if (builder.hasSufficientData()) {
			builder.freeze();
			this.queuedBuilder = builder;
		}
		if (!textboxDone) {
			this.queuedBuilder.applyToBox(this);
			this.queuedBuilder = null;
		}
	}
	
	@Override
	public boolean isFinished() {
		return textboxDone;
	}
	
	@Override
	public boolean begin() {
		if (queuedBuilder != null) {
			queuedBuilder.applyToBox(this);
			queuedBuilder = null;
		}
		if (this.textbox == null) {
			LOG.error("cannot start textbox, no text set");
			return false;
		}
		textboxDone = false;
		textAdvance = 0.0f;
		mode = Mode.TRANSITION_IN;
		animationFactor = 0.0f;
		textbox.setLines(lines);
		textbox.setRenderedGlyphCount(0);
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
			case TRANSITION_IN:
				// Animate textbox appearing.
				animationFactor += transitionInSpeed * deltaTime;
				if (animationFactor >= 1.0f) {
					animationFactor = 1.0f;
					mode = Mode.TEXT_APPEARING;
				}
				textbox.setSlideEffect(interpolationIn.apply(animationFactor));
				break;
			case TEXT_APPEARING:
				// Show text incrementally.
				int oldTextAdvance = (int)textAdvance;
				textAdvance += textSpeed * deltaTime;
				if (textAdvance >= textbox.getGlyphCount()) {
					mode = Mode.IDLE;
					textAdvance = textbox.getGlyphCount();
				}
				else if (KeyMapDesktop.isTextboxAdvanceJustPressed()) {
					// Fast-forwarding text.
					textAdvance = textbox.getGlyphCount();
				}
				textbox.setRenderedGlyphCount((int)textAdvance);
				if (textbox.getRenderedGlyphCount() != oldTextAdvance && voicePlayer != null)
					voicePlayer.play(soundOnTextAdvance);					
				break;
			case IDLE:
				// Allow next textbox / event.
				if (KeyMapDesktop.isTextboxAdvanceJustPressed()) {
					if (voicePlayer != null && soundOnTextboxAdvance != null) {
						voicePlayer.playUnconditionally(soundOnTextboxAdvance);
					}
					if (transitionOutSpeed >= 1.0f/Game.MIN_DELTA_TIME) {
						timeUntilExit = 0.05f;
						mode = Mode.WAIT_FOR_EXIT;					
					}
					else {
						animationFactor = 1.0f;
						mode = Mode.TRANSITION_OUT;
					}
				}
				break;
			case TRANSITION_OUT:
				// Animate textbox disappearing.
				animationFactor -= transitionOutSpeed * deltaTime;
				if (animationFactor <= 0.0f) {
					animationFactor = 0.0f;
					textboxDone = true;
				}
				textbox.setSlideEffect(interpolationOut.apply(animationFactor));
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
		this.textboxDone = true;
		this.strictMode = true;
		this.lines = StringUtils.EMPTY;
		this.textSpeed = Math.max(0.1f, game.params.requestedTextboxSpeed);
		this.textAdvance = 0.0f;
		this.animationFactor = 1.0f;
		this.timeUntilExit = 0.0f;
		this.mode = Mode.TEXT_APPEARING;
		this.interpolationIn = DEFAULT_INTERPOLATION_IN;
		this.interpolationOut = DEFAULT_INTERPOLATION_OUT;
		this.transitionInSpeed = NICE_TRANSITION_IN_SPEED;
		this.transitionOutSpeed = NICE_TRANSITION_IN_SPEED;
	}
	
	private static enum Setting {
		BOX {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
			}
		},
		FONT {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		SIZE {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				Float f = FileCutsceneProvider.nextNumber(s);
				if (f == null) return false;
				b.setSize(f);
				return true;
			}
		},
		SPEAKER {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				if (!s.hasNext()) return false;
				String name = s.next();
				try {
					ESpeaker speaker = ESpeaker.valueOf(name);
					b.setSpeaker(speaker);
					return true;
				}
				catch (IllegalArgumentException e) {
					LOG.debug("no such speaker: " + name, e);
					return false;
				}
			}
		},
		FACE {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				if (!s.hasNext()) return false;
				String name = s.next();
				try {
					EFaceVariation face = EFaceVariation.valueOf(name);
					b.setFace(face);
					return true;
				}
				catch (IllegalArgumentException e) {
					LOG.debug("no such face variation: " + name, e);
					return false;
				}			
			}
		},
		SPEED {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		COLOR {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		BOXCOLOR {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		TRANSITIONIN {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		TRANSITIONOUT {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		SOUNDTEXTBOX {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		SOUNDTEXT {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				return true;// TODO Auto-generated method stub
				
			}
		},
		TEXT {
			@Override
			public boolean readSetting(Scanner s, Builder b) {
				if (!s.hasNextLine()) {
					LOG.error("expected eof token");
					return false;
				}
				String eofToken = s.next();
				s.nextLine();
				StringBuilder lines = new StringBuilder();
				while (s.hasNextLine()) {
					String line = s.nextLine();
					if (line.equals(eofToken)) {
						b.setLines(lines.toString());
						return true;
					}
					lines.append(line).append(StringUtils.LF);
				}
				return false;
			}
		};
		
		public abstract boolean readSetting(Scanner s, Builder textbox);
	}
	
	private static final class Builder {
		private ETextbox box = ETextbox.FC_BLUE;
		private String lines = null;

		private EFreeTypeFontGenerator font = EFreeTypeFontGenerator.MAIN_FONT;
		private float size = PlainTextbox.DEFAULT_TEXT_HEIGHT_RATIO * 40f * 16f;
		private ESpeaker speaker = null;
		private EFaceVariation face = null;
		private Color color = Color.WHITE;
		private Color boxColor = Color.WHITE;
		private float speed = -1; 
		private float transitionInSpeed = IMMEDIATE_TRANSITION_SPEED;
		private float transitionOutSpeed = IMMEDIATE_TRANSITION_SPEED;	
		private Interpolation interpolationIn = DEFAULT_INTERPOLATION_IN;
		private Interpolation interpolationOut = DEFAULT_INTERPOLATION_OUT;
		private ESound soundTextbox = ESound.TEXTBOX_ADVANCE;
		private ESound soundText = ESound.TEXT_ADVANCE;
		
		private boolean frozen = false;
		
		public boolean hasSufficientData() {
			return box != null && lines != null && !lines.isEmpty();
		}
		
		public void freeze() {
			frozen = true;			
		}

		public void setLines(String lines) {
			enforceNotFrozen();
			if (lines == null || lines.isEmpty() || lines.length() > 200) return;
			this.lines = lines;
		}
		
		public void setFace(EFaceVariation face) {
			enforceNotFrozen();
			this.face = face;
		}
		
		public void setSpeaker(ESpeaker speaker) {
			enforceNotFrozen();
			this.speaker = speaker;
		}
		
		public void setSize(float size) {
			enforceNotFrozen();
			if (size>=64 || size < 1.0f) return;
			this.size = size;
		}
		
		public void applyToBox(EventTextbox eventBox) {
			if (!hasSufficientData()) return;
			eventBox.setTextbox(box);
			eventBox.setSpeaker(speaker);
			eventBox.setFaceVariation(face);
			eventBox.setFont(font);
			eventBox.setTextHeightRatio(size/(40.0f*16.0f));
			eventBox.setTextColor(color);
			eventBox.setBoxColor(boxColor);
			eventBox.enableTransitionIn(transitionInSpeed, interpolationIn);
			eventBox.enableTransitionOut(transitionOutSpeed, interpolationOut);
			eventBox.setSoundOnTextAdvance(soundTextbox);
			eventBox.setSoundOnTextboxAdvance(soundText);
			eventBox.setLines(lines);
			if (speed > 0.0f) eventBox.setTextSpeed(speed);
		}
		
		public void enforceNotFrozen() {
			if (frozen) throw new IllegalStateException("cannot modify frozen builder");
		}
	}
	
	public static ACutsceneEvent readNextObject(Scanner s) {
		// Set defaults.
		Builder builder = new Builder(); 
		while (s.hasNext()) {
			String key = s.next().toUpperCase(Locale.ROOT);
			try {
				Setting.valueOf(key).readSetting(s, builder);
			}
			catch (IllegalArgumentException e) {
				LOG.error("no such setting for an event textbox: " + key);
				s.nextLine();
			}
		}

		if (builder.hasSufficientData()) {
			EventTextbox eventTextbox = EventTextboxPool.getInstance().obtain();
			if (builder.hasSufficientData())
				eventTextbox.queueBuilder(builder);
			else {
				eventTextbox = null;
				EventTextboxPool.getInstance().free(eventTextbox);
			}
			return eventTextbox;
		}
		else {
			LOG.error("event textbox requires at least a box and some lines of text");
			return null;
		}		
	}
}
