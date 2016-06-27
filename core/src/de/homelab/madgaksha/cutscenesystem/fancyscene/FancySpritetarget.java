package de.homelab.madgaksha.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public class FancySpritetarget extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancySpritetarget.class);

	private String key = StringUtils.EMPTY;
	private float direction;
	private float dpi;
	
	public FancySpritetarget(String key, float dpi, float direction) {
		super(true);
		this.dpi = dpi;
		this.key = key;
		this.direction = direction;
	}
	
	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		dpi = 1.0f;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		// Get sprite animation of the current target.
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
			LOG.debug("no such target");
			return false;
		}
		Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
		SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent.get(target);
		if (sfdc == null) {
			LOG.debug("target does not possess sprite for direction component");
			return false;
		}
		AtlasAnimation animation = BirdsViewSpriteSystem.getForDirection(direction, sfdc);
		if (animation == null) {
			LOG.debug("could not fetch animation for direction");
			return false;
		}
		efs.setSpriteTexture(key, animation, dpi);
		return false;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, float passedTime) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end() {
	}
	
	@Override
	public boolean configure(EventFancyScene efs) {
		efs.addSprite(key);
		return true;
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
		
		return new FancySpritetarget(key, dpi, direction);
	}
}
