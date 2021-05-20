package com.xpeengine.demo.superjumper.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.xpeengine.core.matcher.XpeObjectMatcherBuilder;
import com.xpeengine.demo.superjumper.SceneKeys;
import com.xpeengine.demo.superjumper.ecs.components.ActionBox;
import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.camera.XpeCamera;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeSystem;
import com.xpeengine.core.ecs.systems.XpeSystemBase;
import com.xpeengine.core.ecs.systems.XpeSystemType;
import com.xpeengine.core.math.aabb.XpeAABBTreeNode;
import com.xpeengine.core.scene.XpeScene;
import com.xpeengine.core.content.ecs.components.XpeAABBComponent;
import com.xpeengine.core.content.ecs.components.gui.XpeGuiEntityTransformComponent;
import com.xpenatan.lists.XpeImmutableIntLinkedHashMap;

public class InputGuiSystem implements XpeSystem {

    public final Array<XpeAABBTreeNode<XpeEntity>> tempArray = new Array<>();

    XpeImmutableIntLinkedHashMap<XpeEntity> entities;
    XpeComponentMapper<XpeAABBComponent> aabbComponentMapper = XpeComponentMapper.create(XpeAABBComponent.class);

    @Override
    public void onAttach(XpeSystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();
        XpeObjectMatcherBuilder matcher = engine.getMatcherManager().getBuilder().all(XpeGuiEntityTransformComponent.class, XpeAABBComponent.class, ActionBox.class);
        entities = engine.getMatcherManager().getEntities(matcher.build());
    }

    @Override
    public void onDetach(XpeSystemBase systemBase) {
    }

    @Override
    public void onProcess(XpeSystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();
        XpeCamera mainGuiCamera = engine.getGuiCamera();

        boolean leftClick = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (leftClick && mainGuiCamera != null) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();


            XpeAABBComponent aabbComponent = getActionBox(mainGuiCamera, engine, x, y);
            int actionID = -1;
            if (aabbComponent != null) {
                actionID = aabbComponent.getID();
            }

            XpeScene currentScene = engine.getSceneManager().getCurrentScene();
            if (currentScene != null) {
                String id = currentScene.getID();


                switch (id) {
                    case SceneKeys.MAIN_SCENE:
                        if (actionID == 1) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.READY_SCENE);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        } else if (actionID == 3) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.HELP_SCENE_01);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                    case SceneKeys.READY_SCENE: {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.GAME_SCENE);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                    case SceneKeys.GAME_SCENE:
                        World world = engine.getManager(World.class);
                        if (actionID == 1) {
                            world.setState(World.WORLD_STATE_PAUSED);
                        }
                        else if (actionID == 2) {
                            world.setState(World.WORLD_STATE_RESUME);
                        }
                        else if (actionID == 3) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.MAIN_SCENE);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        else {
                            if(world.getState() == World.WORLD_STATE_GAME_OVER) {
                                String scenePath = SceneKeys.getScenePath(SceneKeys.MAIN_SCENE);
                                engine.getSceneManager().loadSceneFromFile(scenePath);
                            }
                        }
                        break;
                    case SceneKeys.HIGH_SCORE_SCENE:
                        break;
                    case SceneKeys.HELP_SCENE_01:
                        if (actionID == 1) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.HELP_SCENE_02);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                    case SceneKeys.HELP_SCENE_02:
                        if (actionID == 1) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.HELP_SCENE_03);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                    case SceneKeys.HELP_SCENE_03:
                        if (actionID == 1) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.HELP_SCENE_04);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                    case SceneKeys.HELP_SCENE_04:
                        if (actionID == 1) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.HELP_SCENE_05);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                    case SceneKeys.HELP_SCENE_05:
                        if (actionID == 1) {
                            String scenePath = SceneKeys.getScenePath(SceneKeys.MAIN_SCENE);
                            engine.getSceneManager().loadSceneFromFile(scenePath);
                        }
                        break;
                }
            }
        }
    }

    private XpeAABBComponent getActionBox(XpeCamera mainCamera, XpeEngine engine, float x, float y) {
        Ray ray = mainCamera.getGdxCamera().getPickRay(x, y);
        engine.getGuiAABBTree().rayCast(tempArray, ray);
        for (int i = 0; i < tempArray.size; i++) {
            XpeAABBTreeNode<XpeEntity> nodeData = tempArray.get(i);
            int nodeID = nodeData.data.getNodeID();
            XpeEntity actionEntity = entities.get(nodeID);
            if (actionEntity != null) {
                if(!actionEntity.isEnable())
                    continue;
                XpeAABBComponent component = aabbComponentMapper.getComponent(actionEntity);
                if (component != null) {
                    return component;
                }
            }
        }
        tempArray.clear();
        return null;
    }

    @Override
    public XpeSystemType getSystemType() {
        return XpeSystemType.GUI;
    }
}
