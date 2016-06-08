package de.homelab.madgaksha.cutscenesystem.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
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
 *   Speaker &lt;{@link ESpeaker}&gt;
 *   Face &lt;{@link EFaceVariation}&gt;
 *   Speed IMMEDIATE | &lt;POSITIVE_NUMBER&gt;
 *   Color {@link Color} | &lt;R=0..255&gt; &lt;G=0..255&gt; &lt;B=0..255&gt; &lt;A=0..1&gt;
 *   BoxColor {@link Color} | &lt;R=0..255&gt; &lt;G=0..255&gt; &lt;B=0..255&gt; &lt;A=0..1&gt;
 *   TransitionIn NONE | (&lt;POSITIVE_NUMBER&gt; {@link Interpolation})
 *   TransitionOut NONE | (&lt;POSITIVE_NUMBER&gt; {@link Interpolation})
 *   SoundTextbox &lt;{@link ESound}&gt;
 *   SoundText &lt;{@link ESound}&gt;
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

}
