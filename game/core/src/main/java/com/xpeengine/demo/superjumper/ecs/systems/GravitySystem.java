package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.demo.superjumper.ecs.components.GravityComponent;
import com.xpeengine.demo.superjumper.ecs.components.MovementComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;

public class GravitySystem implements XpeEntitySystem {
    private final XpeComponentMapper<MovementComponent> movementMapper = XpeComponentMapper.create(MovementComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(GravityComponent.class, MovementComponent.class).build();
    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        XpeEngine engine = systemBase.getEngine();
        MovementComponent mov = movementMapper.getComponent(entity);
        float deltaTime = engine.getDeltaTime();
        mov.velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
    }

    @Override
    public void onAttach(XpeEntitySystemBase xpeEntitySystemBase) {

    }

    @Override
    public void onDetach(XpeEntitySystemBase xpeEntitySystemBase) {

    }
}
