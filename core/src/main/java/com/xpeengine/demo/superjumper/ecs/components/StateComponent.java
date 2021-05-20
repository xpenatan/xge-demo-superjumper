package com.xpeengine.demo.superjumper.ecs.components;

import com.xpeengine.core.ecs.components.XpeComponent;

public class StateComponent implements XpeComponent {
	private int state = 0;
	public float time = 0.0f;
	
	public int get() {
		return state;
	}
	
	public void set(int newState) {
		state = newState;
		time = 0.0f;
	}
}
