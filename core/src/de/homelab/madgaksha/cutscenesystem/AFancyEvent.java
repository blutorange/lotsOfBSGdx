package de.homelab.madgaksha.cutscenesystem;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.LocaleRootWordUtils;

public abstract class AFancyEvent implements Comparable<AFancyEvent>, Poolable {
	private final static Logger LOG = Logger.getLogger(AFancyEvent.class);
	private final static String FANCY_SCENE_PREFIX = "de.homelab.madgaksha.cutscenesystem.fancyscene.Fancy";
	
	protected float startTime = 0.0f;
	protected int priority = 0;
	private boolean relative = false;
	private int z = 0;
	
	public final static Comparator<AFancyEvent> ORDER_Z = new Comparator<AFancyEvent>() {
		@Override
		public int compare(AFancyEvent o1, AFancyEvent o2) {
			return o1.z < o2.z ? -1 : o1.z == o2.z ? 0 : 1;
		}
	};
	
	public AFancyEvent(boolean setPriority) {
		try {
			Priority p = Priority.valueOf(getClass().getSimpleName());
			this.priority = p.getPriority();
		}
		catch (IllegalArgumentException e) {
			LOG.error("no priority given for " + getClass().getSimpleName());
		}
	}
	
	/**
	 * @param efs The cutscene event which contains this fancy event.
	 * @return Whether this event can be played back. Should be false for configuration setters.
	 */
	public abstract boolean configure(EventFancyScene efs);
	public abstract boolean begin(EventFancyScene efs);
	public abstract void render();
	/**
	 * Called when some time has passed and the events needs to be updated.
	 * @param deltaTime Time that has passed since the last update.
	 * @param passedTime Time that has passed since the beginning of this event.
	 */
	public abstract void update(float deltaTime, float passedTime);
	public abstract boolean isFinished();
	public abstract void end();
	public void resize(int w, int h) {
		
	}
	
	/**
	 * For sorting the eventList. Lowest start time last, highest first.
	 */
	@Override
	public int compareTo(AFancyEvent you) {
		int n = (startTime < you.startTime) ? -1 : (startTime == you.startTime) ? ((priority < you.priority) ? -1 : (priority == you.priority) ? 0 : 1) : 1;
		return n;
	}
	
	public float getStartTime() {
		return startTime;
	}
	
	public boolean isRelative() {
		return relative;
	}
	public void resolveRelativeStartTime(Map<Class<?>, Float> startMap) {
		if (relative) {
			Float lastStartTime = startMap.get(getClass());
			if (lastStartTime == null) lastStartTime = 0.0f;
			startTime += lastStartTime;
			relative = false;
		}
	}
	
	/**
	 * Read event from its serialized form.
	 * @param scanner Scanner to read from.
	 * @return The event as read from the file, or null if it could not be read.
	 */
	public static AFancyEvent readNextEvent(Scanner scanner, FileHandle parentFile) {
		if (!scanner.hasNext()) {
			LOG.error("expected event type");
			return null;
		}
		String type = LocaleRootWordUtils.capitalizeFully(scanner.next());	
		Integer z = FileCutsceneProvider.nextInteger(scanner);
		if (z == null) z = 0;
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
		if (fancyEvent == null) return null;
		fancyEvent.startTime = startTime;
		fancyEvent.relative = isRelative;
		fancyEvent.z = z;
		return fancyEvent;
	}

	private static AFancyEvent fetchEventFor(String type, Scanner scanner, FileHandle parentFile) {
		String fullName = FANCY_SCENE_PREFIX + type; 
		Class<?> clazz = null;
		Method method = null;
		try {
			clazz = ClassReflection.forName(fullName);
		}
		catch (ReflectionException e) {
			LOG.error("no such fancy event: " + type, e);
			return null;
		}
		
		try {
			method = ClassReflection.getDeclaredMethod(clazz, "readNextObject", Scanner.class, FileHandle.class);
		}
		catch (ReflectionException e) {
			LOG.error("fancyEvent " + type + " does not specify static method readNextObject", e);
			return null;
		}
		
		if (!method.getReturnType().equals(AFancyEvent.class)) {
			LOG.error("fancy event " + type + " without static method readNextObject that returns a fancy event");
			return null;
		};
		
		try {
			return (AFancyEvent) method.invoke(null, scanner, parentFile);
		}
		catch (ReflectionException e) {
			LOG.error("failed to read fancy event: " + type, e);
			return null;
		}
	}
	
	public static enum Priority {
		FancySound(0),
		
		FancySprite(1),
		FancyNinepatch(1),
		
		FancyPosition(2),
		FancyOpacity(2),
		
		FancyMove(3),
		FancyFade(3),
		
		FancyShow(4),
		FancyInclude(4),
		;
		private int p;
		private Priority(int p) {
			this.p = p;
		}
		public int getPriority() {
			return p;
		}
	}
}