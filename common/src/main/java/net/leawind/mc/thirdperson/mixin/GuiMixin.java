package net.leawind.mc.thirdperson.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.leawind.mc.thirdperson.ThirdPersonStatus;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value=Gui.class, priority=2000)
public class GuiMixin {
	@ModifyExpressionValue(method="renderCrosshair", at=@At(value="INVOKE", target="Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
	public boolean r (boolean isFirstPersonReally) {
		return isFirstPersonReally || ThirdPersonStatus.shouldRenderCrosshair();
	}
}
