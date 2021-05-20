package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.demo.superjumper.ecs.components.MovementComponent;
import com.xpeengine.demo.superjumper.ecs.components.PlatformComponent;
import com.xpeengine.demo.superjumper.ecs.components.StateComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.core.transform.XpeTransform;
import com.xpeengine.core.content.ecs.components.XpeAnimation2DComponent;
import com.xpeengine.core.content.ecs.components.XpeEntityTransformComponent;

public class PlatformSystem implements XpeEntitySystem {

    XpeObjectMatcher matcher;

    XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);
    XpeComponentMapper<PlatformComponent> platformMapper = XpeComponentMapper.create(PlatformComponent.class);
    XpeComponentMapper<MovementComponent> movementMapper = XpeComponentMapper.create(MovementComponent.class);
    XpeComponentMapper<StateComponent> stateMapper = XpeComponentMapper.create(StateComponent.class);
    XpeComponentMapper<XpeAnimation2DComponent> animationMapper = XpeComponentMapper.create(XpeAnimation2DComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(PlatformComponent.class, XpeEntityTransformComponent.class, MovementComponent.class,
                StateComponent.class, XpeAnimation2DComponent.class).build();
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {
        matcher = systemBase.getComponentMatcher();
    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        PlatformComponent platform = platformMapper.getComponent(entity);
        XpeAnimation2DComponent animation = animationMapper.getComponent(entity);

        if (platform.type == PlatformComponent.TYPE_MOVING) {
            XpeEntityTransformComponent pos = transformMapper.getComponent(entity);
            MovementComponent mov = movementMapper.getComponent(entity);
            XpeTransform transform = pos.getTransform();

            if (transform.getX() < PlatformComponent.WIDTH / 2) {
                mov.velocity.x = -mov.velocity.x;
                transform.setX(PlatformComponent.WIDTH / 2);
            }
            if (transform.getX() > World.WORLD_WIDTH - PlatformComponent.WIDTH / 2) {
                mov.velocity.x = -mov.velocity.x;
                transform.setX(World.WORLD_WIDTH - PlatformComponent.WIDTH / 2);
            }
        }

        StateComponent state = stateMapper.getComponent(entity);

        if (state.get() == PlatformComponent.STATE_PULVERIZING &&
                state.time > PlatformComponent.PULVERIZE_TIME) {

            systemBase.getEngine().getEntityManager().deleteEntity(entity);
        }

        animation.setState(state.get());
        animation.setTime(state.time);
    }

    public void pulverize (XpeEntity entity) {
        if (matcher.matches(entity, entity.getComponentBits())) {
            StateComponent state = stateMapper.getComponent(entity);
            MovementComponent mov = movementMapper.getComponent(entity);

            state.set(PlatformComponent.STATE_PULVERIZING);
            mov.velocity.x = 0;
        }
    }
}
