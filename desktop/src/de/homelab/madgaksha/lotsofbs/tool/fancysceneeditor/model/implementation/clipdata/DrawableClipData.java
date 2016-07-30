package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata;

import java.util.Arrays;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.comparator.ComparatorEnum;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.IdProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.scene2dext.util.TableUtils;

public class DrawableClipData extends AClipData {
	private final static String LABEL_TYPE = "Type of the drawable.";
	private final static String LABEL_RESOURCE = "The name of the resource to use for the drawable.";
	
	private SelectBox<Enum<?>> sbResource;
	private Type type = Type.SPRITE;
	private float dpi;
	private Enum<?> resource;
	private Table table;
	
	@Override
	public String getTitle() {
		return "Drawable";
	}

	@Override
	public Actor getActor(TimelineProvider timelineProvider, Skin skin) {
		if (table == null) createTable(skin);
		return table;
	}
	
	private void createTable(Skin skin) {
		table = new Table(skin);
		
		final SelectBox<Type> sbType = new SelectBox<>(skin);
		final Type[] typeList = Type.class.getEnumConstants();
		Arrays.sort(typeList, ComparatorEnum.NAME_ASCENDING);
		sbType.setItems(typeList);
		sbType.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!(actor instanceof SelectBox<?>)) return;
				final SelectBox<?> selectBox = (SelectBox<?>)actor;
				final Object selected = selectBox.getSelected();
				if (!(selected instanceof Type)) return;
				final Type t = (Type)selected;
				final boolean hasChanged = t != type;
				type = t;
				if (hasChanged) setupOptionsResource();
			}
		});
		
		sbResource = new SelectBox<>(skin);
		sbResource.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!(actor instanceof SelectBox<?>)) return;
				final SelectBox<?> selectBox = (SelectBox<?>)actor;
				final Object selected = selectBox.getSelected();
				if (!(selected instanceof Enum<?>)) return;
				resource = (Enum<?>)selected;
			}
		});

		setupOptionsResource();
		
		TableUtils.labelledActor(table, LABEL_TYPE, sbType);
		table.row();
		TableUtils.labelledActor(table, LABEL_RESOURCE, sbResource);
		
		sbType.invalidateHierarchy();
	}

	private void setupOptionsResource() {
		sbResource.clearItems();
		final Enum<?>[] resourceList = type.getEnumList();
		Arrays.sort(resourceList, ComparatorEnum.NAME_ASCENDING);
		sbResource.setItems(resourceList);
		sbResource.invalidateHierarchy();
	}

	@Override
	public String getDescription() {
		return "A drawable entity has got a drawable, which can be an image, an animation, a nine patch, or a particle effect. Each drawable possess various propertoes such as position, scale, size, and rotation that can be freely set and animated. The drawable entity allows for the drawable to be changed, which can be used, for example, to create custom sprite animations.";
	}

	// Sprite   A 0.000 Estelle 32.0 OUGI_OUKA_MUSOUGEKI_JUMP3
	@Override
	public void renderEvent(StringBuilder builder, IdProvider idProvider) throws InsufficientResourcesException {
		final String id = idProvider.unique();
		builder.append(type.label).append(" ").append(getStartTime()).append(" ").append(id).append(" ").append(dpi).append(" ").append(resource);
	}

	private static enum Type {
		NINE_PATCH("Ninepatch") {
			@Override
			public Enum<?>[] getEnumList() {
				return ENinePatch.class.getEnumConstants();
			}
		},
		SPRITE("Sprite") {
			@Override
			public Enum<?>[] getEnumList() {
				return ETexture.class.getEnumConstants();
			}
		},
		ANIMATION("Animation") {
			@Override
			public Enum<?>[] getEnumList() {
				return EAnimation.class.getEnumConstants();
			}
		},
		PEFFECT("Peffect") {
			@Override
			public Enum<?>[] getEnumList() {
				return EParticleEffect.class.getEnumConstants();
			}
		};
		private final String label;
		private Type(String label) {
			this.label = label;
		}
		public abstract Enum<?>[] getEnumList();
	}	
}
