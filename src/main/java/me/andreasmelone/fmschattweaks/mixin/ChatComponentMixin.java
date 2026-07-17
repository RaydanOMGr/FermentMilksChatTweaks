package me.andreasmelone.fmschattweaks.mixin;

import me.andreasmelone.fmschattweaks.client.FermentMilksChatTweaksClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @Inject(
            method = "isChatHidden",
            at = @At("RETURN"),
            cancellable = true
    )
    public void isChatHidden(CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValueZ()) {
            cir.setReturnValue(FermentMilksChatTweaksClient.HIDE_CHAT);
        }
    }
}
