package de.homelab.madgaksha.cutscenesystem.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.GameParameters;
import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.event.EventTextbox;
import de.homelab.madgaksha.cutscenesystem.textbox.EFaceVariation;
import de.homelab.madgaksha.cutscenesystem.textbox.PlainTextbox;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.enums.ESpeaker;
import de.homelab.madgaksha.enums.RichterScale;
import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.path.EPath;
import de.homelab.madgaksha.resourcecache.EAnimation;
import de.homelab.madgaksha.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETextbox;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcepool.EParticleEffect;

/**
 * Reads cutscene events from a configuration file<br>
 * Empty lines, multiple spaces are ignored.<br>
 * Lines beginning with two slashes (//) or a hash tag (#) are ignored.<br>
 * Encoding must be UTF-8.
 * 
 * 
 * <br>
 * <br>
 * 
 * The general structure is an array of commands:
 * 
 * <pre>
 * &lt;CutsceneArray&gt; := {&lt;Command&gt;}
 * </pre>
 * 
 * <br>
 * <br>
 * 
 * A command is either an include statement or a cutscene event with parameters:
 * 
 * <pre>
 * &lt;Command&gt; := &lt;CutsceneEvent&gt; | &lt;IncludeStatement&gt;
 * </pre>
 * 
 * <br>
 * <br>
 * 
 * An include statement includes the provided cutscene event configuration file
 * and adds the events.
 * 
 * <pre>
 * &lt;IncludeStatement&gt; := (Include | IncludeI18n) "i18nGameKey"
 * </pre>
 * 
 * The path is relative to the file containing the include statement. When
 * IncludeI18n is specified, the file path is suffixed with
 * <code>.&lt;SHORT_LOCALE_NAME&gt;</code>
 * 
 * <br>
 * <br>
 * 
 * A cutscene event consists of a class and parameters:
 * 
 * <pre>
 * &lt;CutsceneEvent&gt; := &lt;EventClass&gt; &lt;EventParameters&gt;
 * </pre>
 * 
 * <br>
 * <br>
 * 
 * An event class is a class extending {@link ACutsceneEvent}. It must be in the
 * package {@link de.homelab.madgaksha.cutscenesystem.event}.
 * 
 * <pre>
 * &lt;EventClass&gt; := EventTextbox | EventWait | ...
 * </pre>
 * 
 * <br>
 * <br>
 * 
 * Event parameters are different for each cutscene event.
 * 
 * <pre>
 * &lt;EventParameters&gt; := &lt;EventParametersWait&gt; | &lt;EventParametersTextbox&gt; | ...
 * </pre>
 * 
 * That said, most parameters are key-value pairs:
 * 
 * <pre>
 * &lt;KEY&gt; &lt;VALUE&gt; {&lt;VALUE&gt;} &lt;NEWLINE&gt;
 * </pre>
 * 
 * 
 * <h2>Wait event</h2>
 * 
 * The wait event only specifies a wait time:
 * 
 * <pre>
 * &lt;EventParametersWait&gt; := &lt;NUMBER&gt;
 * </pre>
 * 
 * For example:
 * 
 * <pre>
 *  EventWait 2.5
 * </pre>
 * 
 * 
 * <h2>Move event</h2>
 * 
 * The move event specifies details on which entity to move and how:
 * 
 * <pre>
 *  &lt;EventParametersMove&gt; := &lt;GUID&gt;
 * {&lt;{@link EPath} &lt;POSITIVE_NUMBER&gt; [R|A] &lt;PathParameters&gt;}
 * </pre>
 * 
 * The number after the path specifies the time in seconds for that path. If a
 * tile is blocking, the entity will not move there, unless
 * {@link PositionComponent#limitToMap} is set to false. All coordinates
 * specified in tiles.
 * 
 * <br>
 * 
 * For example:
 * 
 * <pre>
 * &gt;&gt;EventMove player
 *   Line 1.0 R 0 -20
 *   Line 1.0 R 20 0
 * </pre>
 * 
 * This would move the player -20 tiles up, then 20 tiles to the right. Path
 * Parameters are as follows:
 * 
 * 
 * <h3>PATH_LINE</h3>
 * 
 * 
 * 
 * <code>&lt;PathParameters&gt; := &lt;END_POINT_X&gt; &lt;END_POINT_Y&gt;</code>
 * 
 * <h2>Fancy scene event</h2>
 * 
 * An animation with multiple sprites, sounds and possibly models (later).
 * 
 * For more details and the format, see the javadocs for {@link EventFancyScene}
 * .
 * 
 * <h2>Stage event</h2>
 * 
 * For entering or exiting NPCs.
 * 
 * <pre>
 * &lt;EventParametersMove&gt; := &lt;GUID&gt; (ENTER|EXIT)
 * [EffectBefore &lt;{@link EParticleEffect}&gt;]
 * [EffectAfter &lt;{@link EParticleEffect}&gt;]
 * [SoundBefore1 &lt;{@link ESound}&gt;]
 * [SoundBefore2 &lt;{@link ESound}&gt;]
 * [SoundAfter1 &lt;{@link ESound}&gt;]
 * [SoundAfter2 &lt;{@link ESound}&gt;]
 * </pre>
 * 
 * SoundBefore1 and SoundAfter1 is played before the particle effect starts.
 * SoundBefore1 and SoundBefore2 is played after the particle effect ends.
 * 
 * <br>
 * 
 * For example:
 * 
 * <pre>
 * &gt;&gt;EventStage joshua1 enter
 *   SoundAfter1 josshua_iku_yo
 *   EffectAfter npc_exit
 * </pre>
 * 
 * 
 * <h2>Textbox event</h2>
 * 
 * Textbox events require more parameters. Each parameters must be on a separate
 * line. The order in which they appear is not important. If any are missing,
 * defaults are used. The only mandatory parameter is <code>Text</code>.
 * 
 * <pre>
 * &lt;EventParametersTextbox&gt; :=
 *   Box &lt;{@link ETextbox}&gt;
 *   Font &lt;{@link EFreeTypeFontGenerator}&gt;
 *   Size &lt;POSITIVE_NUMBER&gt;  
 *   LineSpacing &lt;POSITIVE_NUMBER&gt;
 *   Speaker &lt;{@link ESpeaker}&gt;
 *   Face &lt;{@link EFaceVariation}&gt;
 *   Speed IMMEDIATE | &lt;POSITIVE_NUMBER&gt;
 *   Color {@link Color} | &lt;R=0..255&gt; &lt;G=0..255&gt; &lt;B=0..255&gt; &lt;A=0..1&gt;
 *   BoxColor {@link Color} | &lt;R=0..255&gt; &lt;G=0..255&gt; &lt;B=0..255&gt; &lt;A=0..1&gt;
 *   TransitionIn STANDARD | (&lt;POSITIVE_NUMBER&gt; [={@link Interpolation}=])
 *   TransitionOut STANDARD | ([&lt;POSITIVE_NUMBER&gt; [={@link Interpolation}=])
 *   SoundTextbox NONE | &lt;{@link ESound}&gt;
 *   SoundText NONE | &lt;{@link ESound}&gt;
 *   Text &lt;EOF&gt;
 *   ...
 *   &lt;EOF&gt;
 * </pre>
 * 
 * &lt;EOF&gt; is any arbitrary string marking the end of the text. It must not
 * occur at the beginning of any line in-between. <br>
 * <br>
 * If speaker and face are omitted, the textbox will not have a speaker or face. <br>
 * <br>
 * Font size is relative to to the game screen height. At a game screen height
 * of 720 pixels, the font size is the number of pixels. At a game screen height
 * of 360 pixels, specifying a font size of 18 results in 9 pixels on-screen. <br>
 * <br>
 * Line spacing is relative to the current line height (font size). A line
 * spacing of 0.5f will result in half the height of one line of text between
 * two consecutive lines of text. <br>
 * <br>
 * Box color is the tint that will be applied to the original color of the nine
 * patch. The default color is white, which means the original colors of the
 * nine patch will be used. <br>
 * <br>
 * Color can be the name of a constant defined in {@link Color}, or a quadruple
 * of rgba values. <br>
 * <br>
 * TransitionIn and transitionOut
 * 
 * An example with the the default parameters:
 * 
 * <pre>
 * EventTextbox
 *   Box FC_BLUE
 *   Font MAIN_FONT 
 *   Size {@link PlainTextbox#DEFAULT_TEXT_HEIGHT_RATIO}*720 (=18)
 *   Color WHITE
 *   BoxColor WHITE
 *   Speed {@link GameParameters#requestedTextboxSpeed} (User-defined)
 *   TransitionIn {@link EventTextbox#NICE_TRANSITION_IN_SPEED} sineIn
 *   TransitionOut {@link EventTextbox#NICE_TRANSITION_OUT_SPEED} sineOut
 *   SoundTextbox TEXTBOX_ADVANCE
 *   SoundText TEXT_ADVANCE
 *   Text EOF
 * Hello world.
 * This is great.
 * EOF
 * </pre>
 * 
 * @author madgaksha
 * 
 */
