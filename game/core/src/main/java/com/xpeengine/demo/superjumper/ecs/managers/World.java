package com.xpeengine.demo.superjumper.ecs.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.xpeengine.content.ecs.components.XpeAABBComponent;
import com.xpeengine.content.ecs.components.XpeAnimation2DComponent;
import com.xpeengine.content.ecs.components.XpeEntityTransformComponent;
import com.xpeengine.content.ecs.components.XpeSpriteComponent;
import com.xpeengine.content.ecs.components.utils.XpeAnimation;
import com.xpeengine.demo.superjumper.SceneKeys;
import com.xpeengine.demo.superjumper.ecs.components.*;
import com.xpeengine.demo.superjumper.ecs.systems.*;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.managers.XpeManager;
import com.xpeengine.core.event.*;
import com.xpeengine.core.managers.XpeEntityManager;
import com.xpeengine.core.managers.XpeEventManager;
import com.xpeengine.core.managers.XpeSystemManager;
import com.xpeengine.core.scene.XpeScene;

import java.util.Random;

public class World implements XpeManager {

    public static final XpeEventKey STATE_CHANGE_EVENT = XpeEngineEvents.getKey("STATE_CHANGE_EVENT");

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_PAUSED = 1;
    public static final int WORLD_STATE_RESUME = 2;
    public static final int WORLD_STATE_NEXT_LEVEL = 3;
    public static final int WORLD_STATE_GAME_OVER = 4;
    public static final Vector2 gravity = new Vector2(0, -12);

    public final Random rand;

    public float heightSoFar;
    public int score;
    private int state;

    private XpeEngine engine;

    public World () {
        this.rand = new Random();
    }

    @Override
    public void onAttach(XpeEngine engine) {
        this.engine = engine;

        engine.getEventManager().addEventListener(XpeEngineEvents.END_LOAD_SCENE, new XpeEventListener() {
            @Override
            public void onEvent(XpeEvent event) {
                XpeScene currentScene = engine.getSceneManager().getCurrentScene();
                String id = currentScene.getID();
                System.out.println("Scene ID: " + id);

                switch (id) {
                    case SceneKeys.MAIN_SCENE:
                        break;
                    case SceneKeys.GAME_SCENE:
                        create();
                        break;
                    case SceneKeys.HIGH_SCORE_SCENE:
                        break;
                    case SceneKeys.HELP_SCENE_01:
                        break;
                    case SceneKeys.HELP_SCENE_02:
                        break;
                    case SceneKeys.HELP_SCENE_03:
                        break;
                    case SceneKeys.HELP_SCENE_04:
                        break;
                    case SceneKeys.HELP_SCENE_05:
                        break;
                }
            }
        });

        engine.getEventManager().addEventListener(World.STATE_CHANGE_EVENT, new XpeEventListener() {
            @Override
            public void onEvent(XpeEvent event) {
                if(state == WORLD_STATE_PAUSED) {
                    pauseSystems();
                }
                else if(state == WORLD_STATE_RESUME) {
                    state = WORLD_STATE_RUNNING;
                    resumeSystems();
                }
                else if(state == WORLD_STATE_GAME_OVER) {
                    pauseSystems();
                }
            }
        });
    }

    public void create() {
        createBob();
        generateLevel();

        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
        resumeSystems();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if(engine != null) {
            XpeEventManager eventManager = engine.getEventManager();
            eventManager.sendEvent(XpeEventOptions.get(), World.STATE_CHANGE_EVENT);
        }
    }

    private void generateLevel () {
        createGameBackGround();
        float y = PlatformComponent.HEIGHT / 2;
        float maxJumpHeight = BobComponent.JUMP_VELOCITY * BobComponent.JUMP_VELOCITY / (2 * -gravity.y);
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            int type = rand.nextFloat() > 0.8f ? PlatformComponent.TYPE_MOVING : PlatformComponent.TYPE_STATIC;
            float x = rand.nextFloat() * (WORLD_WIDTH - PlatformComponent.WIDTH) + PlatformComponent.WIDTH / 2;

            createPlatform(type, x, y);

            if (rand.nextFloat() > 0.9f && type != PlatformComponent.TYPE_MOVING) {
                createSpring(x, y + PlatformComponent.HEIGHT / 2 + SpringComponent.HEIGHT / 2);
            }

            if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
                createSquirrel(x + rand.nextFloat(), y + SquirrelComponent.HEIGHT + rand.nextFloat() * 2);
            }

            if (rand.nextFloat() > 0.6f) {
                createCoin(x + MathUtils.random(-0.5f, 0.5f), y + CoinComponent.HEIGHT + rand.nextFloat() * 3);
            }

