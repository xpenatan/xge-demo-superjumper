package com.xpeengine.demo.superjumper.ecs.components;

import com.badlogic.gdx.math.Rectangle;
import com.xpeengine.core.ecs.components.XpeComponent;

public class BoundsComponent implements XpeComponent {
	public final Rectangle bounds = new Rectangle();
}