public class FileCutsceneProvider implements CutsceneEventProvider {
	private final static Logger LOG = Logger
			.getLogger(FileCutsceneProvider.class);

	private final static String CutsceneEventClassPackage = "de.homelab.madgaksha.cutscenesystem.event.";

	private final static Pattern tokenComment = Pattern.compile("//|$");
	private final static Pattern tokenCommand = Pattern.compile(">>.+");

	private Class<? extends ACutsceneEvent> nextCutsceneClass;
	private String nextCommandIdentifier;
	private Command nextCommand;
	private FileHandle inputFile;

	private int eventPosition = -1;
	private final List<ACutsceneEvent> eventList = new ArrayList<ACutsceneEvent>();

	public FileCutsceneProvider(FileHandle fileHandle) {
		Scanner s = null;
		inputFile = fileHandle;
		try {
			String content = fileHandle.readString("UTF-8");
			s = new Scanner(content);
			s.useLocale(Locale.ROOT);
			readCutsceneEventArray(s, eventList);
		} catch (Exception e) {
			LOG.error("could not read cutscene array for " + fileHandle, e);
		} finally {
			if (s != null)
				IOUtils.closeQuietly(s);
		}
		s = null;
		nextCutsceneClass = null;
		nextCommand = null;
		inputFile = null;
	}

