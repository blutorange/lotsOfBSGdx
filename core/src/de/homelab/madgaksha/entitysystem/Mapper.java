package de.homelab.madgaksha.entitysystem;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class Mapper {
    ComponentMapper<de.homelab.madgaksha.entitysystem.component.PositionComponent> PositionComponent = ComponentMapper.getFor(de.homelab.madgaksha.entitysystem.component.PositionComponent.class);
}
