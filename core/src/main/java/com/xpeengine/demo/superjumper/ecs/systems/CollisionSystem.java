package com.xpeengine.demo.superjumper.ecs.systems;

import com.xpeengine.demo.superjumper.ecs.managers.World;
import com.xpeengine.demo.superjumper.ecs.components.*;
import com.xpeengine.core.XpeEngine;
import com.xpeengine.core.ecs.components.XpeComponentMapper;
import com.xpeengine.core.ecs.entities.XpeEntity;
import com.xpeengine.core.ecs.systems.XpeSystem;
import com.xpeengine.core.ecs.systems.XpeSystemBase;
import com.xpeengine.core.managers.XpeEntityManager;
import com.xpeengine.core.managers.XpeMatcherManager;
import com.xpeengine.core.content.ecs.components.XpeEntityTransformComponent;
import com.xpenatan.lists.XpeImmutableIntLinkedHashMap;
import com.xpenatan.lists.nodes.XpeIntLinkedHashMapNode;

import java.util.Random;

public class CollisionSystem implements XpeSystem {

    private final XpeComponentMapper<BoundsComponent> bm = XpeComponentMapper.create(BoundsComponent.class);
    private final XpeComponentMapper<MovementComponent> mm = XpeComponentMapper.create(MovementComponent.class);
    private final XpeComponentMapper<StateComponent> sm = XpeComponentMapper.create(StateComponent.class);
    private final XpeComponentMapper<XpeEntityTransformComponent> tm = XpeComponentMapper.create(XpeEntityTransformComponent.class);

    public static interface CollisionListener {
        public void jump ();
        public void highJump ();
        public void hit ();
        public void coin ();
    }

    private CollisionListener listener;
    private final Random rand = new Random();
    private XpeImmutableIntLinkedHashMap<XpeEntity> bobs;
    private XpeImmutableIntLinkedHashMap<XpeEntity> coins;
    private XpeImmutableIntLinkedHashMap<XpeEntity> squirrels;
    private XpeImmutableIntLinkedHashMap<XpeEntity> springs;
    private XpeImmutableIntLinkedHashMap<XpeEntity> castles;
    private XpeImmutableIntLinkedHashMap<XpeEntity> platforms;

    public CollisionSystem(CollisionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(XpeSystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();
        XpeMatcherManager matcherManager = engine.getMatcherManager();

        bobs = matcherManager.getEntities(matcherManager.getBuilder().all(BobComponent.class, BoundsComponent.class, XpeEntityTransformComponent.class, StateComponent.class).build());
        coins = matcherManager.getEntities(matcherManager.getBuilder().all(CoinComponent.class, BoundsComponent.class).build());
        squirrels = matcherManager.getEntities(matcherManager.getBuilder().all(SquirrelComponent.class, BoundsComponent.class).build());
        springs = matcherManager.getEntities(matcherManager.getBuilder().all(SpringComponent.class, BoundsComponent.class, XpeEntityTransformComponent.class).build());
        castles = matcherManager.getEntities(matcherManager.getBuilder().all(CastleComponent.class, BoundsComponent.class).build());
        platforms = matcherManager.getEntities(matcherManager.getBuilder().all(PlatformComponent.class, BoundsComponent.class, XpeEntityTransformComponent.class).build());
    }

    @Override
    public void onDetach(XpeSystemBase systemBase) {
    }

    @Override
    public void onProcess(XpeSystemBase systemBase) {
        XpeEngine engine = systemBase.getEngine();
        World world = engine.getManager(World.class);
        BobSystem bobSystem = engine.getSystemManager().getSystem(BobSystem.class);
        PlatformSystem platformSystem = engine.getSystemManager().getSystem(PlatformSystem.class);

        XpeIntLinkedHashMapNode<XpeEntity> bobCur = bobs.getHead();
        while (bobCur != null) {
            XpeEntity bob =  bobCur.getValue();
            bobCur = bobCur.getNext();

            StateComponent bobState = sm.getComponent(bob);

            if (bobState.get() == BobComponent.STATE_HIT) {
                continue;
            }

            MovementComponent bobMov = mm.getComponent(bob);
            BoundsComponent bobBounds = bm.getComponent(bob);

            if (bobMov.velocity.y < 0.0f) {
                XpeEntityTransformComponent bobPos = tm.getComponent(bob);

                XpeIntLinkedHashMapNode<XpeEntity> platformCur = platforms.getHead();
                while (platformCur != null) {
                    XpeEntity platform = platformCur.getValue();
                    platformCur = platformCur.getNext();

                    XpeEntityTransformComponent platPos = tm.getComponent(platform);

                    if (bobPos.getTransform().getY() > platPos.getTransform().getY()) {
                        BoundsComponent platBounds = bm.getComponent(platform);

                        if (bobBounds.bounds.overlaps(platBounds.bounds)) {
                            bobSystem.hitPlatform(bob);
                            listener.jump();
                            if (rand.nextFloat() > 0.5f) {
                                platformSystem.pulverize(platform);
                            }

                            break;
                        }
                    }
                }

                XpeIntLinkedHashMapNode<XpeEntity> springsCur = springs.getHead();
                while (springsCur != null) {
                    XpeEntity spring = springsCur.getValue();
                    springsCur = springsCur.getNext();

                    XpeEntityTransformComponent springPos = tm.getComponent(spring);
                    BoundsComponent springBounds = bm.getComponent(spring);

                    if (bobPos.getTransform().getY() > springPos.getTransform().getY()) {
                        if (bobBounds.bounds.overlaps(springBounds.bounds)) {
                            bobSystem.hitSpring(bob);
                            listener.highJump();
                        }
                    }
                }
            }

            XpeIntLinkedHashMapNode<XpeEntity> squirrelsCur = squirrels.getHead();
            while (squirrelsCur != null) {
                XpeEntity squirrel = squirrelsCur.getValue();
                squirrelsCur = squirrelsCur.getNext();

                BoundsComponent squirrelBounds = bm.getComponent(squirrel);

                if (squirrelBounds.bounds.overlaps(bobBounds.bounds)) {
                    bobSystem.hitSquirrel(bob);
                    listener.hit();
                }
            }

            XpeIntLinkedHashMapNode<XpeEntity> coinsCur = coins.getHead();
            while (coinsCur != null) {
                XpeEntity coin = coinsCur.getValue();
                coinsCur = coinsCur.getNext();

                BoundsComponent coinBounds = bm.getComponent(coin);

                if (coinBounds.bounds.overlaps(bobBounds.bounds)) {
                    XpeEntityManager entityManager = engine.getEntityManager();
                    entityManager.deleteEntity(coin);
                    listener.coin();
                    world.score += CoinComponent.SCORE;
                }
            }

            XpeIntLinkedHashMapNode<XpeEntity> castlesCur = castles.getHead();
            while (castlesCur != null) {
                XpeEntity castle = castlesCur.getValue();
                castlesCur = castlesCur.getNext();

                BoundsComponent castleBounds = bm.getComponent(castle);

                if (castleBounds.bounds.overlaps(bobBounds.bounds)) {
                    world.setState(World.WORLD_STATE_NEXT_LEVEL);
                }
            }
        }
    }
}
