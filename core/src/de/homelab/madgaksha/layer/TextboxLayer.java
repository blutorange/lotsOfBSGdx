package de.homelab.madgaksha.layer;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.KeyMap;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.cutscenesystem.Textbox;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;

/**
 * Takes care of handling a sequence of textboxes. Useful for short story sequences.
 * 
 * It draws them and advances to the next box upon pressing a key.
 * 
 * @author madgaksha
 *
 */
public class TextboxLayer extends ALayer {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TextboxLayer.class);
	private final static float ANIMATION_SPEED = 550.0f;
	private final static float ANIMATION_SPEED_180 = 180.0f/ANIMATION_SPEED;
	private final static float ANIMATION_SPEED_540 = 540.0f/ANIMATION_SPEED;
	private final static float ANIMATION_SPEED_720 = 720.0f/ANIMATION_SPEED;
	private final Textbox[] textboxList;
	private int currentTextboxIndex;
	private int endTextboxIndex;
	private boolean allowInput = false;
	private boolean slidingDown = false;
	private float totalTime = ANIMATION_SPEED_540;
	private float animationFactor = 1.0f;
	
	/** We do not want to create a new textbox object all the time...*/
	public final static Textbox[] POOL = new Textbox[100];
	
	/**
	 * Adds a new layer with a set of textboxes. 
	 * @param textboxList
	 */
	public TextboxLayer(Textbox[] textboxList) {
		this(textboxList, 0, textboxList.length);
	}
	/**
	 * Adds a new layer with a set of textboxes.
	 * @param textboxList List of textboxes.
	 * @param start First textbox to show (inclusive).
	 * @param end Last textbox to show (exclusive).
	 */
	public TextboxLayer(Textbox[] textboxList, int start, int end) {
		this.textboxList = textboxList;
		this.currentTextboxIndex = start;
		this.endTextboxIndex = end;
	}
	
	@Override
	public void draw(float deltaTime) {
		if (currentTextboxIndex < endTextboxIndex) {
			viewportPixel.apply();
			batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
			batchPixel.begin();
			textboxList[currentTextboxIndex].render(animationFactor);
			batchPixel.end();
		}
		else removeSelf();
	}

	@Override
	public void update(float deltaTime) {
		if (KeyMap.isTextboxAdvancePressed()) {
			if (allowInput) {
				SoundPlayer.getInstance().play(ESound.TEXTBOX_NEXT);
				totalTime = 0.0f;
				slidingDown = true;
			}
			allowInput = false;
		}
		if (!allowInput) {
			totalTime += deltaTime;
			animationFactor = -0.5f*MathUtils.cosDeg(ANIMATION_SPEED*totalTime)+0.5f;
			if (totalTime >= ANIMATION_SPEED_720) {
				allowInput = true;
				animationFactor = 0.0f;
			}
			else if (slidingDown && totalTime >= ANIMATION_SPEED_180) {
				totalTime = ANIMATION_SPEED_540;
				slidingDown = false;
				++currentTextboxIndex;
			}
		}
	}

	
	@Override
	public void removedFromStack() {
	}

	@Override
	public void addedToStack() {
	}

	@Override
	public boolean isBlockDraw() {
		return false;
	}

	@Override
	public boolean isBlockUpdate() {
		return false;
	}
}
