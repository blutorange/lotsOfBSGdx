package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider.TimelineGetter;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProviderChangeListener.TimelineProviderChangeType;


@Deprecated
public class DetailsTitleLabel extends Label {

	public DetailsTitleLabel (TimelineProvider timelineProvider, Skin skin) {
		this(timelineProvider, skin.get(DetailsTitleLabelStyle.class));
	}

	public DetailsTitleLabel (TimelineProvider timelineProvider, Skin skin, String styleName) {
		this(timelineProvider, skin.get(styleName, DetailsTitleLabelStyle.class));
	}
	
	public DetailsTitleLabel(TimelineProvider timelineProvider, DetailsTitleLabelStyle style) {
		super(StringUtils.EMPTY, style);
		timelineProvider.registerChangeListener(TimelineProviderChangeType.SWITCHED, new TimelineProviderChangeListener() {
			@Override
			public void handle(TimelineGetter getter, TimelineProviderChangeType type) {
				connectTimeline(getter);
			}
		});
	}

	private void connectTimeline(TimelineGetter getter) {
		getter.getTimeline().registerChangeListener(TimelineChangeType.SELECTED, new TimelineChangeListener() {			
			@Override
			public void handle(ModelTimeline timeline, TimelineChangeType type) {
				setSelectedTitle(getter.getTimeline().getSelected());
			}
		});
		setSelectedTitle(getter.getTimeline().getSelected());
	}
	
	protected void setSelectedTitle(DetailsPanel<?> panel) {
		setText(panel.getTitle());
		invalidateHierarchy();
	}

	public static class DetailsTitleLabelStyle extends LabelStyle {
		public float fontScale = 1f;
		
		public DetailsTitleLabelStyle() {
			super();
		}
		public DetailsTitleLabelStyle (BitmapFont font, Color fontColor, float fontScale) {
			super(font, fontColor);
			this.fontScale = Math.max(0f, fontScale);
		}
		public DetailsTitleLabelStyle (DetailsTitleLabelStyle style) {
			super(style);
			this.fontScale = Math.max(0f, style.fontScale); 
		}
	}
}
