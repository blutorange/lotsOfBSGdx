package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;

/**
 * A component for entities always positioned at a certain offset to another
 * entity.
 * 
 * @author madgaksha
 *
 */
public class StickyComponent implements Component, Poolable {

	private final static float DEFAULT_OFFSET_X = 0.0f;
	private final static float DEFAULT_OFFSET_Y = 0.0f;
	private final static boolean DEFAULT_IGNORE_TRACK_OFFSET = false;
	private final static boolean DEFAULT_OFFSET_RELATIVE_TO_CAMERA = false;

	public PositionComponent stickToPositionComponent = null;
	public float offsetX = DEFAULT_OFFSET_X;
	public float offsetY = DEFAULT_OFFSET_Y;
	/** Ignores offset of the position component of the entity being tracked. */
	public boolean ignoreTrackOffset = DEFAULT_IGNORE_TRACK_OFFSET;
	/**
	 * Whether the offset specified should be relative to the camera looking
	 * direction.
	 */
	public boolean offsetRelativeToCamera = DEFAULT_OFFSET_RELATIVE_TO_CAMERA;

	public StickyComponent() {
	}

	public StickyComponent(PositionComponent pc) {
		setup(pc);
	}

	public StickyComponent(PositionComponent pc, boolean ignoreOffset) {
		setup(pc, ignoreOffset);
	}

	public StickyComponent(Entity stickTo, float offsetX, float offsetY) {
		setup(stickTo, offsetX, offsetY);
	}

	public StickyComponent(Entity stickTo, float offsetX, float offsetY, boolean ignoreOffset) {
		setup(stickTo, offsetX, offsetY, ignoreOffset);
	}

	public StickyComponent(Entity stickTo, float offsetX, float offsetY, boolean ignoreOffset,
			boolean offsetRelativeToCamera) {
		setup(stickTo, offsetX, offsetY, ignoreOffset, offsetRelativeToCamera);
	}

	public StickyComponent(Entity stickTo) {
		setup(stickTo);
	}

	public void setup(Entity e) {
		setup(e, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_IGNORE_TRACK_OFFSET, DEFAULT_OFFSET_RELATIVE_TO_CAMERA);
	}

	public void setup(PositionComponent pc) {
		setup(pc, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_IGNORE_TRACK_OFFSET, DEFAULT_IGNORE_TRACK_OFFSET);
	}

	public void setup(PositionComponent pc, boolean ignoreOffset) {
		setup(pc, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, ignoreOffset, DEFAULT_OFFSET_RELATIVE_TO_CAMERA);
	}

	public void setup(Entity e, float offsetX, float offsetY) {
		setup(e, offsetX, offsetY, DEFAULT_IGNORE_TRACK_OFFSET, DEFAULT_OFFSET_RELATIVE_TO_CAMERA);
	}

	public void setup(Entity e, float offsetX, float offsetY, boolean ignoreOffset) {
		setup(e, offsetX, offsetY, ignoreOffset, DEFAULT_OFFSET_RELATIVE_TO_CAMERA);
	}

	public void setup(Entity e, float offsetX, float offsetY, boolean ignoreOffset, boolean offsetRelativeToCamera) {
		final PositionComponent pc = Mapper.positionComponent.get(e);
		if (pc != null)
			setup(pc, offsetX, offsetY, ignoreOffset, offsetRelativeToCamera);
	}

	public void setup(PositionComponent pc, float offsetX, float offsetY) {
		setup(pc, offsetX, offsetY, DEFAULT_IGNORE_TRACK_OFFSET, DEFAULT_OFFSET_RELATIVE_TO_CAMERA);
	}

	public void setup(PositionComponent pc, float offsetX, float offsetY, boolean ignoreOffset,
			boolean offsetRelativeToCamera) {
		this.stickToPositionComponent = pc;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.ignoreTrackOffset = ignoreOffset;
		this.offsetRelativeToCamera = offsetRelativeToCamera;
	}

	@Override
	public void reset() {
		offsetX = DEFAULT_OFFSET_X;
		offsetY = DEFAULT_OFFSET_Y;
		stickToPositionComponent = null;
	}
}
