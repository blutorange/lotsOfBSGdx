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
import de.homelab.madgaksha.cutscenesystem.event.EventTextbox;
import de.homelab.madgaksha.cutscenesystem.textbox.EFaceVariation;
import de.homelab.madgaksha.cutscenesystem.textbox.PlainTextbox;
import de.homelab.madgaksha.enums.ESpeaker;
import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETextbox;

/**
 * Reads cutscene events from a configuration file<br>
 * Empty lines, multiple spaces are ignored.<br>
 * Lines beginning with two slashes (//) or a hash tag (#) are ignored.<br>
 * Encoding must be UTF-8.
 * 
 * 
 * <br><br>
 * 
 * The general structure is an array of commands:
 * 
 * <pre>&lt;CutsceneArray&gt; := {&lt;Command&gt;}</pre>
 * 
 * <br><br>
 * 
 * A command is either an include statement or a cutscene event with parameters:
 * 
 * <pre>&lt;Command&gt; := &lt;CutsceneEvent&gt; | &lt;IncludeStatement&gt;</pre>
 * 
 * <br><br>
 * 
 * An include statement includes the provided cutscene event configuration file and adds the events.
 * 
 * <pre>&lt;IncludeStatement&gt; := (Include | IncludeI18n) "i18nGameKey"</pre>
 * 
 * The path is relative to the file containing the include statement.
 * When IncludeI18n is specified, the file path is suffixed with <code>.&lt;SHORT_LOCALE_NAME&gt;</code>
 * 
 * <br><br>
 * 
 * A cutscene event consists of a class and parameters:
 * 
 * <pre>&lt;CutsceneEvent&gt; := &lt;EventClass&gt; &lt;EventParameters&gt;</pre>
 * 
 * <br><br>
 * 
 * An event class is a class extending {@link ACutsceneEvent}.
 * It must be in the package {@link de.homelab.madgaksha.cutscenesystem.event}.
 * 
 * <pre>&lt;EventClass&gt; := EventTextbox | EventWait | ... </pre>
 * 
 * <br><br>
 * 
 * Event parameters are different for each cutscene event. 
 * 
 * <pre>&lt;EventParameters&gt; := &lt;EventParametersWait&gt; | &lt;EventParametersTextbox&gt; | ...</pre>
 * 
 * That said, most parameters are key-value pairs:
 * 
 * <pre>&lt;KEY&gt; &lt;VALUE&gt; {&lt;VALUE&gt;} &lt;NEWLINE&gt;</pre>
 * 
 * <br><br>
 * 
 * The wait event only specifies a wait time:
 * 
 * <pre>&lt;EventParametersWait&gt; := &lt;NUMBER&gt;</pre>
 * 
 * For example:
 * <pre> EventWait 2.5</pre>
 *   
 * <br><br>
 * 
 * Textbox events require more parameters. Each parameters must be on a separate line.
 * The order in which they appear is not important. If any are missing, defaults are used.
 * The only mandatory parameter is <code>Text</code>. 
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
 * &lt;EOF&gt; is any arbitrary string marking the end of the text. It must not occur at the beginning
 * of any line in-between.
 * <br><br>
 * If speaker and face are omitted, the textbox will not have a speaker or face.
 * <br><br>
 * Font size is relative to to the game screen height. At a game screen height of 720 pixels,
 * the font size is the number of pixels. At a game screen height of 360 pixels, specifying
 * a font size of 18 results in 9 pixels on-screen.
 * <br><br>
 * Line spacing is relative to the current line height (font size). A line spacing of 0.5f
 * will result in half the height of one line of text between two consecutive lines of text.
 * <br><br>
 * Box color is the tint that will be applied to the original color of the nine patch. The
 * default color is white, which means the original colors of the nine patch will be used.
 * <br><br>
 * Color can be the name of a constant defined in {@link Color}, or a quadruple of rgba values.
 * <br><br>
 * TransitionIn and transitionOut 
 * 
 * An example with the the default parameters:
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
 * @author madgaksha
 *
 */
