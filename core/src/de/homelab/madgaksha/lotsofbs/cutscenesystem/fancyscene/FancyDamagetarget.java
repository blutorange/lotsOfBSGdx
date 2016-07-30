package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyDamagetarget extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(FancyDamagetarget.class);

	private long basePowerMin;
	private long basePowerMax;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeLong(basePowerMin);
		out.writeLong(basePowerMax);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		basePowerMin = in.readLong();
		basePowerMax = in.readLong();
	}

	
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
	public void render(Batch batch) {
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
	public void drawableChanged(EventFancyScene scene, String changedKey) {
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
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
