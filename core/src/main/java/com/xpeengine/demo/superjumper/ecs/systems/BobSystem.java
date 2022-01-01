package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.content.ecs.components.XpeAnimation2DComponent;
import com.xpeengine.content.ecs.components.XpeEntityTransformComponent;
import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.demo.superjumper.ecs.components.BobComponent;
import com.xpeengine.demo.superjumper.ecs.components.MovementComponent;
import com.xpeengine.demo.superjumper.ecs.components.StateComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.core.transform.XpeTransform;

public class BobSystem implements XpeEntitySystem {

    XpeComponentMapper<BobComponent> bobMapper = XpeComponentMapper.create(BobComponent.class);
    XpeComponentMapper<StateComponent> stateMapper = XpeComponentMapper.create(StateComponent.class);
    XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);
    XpeComponentMapper<MovementComponent> movementMapper = XpeComponentMapper.create(MovementComponent.class);
    XpeComponentMapper<XpeAnimation2DComponent> animationMapper = XpeComponentMapper.create(XpeAnimation2DComponent.class);

    private float accelX = 0.0f;

    private XpeObjectMatcher matcher;

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(BobComponent.class, StateComponent.class, XpeEntityTransformComponent.class,
                MovementComponent.class, XpeAnimation2DComponent.class).build();
    }

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    @Override
    public void onEnd(XpeEntitySystemBase systemBase, boolean haveEntities) {
        accelX = 0.0f;
    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        XpeEngine engine = systemBase.getEngine();
        XpeEntityTransformComponent t = transformMapper.getComponent(entity);
        StateComponent state = stateMapper.getComponent(entity);
        MovementComponent mov = movementMapper.getComponent(entity);
        BobComponent bob = bobMapper.getComponent(entity);
        XpeAnimation2DComponent animation = animationMapper.getComponent(entity);

        XpeTransform transform = t.getTransform();

        if (state.get() != BobComponent.STATE_HIT && transform.getPosition().y <= 0.5f) {
            hitPlatform(entity);
        }

        if (state.get() != BobComponent.STATE_HIT) {
            mov.velocity.x = -accelX / 10.0f * BobComponent.MOVE_VELOCITY;
        }

        if (mov.velocity.y > 0 && state.get() != BobComponent.STATE_HIT) {
            if (state.get() != BobComponent.STATE_JUMP) {
                state.set(BobComponent.STATE_JUMP);
            }
        }

        if (mov.velocity.y < 0 && state.get() != BobComponent.STATE_HIT) {
            if (state.get() != BobComponent.STATE_FALL) {
                state.set(BobComponent.STATE_FALL);
            }
        }

        animation.setState(state.get());

        if (transform.getPosition().x < 0) {
            transform.setX(World.WORLD_WIDTH);
        }

        if (transform.getPosition().x > World.WORLD_WIDTH) {
            transform.setX(0);
        }

        transform.setScaleX(mov.velocity.x < 0.0f ? Math.abs(transform.getScaleX()) * -1.0f : Math.abs(transform.getScaleX()));

        bob.heightSoFar = Math.max(transform.getY(), bob.heightSoFar);

        if (bob.heightSoFar - 7.5f > transform.getY()) {
            World world = engine.getManager(World.class);
            world.setState(World.WORLD_STATE_GAME_OVER);
        }
    }

    public void hitSquirrel (XpeEntity entity) {
        if (!matcher.matches(entity, entity.getComponentBits())) return;
        StateComponent state = stateMapper.getComponent(entity);
        MovementComponent mov = movementMapper.getComponent(entity);

        mov.velocity.set(0, 0);
        state.set(BobComponent.STATE_HIT);
    }

    public void hitPlatform (XpeEntity entity) {
        if (!matcher.matches(entity, entity.getComponentBits())) return;

        StateComponent state = stateMapper.getComponent(entity);
        MovementComponent mov = movementMapper.getComponent(entity);

        mov.velocity.y = BobComponent.JUMP_VELOCITY;
        state.set(BobComponent.STATE_JUMP);
    }

    public void hitSpring (XpeEntity entity) {
        if (!matcher.matches(entity, entity.getComponentBits())) return;

        StateComponent state = stateMapper.getComponent(entity);
        MovementComponent mov = movementMapper.getComponent(entity);

        mov.velocity.y = BobComponent.JUMP_VELOCITY * 1.5f;
        state.set(BobComponent.STATE_JUMP);
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {
        matcher = systemBase.getComponentMatcher();
    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }
}