public class FileCutsceneProvider implements CutsceneEventProvider {
	private final static Logger LOG = Logger.getLogger(FileCutsceneProvider.class);
	
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
			readCutsceneEventArray(s, eventList);
		}
		catch (Exception e) {
			LOG.error("could not read cutscene array for " + fileHandle, e);
		}
		finally {
			if (s != null) IOUtils.closeQuietly(s);
		}
		s = null;
		nextCutsceneClass = null;
		nextCommand = null;
		inputFile = null;
	}
	
	private void readCutsceneEventArray(Scanner s, List<ACutsceneEvent> eventList) {
		while (s.hasNext()) {
			FileCutsceneProvider.skipComments(s);
			if (readNextCommand(s))
				nextCommand.process(s, eventList);
		} 
	}
	
	private static class Command {
		private final int mode;
		private Class<? extends ACutsceneEvent> commandCutsceneClass;
		private FileHandle commandFileHandle;
		// CutsceneEvent
		public Command(Class<? extends ACutsceneEvent> cutsceneClass) {
			mode = 1;
			this.commandCutsceneClass = cutsceneClass;
		}
		// Include
		public Command(FileHandle fileHandle) {
			mode = 2;
			LOG.debug("processing Include " + fileHandle);
			this.commandFileHandle = fileHandle;
		}
		
		public void process(Scanner s, List<ACutsceneEvent> eventList) {
			switch (mode) {
			case 1:
				ACutsceneEvent event = null;
				StringBuilder sb = new StringBuilder();
				while (s.hasNext() && !FileCutsceneProvider.hasNextCommand(s)) sb.append(s.nextLine()).append(StringUtils.LF);
				Scanner settingScanner = new Scanner(sb.toString());
				try {
					event = ACutsceneEvent.readNextObject(settingScanner, commandCutsceneClass);
				}
				finally {
					IOUtils.closeQuietly(settingScanner);
				}
				if (event != null) eventList.add(event);
				break;
			case 2:
				FileCutsceneProvider provider = new FileCutsceneProvider(this.commandFileHandle);
				eventList.addAll(provider.eventList);
				break;
			default:
				return;
			}
		}
	}

	private boolean readNextCommandIdentifier(Scanner s) {
		while (s.hasNext() && !FileCutsceneProvider.hasNextCommand(s)) s.nextLine(); 
		if (s.hasNext(tokenCommand)) {
			nextCommandIdentifier = s.next(tokenCommand).substring(2);
			return true;
		}
		return false;
	}
		
	
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
		}
		else if (nextCommandIdentifier.equalsIgnoreCase("include")) {
			if (!s.hasNext()) return false;
			String relativeFilePath = nextCommandIdentifier; 
			FileHandle fileHandle = inputFile.child(relativeFilePath);
			nextCommand = new Command(fileHandle);
		}
		else {
			if (!getEventClass(nextCommandIdentifier)) return false;
			nextCommand = new Command(nextCutsceneClass);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean getEventClass(String className) {
		String fullClassName = CutsceneEventClassPackage + className;
		try {
			nextCutsceneClass = (Class<? extends ACutsceneEvent>)ClassReflection.forName(fullClassName);
			if (!ClassReflection.isAssignableFrom(ACutsceneEvent.class, nextCutsceneClass)) {
				LOG.error("not a cutscene event: " + nextCutsceneClass);
				return false;
			};
		} catch (ReflectionException e) {
			LOG.error("no such cutscene event class: " + fullClassName);
			return false;
		}
		return true;
	}
	
	public static void skipComments(Scanner s) {
		while (s.hasNext(tokenComment)) s.nextLine();
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
		return eventPosition < eventList.size() ? eventList.get(eventPosition) : null;  
	}

	/**
	 * Returns the next number as a float.
	 * Does not advance the scanner if there is no valid number next.
	 * @param s The scanner to use.
	 * @return The next number, or null if there is none.
	 */
	public static Float nextNumber(Scanner s) {
		if (s.hasNextFloat()) return s.nextFloat();
		if (s.hasNextInt(10)) return (float)s.nextInt(10);
		if (s.hasNextDouble()) return (float)s.nextDouble();
		if (s.hasNextLong(10)) return (float)s.nextLong(10);
		if (s.hasNextShort(10)) return (float)s.nextShort(10);
		return null;
	}

	public static boolean hasNextCommand(Scanner s) {
		return s.hasNext(tokenCommand);
	}

	private static Color getColorForName(String name) {
		try {
			Field f = ClassReflection.getField(Color.class, name.toUpperCase(Locale.ROOT));
			Object o = f.get(null);
			if (o instanceof Color) return (Color)o;
			return null;
		} catch (ReflectionException e) {
			LOG.error("no such color: " + name);
			return null;
		}
	}
	
	public static Interpolation readNextInterpolation(Scanner s) {
		if (s.hasNext("=.+=")) {
			String param = s.next("=.+=");
			String name = WordUtils.uncapitalize(param.substring(1, param.length() - 1));
			try {
				Field f = ClassReflection.getField(Interpolation.class, name);
				Object o = f.get(null);
				if (o instanceof Interpolation) {
					return (Interpolation)o;
				}
				else {
					LOG.error("not an interpolation class:  " + name);
				}
			}
			catch (ReflectionException e) {
				LOG.error("no such interpolation: " + name, e);
			}
		}
		return null;
	}
	
	/**
	 * Reads a color from the scanner and returns it.
	 * Can be either 3 or 4 number representing rgb(a) values, or a color name as defined in {@link Color}.
	 * r, g, and b are given between 0..255, a is given between 0..1. An alpha value of 1 is opaque.
	 * @param s The scanner to read from.
	 * @return The color, or null if none could be read.
	 */
	public static Color readNextColor(Scanner s) {
		Float r = FileCutsceneProvider.nextNumber(s);
		if (r == null) {
			if (s.hasNext()) {
				Color color = FileCutsceneProvider.getColorForName(s.next());
				if (color != null) return color;
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
		if (a == null) a = 1.0f;
		return new Color(r/255.0f,g/255.0f,b/255.0f,a);
	}

	@Override
	public void eventDone(ACutsceneEvent currentCutsceneEvent) {
		currentCutsceneEvent.end();		
	}

}
