package net.leawind.mc.thirdpersonperspective.core;


import com.mojang.blaze3d.Blaze3D;
import com.mojang.logging.LogUtils;
import net.leawind.mc.thirdpersonperspective.config.Config;
import net.leawind.mc.thirdpersonperspective.mixin.CameraInvoker;
import net.leawind.mc.thirdpersonperspective.userprofile.UserProfile;
import net.leawind.mc.util.smoothvalue.ExpSmoothDouble;
import net.leawind.mc.util.smoothvalue.ExpSmoothVec2;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.slf4j.Logger;

public class CameraAgent {
	public static final Logger          LOGGER               = LogUtils.getLogger();
	public static       BlockGetter     level;
	public static       Camera          camera;
	public static       LocalPlayer     player;
	public static       boolean         isThirdPersonEnabled = false;
	public static       ExpSmoothVec2   smoothOffsetRatio    = new ExpSmoothVec2().setValue(0, 0);
	public static       double          lastTickTime         = 0;
	public static       boolean         isAiming             = false;
	// 上次转动视角的时间
	public static       double          lastTurnTime         = 0;
	/**
	 * 虚相机到平滑眼睛的距离
	 */
	public static       ExpSmoothDouble smoothDistance       = new ExpSmoothDouble().setValue(0).setTarget(0);
	public static       Vec2            relativeRotation     = Vec2.ZERO;

	public static boolean isAvailable () {
		if (!Config.is_mod_enable) {
			return false;
		}
		Minecraft mc     = Minecraft.getInstance();
		Camera    camera = mc.gameRenderer.getMainCamera();
		if (!camera.isInitialized()) {
			return false;
		}
		LocalPlayer player = mc.player;
		return player != null;
	}

	public static void reset () {//TODO
		Minecraft mc = Minecraft.getInstance();
		player = mc.player;
		assert player != null;
		camera = mc.gameRenderer.getMainCamera();
		smoothOffsetRatio.setValue(0, 0);
		smoothDistance.setValue(0);
		LOGGER.info("Reset CameraAgent");
	}

	public static void updateUserProfile (CameraOffsetProfile profile) {
		smoothOffsetRatio.setSmoothFactor(profile.getMode().offsetSmoothFactor);
		smoothDistance.setSmoothFactor(profile.getMode().distanceSmoothFactor);
		LOGGER.info("CameraAgent: updateUserProfile");
	}

	/**
	 * 根据 相对朝向 和距离 计算真正的位置
	 */
	private static Vec3 relativesToAbsolutePosition () {
		return Vec3.directionFromRotation(relativeRotation)
				   .scale(smoothDistance.getValue())
				   .add(PlayerAgent.smoothEyePosition.getValue());
	}

	/**
	 * 鼠标移动导致的相机旋转
	 *
	 * @param turnY 水平角变化量
	 * @param turnX 俯仰角变化量
	 */
	public static void onCameraTurn (double turnY, double turnX) {
		turnY *= 0.15;
		turnX *= -0.15;
		if (turnY != 0 || turnX != 0) {
			lastTurnTime     = Blaze3D.getTime();
			relativeRotation = new Vec2((float)Mth.clamp(relativeRotation.x + turnX, -89.8, 89.8),
										(float)(relativeRotation.y + turnY) % 360f);
		}
	}

	/**
	 * 进入第三人称视角时触发
	 */
	public static void onEnterThirdPerson (float lerpK) {
		reset();
		PlayerAgent.reset();
		isThirdPersonEnabled     = true;
		isAiming                 = false;
		Options.isToggleToAiming = false;
		lastTickTime             = Blaze3D.getTime();
		LOGGER.info("Enter third person, lerpK={}", lerpK);
	}

	/**
	 * 计算并更新相机的朝向和坐标
	 *
	 * @param level  维度
	 * @param entity 实体
	 */
	@PerformanceSensitive
	public static void onRenderTick (BlockGetter level, Entity entity, boolean isMirrored, float lerpK) {
		CameraAgent.level = level;
		player            = (LocalPlayer)entity;
		// 时间
		double now           = Blaze3D.getTime();
		double sinceLastTurn = now - lastTurnTime;
		double sinceLastTick = now - lastTickTime;
		lastTickTime = now;
		PlayerAgent.onRenderTick(lerpK, sinceLastTick);
		// TODO public static CameraOffsetProfile profile;
		CameraOffsetProfile profile = UserProfile.getCameraOffsetProfile();
		profile.setAiming(PlayerAgent.isAiming());
		if (isThirdPersonEnabled) {
			// 平滑更新距离
			smoothDistance.setTarget(profile.getMode().maxDistance).update(sinceLastTick);
			// 如果是非瞄准模式下，且距离过远则强行放回去
			if (!profile.isAiming) {
				smoothDistance.setValue(Math.min(profile.getMode().maxDistance, smoothDistance.getValue()));
			}
			// 平滑更新相机偏移量
			smoothOffsetRatio.setTarget(profile.getMode().getOffsetRatio(smoothDistance.getValue())).update(sinceLastTick);
			// 设置相机朝向和位置
			calculateCameraRotationPosition();
			// TODO 防止穿墙
		}
	}

	/**
	 * 根据偏移量计算相机实际位置
	 */
	private static void calculateCameraRotationPosition () {
		Minecraft mc = Minecraft.getInstance();
		// 设置相机朝向
		((CameraInvoker)camera).invokeSetRotation(relativeRotation.y + 180, -relativeRotation.x);
		// 平滑眼睛到虚相机的向量
		Vec3 eyeToVirtualCamera = Vec3.directionFromRotation(relativeRotation).scale(smoothDistance.getValue());
		// 没有偏移的情况下相机位置
		Vec3   virtualPosition = eyeToVirtualCamera.add(PlayerAgent.smoothEyePosition.getValue());
		double aspectRatio     = (double)mc.getWindow().getWidth() / mc.getWindow().getHeight();
		double halfV           = mc.options.fov().get() * Math.PI / 180;              // 垂直视野角度(弧度制）
		double halfH           = 2 * Math.atan(aspectRatio * Math.tan(halfV / 2));    // 水平视野角度(弧度制）
		double leftOffset      = smoothDistance.getValue() * Math.tan(smoothOffsetRatio.getValue().x * halfV / 2);
		double upOffset        = smoothDistance.getValue() * Math.tan(smoothOffsetRatio.getValue().y * halfH / 2);
		((CameraInvoker)camera).invokeSetPosition(virtualPosition);
		((CameraInvoker)camera).invokeMove(0, upOffset, leftOffset);
	}
}
