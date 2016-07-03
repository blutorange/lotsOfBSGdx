package de.homelab.madgaksha.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;

import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.logging.Logger;

public class FancyDamagetarget extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyDamagetarget.class);

	private long basePowerMin;
	private long basePowerMax;

	public FancyDamagetarget(long damageMin, long damageMax) {
		super(true);
		this.basePowerMax = damageMax;
		this.basePowerMin = damageMin;
	}

	@Override
	public void reset() {
		basePowerMin = 0L;
		basePowerMax = 0L;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		// Get damage component for current target.
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
			LOG.error("no such target");
			return false;
		}
		Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
		long basePower = MathUtils.random(basePowerMin, basePowerMax);
		ComponentUtils.dealDamage(playerHitCircleEntity, target, basePower, true);
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
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		Long basePowerMin = FileCutsceneProvider.nextLong(s);
		if (basePowerMin == null) {
			LOG.error("expected damage min");
			return null;
		}

		Long basePowerMax = FileCutsceneProvider.nextLong(s);
		if (basePowerMax == null)
			basePowerMax = basePowerMin;

		return new FancyDamagetarget(basePowerMin, basePowerMax);
	}
}
