package net.leawind.mc.thirdperson;


import net.leawind.mc.thirdperson.api.config.Config;
import net.leawind.mc.thirdperson.api.core.rotation.SmoothType;
import net.leawind.mc.thirdperson.impl.core.rotation.RotateTarget;
import net.leawind.mc.util.math.vector.api.Vector2d;
import net.leawind.mc.util.math.vector.api.Vector3d;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public final class ThirdPersonStatus {
	public static final @NotNull Vector3d impulse                              = Vector3d.of(0);
	public static final @NotNull Vector2d impulseHorizon                       = Vector2d.of(0);
	/**
	 * @see ThirdPersonKeys#TOGGLE_AIMING
	 */
	public static                boolean  isToggleToAiming                     = false;
	/**
	 * 最近一次 renderTick 的 partialTick
	 * <p>
	 * 在{@link ThirdPersonEvents#onPreRender(float)}开头更新
	 */
	public static                float    lastPartialTick                      = 1;
	public static                double   lastRenderTickTimeStamp              = 0;
	/**
	 * 上一tick中是否以第三人称视角渲染 mc.options.cameraType.isThirdPerson()
	 */
	public static                boolean  wasRenderInThirdPersonLastRenderTick = false;
	/**
	 * 在第三人称视角下暂时使用第一人称视角
	 */
	public static                boolean  isTemporaryFirstPerson               = false;
	/**
	 * 是否正在从第三人称过渡到第一人称
	 */
	public static                boolean  isTransitioningToFirstPerson         = false;
	/**
	 * 在 {@link ThirdPersonEvents#onPreRender} 中更新
	 *
	 * @see ThirdPersonStatus#shouldCameraTurnWithEntity
	 */
	public static                boolean  wasSouldCameraTurnWithEntity         = false;

	/**
	 * 是否正在调整摄像机偏移量
	 */
	public static boolean isAdjustingCameraOffset () {
		return isAdjustingCameraDistance();
	}

	/**
	 * 检查相机距离是否正在调整。
	 */
	public static boolean isAdjustingCameraDistance () {
		return ThirdPerson.isAvailable() && isRenderingInThirdPerson() && ThirdPersonKeys.ADJUST_POSITION.isDown();
	}

	/**
	 * 当前是否以第三人称渲染
	 */
	public static boolean isRenderingInThirdPerson () {
		return !ThirdPerson.mc.options.getCameraType().isFirstPerson();
	}

	/**
	 * 当前是否显示准星
	 */
	public static boolean shouldRenderCrosshair () {
		Config config = ThirdPerson.getConfig();
		return ThirdPerson.isAvailable() && (ThirdPerson.ENTITY_AGENT.wasAiming() ? config.render_crosshair_when_aiming: config.render_crosshair_when_not_aiming);
	}

	/**
	 * 根据玩家的按键判断玩家是否想瞄准
	 */
	public static boolean doesPlayerWantToAim () {
		return isToggleToAiming || ThirdPersonKeys.FORCE_AIMING.isDown();
	}

	/**
	 * 探测射线是否应当起始于相机处，而非玩家眼睛处
	 */
	public static boolean shouldPickFromCamera () {
		if (!ThirdPerson.ENTITY_AGENT.isCameraEntityExist()) {
			return false;
		} else if (!ThirdPerson.getConfig().use_camera_pick_in_creative) {
			return false;
		}
		return ThirdPerson.ENTITY_AGENT.getRawCameraEntity() instanceof Player player && player.isCreative();
	}

	/**
	 * 根据不透明度判断是否需要渲染相机实体
	 *
	 * @return 是否应当渲染相机实体
	 */
	public static boolean shouldRenderCameraEntity () {
		return ThirdPerson.ENTITY_AGENT.getSmoothOpacity() > ThirdPersonConstants.RENDERED_OPACITY_THRESHOLD_MIN;
	}

	/**
	 * 根据不透明度判断是否以原版方式渲染相机实体
	 *
	 * @return 是否以原版方式渲染相机实体
	 */
	public static boolean shouldRenderCameraEntityInVanilla () {
		return ThirdPerson.ENTITY_AGENT.getSmoothOpacity() > ThirdPersonConstants.RENDERED_OPACITY_THRESHOLD_MAX;
	}

	/**
	 * 第三人称下，通常是直接用鼠标控制相机的朝向 CameraAgentImpl#relativeRotation，再根据一些因素决定玩家的朝向。
	 * <p>
	 * 但为了与另一个模组 Do a Barrel Roll 兼容，在特定情况下，允许直接用鼠标控制玩家朝向，而相机跟随玩家旋转。
	 */
	public static boolean shouldCameraTurnWithEntity () {
		return ThirdPerson.ENTITY_AGENT.getRotateTarget() == RotateTarget.CAMERA_ROTATION && ThirdPerson.ENTITY_AGENT.getRotationSmoothType() == SmoothType.HARD;
	}
}
