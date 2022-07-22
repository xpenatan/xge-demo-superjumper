package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.content.ecs.components.XpeAnimation2DComponent;
import com.xpeengine.content.ecs.components.XpeEntityTransformComponent;
import com.xpeengine.content.ecs.components.XpeGameCameraComponent;
import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.components.BobComponent;
import com.xpeengine.demo.superjumper.ecs.components.MovementComponent;
import com.xpeengine.demo.superjumper.ecs.components.StateComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.core.transform.XpeTransform;
import com.xpenatan.lists.XpeImmutableIntLinkedHashMap;

public class CameraSystem implements XpeEntitySystem {

    XpeImmutableIntLinkedHashMap<XpeEntity> bob;

    XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(XpeGameCameraComponent.class, XpeEntityTransformComponent.class).build();
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();
        XpeObjectMatcher matcher = engine.getMatcherManager().getBuilder().all(BobComponent.class, StateComponent.class, XpeEntityTransformComponent.class,
                MovementComponent.class, XpeAnimation2DComponent.class).build();
        bob = engine.getMatcherManager().getEntities(matcher);
    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        XpeEntityTransformComponent cameraTransformComponent = transformMapper.getComponent(entity);
        XpeTransform cameraTransform = cameraTransformComponent.getTransform();

        if (bob.getSize() == 0) {
            return;
        }

        XpeEntity bobEntity = bob.getHead().getValue();

        XpeEntityTransformComponent bobTarget = transformMapper.getComponent(bobEntity);

        if (bobTarget == null) {
            return;
        }
        XpeTransform bobTransform = bobTarget.getTransform();
        float posY = Math.max(cameraTransform.getY(), bobTransform.getY());
        cameraTransform.setY(posY);
    }
}
