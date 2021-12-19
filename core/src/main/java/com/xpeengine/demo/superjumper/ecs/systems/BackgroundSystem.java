package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.components.BackgroundComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeSystem;
import com.xpeengine.core.ecs.systems.XpeSystemBase;
import com.xpeengine.core.transform.XpeTransform;
import com.xpeengine.core.content.ecs.components.XpeEntityTransformComponent;
import com.xpeengine.core.content.ecs.components.XpeGameCameraComponent;
import com.xpenatan.lists.XpeImmutableIntLinkedHashMap;
import com.xpenatan.lists.nodes.XpeIntLinkedHashMapNode;

public class BackgroundSystem implements XpeSystem {

    XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);

    XpeImmutableIntLinkedHashMap<XpeEntity> backgroundList;
    XpeImmutableIntLinkedHashMap<XpeEntity> cameraList;

    @Override
    public void onAttach(XpeSystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();
        XpeObjectMatcher backgroundMatcher = engine.getMatcherManager().getBuilder().all(BackgroundComponent.class, XpeEntityTransformComponent.class).build();
        XpeObjectMatcher cameraMatcher = engine.getMatcherManager().getBuilder().all(XpeGameCameraComponent.class, XpeEntityTransformComponent.class).build();

        backgroundList = engine.getMatcherManager().getEntities(backgroundMatcher);
        cameraList = engine.getMatcherManager().getEntities(cameraMatcher);
    }

    @Override
    public void onDetach(XpeSystemBase xpeSystemBase) {
    }

    @Override
    public void onProcess(XpeSystemBase xpeSystemBase) {
        XpeIntLinkedHashMapNode<XpeEntity> backgroundCur = backgroundList.getHead();
        XpeIntLinkedHashMapNode<XpeEntity> cameraCur = cameraList.getHead();
        if(backgroundCur != null && cameraCur != null) {
            XpeEntity backgroundEntity = backgroundCur.getValue();
            XpeEntity cameraEntity = cameraCur.getValue();

            XpeEntityTransformComponent backGroundTransformC = transformMapper.getComponent(backgroundEntity);
            XpeEntityTransformComponent cameraTransformC = transformMapper.getComponent(cameraEntity);

            XpeTransform transform = backGroundTransformC.getTransform();
            XpeTransform cameraTransform = cameraTransformC.getTransform();

            transform.setPosition(cameraTransform.getX(), cameraTransform.getY(), 0);
        }
    }
}