package de.homelab.madgaksha.enums;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public enum EShapeType {
	RECTANGLE,
	CIRCLE,
	ELLIPSE,
	POLYGON,
	POLYLINE;
	
	public static EShapeType valueOf(Shape2D shape) {
		if (shape instanceof Rectangle) return EShapeType.RECTANGLE;
		else if (shape instanceof Circle) return EShapeType.CIRCLE;
		else if (shape instanceof Polygon) return EShapeType.POLYGON;
		else if (shape instanceof Polyline) return EShapeType.POLYLINE;
		else if (shape instanceof Ellipse) return EShapeType.ELLIPSE;
		else return null;
	}
}
