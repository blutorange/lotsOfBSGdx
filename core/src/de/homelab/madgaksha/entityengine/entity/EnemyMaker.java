package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.GeoUtil;

public abstract class EnemyMaker extends AEntityMaker implements IBehaviour, ITrigger, IReceive {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EnemyMaker.class);
	
	private Rectangle boundingBox;
	private Circle boundingCircle;
	private BoundingBoxComponent boundingBoxComponent = new BoundingBoxComponent();
	
	public EnemyMaker(Shape2D shape, ETrigger trigger) {
		super();
		
		boundingBox = requestedBoundingBox();
		boundingCircle = requestedBoundingCircle();
		
		Component triggerComponent = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		PositionComponent positionComponent = MakerUtils.makePositionAtCenter(shape);
		
		// First the bounding box is what's been set on the map, we change it
		// when the enemy spawns.
		Rectangle r = GeoUtil.getBoundingBox(shape);
		boundingBoxComponent.centerX = r.x;
		boundingBoxComponent.centerY = r.y;
		boundingBoxComponent.halfwidth = r.width*0.5f;
		boundingBoxComponent.halfheight = r.height*0.5f;
				
		add(boundingBoxComponent);	
		add(positionComponent);
		if (triggerComponent != null) add(triggerComponent);
		
		
		
		//TODO remove component when triggered to activate enemy ?
		// this.add(deactivatedComponent / inactiveComponent);
		//TODO
		// Or add a PropertiesToBeAddedLaterComponent?
}

	@Override
	public void reinitializeEntity() {
	}

	@Override
	public void behave() {
		// TODO Auto-generated method stub
	}	
	
	@Override
	public void callbackStartup() {
		// Enemy spawning, add real bounding box.
		boundingBoxComponent.set(boundingBox);
		add(new BoundingSphereComponent(boundingCircle));
		triggeredStartup();
	}
	@Override
	public void callbackTouch(Entity e) {
		// Enemy spawning, add real bounding box.
		boundingBoxComponent.set(boundingBox);
		add(new BoundingSphereComponent(boundingCircle));
		triggeredTouch(e);
	}
	@Override
	public void callbackScreen() {
		// Enemy spawning, add real bounding box.
		boundingBoxComponent.set(boundingBox);
		add(new BoundingSphereComponent(boundingCircle));
		triggeredScreen();
	}
	@Override
	public void callbackTouched(Entity e) {
		// Enemy spawning, add real bounding box.
		boundingBoxComponent.set(boundingBox);
		add(new BoundingSphereComponent(boundingCircle));
		triggeredTouched(e);
		
	}
	
	public Rectangle getBoundingBox() {
		// Enemy spawning, add real bounding box.
		boundingBoxComponent.set(boundingBox);
		add(new BoundingSphereComponent(boundingCircle));
		return boundingBox;
	}
	
	protected abstract Rectangle requestedBoundingBox();
	protected abstract Circle requestedBoundingCircle();
	protected abstract void triggeredStartup();
	protected abstract void triggeredTouch(Entity e);
	protected abstract void triggeredScreen();
	protected abstract void triggeredTouched(Entity e);
}
