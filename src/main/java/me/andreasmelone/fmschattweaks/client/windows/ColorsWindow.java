package me.andreasmelone.fmschattweaks.client.windows;

import com.google.common.collect.ImmutableList;
import me.andreasmelone.minimized.api.ctx.WindowContext;
import me.andreasmelone.minimized.api.window.AbstractViewportWindow;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ColorsWindow extends AbstractViewportWindow<ChatScreen> {
    private static final int COLOR_PADDING_X = 5;
    private static final int COLOR_PADDING_Y = 5;

    private static final List<ChatFormatting> COLORS;

    private int minWidth;
    private int minHeight;

    private int longestFirstColumn, longestSecondColumn;

    @Override
    public void onOpen(WindowContext<ChatScreen> ctx) {
        for (int i = 0; i < COLORS.size(); i++) {
            int width = ctx.font().width(getNameComponentForColor(COLORS.get(i)));

            if ((i & 1) == 0) {
                this.longestFirstColumn = Math.max(this.longestFirstColumn, width);
            } else {
                this.longestSecondColumn = Math.max(this.longestSecondColumn, width);
            }
        }
        this.minWidth = longestFirstColumn + longestSecondColumn + (COLOR_PADDING_X * 3);
        this.minHeight = ((int)Math.ceil((double)COLORS.size() / 2)) * (COLOR_PADDING_Y + ctx.font().lineHeight);
    }

    @Override
    public Component getName(float partialTick) {
        return Component.literal("colors");
    }

    @Override
    public void render(WindowContext<ChatScreen> ctx, GuiGraphics graphics, float partialTick) {
        for (int i = 0; i < COLORS.size(); i++) {
            ChatFormatting color = COLORS.get(i);
            boolean isEven = (i & 1) == 0;

            int x = COLOR_PADDING_X;
            x += isEven ? 0 : (longestFirstColumn + COLOR_PADDING_X);
            int y = ((i >> 1) + 1) * COLOR_PADDING_Y + ctx.font().lineHeight * (i >> 1);

            MutableComponent text = getNameComponentForColor(color);
//            graphics.fill(x, y, x + (isEven ? longestFirstColumn : longestSecondColumn), y + ctx.font().lineHeight, (0xFFFFFFFF - color.getColor()) | 0xFF000000);
            graphics.drawString(ctx.font(), text, x, y, 0xFFFFFFFF);
        }
    }

    @Override
    public Result keyPressed(WindowContext<ChatScreen> context, int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) return Result.PASS;
        return Result.CONSUME;
    }

    @Override
    public Result keyReleased(WindowContext<ChatScreen> context, int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) return Result.PASS;
        return Result.CONSUME;
    }

    @Override
    protected float getContentWidth() {
        return minWidth;
    }

    @Override
    protected float getContentHeight() {
        return minHeight;
    }

    private static @NotNull MutableComponent getNameComponentForColor(ChatFormatting color) {
        return Component.literal("&" + color.getChar() + " - " + color.getName()).withStyle(color);
    }

    static {
        List<ChatFormatting> colors = new ArrayList<>();
        for (ChatFormatting potentialColor : ChatFormatting.values()) {
            if(potentialColor.isColor()) {
                colors.add(potentialColor);
            }
        }

        COLORS = ImmutableList.copyOf(colors);
    }
}
