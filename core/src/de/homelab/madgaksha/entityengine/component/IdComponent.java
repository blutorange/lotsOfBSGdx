package de.homelab.madgaksha.entityengine.component;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Component;

/**
 * Entities with a unique id. Used for cutscenes etc. Should not be used for
 * performance critical code. IDs must consist only of lower-case letter a-z and
 * number 0-9. User-provided IDs are converted to lower-case automatically. <br>
 * <br>
 * IDs are read in as is, they are not checked for uniqueness. If two map
 * objects have got the same ID, the one that is processed when the map is read
 * later will overwrite the first. The order in which they are read is
 * undefined. <br>
 * <br>
 * Furthermore, certain names are reserved for system entities. Again, these IDs
 * should only be used for scripting etc. and not by entity systems.
 * 
 * <ul>
 * <li>player The player entity</li>
 * <li>camera The camera entity.</li>
 * <li>TBC</li>
 * </ul>
 * 
 * @author madgaksha
 */
public class IdComponent implements Component {

	private String id = StringUtils.EMPTY;

	public IdComponent() {
		throw new UnsupportedOperationException();
	}

	public IdComponent(String id) {
		if (id == null)
			id = StringUtils.EMPTY;
		else
			id = id.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "IdComponent(" + id + ")";
	}
}
