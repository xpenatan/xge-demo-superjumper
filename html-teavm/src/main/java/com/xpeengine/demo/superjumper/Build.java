package com.xpeengine.demo.superjumper;

import com.github.xpenatan.gdx.backends.teavm.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.plugins.TeaReflectionSupplier;

import java.io.File;

public class Build {

	public static void main(String[] args) {
		TeaReflectionSupplier.addReflectionClass("com.xpeengine.core.content.ecs.");
		TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
		teaBuildConfiguration.assetsPath.add(new File("../android/assets"));;
		teaBuildConfiguration.webappPath = new File(".").getAbsolutePath();
		teaBuildConfiguration.obfuscate = false;
		teaBuildConfiguration.mainApplicationClass = MainGame.class.getName();
		TeaBuilder.build(teaBuildConfiguration);
	}
}
