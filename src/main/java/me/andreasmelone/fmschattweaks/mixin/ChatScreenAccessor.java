package me.andreasmelone.fmschattweaks.mixin;

import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatScreen.class)
public interface ChatScreenAccessor {
    @Invoker("setChatLine")
    void invokeSetChatLine(String chatLine);
}
