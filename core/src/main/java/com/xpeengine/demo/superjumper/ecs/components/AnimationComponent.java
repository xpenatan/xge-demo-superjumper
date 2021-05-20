package com.xpeengine.demo.superjumper.ecs.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.xpeengine.core.ecs.components.XpeComponent;

public class AnimationComponent implements XpeComponent {
	public IntMap<Animation> animations = new IntMap<Animation>();
}
