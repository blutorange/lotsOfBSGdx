package entitysystem;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Disposable;

import de.homelab.madgaksha.logging.Logger;

/**
 * Based on github.com/libgdx/ashley/issues/179
 * @author mad_gaksha
 *
 */
public class DisposableEngine extends Engine {
	private final static Logger LOG = Logger.getLogger(DisposableEngine.class);
	public void dispose() {
		final ImmutableArray<Entity> entitities = getEntities();
		for (Entity e : entitities) {
			final ImmutableArray<Component> components = e.getComponents();
			if (e instanceof Disposable) { 
				final Disposable disposable = (Disposable)e;
				disposable.dispose();
				LOG.debug("disposed entity " + e.toString());
			}
			for (Component component : components) {
				if (!(component instanceof Disposable)) continue;
				final Disposable disposable = (Disposable)component;
				disposable.dispose();
				LOG.debug("disposed component " + component.toString() + ", entity " + e.toString());
			}
		}
	}	
}
