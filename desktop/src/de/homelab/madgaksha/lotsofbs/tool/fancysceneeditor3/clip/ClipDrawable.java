package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyShow;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.DrawableEffect;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.DrawableInterpolation;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.DrawableProperty;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.EShadow;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.IFade;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.IMove;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.ISlide;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.IZoom;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.PColor;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.PCrop;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.POpacity;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.POrigin;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.PPosition;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.PScale;

public abstract class ClipDrawable extends AClip {
	private float dpi = 1f;

	private List<IMove> moveList = new LinkedList<>();
	private List<ISlide> slideList = new LinkedList<>();
	private List<IFade> fadeList = new LinkedList<>();
	private List<IZoom> zoomList = new LinkedList<>();

	private List<PPosition> positionList = new LinkedList<>();
	private List<PColor> colorList = new LinkedList<>();
	private List<PCrop> cropList = new LinkedList<>();
	private List<POpacity> opacityList = new LinkedList<>();
	private List<PScale> scaleList = new LinkedList<>();
	private List<POrigin> originList = new LinkedList<>();

	private List<EShadow> shadowList = new LinkedList<>();

	protected abstract void readDrawable(Json json, JsonValue jsonData);

	protected abstract void writeDrawable(Json json);

	protected abstract AFancyEvent getFancyDrawable(float adjustedStartTime, String key, float dpi);

	public void add(final PPosition x) {
		positionList.add(x);
	}
	public void add(final EShadow x) {
		shadowList.add(x);
	}

	@Override
	protected final void writeSubclass(final Json json) {
		json.writeValue("dpi", dpi);
		json.writeValue("colorList", colorList, LinkedList.class, PColor.class);
		json.writeValue("cropList", cropList, LinkedList.class, PCrop.class);
		json.writeValue("fadeList", fadeList, LinkedList.class, IFade.class);
		json.writeValue("moveList", moveList, LinkedList.class, IMove.class);
		json.writeValue("opacityList", opacityList, LinkedList.class, POpacity.class);
		json.writeValue("originList", originList, LinkedList.class, POrigin.class);
		json.writeValue("positionList", positionList, LinkedList.class, PPosition.class);
		json.writeValue("scaleList", scaleList, LinkedList.class, PScale.class);
		json.writeValue("shadowList", shadowList, LinkedList.class, EShadow.class);
		json.writeValue("slideList", slideList, LinkedList.class, ISlide.class);
		json.writeValue("zoomList", zoomList, LinkedList.class, IZoom.class);
		json.writeObjectStart("drawable");
		writeDrawable(json);
		json.writeObjectEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final void readSubclass(final Json json, final JsonValue jsonData) {
		dpi = json.readValue(Float.class, jsonData);
		colorList = json.readValue(LinkedList.class, PColor.class, jsonData);
		cropList = json.readValue(LinkedList.class, PCrop.class, jsonData);
		fadeList = json.readValue(LinkedList.class, IFade.class, jsonData);
		moveList = json.readValue(LinkedList.class, IMove.class, jsonData);
		opacityList = json.readValue(LinkedList.class, POpacity.class, jsonData);
		originList = json.readValue(LinkedList.class, POrigin.class, jsonData);
		positionList = json.readValue(LinkedList.class, PPosition.class, jsonData);
		scaleList = json.readValue(LinkedList.class, PScale.class, jsonData);
		shadowList = json.readValue(LinkedList.class, EShadow.class, jsonData);
		slideList = json.readValue(LinkedList.class, ISlide.class, jsonData);
		zoomList = json.readValue(LinkedList.class, IZoom.class, jsonData);
		readDrawable(json, jsonData);
	}

	public Collection<AFancyEvent> compileJavaWithoutEffects(final String key, final float deltaTime,
			final float adjustedStartTime) throws InsufficientResourcesException {
		final int size = 2 + positionList.size() + colorList.size() + cropList.size() + scaleList.size()
		+ opacityList.size() + moveList.size() + fadeList.size() + zoomList.size() + slideList.size()
		+ originList.size();

		final Collection<AFancyEvent> list = new ArrayList<>(size);

		final int zIndex = getZIndex();

		list.add(new FancyShow(adjustedStartTime, getZIndex(), key, getDuration()));
		list.add(getFancyDrawable(adjustedStartTime, key, dpi));

		ClipDrawable.<PPosition> addProperty(positionList, moveList, key, zIndex, deltaTime, list);
		ClipDrawable.<POpacity> addProperty(opacityList, fadeList, key, zIndex, deltaTime, list);
		ClipDrawable.<PScale> addProperty(scaleList, zoomList, key, zIndex, deltaTime, list);
		ClipDrawable.<PCrop> addProperty(cropList, slideList, key, zIndex, deltaTime, list);

		for (final DrawableProperty p : colorList)
			list.add(p.toFancyEvent(key, zIndex, deltaTime));
		for (final DrawableProperty p : originList)
			list.add(p.toFancyEvent(key, zIndex, deltaTime));

		return list;
	}

	@Override
	public Collection<AFancyEvent> compileJava(final IdProvidable id, final float deltaTime,
			final float adjustedStartTime) throws InsufficientResourcesException {
		final String key = id.uniqueId();
		final Collection<AFancyEvent> list = compileJavaWithoutEffects(key, deltaTime, adjustedStartTime);

		for (final DrawableEffect e : shadowList)
			list.addAll(e.toFancyEventList(key, getZIndex(), this, deltaTime));

		return list;
	}

	private static <T extends DrawableProperty> void addProperty(final List<T> listT,
			final List<? extends DrawableInterpolation<T>> listI, final String key, final int zIndex,
					final float deltaTime, final Collection<AFancyEvent> list) {
		for (final DrawableProperty p : listT)
			list.add(p.toFancyEvent(key, zIndex, deltaTime));
		for (int i = 0; i < listI.size() - 1; ++i) {
			final T p1 = listT.get(i);
			final T p2 = listT.get(i + 1);
			listI.get(i).toFancyEvent(key, zIndex, p1, p2, deltaTime);
		}
	}
}