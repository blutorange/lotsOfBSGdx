package de.homelab.madgaksha.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
	}
	public static String get(NinePatch np) {
		return new StringBuilder()
		.append(np.toString())
		.append("\n  color=").append(np.getColor())
		.append("\n  topHeight=").append(np.getTopHeight())
		.append("\n  bottomHeight=").append(np.getBottomHeight())
		.append("\n  middleHeight=").append(np.getMiddleHeight())
		.append("\n  leftWidth=").append(np.getLeftWidth())
		.append("\n  middleWidth=").append(np.getMiddleWidth())
		.append("\n  rightWidth=").append(np.getRightWidth())
		.append("\n  padTop=").append(np.getPadTop())
		.append("\n  padRight=").append(np.getPadRight())
		.append("\n  padBottom=").append(np.getPadBottom())
		.append("\n  padLeft=").append(np.getPadLeft())
		.append("\n  totalHeight=").append(np.getTotalHeight())
		.append("\n  totalWidth=").append(np.getTotalWidth())
		.toString();
		}
}