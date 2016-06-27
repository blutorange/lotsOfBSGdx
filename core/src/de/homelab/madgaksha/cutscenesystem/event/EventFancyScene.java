package de.homelab.madgaksha.cutscenesystem.event;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.FancySpriteWrapper;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

/**
 * The following commands are available for reading from file.
 * <ul>
 * <li>Include &lt;Relative/Absolute> &lt;StartTime&gt; &lt;FileName&gt;</li>
 * <li>Sound &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;Entity&gt; &lt;Sound&gt;</li> 
 * <li>Sprite &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;DPI&gt; &lt;Texture&gt;</li>
 * <li>Spritetarget &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;DPI&gt; &lt;LookingDirection&gt;</li>
 * <li>Ninepatch &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;halfWidth&gt; &lt;halfHeight&gt; &lt;NinePatch&gt;</li>
 * <li>Position &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;X&gt; &lt;Y&gt;</li>
 * <li>Scale &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;cropLeft&gt; &lt;cropBottom&gt; [&lt;cropRight&gt;=&lt;cropLeft&gt;] [&lt;cropTop&gt;=&lt;cropBottom&gt;]</li>
 * <li>Scale &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;scaleX&gt; [&lt;scaleY&gt;=&lt;scaleX&gt;]</li>
 * <li>Opacity &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;Opacity&gt;</li>
 * <li>Move &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;PathType&gt; &lt;Duration&gt; [=&lt;Interpolation&gt;=(=linear)] &lt;Relative/Absolute&gt; &lt;PathDetails&gt;</li>
 * <li>Zoom &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;Duration&gt; [=&lt;Interpolation&gt;=(=linear)] &lt;targetScaleX&gt; [&lt;targetScaleY&gt;=&lt;targetScaleX&gt;]</li>
 * <li>Show &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;Duration&gt;</li>
 * <li>Fade &lt;Relative/Absolute&gt; &lt;StartTime&gt; &lt;SpriteName&gt; &lt;Duration&gt; &lt;TargetOpacity&gt; [Interpolation=linear]</li>
 * </ul>
 * 
 * Additionally, a z-index may be given immediately after the event name (and before the absolute/relative flag).
 * The higher the z-index, the earlier events are processed (updated/rendered). This applies only to events
 * of the same type, each type of event has got an additional internal priority too.
 * <br><br>
 * (0,0) is at the center of the screen, and the screen's dimensions are width=8 and height=9.
 * <br><br>
 * A 8x9 pixel sprite with a DPI of 1 will fill the entire game screen.
 * <br><br>
 * For example, a simple fade in with an image that move from the bottom left to the center:
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

	private Stack<AFancyEvent> eventList = new Stack<AFancyEvent>();
	private Stack<AFancyEvent> activeList = new Stack<AFancyEvent>();
	private Map<String, FancySpriteWrapper> spriteMap = new HashMap<String, FancySpriteWrapper>();
	private float totalTime = 0.0f;

	/**
	 * Constructs a new fancy scene with an empty event list. Internal use only.
	 */
	private EventFancyScene() {
	}

	public EventFancyScene(List<AFancyEvent> eventList) {
		for (AFancyEvent fe : eventList) {
			if (fe.configure(this)) {
				this.eventList.add(fe);
			}
		}
	}

	@Override
	public void reset() {
		eventList.clear();
		activeList.clear();
		totalTime = 0.0f;
	}

	@Override
	public boolean isFinished() {
		return eventList.isEmpty() && activeList.isEmpty();
	}

	@Override
	public void render() {
		viewportPixel.apply();
		batchPixel.begin();
		renderMain();
		batchPixel.end();
	}
	
	public void renderMain() {
		for (AFancyEvent fe : activeList)
			fe.render();
	}

	@Override
	public void update(float deltaTime) {
		totalTime += deltaTime;
		boolean changed = false;
		// Add events that should start now.
		while (!eventList.empty() && totalTime >= eventList.peek().getStartTime()) {
			AFancyEvent fe = eventList.pop();
			if (fe.begin(this)) {
				activeList.push(fe);
				fe.update(totalTime - fe.getStartTime(), totalTime - fe.getStartTime());
				changed = true;
			}
		}
		if (changed) {
			Collections.sort(activeList, AFancyEvent.ORDER_Z);
		}
		// Update events and remove those that are done.
		for (Iterator<AFancyEvent> it = activeList.iterator(); it.hasNext();) {
			AFancyEvent fe = it.next();
			if (fe.isFinished()) {
				fe.end();
				it.remove();
			} else
				fe.update(deltaTime, totalTime - fe.getStartTime());
		}
	}

	@Override
	public void resize(int w, int h) {
		for (AFancyEvent fe : eventList)
			fe.resize(w, h);
		for (AFancyEvent fe : activeList)
			fe.resize(w, h);
	}

	@Override
	public boolean begin() {
		Collections.sort(eventList, Collections.reverseOrder());
		return !eventList.isEmpty();
	}

	@Override
	public void end() {
	}

	public void addSprite(String key) {
		if (!spriteMap.containsKey(key))
			spriteMap.put(key, new FancySpriteWrapper());
	}

	private void setSpriteTexture(String key, AtlasRegion region, float dpi) {
		FancySpriteWrapper fsw = getSprite(key);
		if (region != null) fsw.sprite.setAtlasRegion(region);
		fsw.spriteDpi = dpi;
	}
	public void setSpriteTexture(String key, ETexture texture, float dpi) {
		FancySpriteWrapper fsw = getSprite(key);
		setSpriteTexture(key, ResourceCache.getTexture(texture), dpi);
		fsw.mode = FancySpriteWrapper.Mode.TEXTURE;
	}
	public void setSpriteTexture(String key, AtlasAnimation animation, float dpi) {
		FancySpriteWrapper fsw = getSprite(key);
		if (animation != null) {
			fsw.atlasAnimation = animation;
			setSpriteTexture(key, animation.getKeyFrame(0.0f), dpi);
		}
		fsw.mode = FancySpriteWrapper.Mode.ATLAS_ANIMATION;
	}
	public void setSpriteTexture(String key, ENinePatch ninePatch, Vector2 dimensions) {
		FancySpriteWrapper fsw = getSprite(key);
		NinePatch np = ResourceCache.getNinePatch(ninePatch);
		if (np != null) {
			fsw.ninePatch = np;
			fsw.ninePatchDimensions.set(dimensions);
			fsw.mode = FancySpriteWrapper.Mode.NINE_PATCH;
		}
	}
	

	public void setSpritePosition(String key, Vector2 position) {
		getSprite(key).position.set(position);
	}
	
	public void setSpriteScale(String key, Vector2 scale) {
		getSprite(key).scale.set(scale);
	}

	public void setSpriteColor(String key, Color color) {
		getSprite(key).color.set(color);		
	}
	
	public void setSpriteCrop(String key, Vector2 cropX, Vector2 cropY) {
		FancySpriteWrapper fsw = getSprite(key);
		fsw.cropX.set(cropX);
		fsw.cropY.set(cropY);
	}
	
	public void setSpriteOpacity(String key, float opacity) {
		getSprite(key).opacity = opacity;		
	}
	
	public FancySpriteWrapper getSprite(String key) {
		addSprite(key);
		return spriteMap.get(key);

	}

	public static EventFancyScene readNextObject(Scanner s, FileHandle inputFile) {
		if (!s.hasNextLine()) {
			LOG.error("expected file name for fancyScene configuration file");
			return null;
		}
		String relativeFilePath = s.nextLine().trim();
		FileHandle fileHandle = inputFile.parent().child(relativeFilePath);
		EventFancyScene efs = new EventFancyScene();
		return readEventList(fileHandle, efs) ? efs : null;
	}

	private static boolean readEventList(FileHandle fileHandle, EventFancyScene efs) {
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
					efs.eventList.add(fancyEvent);
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
}
