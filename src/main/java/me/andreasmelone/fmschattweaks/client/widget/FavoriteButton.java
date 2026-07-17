package me.andreasmelone.fmschattweaks.client.widget;

import me.andreasmelone.fmschattweaks.FermentMilksChatTweaks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FavoriteButton extends Button {
    private static final ResourceLocation FAVORITE_BUTTON_SPRITE = FermentMilksChatTweaks.id("textures/gui/button/favorite.png");
    private static final int TEXTURE_SIZE = 20;

    protected boolean isFavorite = false;

    public FavoriteButton(int pX, int pY, OnPress pOnPress) {
        super(pX, pY, 20, 20, CommonComponents.EMPTY, pOnPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderTexture(
                graphics, FAVORITE_BUTTON_SPRITE,
                getX(), getY(),
                isFavorite ? TEXTURE_SIZE : 0, 0,
                TEXTURE_SIZE,
                TEXTURE_SIZE, TEXTURE_SIZE,
                64, 64
        );
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
