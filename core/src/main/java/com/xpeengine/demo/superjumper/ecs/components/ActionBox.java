package com.xpeengine.demo.superjumper.ecs.components;

import com.xpeengine.core.ecs.components.XpeComponent;
import com.xpeengine.ui.autobind.AutoUIAnnotation;

@AutoUIAnnotation.XpeAutoClassBind(aliasName = "ActionBox",
    groupNames = { "Game", "Gui" }, groupType = AutoUIAnnotation.GroupType.CUSTOM)
public class ActionBox implements XpeComponent {
}
