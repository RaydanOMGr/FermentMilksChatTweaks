package me.andreasmelone.fmschattweaks.client.windows;

import me.andreasmelone.fmschattweaks.client.quick.QuickCommandsManager;
import me.andreasmelone.fmschattweaks.client.widget.ClickableLabel;
import me.andreasmelone.fmschattweaks.mixin.ChatScreenAccessor;
import me.andreasmelone.minimized.api.ctx.WindowContext;
import me.andreasmelone.minimized.api.window.AbstractViewportWindow;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class QuickCommandsWindow extends AbstractViewportWindow<ChatScreen> {
    private final static int PADDING_LABELS = 5;

    private Consumer<String> onAddedListener;
    private Consumer<String> onRemovedListener;

    private int width;
    private int height;

    @Override
    public void onOpen(WindowContext<ChatScreen> context) {
        QuickCommandsManager.getInstance().onAdd(this.onAddedListener = (s) -> initLabels(context));
        QuickCommandsManager.getInstance().onRemove(this.onRemovedListener = (String s) -> initLabels(context));

        initLabels(context);
    }

    @Override
    public void onClose() {
        QuickCommandsManager.getInstance().removeOnAdd(this.onAddedListener);
        QuickCommandsManager.getInstance().removeOnRemove(this.onRemovedListener);
    }

    private void initLabels(WindowContext<ChatScreen> context) {
        context.clearWidgets();

        int i = 0;
        for (String quickCommand : QuickCommandsManager.getInstance().getQuickCommands()) {
            ClickableLabel label = new ClickableLabel(
                    5, PADDING_LABELS * (i+1) + context.font().lineHeight * i,
                    Component.literal(quickCommand), context.font(),
                    (s) -> {
                        if(isHoldingShift()) {
                            context.screen().handleChatInput(s.getMessage().getString(), true);
                        } else {
                            ((ChatScreenAccessor)context.screen()).invokeSetChatLine(s.getMessage().getString());
                        }
                    }
            );
            context.addRenderableWidget(label);
            this.width = Math.max(this.width, label.getWidth() + 10);
            i++;
        }
        height = PADDING_LABELS * (i+1) + context.font().lineHeight * i;
    }

    @Override
    public Component getName(float partialTick) {
        return Component.literal("commands");
    }

    @Override
    protected float getContentWidth() {
        return width;
    }

    @Override
    protected float getContentHeight() {
        return height;
    }
}
