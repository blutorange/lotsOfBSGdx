package de.homelab.madgaksha.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;

import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionListComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionListComponent.SpriteDirection;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionListComponent.SpriteMode;
import de.homelab.madgaksha.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public class FancySpritetarget extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancySpritetarget.class);

	private String key;
	private SpriteMode mode;
	private float direction;
	private float dpi;

	public FancySpritetarget(String key, float dpi, float direction, SpriteMode mode) {
		super(true);
		this.dpi = dpi;
		this.key = key;
		this.direction = direction;
		this.mode = mode;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		mode = SpriteMode.NORMAL;
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
		SpriteForDirectionListComponent sfdcl = Mapper.spriteForDirectionListComponent.get(target);
		if (sfdcl != null) {
			SpriteDirection sd = mode.getSpriteDirection(sfdcl);
			if (sd == null) {
				LOG.error("target does not possess this sprite mode");
				sd = SpriteMode.NORMAL.getSpriteDirection(sfdcl);
			}
			if (sd == null) {
				LOG.error("target does not possess normal sprite mode");
				return false;
			}
			animation = BirdsViewSpriteSystem.getForDirection(direction, sd);
		} else {
			SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent.get(target);
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

		SpriteMode mode = SpriteMode.NORMAL;
		if (s.hasNext()) {
			String name = s.next().toUpperCase(Locale.ROOT);
			try {
				mode = SpriteMode.valueOf(name);
			} catch (IllegalArgumentException e) {
				LOG.error("no such sprite mode", e);
				mode = SpriteMode.NORMAL;
			}
		}

		return new FancySpritetarget(key, dpi, direction, mode);
	}
}