	/**
	 * Reads every cutscene event entry and fill the given array with the
	 * events.
	 * 
	 * @param s
	 *            Scanner to read from.
	 * @param eventList
	 *            List of events to fill.
	 */
	private void readCutsceneEventArray(Scanner s,
			List<ACutsceneEvent> eventList) {
		while (s.hasNext()) {
			FileCutsceneProvider.skipComments(s);
			if (readNextCommand(s))
				nextCommand.process(s, eventList);
		}
	}

	private static class Command {
		private final int mode;
		private Class<? extends ACutsceneEvent> cutsceneClass;
		private FileHandle fileHandle;

		// CutsceneEvent
		public Command(Class<? extends ACutsceneEvent> cutsceneClass,
				FileHandle inputFile) {
			mode = 1;
			this.cutsceneClass = cutsceneClass;
			this.fileHandle = inputFile;
		}

		// Include
		public Command(FileHandle fileHandle) {
			mode = 2;
			LOG.debug("processing Include " + fileHandle);
			this.fileHandle = fileHandle;
		}

		public void process(Scanner s, List<ACutsceneEvent> eventList) {
			switch (mode) {
			case 1:
				ACutsceneEvent event = null;
				StringBuilder sb = new StringBuilder();
				while (s.hasNext() && !FileCutsceneProvider.hasNextCommand(s))
					sb.append(s.nextLine()).append(StringUtils.LF);
				Scanner settingScanner = new Scanner(sb.toString());
				settingScanner.useLocale(Locale.ROOT);
				try {
					event = ACutsceneEvent.readNextObject(settingScanner,
							cutsceneClass, fileHandle);
				} finally {
					IOUtils.closeQuietly(settingScanner);
				}
				if (event != null)
					eventList.add(event);
				break;
			case 2:
				FileCutsceneProvider provider = new FileCutsceneProvider(
						this.fileHandle);
				eventList.addAll(provider.eventList);
				break;
			default:
				return;
			}
		}
	}

	/**
	 * Reads the next command string (include or cutsceneEvent) and stores the
	 * result in the variables {@link #nextCommandIdentifier}.
	 * 
	 * @param s
	 *            Scanner to read from.
	 * @return Whether the next command has been read successfully.
	 */
	private boolean readNextCommandIdentifier(Scanner s) {
		while (s.hasNext() && !FileCutsceneProvider.hasNextCommand(s))
			s.nextLine();
		if (s.hasNext(tokenCommand)) {
			nextCommandIdentifier = s.next(tokenCommand).substring(2);
			return true;
		}
		return false;
	}

	/**
	 * Reads the next command, builds the {@link Command} object and stores the
	 * result in {@link #nextCommand}.
	 * 
	 * @param s
	 *            Scanner to read from.
	 * @return Whether the next command has been read successfully.
	 */
	private boolean readNextCommand(Scanner s) {
		if (!readNextCommandIdentifier(s)) {
			return false;
		}
		LOG.debug("processing Command token " + nextCommandIdentifier);
		if (nextCommandIdentifier.equalsIgnoreCase("includei18n")) {
			if (!s.hasNext()) {
				LOG.error("expected file path after IncludeI18n statement");
				return false;
			}
			String relativeFilePath = s.next() + "." + I18n.getShortName();
			FileHandle fileHandle = inputFile.parent().child(relativeFilePath);
			nextCommand = new Command(fileHandle);
		} else if (nextCommandIdentifier.equalsIgnoreCase("include")) {
			if (!s.hasNext())
				return false;
			String relativeFilePath = nextCommandIdentifier;
			FileHandle fileHandle = inputFile.child(relativeFilePath);
			nextCommand = new Command(fileHandle);
		} else {
			if (!getEventClass(nextCommandIdentifier))
				return false;
			nextCommand = new Command(nextCutsceneClass, inputFile);
		}
		return true;
	}

