package com.xpeengine.demo.superjumper.ecs.components;

import com.xpeengine.core.ecs.components.XpeComponent;

public class BobComponent implements XpeComponent {
	public static final int STATE_JUMP = 0;
	public static final int STATE_FALL = 1;
	public static final int STATE_HIT = 2;
	public static final float JUMP_VELOCITY = 11;
	public static final float MOVE_VELOCITY = 20;
	public static final float WIDTH = 0.8f;
	public static final float HEIGHT = 0.8f;
	
	public float heightSoFar = 0.0f;
}
