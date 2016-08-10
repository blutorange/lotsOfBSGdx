package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;

public interface TimelineProvider extends ChangeListenable<TimelineProviderChangeType, TimelineProviderChangeListener>{	
	public static interface TimelineGetter {
		public ModelTimeline getTimeline();		
	}
}
