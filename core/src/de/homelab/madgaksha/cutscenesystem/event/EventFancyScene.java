package de.homelab.madgaksha.cutscenesystem.event;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.FancyDrawable;
import de.homelab.madgaksha.cutscenesystem.fancyscene.drawable.DrawableAnimation;
import de.homelab.madgaksha.cutscenesystem.fancyscene.drawable.DrawableNinePatch;
import de.homelab.madgaksha.cutscenesystem.fancyscene.drawable.DrawableParticleEffect;
import de.homelab.madgaksha.cutscenesystem.fancyscene.drawable.DrawableSprite;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.INewObject;

/**
 * The following commands are available for reading from file.
 * <ul>
 * <li><b>Include</b> &lt;Relative/Absolute> &lt;StartTime&gt; &lt;FileName&gt;
 * </li>
 * <li><b>Sound</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;Entity&gt;
 * &lt;Sound&gt;</li>
 * <li><b>Soundtarget</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;soundType&gt;</li>
 * <li><b>Shake</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;RichterScale&gt; &lt;Duration&gt;</li>
 * <li><b>Sprite</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;DPI&gt; &lt;Texture&gt;</li>
 * <li><b>Spritetarget</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;DPI&gt; &lt;LookingDirection&gt;</li>
 * <li><b>Ninepatch</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;halfWidth&gt; &lt;halfHeight&gt; &lt;NinePatch&gt;
 * </li>
 * <li><b>Position</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;X&gt; &lt;Y&gt; [&lt;randomDeltaX=0&gt;]
 * [&lt;randomDeltaY=0&gt;]</li>
 * <li><b>Origin</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;relativeOriginX&gt; &lt;relativeOriginY&gt;</li>
 * <li><b>Crop</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;cropLeft&gt; &lt;cropBottom&gt;
 * [&lt;cropRight&gt;=&lt;cropLeft&gt;] [&lt;cropTop&gt;=&lt;cropBottom&gt;]
 * </li>
 * <li><b>Scale</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;scaleX&gt; [&lt;scaleY&gt;=&lt;scaleX&gt;]</li>
 * <li><b>Opacity</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;Opacity&gt;</li>
 * <li><b>Move</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;PathType&gt; &lt;Duration&gt;
 * [=&lt;Interpolation&gt;=(=linear)] &lt;Relative/Absolute&gt;
 * &lt;PathDetails&gt;</li>
 * <li><b>Slide</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;Duration&gt; [=&lt;Interpolation&gt;=(=linear)]
 * &lt;CropLeft&gt; &lt;CropBottom&gt; [&lt;cropRight&gt;=&lt;cropLeft&gt;]
 * [&lt;cropTop&gt;=&lt;cropBottom&gt;]</li>
 * <li><b>Zoom</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;Duration&gt; [=&lt;Interpolation&gt;=(=linear)]
 * &lt;targetScaleX&gt; [&lt;targetScaleY&gt;=&lt;targetScaleX&gt;]</li>
 * <li><b>Show</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;Duration&gt;</li>
 * <li><b>Fade</b> &lt;Relative/Absolute&gt; &lt;StartTime&gt;
 * &lt;SpriteName&gt; &lt;Duration&gt; [Interpolation=linear]
 * &lt;TargetOpacity&gt;</li>
 * </ul>
 * 
 * Additionally, a z-index may be given immediately after the event name (and
 * before the absolute/relative flag). The higher the z-index, the earlier
 * events are processed (updated/rendered). This applies only to events of the
 * same type, each type of event has got an additional internal priority too.
 * <br>
 * <br>
 * (0,0) is at the center of the screen, and the screen's dimensions are width=8
 * and height=9. <br>
 * <br>
 * A 8x9 pixel sprite with a DPI of 1 will fill the entire game screen. <br>
 * <br>
 * For example, a simple fade in with an image that move from the bottom left to
 * the center:
 * 
 * <pre>
 * Sprite R 0.0 Joshua  100.0 FACE_JOSHUA_01
 * Sprite R 0.0 Estelle 100.0 FACE_ESTELLE_01
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_02
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_03
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_04
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_05
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_06
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_07
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_08
 * Sprite R 0.2 Estelle 100.0 FACE_ESTELLE_09
 * 
 * Position R 0.0 Estelle -4.0 -4.5
 * Position R 0.0 Joshua  -4.0 -4.0
 * Opacity R 0.0 Estelle 0.0
 * 
 * Move R 0.0 Estelle Line 0.5 A 0.0 0.0
 * Fade R 0.0 Estelle 0.5 1.0 Linear
 * 
 * Show 1 R 0.0 Estelle 2.0
 * Show 2 R 0.0 Joshua 2.0
 * 
 * Sound R 0.5 Player ESTELLE_TOTTEOKI_O_MISETE_AGERU
 * </pre>
 * 
 * If there is any overlap, sprite Joshua will be drawn over sprite Estelle.
 * 
 * @author madgaksha
 *
 */
