package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.GeoUtil;

public final class MakerUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MakerUtils.class);
	private MakerUtils(){}

	public static BoundingBoxComponent makeBoundingBox(Shape2D shape) {
		return new BoundingBoxComponent(GeoUtil.getBoundingBox(shape));
	}

	public static PositionComponent makePositionAtCenter(Shape2D shape) {
		final PositionComponent pc = new PositionComponent();
		if (shape instanceof Rectangle) {
			Rectangle r = (Rectangle)shape;
			pc.x = r.x + 0.5f * r.width;
			pc.y = r.y + 0.5f * r.height;
		}
		else if (shape instanceof Circle) {
			pc.x = ((Circle)shape).x;
			pc.y = ((Circle)shape).y;
		}
		else if (shape instanceof Ellipse) {
			Ellipse e = (Ellipse)shape;
			pc.x = e.x + 0.5f * e.width;
			pc.y = e.y + 0.5f * e.height;
		}
		else if (shape instanceof Polygon) {
			Polygon p = (Polygon)shape;
			Rectangle r = p.getBoundingRectangle();
			pc.x = r.x + 0.5f * r.width;
			pc.y = r.y + 0.5f * r.height;
		}
		else if (shape instanceof Polyline) {
			Polyline pl = (Polyline)shape;
			Polygon pp = new Polygon(pl.getVertices());
			Rectangle r = pp.getBoundingRectangle();
			pc.x = r.x + 0.5f * r.width;
			pc.y = r.y + 0.5f * r.height;
		}
		return pc;
	}

	public static Component makeTrigger(ITrigger triggerAcceptingObject, IReceive triggerReceivingObject, ETrigger trigger, ECollisionGroup group) {
		switch (trigger) {
		case MANUAL:
			return null;
		case SCREEN:		
			return new TriggerScreenComponent(triggerAcceptingObject);
		case STARTUP:
			return new TriggerStartupComponent(triggerAcceptingObject);
		case TOUCH:
			final ReceiveTouchComponent ttc = makeReceiveTouch(group, triggerReceivingObject);
			return ttc;
		default:
			return null;		
		}
	}
	
	public static ReceiveTouchComponent makeReceiveTouch(ECollisionGroup group, IReceive triggerReceivingObject) {
		switch (group) {
		case GROUP_01:
			return new ReceiveTouchGroup01Component(triggerReceivingObject);
		case GROUP_02:
			return new ReceiveTouchGroup02Component(triggerReceivingObject);
		case GROUP_03:
			return new ReceiveTouchGroup03Component(triggerReceivingObject);
		case GROUP_04:
			return new ReceiveTouchGroup04Component(triggerReceivingObject);
		case GROUP_05:
			return new ReceiveTouchGroup05Component(triggerReceivingObject);
		default:
			return new ReceiveTouchGroup05Component(triggerReceivingObject);
		}
	}
}
