package com.xpeengine.demo.superjumper.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.xpeengine.core.ecs.components.XpeComponent;
import com.xpeengine.core.io.datamap.XpeIODataListener;
import com.xpeengine.core.io.datamap.XpeIODataMap;
import com.xpeengine.core.io.datamap.XpeIOProperties;
import com.xpeengine.core.io.datamap.XpeIOUpdateType;
import com.xpeengine.core.ui.editor.XpeEditorKeys;
import com.xpeengine.core.ui.editor.XpeUICheckBox;
import com.xpeengine.core.ui.editor.XpeUIEditText;
import com.xpeengine.core.ui.editor.XpeUIVector3;

public class MovementComponent implements XpeComponent, XpeIODataListener {

	private static final int VELOCITY_KEY = 10;

	public final Vector2 velocity = new Vector2();
	public final Vector2 accel = new Vector2();

	@Override
	public void onUpdate(XpeIOUpdateType type, XpeIODataMap dataMap) {
		XpeIOProperties properties = dataMap.getProperties();
		boolean isUI = properties.getBoolean(XpeEditorKeys.KEY_UPDATE_UI, false);

		if(type == XpeIOUpdateType.DATA_TO_MAP) {
			dataMap.put(VELOCITY_KEY, velocity);

			if(isUI) {
				XpeUIEditText.set(dataMap, VELOCITY_KEY, "Velocity:");
			}
		}
		else if(type == XpeIOUpdateType.MAP_TO_DATA) {
			dataMap.getVector2(VELOCITY_KEY, velocity);
		}

	}
}
