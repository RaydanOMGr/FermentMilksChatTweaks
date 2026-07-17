package me.andreasmelone.fmschattweaks.client.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ClickableLabel extends AbstractStringWidget {
    private final Consumer<ClickableLabel> onClickListener;

    public ClickableLabel(int x, int y, Component message, Font font, Consumer<ClickableLabel> onClickListener) {
        super(x, y, font.width(message), font.lineHeight, message, font);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawString(getFont(), getMessage(), this.getX(), this.getY(), isHovered() ? 0xFFFFFF00 : getColor());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onClickListener.accept(this);
    }

    @Override
    public void setMessage(@NotNull Component message) {
        super.setMessage(message);
        this.width = getFont().width(message);
    }
}
