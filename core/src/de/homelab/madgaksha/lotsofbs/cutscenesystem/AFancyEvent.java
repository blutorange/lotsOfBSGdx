package de.homelab.madgaksha.lotsofbs.cutscenesystem;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.AFancyWithDrawable;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.LocaleRootWordUtils;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public abstract class AFancyEvent implements Poolable, Serializable {
	/**
	 * Initial version. 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(AFancyEvent.class);
	private final static String FANCY_SCENE_PREFIX = AFancyWithDrawable.class.getPackage().getName() +  ".Fancy";

	private boolean originalRelative = false;
	protected float startTime = 0.0f;
	private int z = 0;
	@Transient protected int priority = 0;
	@Transient private boolean relative = false;

	/**
	 * For sorting the eventList by their priority, then z-index. Lowest z-index first, highest last.
	 */
	public final static Comparator<AFancyEvent> ORDER_PRIORITY_Z = new Comparator<AFancyEvent>() {
		@Override
		public int compare(AFancyEvent o1, AFancyEvent o2) {
			return (o1.priority < o2.priority) ? -1
					: (o1.priority == o2.priority) ? ((o1.z < o2.z) ? -1 : (o1.z == o2.z) ? 0 : 1) : 1;
		}
	};

	 private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(startTime);
		out.writeBoolean(originalRelative);
		out.writeInt(z);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final float startTime = in.readFloat();
		if (startTime < 0) throw new InvalidDataException("start time must be >= 0");
		this.startTime = startTime;
		
		originalRelative = in.readBoolean();
		z = in.readInt();
		
		try {
			Priority p = Priority.valueOf(getClass().getSimpleName());
			priority = p.getPriority();
		} catch (IllegalArgumentException e) {
			LOG.error("no priority given for " + getClass().getSimpleName());
			priority = 0;
		}
		relative = originalRelative;
	}
	
	/**
	 * For sorting the eventList by their start time, then priority. Lowest start time first, highest last.
	 */
	public final static Comparator<AFancyEvent> ORDER_START_TIME_PRIORITY = new Comparator<AFancyEvent>() {
		@Override
		public int compare(AFancyEvent o1, AFancyEvent o2) {
			return (o1.startTime < o2.startTime) ? -1
					: (o1.startTime == o2.startTime)
							? ((o1.priority < o2.priority) ? -1 : (o1.priority == o2.priority) ? 0 : 1) : 1;
		}
	};

	/** For serialization only.
	 * @serial
	 */
	protected AFancyEvent() {
	}
	
	public AFancyEvent(boolean setPriority) {
		try {
			Priority p = Priority.valueOf(getClass().getSimpleName());
			this.priority = p.getPriority();
		} catch (IllegalArgumentException e) {
			LOG.error("no priority given for " + getClass().getSimpleName());
		}
	}

	/**
	 * Called at the earliest frame the event should be active. It is not called
	 * in the correct z-order and should thus not perform any functions related
	 * to {@link #update(float, float)}.
	 * 
	 * @param scene
	 * @return True iff this event may now be updated and rendered. Can be false for setters etc.
	 * that only need to be called once.
	 */
	public abstract boolean begin(EventFancyScene scene);

	public abstract void render(Batch batch);

	/**
	 * Called when the type of a drawable changed, eg. from Animation to NinePatch.
	 * @param scene The scene for which the drawable changed.
	 * @param key The key of the drawable that changed.
	 */
	public abstract void drawableChanged(EventFancyScene scene, String key);
	
	/**
	 * Called when some time has passed and this event needs to be updated.
	 * 
	 * @param deltaTime
	 *            Time that has passed since the last update.
	 * @param passedTime
	 *            Time that has passed since the beginning of this event.
	 */
	public abstract void update(float passedTime);

	/**
	 * Called each frame to check whether this event is over.
	 * 
	 * @return Whether this event is over.
	 */
	public abstract boolean isFinished();

	/**
	 * Called after {@link #isFinished()} returned true. It is called in the
	 * correct z order and may perform final {@link #update}s and set attributes
	 * to their correct values for the end time.
	 */
	public abstract void end();

	public void resize(int w, int h) {
	}

	/**
	 * Called when this fancy event is added to a {@link EventFancyScene}. This
	 * method should prepare the scene such as by adding the required
	 * {@link FancyDrawable}s via a call to
	 * {@link EventFancyScene#requestDrawable(String)}.
	 * 
	 * @param scene
	 */
	public abstract void attachedToScene(EventFancyScene scene);

	public float getStartTime() {
		return startTime;
	}

	public boolean isRelative() {
		return relative;
	}

	public void resolveRelativeStartTime(Map<Class<?>, Float> startMap) {
		if (relative) {
			Float lastStartTime = startMap.get(getClass());
			if (lastStartTime == null)
				lastStartTime = 0.0f;
			startTime += lastStartTime;
			relative = false;
		}
	}

	/**
	 * Read event from its serialized form.
	 * 
	 * @param scanner
	 *            Scanner to read from.
	 * @return The event as read from the file, or null if it could not be read.
	 */
	public static AFancyEvent readNextEvent(Scanner scanner, FileHandle parentFile) {
		if (!scanner.hasNext()) {
			LOG.error("expected event type");
			return null;
		}
		String type = LocaleRootWordUtils.capitalizeFully(scanner.next());
		Integer z = FileCutsceneProvider.nextInteger(scanner);
		if (z == null)
			z = 0;
		if (!scanner.hasNext()) {
			LOG.error("expected relative/absolute flag");
			return null;
		}
		boolean isRelative = scanner.next().equalsIgnoreCase("r");
		Float startTime = FileCutsceneProvider.nextNumber(scanner);
		if (startTime == null) {
			LOG.error("expected start time");
			return null;
		}
		AFancyEvent fancyEvent = fetchEventFor(type, scanner, parentFile);
		if (fancyEvent == null)
			return null;
		fancyEvent.startTime = startTime;
		fancyEvent.relative = isRelative;
		fancyEvent.originalRelative = isRelative;
		fancyEvent.z = z;
		return fancyEvent;
	}

	private static AFancyEvent fetchEventFor(String type, Scanner scanner, FileHandle parentFile) {
		String fullName = FANCY_SCENE_PREFIX + type;
		Class<?> clazz = null;
		Method method = null;
		try {
			clazz = ClassReflection.forName(fullName);
		} catch (ReflectionException e) {
			LOG.error("no such fancy event: " + type, e);
			return null;
		}

		try {
			method = ClassReflection.getDeclaredMethod(clazz, "readNextObject", Scanner.class, FileHandle.class);
		} catch (ReflectionException e) {
			LOG.error("fancyEvent " + type + " does not specify static method readNextObject", e);
			return null;
		}

		if (!method.getReturnType().equals(AFancyEvent.class)) {
			LOG.error("fancy event " + type + " without static method readNextObject that returns a fancy event");
			return null;
		}
		;

		try {
			return (AFancyEvent) method.invoke(null, scanner, parentFile);
		} catch (ReflectionException e) {
			LOG.error("failed to read fancy event: " + type, e);
			return null;
		}
	}

	public static enum Priority {
		FancySound(0),
		FancySoundtarget(0),
		FancyDamagetarget(0),
		FancyShake(0),

		FancySprite(1),
		FancyAnimation(1),
		FancySpritetarget(1),
		FancyNinepatch(1),
		FancyPeffect(1),

		FancyPosition(2),
		FancyOrigin(2),
		FancyOpacity(2),
		FancyScale(2),
		FancyCrop(2),

		FancyMove(3),
		FancyFade(3),
		FancyZoom(3),
		FancySlide(3),

		FancyShow(4),
		FancyInclude(4),;

		private int p;

		private Priority(int p) {
			this.p = p;
		}

		public int getPriority() {
			return p;
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + startTime;
	}
}
