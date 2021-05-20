package com.xpeengine.demo.superjumper.ecs.components;

import com.xpeengine.core.ecs.components.XpeComponent;
import com.xpeengine.ui.autobind.AutoUIAnnotation;

@AutoUIAnnotation.XpeAutoClassBind(aliasName = "BackgroundComponent",
        groupNames = { "Game", "Main" }, groupType = AutoUIAnnotation.GroupType.CUSTOM)
public class BackgroundComponent implements XpeComponent {
}