	/**
	 * Loads the event class with the corresponding name inside the package
	 * {@link #CutsceneEventClassPackage}.
	 * 
	 * @param className
	 *            Name of the class to load.
	 * @return Whether the class could be loaded.
	 */
	@SuppressWarnings("unchecked")
	private boolean getEventClass(String className) {
		String fullClassName = CutsceneEventClassPackage + className;
		try {
			nextCutsceneClass = (Class<? extends ACutsceneEvent>) ClassReflection
					.forName(fullClassName);
			if (!ClassReflection.isAssignableFrom(ACutsceneEvent.class,
					nextCutsceneClass)) {
				LOG.error("not a cutscene event: " + nextCutsceneClass);
				return false;
			}
			;
		} catch (ReflectionException e) {
			LOG.error("no such cutscene event class: " + fullClassName);
			return false;
		}
		return true;
	}

	/**
	 * Skips as much comment as possible.
	 * 
	 * @param s
	 *            Scanner to read from and skip comments.
	 */
	public static void skipComments(Scanner s) {
		while (s.hasNext(tokenComment))
			s.nextLine();
	}

	@Override
	public void initialize() {
		eventPosition = -1;
	}

	@Override
	public void end() {
		eventList.clear();
	}

	@Override
	public ACutsceneEvent nextCutsceneEvent(int i) {
		eventPosition += 1;
		return eventPosition < eventList.size() ? eventList.get(eventPosition)
				: null;
	}

