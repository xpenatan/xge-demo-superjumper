package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.content.ecs.components.XpeEntityTransformComponent;
import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.components.BoundsComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.core.transform.XpeTransform;

public class BoundsSystem implements XpeEntitySystem {

    XpeComponentMapper<XpeEntityTransformComponent> transformMapper = XpeComponentMapper.create(XpeEntityTransformComponent.class);
    XpeComponentMapper<BoundsComponent> boundsMapper = XpeComponentMapper.create(BoundsComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(BoundsComponent.class, XpeEntityTransformComponent.class).build();
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        XpeEntityTransformComponent pos = transformMapper.getComponent(entity);
        BoundsComponent bounds = boundsMapper.getComponent(entity);

        XpeTransform transform = pos.getTransform();
        bounds.bounds.x = transform.getX() - bounds.bounds.width * 0.5f;
        bounds.bounds.y = transform.getY() - bounds.bounds.height * 0.5f;
    }
}
