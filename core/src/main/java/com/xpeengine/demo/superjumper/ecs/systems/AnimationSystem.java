package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.core.matcher.XpeObjectMatcher;
import com.xpeengine.demo.superjumper.ecs.components.StateComponent;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeEntitySystem;
import com.xpeengine.core.ecs.systems.XpeEntitySystemBase;
import com.xpeengine.core.content.ecs.components.XpeAnimation2DComponent;

public class AnimationSystem implements XpeEntitySystem {

    XpeComponentMapper<XpeAnimation2DComponent> animationMapper = XpeComponentMapper.create(XpeAnimation2DComponent.class);
    XpeComponentMapper<StateComponent> stateMapper = XpeComponentMapper.create(StateComponent.class);

    @Override
    public XpeObjectMatcher onCreateMatcher(XpeEngine engine) {
        return engine.getMatcherManager().getBuilder().all(XpeAnimation2DComponent.class, StateComponent.class).build();
    }

    @Override
    public void onAttach(XpeEntitySystemBase systemBase) {
    }

    @Override
    public void onDetach(XpeEntitySystemBase systemBase) {

    }

    @Override
    public void onProcess(XpeEntitySystemBase systemBase, XpeEntity entity) {
        StateComponent stateComponent = stateMapper.getComponent(entity);
        XpeAnimation2DComponent animationComponent = animationMapper.getComponent(entity);
        stateComponent.time += systemBase.getEngine().getDeltaTime();
        animationComponent.setTime(stateComponent.time);
    }
}