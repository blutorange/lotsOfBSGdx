package de.homelab.madgaksha.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DebugStringifier {
	private DebugStringifier(){}
	public static String get(Sprite s) {
		return new StringBuilder()
		.append(s.toString())
		.append("\n  x=").append(s.getX())
		.append("\n  y=").append(s.getY())
		.append("\n  width=").append(s.getWidth())
		.append("\n  height=").append(s.getHeight())
		.append("\n  originX=").append(s.getOriginX())
		.append("\n  originY=").append(s.getOriginY())
		.append("\n  regionX=").append(s.getRegionX())
		.append("\n  regionY=").append(s.getRegionY())
		.append("\n  regionWidth=").append(s.getRegionWidth())
		.append("\n  regionHeight=").append(s.getRegionHeight())
		.append("\n  rotation=").append(s.getRotation())
		.append("\n  scaleX=").append(s.getScaleX())
		.append("\n  scaleY=").append(s.getScaleY())
		.append("\n  u=").append(s.getU())
		.append("\n  v=").append(s.getV())
		.append("\n  u2=").append(s.getU2())
		.append("\n  v2=").append(s.getV2())
		.append("\n  color=").append(s.getColor())
		.toString();
	}
	public static String get(Camera c) {
		return new StringBuilder()
		.append(c.toString())
		.append("\n  position=").append(c.position)
		.append("\n  direction=").append(c.direction)
		.append("\n  up=").append(c.up)
		.append("\n  width=").append(c.viewportWidth)
		.append("\n  height=").append(c.viewportHeight)
		.append("\n  near=").append(c.near)
		.append("\n  far=").append(c.far)
		.toString();
	}
	public static String get(PerspectiveCamera c) {
		return new StringBuilder()
		.append(c.toString())
		.append("\n  position=").append(c.position)
		.append("\n  direction=").append(c.direction)
		.append("\n  up=").append(c.up)
		.append("\n  width=").append(c.viewportWidth)
		.append("\n  height=").append(c.viewportHeight)
		.append("\n  near=").append(c.near)
		.append("\n  far=").append(c.far)
		.append("\n  fieldOfView=").append(c.fieldOfView)
		.toString();
	}}
