package me.andreasmelone.fmschattweaks.client.windows;

import me.andreasmelone.fmschattweaks.client.FermentMilksChatTweaksClient;
import me.andreasmelone.minimized.api.ctx.WindowContext;
import me.andreasmelone.minimized.api.window.AbstractViewportWindow;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ChatManagementWindow extends AbstractViewportWindow<ChatScreen> {
    @Override
    public void onOpen(WindowContext<ChatScreen> context) {
        context.addRenderableWidget(Button.builder(getButtonMessage(FermentMilksChatTweaksClient.HIDE_CHAT), (b) -> {
            FermentMilksChatTweaksClient.HIDE_CHAT = !FermentMilksChatTweaksClient.HIDE_CHAT;
            b.setMessage(getButtonMessage(FermentMilksChatTweaksClient.HIDE_CHAT));
        }).bounds(5, 5, 80, 20).build());
    }

    @Override
    protected float getContentWidth() {
        return 90;
    }

    @Override
    protected float getContentHeight() {
        return 40;
    }

    @Override
    public Component getName(float v) {
        return Component.literal("management");
    }

    @Override
    public @NotNull CompoundTag serializeAdditionalData() {
        CompoundTag tag = super.serializeAdditionalData();
        tag.putBoolean("hideChat", FermentMilksChatTweaksClient.HIDE_CHAT);
        return tag;
    }

    @Override
    public void deserializeAdditionalData(CompoundTag tag) {
        super.deserializeAdditionalData(tag);
        FermentMilksChatTweaksClient.HIDE_CHAT = !tag.contains("hideChat") || tag.getBoolean("hideChat");
    }

    private static Component getButtonMessage(boolean isHidden) {
        return Component.literal(isHidden ? "Show chat" : "Hide chat");
    }
}
