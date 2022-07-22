package com.xpeengine.demo.superjumper;

import com.github.xpenatan.gdx.backends.teavm.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.plugins.TeaReflectionSupplier;

import java.io.File;

public class TeaVMBuild {

	public static void main(String[] args) {
		TeaReflectionSupplier.addReflectionClass("com.xpeengine.demo.superjumper.ecs.components");

		TeaReflectionSupplier.addReflectionClass("com.xpeengine.core.ecs");
		TeaReflectionSupplier.addReflectionClass("com.xpeengine.content.ecs");

		TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
		teaBuildConfiguration.assetsPath.add(new File("../assets"));;
		teaBuildConfiguration.webappPath = new File(".").getAbsolutePath();
		teaBuildConfiguration.obfuscate = false;
		teaBuildConfiguration.logClasses = true;
		teaBuildConfiguration.mainApplicationClass = GameApplication.class.getName();
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/xpeengine/content/test/shaders/scene_v.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/xpeengine/content/test/shaders/scene_f.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/xpeengine/content/test/shaders/depth.fs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/xpeengine/content/test/shaders/depth.vs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/gdx-pbr.vs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/gdx-pbr.fs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/depth.vs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/depth.fs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/ibl-sun.vs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/ibl-sun.fs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/brdfLUT.png");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/skybox.vs.glsl");
		teaBuildConfiguration.additionalAssetsClasspathFiles.add("net/mgsx/gltf/shaders/skybox.fs.glsl");
		TeaBuilder.build(teaBuildConfiguration);
	}
}