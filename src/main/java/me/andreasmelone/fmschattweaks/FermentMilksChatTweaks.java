package me.andreasmelone.fmschattweaks;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(value = FermentMilksChatTweaks.MODID)
public class FermentMilksChatTweaks {
    public static final String MODID = "fmschattweks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FermentMilksChatTweaks() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
