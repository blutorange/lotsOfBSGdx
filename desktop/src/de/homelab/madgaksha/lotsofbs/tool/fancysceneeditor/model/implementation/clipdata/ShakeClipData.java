package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.enums.RichterScale;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.IdProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.scene2dext.util.TableUtils;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;
import de.homelab.madgaksha.scene2dext.widget.NumericInput.NumericInputListener;

public class ShakeClipData extends AClipData {
	private static final String LABEL_DURATION = "The duration of the shake in seconds.";
	private static final String LABEL_SHAKE = "The strength of the shake. M1 is the weakest, M10 the strongest.";
	
	private RichterScale richterScale = RichterScale.M5;
	private Table table;

	protected ShakeClipData(ModelClip clip) {
		super(clip);
	}
	
	@Override
	public Actor getActor(TimelineProvider timelineProvider, Skin skin) {
		if (table == null) createTable(skin);
		return table;
	}
	
	private void createTable(Skin skin) {
		table = new Table(skin);
		
		NumericInput niDuration = new NumericInput(1f, skin);
		niDuration.setMinMax(0f, 1f);

		SelectBox<RichterScale> sbRichterScale = new SelectBox<>(skin);
		sbRichterScale.setItems(getRichterScaleArray());
		sbRichterScale.setSelected(richterScale);
	
		TableUtils.labelledActor(table, LABEL_DURATION, niDuration);
		table.row();
		TableUtils.labelledActor(table, LABEL_SHAKE, sbRichterScale);
		
		sbRichterScale.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!(actor instanceof SelectBox)) return;
				final SelectBox<?> selectBox = (SelectBox<?>)actor;
				final Object selected = selectBox.getSelected();
				if (!(selected instanceof RichterScale)) return;
				final RichterScale rs = (RichterScale)selected;
				richterScale = rs;
				getClip().invalidateClipData();
			}
		});
		
		niDuration.addListener(new NumericInputListener(){
			@Override
			protected void changed(float value, Actor actor) {
								
			}
		});
	}

	@Override
	public String getTitle() {
		return "Shake";
	}
	
	private RichterScale[] getRichterScaleArray() {
		return RichterScale.class.getEnumConstants();
	}

	@Override
	public String getDescription() {
		return "A shake moves the camera at random to produce a shaking effect.";
	}

	// Shake A 0.300 M3 0.25
	@Override
	public void performRenderEvent(StringBuilder builder, IdProvider idProvider) {
		builder.append("Shake A ").append(getStartTime()).append(" ").append(richterScale.toString()).append(" ").append(getDuration());
	}

}
