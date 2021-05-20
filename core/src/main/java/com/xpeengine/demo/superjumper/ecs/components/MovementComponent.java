package com.xpeengine.demo.superjumper.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.xpeengine.core.ecs.components.XpeComponent;

public class MovementComponent implements XpeComponent {
	public final Vector2 velocity = new Vector2();
	public final Vector2 accel = new Vector2();
}
