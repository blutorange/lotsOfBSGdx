package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent.AnimationForDirection;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent.AnimationMode;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancySpritetarget extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancySpritetarget.class);

	private String key;
	private AnimationMode mode;
	private float direction;
	private float dpi;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(key);
		out.writeObject(mode);
		out.writeFloat(direction);
		out.writeFloat(dpi);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final String key = in.readUTF();
		if (key == null || key.isEmpty()) throw new InvalidDataException("key cannot be null or empty");

		final Object mode = in.readObject();
		if (mode == null || !(mode instanceof AnimationMode)) throw new InvalidDataException("unknown animation mode");

		this.key = key;
		this.mode = (AnimationMode)mode;
		direction = in.readFloat();
		dpi = in.readFloat();
	}

	public FancySpritetarget(final String key, final float dpi, final float direction, final AnimationMode mode) {
		this(0, 0, key, dpi, direction, mode);
	}

	public FancySpritetarget(final float startTime, final int z, final String key, final float dpi, final float direction, final AnimationMode mode) {
		super(startTime, z, true);
		this.dpi = dpi;
		this.key = key;
		this.direction = direction;
		this.mode = mode;
	}


	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		mode = AnimationMode.NORMAL;
		dpi = 1.0f;
		direction = 0.0f;
	}

	@Override
	public boolean begin(final EventFancyScene scene) {
		// Get target
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
			LOG.error("no such target");
			return false;
		}
		final Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);

		// Animation
		AtlasAnimation animation = null;
		final AnimationModeListComponent sfdcl = Mapper.animationModeListComponent.get(target);
		if (sfdcl != null) {
			AnimationForDirection sd = mode.getAnimationForDirection(sfdcl);
			if (sd == null) {
				LOG.error("target does not possess this sprite mode");
				sd = AnimationMode.NORMAL.getAnimationForDirection(sfdcl);
			}
			if (sd == null) {
				LOG.error("target does not possess normal sprite mode");
				return false;
			}
			animation = BirdsViewSpriteSystem.getForDirection(direction, sd);
		} else {
			final AnimationForDirectionComponent sfdc = Mapper.animationForDirectionComponent.get(target);
			if (sfdc == null) {
				LOG.error("target does not possess sprite for direction component");
				return false;
			}
			animation = BirdsViewSpriteSystem.getForDirection(direction, sfdc);
		}

		// Check animation and set it
		if (animation == null) {
			LOG.error("could not fetch animation for direction");
			return false;
		}
		scene.requestDrawableAnimation(key).setLoadedDrawable(animation, dpi);
		return false;
	}

	@Override
	public void render(final Batch batch) {
	}

	@Override
	public void update(final float passedTime) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end() {
	}

	@Override
	public void drawableChanged(final EventFancyScene scene, final String changedKey) {
	}

	@Override
	public void attachedToScene(final EventFancyScene scene) {
		scene.requestDrawableAnimation(key);
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		final String key = s.next();

		final Float dpi = FileCutsceneProvider.nextNumber(s);
		if (dpi == null) {
			LOG.error("expected dpi");
			return null;
		}

		final Float direction = FileCutsceneProvider.nextNumber(s);
		if (direction == null) {
			LOG.error("expected direction");
			return null;
		}

		AnimationMode mode = AnimationMode.NORMAL;
		if (s.hasNext()) {
			final String name = s.next().toUpperCase(Locale.ROOT);
			try {
				mode = AnimationMode.valueOf(name);
			} catch (final IllegalArgumentException e) {
				LOG.error("no such sprite mode", e);
				mode = AnimationMode.NORMAL;
			}
		}

		return new FancySpritetarget(key, dpi, direction, mode);
	}
}
