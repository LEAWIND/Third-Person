package net.leawind.mc.thirdperson.userprofile;


import com.mojang.logging.LogUtils;
import net.leawind.mc.thirdperson.ExpectPlatform;
import net.leawind.mc.thirdperson.ThirdPersonMod;
import net.leawind.mc.thirdperson.config.Config;
import net.leawind.mc.thirdperson.core.cameraoffset.CameraOffsetProfile;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;

public class UserProfile {
	public static final Logger                LOGGER                      = LogUtils.getLogger();
	public static final CameraOffsetProfile[] cameraOffsetProfilesDefault = new CameraOffsetProfile[]{
		CameraOffsetProfile.DEFAULT_CLOSER, CameraOffsetProfile.DEFAULT_FARTHER};
	@NotNull
	public static       CameraOffsetProfile[] cameraOffsetProfiles;

	@NotNull
	public static CameraOffsetProfile getCameraOffsetProfile () {
		return cameraOffsetProfiles[getProfileIndex()];
	}

	private static int getProfileIndex () {
		return Config.is_only_one_third_person_mode && Minecraft.getInstance().options.getCameraType().isMirrored() ? 1: 0;
	}

	public static void loadDefault () {
		cameraOffsetProfiles = new CameraOffsetProfile[]{cameraOffsetProfilesDefault[0].clone(),
														 cameraOffsetProfilesDefault[1].clone()};
	}

	/**
	 * 从文件中加载
	 */
	public static void load () {
		Path profilePath = getProfilePath();
		if (profilePath.toFile().exists()) {
			try {
				FileInputStream   fileIn = new FileInputStream(profilePath.toFile());
				ObjectInputStream in     = new ObjectInputStream(fileIn);
				cameraOffsetProfiles = (CameraOffsetProfile[])in.readObject();
				in.close();
				fileIn.close();
				LOGGER.info("User Profile is loaded");
			} catch (IOException | ClassNotFoundException e) {
				LOGGER.error("Failed to load profile from {}", profilePath.toAbsolutePath());
				throw new RuntimeException(e);
			}
		} else {
			LOGGER.info("User Profile is not found at {}", profilePath.toAbsolutePath());
		}
	}

	private static Path getProfilePath () {
		return ExpectPlatform.getConfigDirectory().resolve(String.format("%s.profile.ser", ThirdPersonMod.MOD_ID));
	}

	/**
	 * 玩家松开调整偏移量的按键时保存
	 */
	public static void save () {
		Path profilePath = getProfilePath();
		try {
			FileOutputStream   fileOut = new FileOutputStream(profilePath.toFile());
			ObjectOutputStream out     = new ObjectOutputStream(fileOut);
			out.writeObject(cameraOffsetProfiles);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			LOGGER.error("Failed to save user profile to {}", profilePath.toAbsolutePath());
			throw new RuntimeException(e);
		}
		LOGGER.info("User Profile is saved");
	}
}