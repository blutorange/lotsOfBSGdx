package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;

public interface TimelineProviderChangeListener {
	public void handle(TimelineGetter getter, TimelineProviderChangeType type);
	public static enum TimelineProviderChangeType {
		SWITCHED;
	}
}
