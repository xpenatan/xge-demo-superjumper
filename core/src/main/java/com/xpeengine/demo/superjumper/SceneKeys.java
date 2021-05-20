package com.xpeengine.demo.superjumper;

public class SceneKeys {
    public final static String MAIN_SCENE = "MAIN_SCENE";
    public final static String GAME_SCENE = "GAME_SCENE";
    public final static String READY_SCENE = "READY_SCENE";
    public final static String HIGH_SCORE_SCENE = "HIGH_SCORE_SCENE";
    public final static String HELP_SCENE_01 = "HELP_SCENE_01";
    public final static String HELP_SCENE_02 = "HELP_SCENE_02";
    public final static String HELP_SCENE_03 = "HELP_SCENE_03";
    public final static String HELP_SCENE_04 = "HELP_SCENE_04";
    public final static String HELP_SCENE_05 = "HELP_SCENE_05";

    public static String getScenePath(String scene) {
        switch (scene) {
            case SceneKeys.MAIN_SCENE:
                return "scene/main";
            case SceneKeys.READY_SCENE:
                return "scene/ready";
            case SceneKeys.GAME_SCENE:
                return "scene/game";
            case SceneKeys.HIGH_SCORE_SCENE:
                break;
            case SceneKeys.HELP_SCENE_01:
                return "scene/help01";
            case SceneKeys.HELP_SCENE_02:
                return "scene/help02";
            case SceneKeys.HELP_SCENE_03:
                return "scene/help03";
            case SceneKeys.HELP_SCENE_04:
                return "scene/help04";
            case SceneKeys.HELP_SCENE_05:
                return "scene/help05";
        }
        return null;
    }
}
