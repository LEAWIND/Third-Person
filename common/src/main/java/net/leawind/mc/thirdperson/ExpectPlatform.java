package net.leawind.mc.thirdperson;


import net.leawind.mc.thirdperson.config.Config;
import net.minecraft.client.gui.screens.Screen;

import java.nio.file.Path;

public class ExpectPlatform {
	@dev.architectury.injectables.annotations.ExpectPlatform
	public static Path getConfigDirectory () {
		throw new AssertionError();
	}

	@dev.architectury.injectables.annotations.ExpectPlatform
	public static Screen buildConfigScreen (Config config, Screen parent) {
		throw new AssertionError();
	}
}
