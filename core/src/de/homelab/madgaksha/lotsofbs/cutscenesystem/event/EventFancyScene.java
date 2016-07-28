package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGameFixed;

import java.io.IOException;
import java.io.Serializable;
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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.Game;
import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.ADrawable;
import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.DrawableAnimation;
import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.DrawableMock;
import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.DrawableNinePatch;
import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.DrawableParticleEffect;
import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.DrawableSprite;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.util.Transient;

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
public class EventFancyScene extends ACutsceneEvent implements Serializable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	public final static ADrawable<Object, Object> PROTOTYPE_DRAWABLE_MOCK = new DrawableMock();
	private final static Logger LOG = Logger.getLogger(EventFancyScene.class);
	private final static ADrawable<ETexture, CroppableAtlasSprite> PROTOTYPE_DRAWABLE_SPRITE = new DrawableSprite();
	private final static ADrawable<EAnimation, AtlasAnimation> PROTOTYPE_DRAWABLE_ANIMATION = new DrawableAnimation();
	private final static ADrawable<EParticleEffect, PooledEffect> PROTOTYPE_DRAWABLE_PARTICLE_EFFECT = new DrawableParticleEffect();
	private final static ADrawable<ENinePatch, NinePatch> PROTOTYPE_DRAWABLE_NINE_PATCH = new DrawableNinePatch();

	private List<AFancyEvent> eventList = new ArrayList<AFancyEvent>();
	@Transient private  Stack<AFancyEvent> queueList = new Stack<AFancyEvent>();
	@Transient private LinkedList<AFancyEvent> activeList = new LinkedList<AFancyEvent>();

	@Transient private Map<String, ADrawable<?, ?>> drawableMap = new HashMap<String, ADrawable<?, ?>>();

	@Transient private Vector3 shake = new Vector3();
	@Transient private float totalTime = 0.0f;
	@Transient private float deltaTime = 0.0f;
	@Transient private boolean isSpedup = false;
	@Transient private EventFancyScene parent;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(eventList.size());
		for (AFancyEvent fe : eventList)
			out.writeObject(fe);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		eventList = new ArrayList<AFancyEvent>();
		queueList = new Stack<AFancyEvent>();
		activeList = new LinkedList<AFancyEvent>();
		drawableMap = new HashMap<String, ADrawable<?, ?>>();
		shake = new Vector3();
		totalTime = 0.0f;
		deltaTime = 0.0f;
		isSpedup = false;
		
		reset();
		final int size = in.readInt();
		if (size < 0) throw new InvalidDataException("list size must be >= 0");
		for (int i = 0 ; i != size; ++i) {
			final Object fe = in.readObject();
			if (fe == null || !(fe instanceof AFancyEvent)) {
				eventList.clear();
				throw new InvalidDataException("invalid data for fancy event");
			}
			eventList.add((AFancyEvent)fe);
		}
		for (AFancyEvent fe : eventList)
			fe.attachedToScene(this);
	}
	
	public EventFancyScene(List<AFancyEvent> eventList) {
		this.eventList.addAll(eventList);
		Collections.sort(this.eventList, Collections.reverseOrder(AFancyEvent.ORDER_START_TIME_PRIORITY));
		for (AFancyEvent fe : this.eventList)
			fe.attachedToScene(this);
	}

	@Override
	public boolean isFinished() {
		return queueList.isEmpty() && activeList.isEmpty();
	}

	@Override
	public void render() {
		viewportGameFixed.setShake(shake.x, shake.y, shake.z);
		viewportGameFixed.apply(false, batchPixel);
		batchPixel.begin();
		renderMain(batchPixel);
		batchPixel.end();
	}

	/**
	 * Renders to the given batch on which {@link Batch#begin()} should have been
	 * called, applying shaking as well via batch transforms.
	 * This is used by the {@link de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.MyFancySceneEditor}.
	 * @param batch Batch to draw with. {@link Batch#begin()} should have been called.
	 */
	private Matrix4 oldTransform;
	public void render(Batch batch) {
		if (oldTransform == null) {
			oldTransform = new Matrix4();
		}
		oldTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(batch.getTransformMatrix().translate(shake.x*Game.VIEWPORT_GAME_AR_NUM, shake.y*Game.VIEWPORT_GAME_AR_DEN, shake.z));
		renderMain(batch);
		batch.setTransformMatrix(oldTransform);
	}
	
	public void renderMain(Batch batch) {
		for (AFancyEvent fe : activeList)
			fe.render(batch);
	}

	//TODO need to remove events that have ended in the corrected order, depending on their end time
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
			Collections.sort(activeList, AFancyEvent.ORDER_PRIORITY_Z);
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
		// Not needed anymore as we are working with a virtual fixed screen size
		// now.

		// for (AFancyEvent fe : queueList)
		// fe.resize(w, h);
		// for (AFancyEvent fe : activeList)
		// fe.resize(w, h);
	}

	@Override
	public boolean begin() {
		for (ADrawable<?, ?> drawable : drawableMap.values())
			drawable.reset();
		this.activeList.clear();
		this.queueList.clear();
		this.queueList.addAll(eventList);
		this.isSpedup = false;
		this.totalTime = 0.0f;
		this.deltaTime = 0.0f;
		this.shake.set(0.0f, 0.0f, 0.0f);
		return !queueList.isEmpty();
	}

	@Override
	public void reset() {
		for (ADrawable<?, ?> drawable : drawableMap.values()) {
			drawable.dispose();
		}
		for (AFancyEvent fe : eventList) {
			// TODO call ResourcePool.freeFancyEvent(fe); instead
			fe.reset();
		}
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
	 * @param key
	 *            Unique id for the drawable. Uniqueness is determined by
	 *            {@link String#compareTo(String)}.
	 * @return A fancy drawable for this string. It is either fetched from the
	 *         cache, or newly created. For the same key, this method will
	 *         always return the same object.
	 */
	@SuppressWarnings("unchecked")
	private <S, T> ADrawable<S, T> getADrawable(String key, ADrawable<S, T> prototype) {
		final ADrawable<?, ?> drawable = drawableMap.get(key);
		if (drawable == null) {
			// Create new drawable.
			final ADrawable<S, T> newObject = prototype.newObject();
			drawableMap.put(key, newObject);
			return newObject;
		} else if (drawable.getClass() != prototype.getClass()) {
			// Dispose old drawable.
			drawable.dispose();
			// Acquire new drawable.
			final ADrawable<S, T> newObject = prototype.newObject();
			drawableMap.put(key, newObject);
			// Inform events drawables changed.
			for (AFancyEvent fe : activeList) 
				fe.drawableChanged(this, key);
			return newObject;
		}
		// Drawable exists already with the correct type, so just return it.
		return (ADrawable<S, T>) drawable;
	}

	/**
	 * Returns a drawable for the given key, of unknown subclass. Therefore,
	 * you can only perform operations common to all subclasses as defined
	 * in {@link ADrawable}.
	 * @param key Key of the drawable.
	 * @return The drawable, or a {@link DrawableMock} when no such drawable exists.
	 */
	public ADrawable<?, ?> getADrawable(String key) {
		return drawableMap.getOrDefault(key, PROTOTYPE_DRAWABLE_MOCK);
	}

	/**
	 * For checking for sanity while loading. It is not required, but animation
	 * files should load an object before using it.
	 * 
	 * @param key
	 *            ID of the {@link ADrawable} to check.
	 */
	public void checkForDrawable(String key) {
		if (!drawableMap.containsKey(key)) {
			LOG.error("warning: no object exists yet for " + key);
		}
	}

	public ADrawable<ETexture, CroppableAtlasSprite> requestDrawableSprite(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_SPRITE);
	}

	public ADrawable<EAnimation, AtlasAnimation> requestDrawableAnimation(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_ANIMATION);
	}

	public ADrawable<ENinePatch, NinePatch> requestDrawableNinePatch(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_NINE_PATCH);
	}

	public ADrawable<EParticleEffect, PooledEffect> requestDrawableParticleEffect(String key) {
		return getADrawable(key, PROTOTYPE_DRAWABLE_PARTICLE_EFFECT);
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
	
	public float getStateTime() {
		return totalTime; 
	}
}