	/**
	 * Returns the next number as a float. Does not advance the scanner if there
	 * is no valid number next.
	 * 
	 * @param s
	 *            The scanner to use.
	 * @return The next number, or null if there is none.
	 */
	public static Float nextNumber(Scanner s) {
		if (s.hasNextFloat())
			return s.nextFloat();
		if (s.hasNextInt(10))
			return (float) s.nextInt(10);
		if (s.hasNextDouble())
			return (float) s.nextDouble();
		if (s.hasNextLong(10))
			return (float) s.nextLong(10);
		if (s.hasNextShort(10))
			return (float) s.nextShort(10);
		return null;
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return Whether the is a command matching {@link #tokenCommand}.
	 */
	public static boolean hasNextCommand(Scanner s) {
		return s.hasNext(tokenCommand);
	}

	/**
	 * @param name
	 *            Name of the color.
	 * @return A {@link Color} object for the given name, or null if there is no
	 *         such color.
	 */
	private static Color getColorForName(String name) {
		try {
			Field f = ClassReflection.getField(Color.class,
					name.toUpperCase(Locale.ROOT));
			Object o = f.get(null);
			if (o instanceof Color)
				return (Color) o;
			return null;
		} catch (ReflectionException e) {
			LOG.error("no such color: " + name);
			return null;
		}
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An {@link Interpolation} object, or null if no such interpolation
	 *         exists.
	 */
	public static Interpolation readNextInterpolation(Scanner s) {
		if (s.hasNext("=.+=")) {
			String param = s.next("=.+=");
			String name = WordUtils.uncapitalize(param.substring(1,
					param.length() - 1));
			try {
				Field f = ClassReflection.getField(Interpolation.class, name);
				Object o = f.get(null);
				if (o instanceof Interpolation) {
					return (Interpolation) o;
				} else {
					LOG.error("not an interpolation class:  " + name);
				}
			} catch (ReflectionException e) {
				LOG.error("no such interpolation: " + name, e);
			}
		}
		return null;
	}

	/**
	 * Reads a color from the scanner and returns it. Can be either 3 or 4
	 * number representing rgb(a) values, or a color name as defined in
	 * {@link Color}. r, g, and b are given between 0..255, a is given between
	 * 0..1. An alpha value of 1 is opaque.
	 * 
	 * @param s
	 *            The scanner to read from.
	 * @return The color, or null if none could be read.
	 */
	public static Color readNextColor(Scanner s) {
		Float r = FileCutsceneProvider.nextNumber(s);
		if (r == null) {
			if (s.hasNext()) {
				Color color = FileCutsceneProvider.getColorForName(s.next());
				if (color != null)
					return color;
			}
			LOG.error("expected color value or name");
			s.nextLine();
			return null;
		}
		Float g = FileCutsceneProvider.nextNumber(s);
		if (g == null) {
			LOG.error("expected color value");
			s.nextLine();
			return null;
		}
		Float b = FileCutsceneProvider.nextNumber(s);
		if (b == null) {
			LOG.error("expected color value");
			s.nextLine();
			return null;
		}
		Float a = FileCutsceneProvider.nextNumber(s);
		if (a == null)
			a = 1.0f;
		return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a);
	}

	@Override
	public void eventDone(ACutsceneEvent currentCutsceneEvent) {
		currentCutsceneEvent.end();
	}

	/**
	 * @return The remaining of the line, with trailing and leading white spaces
	 *         removed.
	 */
	public static String readRemainingOfLine(Scanner s) {
		if (!s.hasNextLine())
			return null;
		return s.nextLine().trim();
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return Normalized object id.
	 */
	public static String readNextGuid(Scanner s) {
		if (!s.hasNext())
			return null;
		return s.next().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An {@link EParticleEffect} instance, or null if it could not be
	 *         read.
	 */
	public static EParticleEffect nextParticleEffect(Scanner s) {
		if (!s.hasNext())
			return null;
		String pe = s.next().toUpperCase(Locale.ROOT);
		try {
			return EParticleEffect.valueOf(pe);
		} catch (IllegalArgumentException e) {
			LOG.error("no such particle effect: " + pe);
			return null;
		}
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return A {@link ESound} instance, or null if it could not be read.
	 */
	public static ESound nextSound(Scanner s) {
		if (!s.hasNext())
			return null;
		String se = s.next().toUpperCase(Locale.ROOT);
		try {
			return ESound.valueOf(se);
		} catch (IllegalArgumentException e) {
			LOG.error("no such sound effect: " + se);
			return null;
		}
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link EPath}, or null if it could not be read.
	 */
	public static EPath nextPath(Scanner s) {
		if (!s.hasNext())
			return null;
		String pathType = "PATH_" + s.next().toUpperCase(Locale.ROOT);
		try {
			return EPath.valueOf(pathType);
		} catch (IllegalArgumentException e) {
			LOG.error("no such path: " + pathType);
			return null;
		}
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link ETexture}, or null if it could not be read.
	 */
	public static ETexture nextTexture(Scanner s) {
		if (!s.hasNext())
			return null;
		String t = s.next().toUpperCase(Locale.ROOT);
		try {
			return ETexture.valueOf(t);
		} catch (IllegalArgumentException e) {
			LOG.error("no such texture: " + t);
			return null;
		}
	}
	
	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link EAnimation}, or null if it could not be read.
	 */
	public static EAnimation nextAnimation(Scanner s) {
		if (!s.hasNext())
			return null;
		String a = s.next().toUpperCase(Locale.ROOT);
		try {
			return EAnimation.valueOf(a);
		} catch (IllegalArgumentException e) {
			LOG.error("no such animation: " + a);
			return null;
		}
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link ENinePatch}, or null if it could not be
	 *         read.
	 */
	public static ENinePatch nextNinePatch(Scanner s) {
		if (!s.hasNext())
			return null;
		String t = s.next().toUpperCase(Locale.ROOT);
		try {
			return ENinePatch.valueOf(t);
		} catch (IllegalArgumentException e) {
			LOG.error("no such nine patch: " + t);
			return null;
		}
	}
	
	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link RichterScale}, or null if it could not be
	 *         read.
	 */
	public static RichterScale nextRichterScale(Scanner s) {
		if (!s.hasNext())
			return null;
		String rs = s.next().toUpperCase(Locale.ROOT);
		try {
			return RichterScale.valueOf(rs);
		} catch (IllegalArgumentException e) {
			LOG.error("no such richter scale: " + rs);
			return null;
		}
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link Long}, or null if no number could be read.
	 */
	public static Long nextLong(Scanner s) {
		if (s.hasNextLong(10))
			return s.nextLong(10);
		if (s.hasNextInt(10))
			return (long) s.nextInt(10);
		if (s.hasNextShort(10))
			return (long) s.nextShort(10);
		return null;
	}

	/**
	 * @param s
	 *            Scanner to read from.
	 * @return An instance of {@link Integer}, or null if no number could be
	 *         read.
	 */
	public static Integer nextInteger(Scanner s) {
		if (s.hasNextInt(10))
			return s.nextInt(10);
		if (s.hasNextShort(10))
			return (int) s.nextShort(10);
		if (s.hasNextLong(10))
			return (int) s.nextLong(10);
		return null;
	}


}
