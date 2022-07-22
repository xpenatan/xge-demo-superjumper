package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.content.ecs.components.XpeEntityTransformComponent;
import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.demo.superjumper.ecs.components.MovementComponent;
import com.xpeengine.demo.superjumper.ecs.components.SquirrelComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.core.transform.XpeTransform;

public class SquirrelSystem implements XpeEntitySystem {

    XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);
    XpeComponentMapper<MovementComponent> movementMapper = XpeComponentMapper.create(MovementComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(SquirrelComponent.class, XpeEntityTransformComponent.class, MovementComponent.class).build();
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {
    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        XpeEntityTransformComponent t = transformMapper.getComponent(entity);
        MovementComponent mov = movementMapper.getComponent(entity);
        XpeTransform transform = t.getTransform();
        if (transform.getX() < SquirrelComponent.WIDTH * 0.5f) {
            transform.setX(SquirrelComponent.WIDTH * 0.5f);
            mov.velocity.x = SquirrelComponent.VELOCITY;
        }
        if (transform.getX() > World.WORLD_WIDTH - SquirrelComponent.WIDTH * 0.5f) {
            transform.setX(World.WORLD_WIDTH - SquirrelComponent.WIDTH * 0.5f);
            mov.velocity.x = -SquirrelComponent.VELOCITY;
        }
        float scale = mov.velocity.x < 0.0f ? Math.abs(transform.getScaleX()) * -1.0f : Math.abs(transform.getScaleX());
        transform.setScaleX(scale);
    }
}