package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyCrop extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyCrop.class);

	private String key = StringUtils.EMPTY;
	private Vector2 cropX = new Vector2(1.0f, 1.0f);
	private Vector2 cropY = new Vector2(1.0f, 1.0f);

	public FancyCrop(String key, float cropLeft, float cropBottom, float cropRight, float cropTop) {
		super(true);
		this.key = key;
		this.cropX.set(cropLeft, cropRight);
		this.cropY.set(cropBottom, cropTop);
	}

	@Override
	public void reset() {
		cropX.set(1.0f, 1.0f);
		cropY.set(1.0f, 1.0f);
		key = StringUtils.EMPTY;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.getDrawable(key).setCrop(cropX, cropY);
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

		Float cropLeft = FileCutsceneProvider.nextNumber(s);
		if (cropLeft == null) {
			LOG.error("expected crop left");
			return null;
		}

		Float cropBottom = FileCutsceneProvider.nextNumber(s);
		if (cropBottom == null) {
			LOG.error("expected crop bottom");
			return null;
		}

		Float cropRight = FileCutsceneProvider.nextNumber(s);
		if (cropRight == null)
			cropRight = cropLeft;

		Float cropTop = FileCutsceneProvider.nextNumber(s);
		if (cropTop == null)
			cropTop = cropBottom;

		return new FancyCrop(key, cropLeft, cropBottom, cropRight, cropTop);
	}

}
