package com.xpeengine.demo.superjumper.ecs.systems;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.systems.XpeSystem;
import com.xpeengine.core.ecs.systems.XpeSystemBase;
import com.xpeengine.core.managers.XpeSystemManager;

public class InputSystem implements XpeSystem {
    @Override
    public void onProcess(XpeSystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();

        XpeSystemManager systemManager = engine.getSystemManager();
        BobSystem system = systemManager.getSystem(BobSystem.class);

        Application.ApplicationType appType = Gdx.app.getType();

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX = 0.0f;

        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) accelX = 5f;
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) accelX = -5f;
        }
        system.setAccelX(accelX);
    }
}
