package com.xpeengine.demo.superjumper;

import com.xpeengine.core.content.ecs.managers.*;
import com.xpeengine.demo.superjumper.ecs.components.ActionBox;
import com.xpeengine.demo.superjumper.ecs.components.BackgroundComponent;
import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.XpeEnginePackage;
import com.xpeengine.core.managers.XpeClassManager;
import com.xpeengine.core.content.ecs.systems.*;
import com.xpeengine.core.content.ecs.systems.gui.XpeGuiCameraSystem;
import com.xpeengine.demo.superjumper.ecs.systems.*;

public class GamePackage implements XpeEnginePackage {
    @Override
    public void init(XpeEngine engine) {
        XpeClassManager classManager = engine.getClassManager();

        classManager.registerClass(1, ActionBox.class);
        classManager.registerClass(2, BackgroundComponent.class);

        classManager.registerClass(4, XpeSpriteBatchManager.class, Xpe2DManager.class);
    }

    @Override
    public void onCreate(XpeEngine engine) {

        engine.addManager(World.class, new World());

        engine.setPixelPerMeter(32f);

        engine.getSystemManager().attachSystem(new XpeDynamicAABBSystem());
        engine.getSystemManager().attachSystem(new XpeGameCameraSystem());
        engine.getSystemManager().attachSystem(new XpeGuiCameraSystem());
        engine.getSystemManager().attachSystem(new XpeRender2DSystem());
        engine.getSystemManager().attachSystem(new XpeGuiRender2DSystem());

        engine.getSystemManager().attachSystem(new CameraSystem());
        engine.getSystemManager().attachSystem(new BobSystem());
        engine.getSystemManager().attachSystem(new GravitySystem());
        engine.getSystemManager().attachSystem(new MovementSystem());
        engine.getSystemManager().attachSystem(new BackgroundSystem());
        engine.getSystemManager().attachSystem(new InputGuiSystem());
        engine.getSystemManager().attachSystem(new BoundsSystem());
        engine.getSystemManager().attachSystem(new PlatformSystem());
        engine.getSystemManager().attachSystem(new SquirrelSystem());
        engine.getSystemManager().attachSystem(new StateSystem());
        engine.getSystemManager().attachSystem(new AnimationSystem());
        engine.getSystemManager().attachSystem(new InputSystem());
        engine.getSystemManager().attachSystem(new CollisionSystem(new CollisionSystem.CollisionListener() {
            @Override
            public void jump() {
            }

            @Override
            public void highJump() {
            }

            @Override
            public void hit() {
            }

            @Override
            public void coin() {
            }
        }));

        engine.getSceneManager().loadSceneFromFile("scene/main");
    }
}
