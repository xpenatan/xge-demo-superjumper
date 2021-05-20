package com.xpeengine.demo.superjumper.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.components.MovementComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.ecs.components.XpeEntityTransformComponent;

public class MovementSystem implements XpeEntitySystem {
    private final XpeComponentMapper<MovementComponent> movementMapper = XpeComponentMapper.create(MovementComponent.class);
    private final XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);

    private final Vector2 tmp = new Vector2();

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(XpeEntityTransformComponent.class, MovementComponent.class).build();
    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        XpeEngine engine = systemBase.getEngine();
        XpeEntityTransformComponent pos = transformMapper.getComponent(entity);
        MovementComponent mov = movementMapper.getComponent(entity);
        float deltaTime = engine.getDeltaTime();

        tmp.set(mov.accel).scl(deltaTime);
        mov.velocity.add(tmp);

        tmp.set(mov.velocity).scl(deltaTime);
        pos.getTransform().addPosition(tmp.x, tmp.y, 0.0f);
    }

    @Override
    public void onAttach(XpeEntitySystemBase xpeEntitySystemBase) {

    }

    @Override
    public void onDetach(XpeEntitySystemBase xpeEntitySystemBase) {

    }
}
