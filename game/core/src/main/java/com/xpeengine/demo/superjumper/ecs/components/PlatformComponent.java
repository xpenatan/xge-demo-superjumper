package com.xpeengine.demo.superjumper.ecs.components;

import com.xpeengine.core.ecs.components.XpeComponent;

public class PlatformComponent implements XpeComponent {
	public static final float WIDTH = 2;
	public static final float HEIGHT = 0.5f;
	public static final int TYPE_STATIC = 0;
	public static final int TYPE_MOVING = 1;
	public static final int STATE_NORMAL = 0;
	public static final int STATE_PULVERIZING = 1;
	public static final float PULVERIZE_TIME = 0.2f * 4;
	public static final float VELOCITY = 2;
	
	public int type = TYPE_STATIC;
}
