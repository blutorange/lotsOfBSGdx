package de.homelab.madgaksha.layer;
import static de.homelab.madgaksha.GlobalBag.game;
public abstract class ALayer {
	public final static int CONTINUE_NORMALLY = 0;
	public final static int BLOCK_UPDATE = 1;
	public final static int BLOCK_DRAW = 2;
	public final static int PROCESSING_DONE = 4;
	
	final static int BLOCK_RENDER_AND_UPDATE = BLOCK_UPDATE | BLOCK_DRAW;
	
	/** Called when this layer should be drawn.
	 * @param deltaTime Time difference since the last frame in seconds.
	 */
	public abstract void draw(float deltaTime);
	
	/** Called when this layer should be drawn and updated.
	 * @param deltaTime Time difference since the last frame in seconds.
	 * @return Whether and how layer deeper on the stack should be processed.
	 */
	public abstract void update(float deltaTime);
	
	/** Called when this layer is removed from the layer stack. */
	public abstract void removedFromStack();
	
	/** Called when this layer is added to the layer stack. */
	public abstract void addedToStack();

	/** @return Whether this layer is currently blocking drawing 
	 * operations from propagating down the stack. */
	public abstract boolean isBlockDraw();
	
	/** @return Whether this layer is currently blocking update 
	 * operations from propagating down the stack. */
	public abstract boolean isBlockUpdate();
	
	protected void removeSelf() {
		game.popLayer(this);
	}
}