public class EventFancyScene extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventFancyScene.class);
	private final static DrawableSprite PROTOTYPE_DRAWABLE_SPRITE = new DrawableSprite();
	private final static DrawableAnimation PROTOTYPE_DRAWABLE_ANIMATION = new DrawableAnimation();
	private final static DrawableParticleEffect PROTOTYPE_DRAWABLE_PARTICLE_EFFECT = new DrawableParticleEffect();
	private final static DrawableNinePatch PROTOTYPE_DRAWABLE_NINE_PATCH = new DrawableNinePatch();
	
	
	private final Vector3 oldPosition = new Vector3();
	private final List<AFancyEvent> eventList = new ArrayList<AFancyEvent>();
	private final Stack<AFancyEvent> queueList = new Stack<AFancyEvent>();
	private final LinkedList<AFancyEvent> activeList = new LinkedList<AFancyEvent>();
	
	@Deprecated
	private final Map<String, FancyDrawable> drawableMap = new HashMap<String, FancyDrawable>();
	
	private final Map<String, DrawableSprite> mapSprite = new HashMap<String, DrawableSprite>();
	private final Map<String, DrawableAnimation> mapAnimation = new HashMap<String, DrawableAnimation>();
	private final Map<String, DrawableNinePatch> mapNinePatch = new HashMap<String, DrawableNinePatch>();
	private final Map<String, DrawableParticleEffect> mapParticleEffect = new HashMap<String, DrawableParticleEffect>();
	
	private final Vector3 shake = new Vector3();
	private float totalTime = 0.0f;
	private float deltaTime = 0.0f;
	private boolean isSpedup = false;
	private EventFancyScene parent;

	public EventFancyScene(List<AFancyEvent> eventList) {
		this.eventList.addAll(eventList);
		Collections.sort(this.eventList, Collections.reverseOrder());
		for (AFancyEvent fe : this.eventList)
			fe.attachedToScene(this);
	}

	@Override
	public boolean isFinished() {
		return queueList.isEmpty() && activeList.isEmpty();
	}

	@Override
	public void render() {
		oldPosition.set(viewportPixel.getCamera().position);
		// Same as viewportPixel.apply(), but restricts the drawing area to the
		// main game screen and adds a shake effect.
		HdpiUtils.glViewport(viewportPixel.getScreenX(), viewportPixel.getScreenY(),
				GlobalBag.viewportGame.getScreenWidth(), GlobalBag.viewportGame.getScreenHeight());
		viewportPixel.getCamera().viewportWidth = viewportGame.getScreenWidth();
		viewportPixel.getCamera().viewportHeight = viewportGame.getScreenHeight();
		viewportPixel.getCamera().position.set(shake.x + viewportGame.getScreenWidth() * 0.5f,
				shake.y + GlobalBag.viewportGame.getScreenHeight() * 0.5f, shake.z);
		viewportPixel.getCamera().update();
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		batchPixel.begin();
		renderMain();
		batchPixel.end();
		viewportPixel.getCamera().position.set(oldPosition);
	}

	public void renderMain() {
		for (AFancyEvent fe : activeList)
			fe.render();
	}

	@Override
	public void update(float deltaTime, boolean isSpedup) {
		this.deltaTime = deltaTime;
		this.isSpedup = isSpedup;
		totalTime += deltaTime;

		boolean listChanged = false;
		boolean eventsStarted = false;

		this.shake.set(0.0f, 0.0f, 0.0f);

		// We need to update all events currently active and remove those that
		// are done before we can add new events.
		for (Iterator<AFancyEvent> it = activeList.iterator(); it.hasNext();) {
			AFancyEvent fe = it.next();
			fe.update(totalTime - fe.getStartTime());
			if (fe.isFinished()) {
				fe.end();
				it.remove();
			}
		}

		// Now we can add all new events that should start now.
		while (!queueList.empty() && totalTime >= queueList.peek().getStartTime()) {
			AFancyEvent fe = queueList.pop();
			if (fe.begin(this)) {
				activeList.push(fe);
				listChanged = true;
			}
			eventsStarted = true;
		}

		// Sort all active events by their z-index.
		if (listChanged) {
			Collections.sort(activeList, AFancyEvent.ORDER_Z);
		}

		// Finally we need to update all events that have been added.
		// As events may depend on each other, we need to update all events
		// again,
		// not only those that have been added.
		if (eventsStarted) {
			for (Iterator<AFancyEvent> it = activeList.iterator(); it.hasNext();) {
				AFancyEvent fe = it.next();
				fe.update(totalTime - fe.getStartTime());
				if (fe.isFinished()) {
					fe.end();
					it.remove();
				}
			}
		}
	}

	@Override
	public void resize(int w, int h) {
		for (AFancyEvent fe : queueList)
			fe.resize(w, h);
		for (AFancyEvent fe : activeList)
			fe.resize(w, h);
	}

	@Override
	public boolean begin() {
		for (FancyDrawable fd : drawableMap.values())
			fd.resetToDefaults();
		this.activeList.clear();
		this.queueList.clear();
		this.queueList.addAll(eventList);
		this.isSpedup = false;
		totalTime = 0.0f;
		deltaTime = 0.0f;
		shake.set(0.0f, 0.0f, 0.0f);
		return !queueList.isEmpty();
	}

	@Override
	public void reset() {
		for (FancyDrawable fd : drawableMap.values())
			fd.cleanup();
		for (AFancyEvent fe : eventList)
			// TODO call ResourcePool.freeFancyEvent(fe); instead
			fe.reset();
		eventList.clear();
		drawableMap.clear();
		queueList.clear();
		activeList.clear();
		totalTime = 0.0f;
		deltaTime = 0.0f;
		shake.set(0f, 0f, 0f);
		parent = null;
		isSpedup = false;
	}

	@Override
	public void end() {
	}

	/**
	 * Requests a new drawable to be created for the given key. Drawables will
	 * be created on-the-fly, use this for performance optimizations.
	 * 
	 * @param key
	 *            Name of the drawable.
	 */
	@Deprecated
	public void requestDrawable(String key) {
		getDrawable(key);
	}

	/**
	 * @param key
	 *            Unique id for the drawable. Uniqueness is determined by
	 *            {@link String#compareTo(String)}.
	 * @return A fancy drawable for this string. It is either fetched from the
	 *         cache, or newly created. For the same key, this method will
	 *         always return the same object.
	 */
	@Deprecated
	public FancyDrawable getDrawable(String key) {
		FancyDrawable drawable = drawableMap.get(key);
		if (drawable == null) {
			drawable = new FancyDrawable();
			drawableMap.put(key, drawable);
		}
		return drawable;
	}
	
	private <T extends INewObject<T>> T getADrawable(String key, T prototype, Map<String, T> map) {	
		T drawable = map.get(key);
		if (drawable == null) {
			drawable = prototype.newObject();
			map.put(key, drawable);
		}
		return drawable;
	}
	public DrawableSprite requestDrawableSprite(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_SPRITE, mapSprite);
	}
	public DrawableAnimation requestDrawableAnimation(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_ANIMATION, mapAnimation);
	}
	public DrawableNinePatch requestDrawableNinePatch(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_NINE_PATCH, mapNinePatch);
	}
	public DrawableParticleEffect requestDrawableParticleEffect(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_PARTICLE_EFFECT, mapParticleEffect);
	}

	/**
	 * The time that has passed since the last frame. Events can save a
	 * reference to the parent and access the deltaTime if they need it to
	 * prevent rounding errors from propagating. Note, however, that
	 * {@link #update(float)} may be called more than once per frame.
	 * Implemented events need to check the argument passedTime.
	 * 
	 * @return Time since the last frame.
	 */
	public float getDeltaTime() {
		return deltaTime;
	}

	public void setParent(EventFancyScene scene) {
		// Prevent stack overflow when this scene is provided as the argument.
		if (this != scene)
			this.parent = scene;
	}

	public void shakeScreenBy(Vector3 shake) {
		this.shake.add(shake);
		if (parent != null)
			parent.shakeScreenBy(shake);
	}

	public void shakeScreenBy(float shakeX, float shakeY) {
		shake.x += shakeX;
		shake.y += shakeY;
		if (parent != null)
			parent.shakeScreenBy(shakeX, shakeY);
	}

	public void shakeScreenBy(Vector2 shake) {
		this.shake.x += shake.x;
		this.shake.y += shake.y;
		if (parent != null)
			parent.shakeScreenBy(shake);
	}

	public static EventFancyScene readNextObject(Scanner s, FileHandle inputFile) {
		if (!s.hasNextLine()) {
			LOG.error("expected file name for fancyScene configuration file");
			return null;
		}
		String relativeFilePath = s.nextLine().trim();
		FileHandle fileHandle = inputFile.isDirectory() ? inputFile.child(relativeFilePath)
				: inputFile.parent().child(relativeFilePath);
		List<AFancyEvent> eventList = new ArrayList<AFancyEvent>();
		if (!readEventList(fileHandle, eventList))
			return null;
		return new EventFancyScene(eventList);
	}

	private static boolean readEventList(FileHandle fileHandle, List<AFancyEvent> eventList) {
		Scanner scanner = null;
		Map<Class<?>, Float> startMap = new HashMap<Class<?>, Float>();
		try {
			String content = fileHandle.readString("UTF-8");
			scanner = new Scanner(content);
			scanner.useLocale(Locale.ROOT);
			while (scanner.hasNextLine()) {
				Scanner lineScanner = null;
				try {
					String line = scanner.nextLine().trim();
					// Skip comments
					if (line.isEmpty() || line.length() >= 2 && line.substring(0, 2).equals("//"))
						continue;
					// Read this fancy event
					lineScanner = new Scanner(line);
					lineScanner.useLocale(Locale.ROOT);
					AFancyEvent fancyEvent = AFancyEvent.readNextEvent(lineScanner, fileHandle);
					if (fancyEvent == null) {
						LOG.error("failed to read fancy event for parameters: " + line);
						return false;
					}
					// Convert relative start time to absolute start time.
					fancyEvent.resolveRelativeStartTime(startMap);
					startMap.put(fancyEvent.getClass(), fancyEvent.getStartTime());
					// Add event
					eventList.add(fancyEvent);
				} finally {
					IOUtils.closeQuietly(lineScanner);
				}
			}
		} catch (GdxRuntimeException e) {
			LOG.error("could not read fancy event configuration file", e);
			return false;
		} finally {
			IOUtils.closeQuietly(scanner);
		}
		return true;
	}

	public boolean isSpedup() {
		return isSpedup;
	}
}
