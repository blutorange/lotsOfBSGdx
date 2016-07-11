package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchModel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.Environment;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ModelComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.GeoUtil;

public class ModelRenderSystem extends EntitySystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ModelRenderSystem.class);
	private Family family = null;
	private ImmutableArray<Entity> entities;

	private Environment environment;

	public ModelRenderSystem() {
		this(DefaultPriority.modelRenderSystem);
	}

	@SuppressWarnings("unchecked")
	public ModelRenderSystem(int priority) {
		super(priority);
		this.family = Family.all(ModelComponent.class, PositionComponent.class).exclude(InvisibleComponent.class).get();
		environment = level.getEnvironment();
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix and render models.
		batchModel.begin(viewportGame.getCamera());

		for (int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final ModelComponent mc = Mapper.modelComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final RotationComponent rc = Mapper.rotationComponent.get(entity);
			final ScaleComponent sc = Mapper.scaleComponent.get(entity);
			final BoundingBoxRenderComponent bbrc = Mapper.boundingBoxRenderComponent.get(entity);
			mc.modelInstance.transform.idt();

			// Do not render if off-screen.
			if (bbrc != null && !GeoUtil.boundingBoxVisible(bbrc, pc))
				continue;

			// Set position.
			mc.modelInstance.transform.translate(pc.x + pc.offsetX, pc.y + pc.offsetY, pc.z + pc.offsetZ);

			// Scale if desired.
			if (sc != null)
				mc.modelInstance.transform.scale(sc.scaleX, sc.scaleY, sc.scaleZ);

			// Rotate if desired.
			if (rc != null) {
				mc.modelInstance.transform.rotate(rc.axisX, rc.axisY, rc.axisZ, rc.thetaZ);
			}

			// Render model.
			batchModel.render(mc.modelInstance, environment);
		}
		batchModel.end();
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(family);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entities = null;
	}
}
