package me.andreasmelone.fmschattweaks.mixin;

import me.andreasmelone.fmschattweaks.client.quick.QuickCommandsManager;
import me.andreasmelone.fmschattweaks.client.widget.FavoriteButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    @Shadow protected EditBox input;

    @Unique private FavoriteButton favoriteButton;

    protected ChatScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    public void initInject(CallbackInfo ci) {
        this.addRenderableWidget(favoriteButton = new FavoriteButton(this.width - 25, this.height - 50, (b) -> {
            if(favoriteButton.isFavorite()) {
                QuickCommandsManager.getInstance().removeQuickCommand(this.input.getValue());
                favoriteButton.setFavorite(false);
            } else {
                QuickCommandsManager.getInstance().addQuickCommand(this.input.getValue());
                favoriteButton.setFavorite(true);
            }
        }));
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD")
    )
    public void mouseClickedInject(double pMouseX, double pMouseY, int pButton, CallbackInfoReturnable<Boolean> cir) {
        if(this.getChildAt(pMouseX, pMouseY).isEmpty()) {
            this.setFocused(this.input);
        }
    }

    @Inject(
            method = "onEdited",
            at = @At("TAIL")
    )
    public void onEditInjected(String newMessage, CallbackInfo ci) {
        String s = this.input.getValue();
        favoriteButton.setFavorite(QuickCommandsManager.getInstance().getQuickCommands().contains(s));
    }

    @Inject(
            method = "moveInHistory",
            at = @At("HEAD")
    )
    public void moveInHistoryInjected(int pMsgPos, CallbackInfo ci) {
        this.setFocused(this.input);
    }
}
