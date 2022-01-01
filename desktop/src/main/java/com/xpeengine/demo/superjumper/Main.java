package com.xpeengine.demo.superjumper;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xpeengine.core.XpeEngineApp;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 480;
        config.height = 640;
        config.title = "ID 0";
        config.vSyncEnabled = true;
        new LwjglApplication(new MainGame(), config);
    }
}
