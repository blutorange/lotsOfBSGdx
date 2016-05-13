package de.homelab.madgaksha.util;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class DebugStringifier {
	private DebugStringifier(){}
	public static String get(Sprite s) {
		final StringBuilder sb = new StringBuilder();
		sb.append("x=").append(s.getX());
		sb.append(";y=").append(s.getY());
		sb.append(";width=").append(s.getWidth());
		sb.append(";height=").append(s.getHeight());
		sb.append(";originX=").append(s.getOriginX());
		sb.append(";originY=").append(s.getOriginY());
		sb.append(";regionX=").append(s.getRegionX());
		sb.append(";regionY=").append(s.getRegionY());
		sb.append(";regionWidth=").append(s.getRegionWidth());
		sb.append(";regionHeight=").append(s.getRegionHeight());
		sb.append(";rotation=").append(s.getRotation());
		sb.append(";scaleX=").append(s.getScaleX());
		sb.append(";scaleY=").append(s.getScaleY());
		sb.append(";u=").append(s.getU());
		sb.append(";v=").append(s.getV());
		sb.append(";u2=").append(s.getU2());
		sb.append(";v2=").append(s.getV2());
		sb.append(";color=").append(s.getColor());
		return sb.toString();
	}
}
