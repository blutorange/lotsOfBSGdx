package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface RenderableTimeline extends TimeInterval, Seekable {
	/**
	 * Sets the time at which the timeline should be drawn. When this function
	 * is called, it must behave as if #setDrawAtCurrentTime(false) had been
	 * called immediately before this function.
	 */
	public void seek(float time);

	/**
	 * When set to true, {@link #draw(Batch)} must behave as if
	 * seek(getCurrentTime()) had been called immediately before draw.
	 * 
	 * @param drawAtCurrentTime
	 */
	public void setDrawAtCurrentTime(boolean drawAtCurrentTime);

	/**
	 * Called when the timeline needs to drawn. Must draw the timeline at the
	 * {@link #getCurrentTime()}.
	 */
	public void draw(Batch batch);

	/**
	 * Sets the area, ie. the coordinates, at which the timeline should be
	 * drawn.
	 * 
	 * @param area
	 *            The rectangular area at which the timeline should be drawn.
	 */
	public void layout(Rectangle area);
		
	/**
	 * Called when {@link #draw(Batch)} will not be called anymore.
	 * Can be used to cleanup resources etc.
	 */
	public void close();
}
