package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.components.StateComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;

public class StateSystem implements XpeEntitySystem {

    XpeComponentMapper<StateComponent> stateMapper = XpeComponentMapper.create(StateComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(StateComponent.class).build();
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {
    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        stateMapper.getComponent(entity).time += systemBase.getEngine().getDeltaTime();
    }
}