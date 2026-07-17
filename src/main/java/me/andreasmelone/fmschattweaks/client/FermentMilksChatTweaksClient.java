package me.andreasmelone.fmschattweaks.client;

import me.andreasmelone.fmschattweaks.FermentMilksChatTweaks;
import me.andreasmelone.fmschattweaks.client.quick.QuickCommandsManager;
import me.andreasmelone.fmschattweaks.client.windows.ChatManagementWindow;
import me.andreasmelone.fmschattweaks.client.windows.ColorsWindow;
import me.andreasmelone.fmschattweaks.client.windows.QuickCommandsWindow;
import me.andreasmelone.minimized.api.Minimized;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = FermentMilksChatTweaks.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FermentMilksChatTweaksClient {
    public static final Path QUICK_COMMANDS_CONFIG =
            FMLPaths.CONFIGDIR.get().resolve(FermentMilksChatTweaks.MODID).resolve("quick_commands.json").normalize();
    public static boolean HIDE_CHAT = false;

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new FermentMilksChatTweaksClient());

        Minimized.screen(ChatScreen.class)
                .addWindow(FermentMilksChatTweaks.id("colors"), ColorsWindow::new)
                .addWindow(FermentMilksChatTweaks.id("quickcommands"), QuickCommandsWindow::new)
                .addWindow(FermentMilksChatTweaks.id("management"), ChatManagementWindow::new);

        QuickCommandsManager.getInstance().init();

        QuickCommandsManager.getInstance().load();
        QuickCommandsManager.getInstance().save();
    }

    @SubscribeEvent
    public void onShutdown(GameShuttingDownEvent event) {
        QuickCommandsManager.getInstance().shutdown();
    }
}
