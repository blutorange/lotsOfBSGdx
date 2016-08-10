package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata;

import java.util.Arrays;

import javax.naming.InsufficientResourcesException;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.comparator.ComparatorEnum;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.IdProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty.PropertyCrop;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty.PropertyOpacity;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty.PropertyPosition;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty.PropertyScale;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.DrawablePropertyEditor;
import de.homelab.madgaksha.scene2dext.listener.ButtonListener.ButtonAdapter;
import de.homelab.madgaksha.scene2dext.util.TableUtils;
import de.homelab.madgaksha.scene2dext.widget.NumericInput;
import de.homelab.madgaksha.scene2dext.widget.NumericInput.NumericInputListener;

public class DrawableClipData extends AClipData {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DrawableClipData.class);

	private final static String LABEL_TYPE = "Type of the drawable.";
	private final static String LABEL_RESOURCE = "The name of the resource to use for the drawable.";
	private final static String LABEL_DPI = "Resolution of the drawable, units per pixel.";
	private final static String LABEL_Z_ORDER = "Elevation of the drawable. The higher, the closer to the viewer.";
	private final static String LABEL_PROPERTIES = "Properties";

	private final ModelDrawableProperty<?> drawablePropertyList[];

	private int zOrder = 1;
	private float dpi = 32f;
	private Type type = Type.SPRITE;
	private Enum<?> resource = ETexture.DEFAULT;

	private Table tablePropertyButtons;
	private SelectBox<Enum<?>> sbResource;
	private SelectBox<Type> sbType;
	private NumericInput niDpi;
	private NumericInput niZOrder;
	private Table table;
	private Skin oldSkin = new Skin();

	private DrawablePropertyEditor drawablePropertyEditor;

	protected DrawableClipData(final ModelClip clip) {
		super(clip);
		drawablePropertyList = new ModelDrawableProperty[]{
				new PropertyPosition(),
				new PropertyOpacity(),
				new PropertyScale(),
				new PropertyCrop(),
		};
	}

	@Override
	public String getTitle() {
		return "Drawable";
	}

	@Override
	public Actor getActor(final TimelineProvider timelineProvider, final Skin skin) {
		if (table == null)
			createElements(skin);
		if (!((Object) oldSkin).equals(skin)) {
			layout(skin);
			oldSkin = skin;
		}
		return table;
	}

	private void createElements(final Skin skin) {
		final Enum<?> initialResource = resource;
		table = new Table(skin);

		sbType = new SelectBox<>(skin);
		final Type[] typeList = Type.values();
		Arrays.sort(typeList, ComparatorEnum.NAME_ASCENDING);
		sbType.setItems(typeList);

		sbType.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if (!(actor instanceof SelectBox<?>))
					return;
				final SelectBox<?> sb = (SelectBox<?>) actor;
				final Object s = sb.getSelected();
				if (!(s instanceof Type))
					return;
				type = (Type) s;
				setupOptionsResource();
			}
		});

		sbResource = new SelectBox<>(skin);
		sbResource.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if (!(actor instanceof SelectBox<?>))
					return;
				final SelectBox<?> sb = (SelectBox<?>) actor;
				final Object s = sb.getSelected();
				if (!(s instanceof Enum<?>))
					return;
				resource = (Enum<?>) s;
				getClip().invalidateClipData();
			}
		});

		niZOrder = new NumericInput(1, skin);
		niZOrder.setFormat("%.0f");
		niZOrder.setMinMax(0, 1000);
		niZOrder.setStep(1);
		niZOrder.addListener(new NumericInputListener() {
			@Override
			protected void changed(final float value, final Actor actor) {
				zOrder = MathUtils.round(value);
				getClip().invalidateClipData();
			}
		});

		niDpi = new NumericInput(1f, skin);
		niDpi.setFormat("%.1f");
		niDpi.setStep(0.1f);
		niDpi.setMinMax(0.1f, 400f);
		niDpi.addListener(new NumericInputListener() {
			@Override
			protected void changed(final float value, final Actor actor) {
				dpi = value;
				getClip().invalidateClipData();
			}
		});

		tablePropertyButtons = new Table(skin);
		for (final ModelDrawableProperty<?> modelProperty : drawablePropertyList) {
			final TextButton buttonProperty = new TextButton(modelProperty.getName(), skin, "toggle");
			tablePropertyButtons.add(buttonProperty).fillX().expandX();
			buttonProperty.addListener(new ButtonAdapter() {
				@Override
				public void checked(final Button button) {
					for (final Cell<?> c : tablePropertyButtons.getCells()) {
						final Object a = c.getActor();
						if (a instanceof Button && !a.equals(button)) ((Button)a).setChecked(false);
					}
					modelProperty.setClipData(DrawableClipData.this);
					drawablePropertyEditor.setModel(modelProperty);
				}
			});
		}

		drawablePropertyEditor = new DrawablePropertyEditor(skin);

		// Default options.
		sbType.setSelected(type);
		sbResource.setSelected(initialResource);
		niDpi.setValue(dpi);
		niZOrder.setValue(1);
		((Button)tablePropertyButtons.getCells().get(0).getActor()).setChecked(true);
	}

	private void layout(final Skin skin) {
		table.setSkin(skin);
		sbType.setStyle(skin.get(SelectBoxStyle.class));
		sbResource.setStyle(skin.get(SelectBoxStyle.class));
		niDpi.setStyle(skin.get(TextFieldStyle.class));
		table.clearChildren();
		TableUtils.labelledActor(table, LABEL_TYPE, sbType);
		table.row();
		TableUtils.labelledActor(table, LABEL_RESOURCE, sbResource);
		table.row();
		TableUtils.labelledActor(table, LABEL_DPI, niDpi);
		table.row();
		TableUtils.labelledActor(table, LABEL_Z_ORDER, niZOrder);
		table.row();
		TableUtils.heading(table, LABEL_PROPERTIES, skin).colspan(2);
		table.row();
		table.add(tablePropertyButtons).fillX().colspan(2);
		table.row();
		table.add(drawablePropertyEditor).fillX().colspan(2);

		table.invalidate();
		niDpi.invalidate();
		sbResource.invalidate();
		sbType.invalidate();
	}

	private void setupOptionsResource() {
		sbResource.clearItems();
		final Enum<?>[] resourceList = sbType.getSelected().getEnumList();
		Arrays.sort(resourceList, ComparatorEnum.NAME_ASCENDING);
		sbResource.setItems(resourceList);
		getClip().invalidateClipData();
		resource = sbResource.getSelected();
		sbResource.invalidateHierarchy();
	}

	@Override
	public String getDescription() {
		return "A drawable entity has got a drawable, which can be an image, an animation, a nine patch, or a particle effect. Each drawable possess various propertoes such as position, scale, size, and rotation that can be freely set and animated. The drawable entity allows for the drawable to be changed, which can be used, for example, to create custom sprite animations.";
	}

	// Sprite A 0.000 Estelle 32.0 OUGI_OUKA_MUSOUGEKI_JUMP3
	// Show 11 A 0.425 SimpleHit 1.0
	@Override
	public void performRenderEvent(final StringBuilder builder, final IdProvider idProvider) throws InsufficientResourcesException {
		final String id = idProvider.uniqueId();
		builder.append(type.label).append(" A ").append(getStartTime()).append(StringUtils.SPACE).append(id)
		.append(StringUtils.SPACE).append(dpi).append(StringUtils.SPACE).append(resource.name());
		builder.append(StringUtils.LF);
		builder.append("Show ").append(zOrder).append(" A ").append(getStartTime()).append(StringUtils.SPACE).append(id).append(StringUtils.SPACE)
		.append(getDuration());
	}

	private static enum Type {
		NINE_PATCH("Ninepatch") {
			@Override
			public Enum<?>[] getEnumList() {
				return ENinePatch.values();
			}
		},
		SPRITE("Sprite") {
			@Override
			public Enum<?>[] getEnumList() {
				return ETexture.values();
			}
		},
		ANIMATION("Animation") {
			@Override
			public Enum<?>[] getEnumList() {
				return EAnimation.values();
			}
		},
		PEFFECT("Peffect") {
			@Override
			public Enum<?>[] getEnumList() {
				return EParticleEffect.values();
			}
		};
		private final String label;

		private Type(final String label) {
			this.label = label;
		}

		public abstract Enum<?>[] getEnumList();
	}
}
