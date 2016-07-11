package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;

import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

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
	private final static Logger LOG = Logger.getLogger(FancySpritetarget.class);

	private String key;
	private AnimationMode mode;
	private float direction;
	private float dpi;

	public FancySpritetarget(String key, float dpi, float direction, AnimationMode mode) {
		super(true);
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
	public boolean begin(EventFancyScene efs) {
		// Get target
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
			LOG.error("no such target");
			return false;
		}
		Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);

		// Animation
		AtlasAnimation animation = null;
		AnimationModeListComponent sfdcl = Mapper.animationModeListComponent.get(target);
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
			AnimationForDirectionComponent sfdc = Mapper.animationForDirectionComponent.get(target);
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
		efs.getDrawable(key).setDrawable(animation, dpi);
		return false;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float passedTime) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end() {
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
		scene.requestDrawable(key);
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();

		Float dpi = FileCutsceneProvider.nextNumber(s);
		if (dpi == null) {
			LOG.error("expected dpi");
			return null;
		}

		Float direction = FileCutsceneProvider.nextNumber(s);
		if (direction == null) {
			LOG.error("expected direction");
			return null;
		}

		AnimationMode mode = AnimationMode.NORMAL;
		if (s.hasNext()) {
			String name = s.next().toUpperCase(Locale.ROOT);
			try {
				mode = AnimationMode.valueOf(name);
			} catch (IllegalArgumentException e) {
				LOG.error("no such sprite mode", e);
				mode = AnimationMode.NORMAL;
			}
		}

		return new FancySpritetarget(key, dpi, direction, mode);
	}
}