            y += (maxJumpHeight - 0.5f);
            y -= rand.nextFloat() * (maxJumpHeight / 3);
        }

        createCastle(WORLD_WIDTH / 2, y);
    }

    private void createBob() {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Bob");

        XpeEntityTransformComponent transformComponent = entity.attachComponent(new XpeEntityTransformComponent());
        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        XpeAnimation2DComponent animationComponent = entity.attachComponent(new XpeAnimation2DComponent());
        BobComponent bob = entity.attachComponent(new BobComponent());
        BoundsComponent bounds = entity.attachComponent(new BoundsComponent());
        GravityComponent gravityComponent = entity.attachComponent(new GravityComponent());
        MovementComponent movementComponent = entity.attachComponent(new MovementComponent());
        StateComponent state = entity.attachComponent(new StateComponent());

        spriteComponent.setTexturePath("data/items.png");
        spriteComponent.setSortOrder(1);

        animationComponent.setState(BobComponent.STATE_JUMP);

        animationComponent.addTextureRegion(BobComponent.STATE_JUMP, 0, 128, 32, 32);
        animationComponent.addTextureRegion(BobComponent.STATE_JUMP, 32, 128, 32, 32);
        XpeAnimation bobJumpAnimation = animationComponent.getAnimation(BobComponent.STATE_JUMP);
        bobJumpAnimation.setFrameDuration(0.2f);
        bobJumpAnimation.setPlayMode(Animation.PlayMode.LOOP);

        animationComponent.addTextureRegion(BobComponent.STATE_FALL, 64, 128, 32, 32);
        animationComponent.addTextureRegion(BobComponent.STATE_FALL, 96, 128, 32, 32);
        XpeAnimation bobFallAnimation = animationComponent.getAnimation(BobComponent.STATE_FALL);
        bobFallAnimation.setFrameDuration(0.2f);
        bobFallAnimation.setPlayMode(Animation.PlayMode.LOOP);

        animationComponent.addTextureRegion(BobComponent.STATE_HIT, 128, 128, 32, 32);
        XpeAnimation bobHitAnimation = animationComponent.getAnimation(BobComponent.STATE_HIT);
        bobHitAnimation.setFrameDuration(0.2f);
        bobHitAnimation.setPlayMode(Animation.PlayMode.LOOP);

        transformComponent.set2D(true);
        transformComponent.getTransform().setPosition(5.0f, 1.0f, 0.0f);

        bounds.bounds.width = BobComponent.WIDTH;
        bounds.bounds.height = BobComponent.HEIGHT;
        state.set(BobComponent.STATE_JUMP);

        entityManager.attachEntity(entity);
    }

    private void createPlatform(int type, float x, float y) {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Platform");

        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeAnimation2DComponent animationComponent = entity.attachComponent(new XpeAnimation2DComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        PlatformComponent platform = entity.attachComponent(new PlatformComponent());
        BoundsComponent bounds = entity.attachComponent(new BoundsComponent());
        MovementComponent movement = entity.attachComponent(new MovementComponent());
        XpeEntityTransformComponent position = entity.attachComponent(new XpeEntityTransformComponent());
        StateComponent state = entity.attachComponent(new StateComponent());

        position.set2D(true);
        spriteComponent.setTexturePath("data/items.png");

        animationComponent.addTextureRegion(PlatformComponent.STATE_NORMAL, 64, 160, 64, 16);
        XpeAnimation platformNormalAnimation = animationComponent.getAnimation(PlatformComponent.STATE_NORMAL);
        platformNormalAnimation.setFrameDuration(0.2f);
        platformNormalAnimation.setPlayMode(Animation.PlayMode.LOOP);

        animationComponent.addTextureRegion(PlatformComponent.STATE_PULVERIZING, 64, 160, 64, 16);
        animationComponent.addTextureRegion(PlatformComponent.STATE_PULVERIZING, 64, 176, 64, 16);
        animationComponent.addTextureRegion(PlatformComponent.STATE_PULVERIZING, 64, 192, 64, 16);
        animationComponent.addTextureRegion(PlatformComponent.STATE_PULVERIZING, 64, 208, 64, 16);
        XpeAnimation platformPulverizingAnimation = animationComponent.getAnimation(PlatformComponent.STATE_PULVERIZING);
        platformPulverizingAnimation.setFrameDuration(0.2f);
        platformPulverizingAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        bounds.bounds.width = PlatformComponent.WIDTH;
        bounds.bounds.height = PlatformComponent.HEIGHT;

        position.getTransform().setPosition(x, y, 0);

        state.set(PlatformComponent.STATE_NORMAL);

        platform.type = type;
        if (type == PlatformComponent.TYPE_MOVING) {
            movement.velocity.x = rand.nextBoolean() ? PlatformComponent.VELOCITY : -PlatformComponent.VELOCITY;
        }

        entityManager.attachEntity(entity);
    }

    private void createSpring(float x, float y) {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Spring");

        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        XpeAnimation2DComponent animationComponent = entity.attachComponent(new XpeAnimation2DComponent());
        SpringComponent spring = entity.attachComponent(new SpringComponent());
        BoundsComponent bounds = entity.attachComponent(new BoundsComponent());
        XpeEntityTransformComponent position = entity.attachComponent(new XpeEntityTransformComponent());

        position.set2D(true);
        spriteComponent.setSortOrder(3);
        spriteComponent.setTexturePath("data/items.png");

        spriteComponent.setRegion(128, 0, 32, 32);

        bounds.bounds.width = SpringComponent.WIDTH;
        bounds.bounds.height = SpringComponent.HEIGHT;

        position.getTransform().setPosition(x, y, 0.0f);

        entityManager.attachEntity(entity);
    }

    private void createSquirrel(float x, float y) {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Squirrel");

        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        XpeAnimation2DComponent animationComponent = entity.attachComponent(new XpeAnimation2DComponent());
        SquirrelComponent squirrel = entity.attachComponent(new SquirrelComponent());
        BoundsComponent bounds = entity.attachComponent(new BoundsComponent());
        MovementComponent movement = entity.attachComponent(new MovementComponent());
        XpeEntityTransformComponent position = entity.attachComponent(new XpeEntityTransformComponent());
        StateComponent state = entity.attachComponent(new StateComponent());

        movement.velocity.x = rand.nextFloat() > 0.5f ? SquirrelComponent.VELOCITY : -SquirrelComponent.VELOCITY;

        spriteComponent.setTexturePath("data/items.png");

        animationComponent.setState(SquirrelComponent.STATE_NORMAL);
        animationComponent.addTextureRegion(SquirrelComponent.STATE_NORMAL, 0, 160, 32, 32);
        animationComponent.addTextureRegion(SquirrelComponent.STATE_NORMAL, 32, 160, 32, 32);
        XpeAnimation platformNormalAnimation = animationComponent.getAnimation(SquirrelComponent.STATE_NORMAL);
        platformNormalAnimation.setFrameDuration(0.2f);
        platformNormalAnimation.setPlayMode(Animation.PlayMode.LOOP);

        bounds.bounds.width = SquirrelComponent.WIDTH;
        bounds.bounds.height = SquirrelComponent.HEIGHT;

        position.set2D(true);
        position.getTransform().setPosition(x, y, 0.0f);

        state.set(SquirrelComponent.STATE_NORMAL);

        entityManager.attachEntity(entity);
    }

    private void createCoin(float x, float y) {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Coin");

        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        XpeAnimation2DComponent animationComponent = entity.attachComponent(new XpeAnimation2DComponent());
        CoinComponent coin = entity.attachComponent(new CoinComponent());
        BoundsComponent bounds = entity.attachComponent(new BoundsComponent());
        XpeEntityTransformComponent position = entity.attachComponent(new XpeEntityTransformComponent());
        StateComponent state = entity.attachComponent(new StateComponent());

        spriteComponent.setTexturePath("data/items.png");

        animationComponent.setState(CoinComponent.STATE_NORMAL);
        animationComponent.addTextureRegion(CoinComponent.STATE_NORMAL, 128, 32, 32, 32);
        animationComponent.addTextureRegion(CoinComponent.STATE_NORMAL, 160, 32, 32, 32);
        animationComponent.addTextureRegion(CoinComponent.STATE_NORMAL, 192, 32, 32, 32);
        animationComponent.addTextureRegion(CoinComponent.STATE_NORMAL, 160, 32, 32, 32);
        XpeAnimation platformNormalAnimation = animationComponent.getAnimation(CoinComponent.STATE_NORMAL);
        platformNormalAnimation.setFrameDuration(0.2f);
        platformNormalAnimation.setPlayMode(Animation.PlayMode.LOOP);

        bounds.bounds.width = CoinComponent.WIDTH;
        bounds.bounds.height = CoinComponent.HEIGHT;

        position.set2D(true);
        position.getTransform().setPosition(x, y, 0.0f);

        state.set(CoinComponent.STATE_NORMAL);

        entityManager.attachEntity(entity);
    }

    private void createCastle(float x, float y) {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Castle");

        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        CastleComponent castle = entity.attachComponent(new CastleComponent());
        BoundsComponent bounds = entity.attachComponent(new BoundsComponent());
        XpeEntityTransformComponent position = entity.attachComponent(new XpeEntityTransformComponent());

        bounds.bounds.width = CastleComponent.WIDTH;
        bounds.bounds.height = CastleComponent.HEIGHT;

        spriteComponent.setTexturePath("data/items.png");
        spriteComponent.setRegion(128, 64, 64, 64);

        position.set2D(true);
        position.getTransform().setPosition(x, y, 2.0f);

        entityManager.attachEntity(entity);
    }

    private void createGameBackGround() {
        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity entity = entityManager.createEntity(false);
        entity.setName("Game Background");

        XpeAABBComponent aabbComponent = entity.attachComponent(new XpeAABBComponent());
        XpeSpriteComponent spriteComponent = entity.attachComponent(new XpeSpriteComponent());
        BackgroundComponent backgroundComponent = entity.attachComponent(new BackgroundComponent());
        XpeEntityTransformComponent position = entity.attachComponent(new XpeEntityTransformComponent());

        position.set2D(true);
        spriteComponent.setTexturePath("data/background.png");
        spriteComponent.setRegion(0, 0, 320, 480);
        spriteComponent.offsetX = 0.5f;
        spriteComponent.offsetY = 0.5f;

        entityManager.attachEntity(entity);
    }

    public void pauseSystems() {
        XpeSystemManager systemManager = engine.getSystemManager();
        systemManager.getSystemBase(BobSystem.class).setProcessing(false);
        systemManager.getSystemBase(SquirrelSystem.class).setProcessing(false);
        systemManager.getSystemBase(PlatformSystem.class).setProcessing(false);
        systemManager.getSystemBase(GravitySystem.class).setProcessing(false);
        systemManager.getSystemBase(MovementSystem.class).setProcessing(false);
        systemManager.getSystemBase(BoundsSystem.class).setProcessing(false);
        systemManager.getSystemBase(StateSystem.class).setProcessing(false);
        systemManager.getSystemBase(AnimationSystem.class).setProcessing(false);
        systemManager.getSystemBase(CollisionSystem.class).setProcessing(false);

        XpeEntityManager entityManager = engine.getEntityManager();
        if(state == WORLD_STATE_PAUSED) {
            XpeEntity pauseMenu = entityManager.getTag("pauseMenu");
            XpeEntity quitMenu = entityManager.getTag("quitMenu");
            XpeEntity resumeMenu = entityManager.getTag("resumeMenu");
            if(pauseMenu != null)
                pauseMenu.setEnable(false);
            if(quitMenu != null)
                quitMenu.setEnable(true);
            if(resumeMenu != null)
                resumeMenu.setEnable(true);
        }
        else if(state == WORLD_STATE_GAME_OVER) {
            XpeEntity pauseMenu = entityManager.getTag("pauseMenu");
            XpeEntity gameOverMenu = entityManager.getTag("gameOver");
            if(gameOverMenu != null)
                gameOverMenu.setEnable(true);
            if(pauseMenu != null)
                pauseMenu.setEnable(false);
        }
    }

    public void resumeSystems() {
        XpeSystemManager systemManager = engine.getSystemManager();
        systemManager.getSystemBase(BobSystem.class).setProcessing(true);
        systemManager.getSystemBase(SquirrelSystem.class).setProcessing(true);
        systemManager.getSystemBase(PlatformSystem.class).setProcessing(true);
        systemManager.getSystemBase(GravitySystem.class).setProcessing(true);
        systemManager.getSystemBase(MovementSystem.class).setProcessing(true);
        systemManager.getSystemBase(BoundsSystem.class).setProcessing(true);
        systemManager.getSystemBase(StateSystem.class).setProcessing(true);
        systemManager.getSystemBase(AnimationSystem.class).setProcessing(true);
        systemManager.getSystemBase(CollisionSystem.class).setProcessing(true);

        XpeEntityManager entityManager = engine.getEntityManager();
        XpeEntity pauseMenu = entityManager.getTag("pauseMenu");
        XpeEntity quitMenu = entityManager.getTag("quitMenu");
        XpeEntity resumeMenu = entityManager.getTag("resumeMenu");

        if(pauseMenu != null)
            pauseMenu.setEnable(true);
        if(quitMenu != null)
            quitMenu.setEnable(false);
        if(resumeMenu != null)
            resumeMenu.setEnable(false);
        XpeEntity gameOverMenu = entityManager.getTag("gameOver");
        if(gameOverMenu != null)
            gameOverMenu.setEnable(false);
    }
}
