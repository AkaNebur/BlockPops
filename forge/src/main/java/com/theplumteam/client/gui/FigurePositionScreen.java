package com.theplumteam.client.gui;

import com.theplumteam.forge.BlockPopsModForge;
import com.theplumteam.network.FigurePositionPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FigurePositionScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(FigurePositionScreen.class);

    private final BlockPos blockPos;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double scale;

    private AbstractSliderButton sliderX;
    private AbstractSliderButton sliderY;
    private AbstractSliderButton sliderZ;
    private AbstractSliderButton sliderScale;

    public FigurePositionScreen(BlockPos blockPos, double offsetX, double offsetY, double offsetZ, double scale) {
        super(Component.literal("Adjust Figure Position"));
        this.blockPos = blockPos;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.scale = scale;
        LOGGER.info("FigurePositionScreen opened at {} with offsets: X={}, Y={}, Z={}, Scale={}",
                    blockPos, offsetX, offsetY, offsetZ, scale);
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;

        // X Offset Slider (-1 to 1)
        this.sliderX = new AbstractSliderButton(centerX - 100, startY, 200, 20,
                Component.literal("X Offset: " + String.format("%.2f", offsetX)),
                (offsetX + 1.0) / 2.0) {
            @Override
            protected void updateMessage() {
                offsetX = (this.value * 2.0) - 1.0;
                this.setMessage(Component.literal("X Offset: " + String.format("%.2f", offsetX)));
                sendUpdate(); // Update in real-time
            }

            @Override
            protected void applyValue() {
                offsetX = (this.value * 2.0) - 1.0;
                sendUpdate(); // Update in real-time
            }
        };
        this.addRenderableWidget(sliderX);

        // Y Offset Slider (-1 to 1)
        this.sliderY = new AbstractSliderButton(centerX - 100, startY + 30, 200, 20,
                Component.literal("Y Offset: " + String.format("%.2f", offsetY)),
                (offsetY + 1.0) / 2.0) {
            @Override
            protected void updateMessage() {
                offsetY = (this.value * 2.0) - 1.0;
                this.setMessage(Component.literal("Y Offset: " + String.format("%.2f", offsetY)));
                sendUpdate(); // Update in real-time
            }

            @Override
            protected void applyValue() {
                offsetY = (this.value * 2.0) - 1.0;
                sendUpdate(); // Update in real-time
            }
        };
        this.addRenderableWidget(sliderY);

        // Z Offset Slider (-1 to 1)
        this.sliderZ = new AbstractSliderButton(centerX - 100, startY + 60, 200, 20,
                Component.literal("Z Offset: " + String.format("%.2f", offsetZ)),
                (offsetZ + 1.0) / 2.0) {
            @Override
            protected void updateMessage() {
                offsetZ = (this.value * 2.0) - 1.0;
                this.setMessage(Component.literal("Z Offset: " + String.format("%.2f", offsetZ)));
                sendUpdate(); // Update in real-time
            }

            @Override
            protected void applyValue() {
                offsetZ = (this.value * 2.0) - 1.0;
                sendUpdate(); // Update in real-time
            }
        };
        this.addRenderableWidget(sliderZ);

        // Scale Slider (0.1 to 2.0)
        this.sliderScale = new AbstractSliderButton(centerX - 100, startY + 90, 200, 20,
                Component.literal("Scale: " + String.format("%.2f", scale)),
                (scale - 0.1) / 1.9) {
            @Override
            protected void updateMessage() {
                scale = 0.1 + (this.value * 1.9);
                this.setMessage(Component.literal("Scale: " + String.format("%.2f", scale)));
                sendUpdate(); // Update in real-time
            }

            @Override
            protected void applyValue() {
                scale = 0.1 + (this.value * 1.9);
                sendUpdate(); // Update in real-time
            }
        };
        this.addRenderableWidget(sliderScale);

        // Reset Button
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), button -> {
            offsetX = 0.0;
            offsetY = 0.1;
            offsetZ = 0.0;
            scale = 1.0;
            this.rebuildWidgets();
            sendUpdate();
        }).bounds(centerX - 100, startY + 120, 95, 20).build());

        // Done Button
        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            this.onClose();
        }).bounds(centerX + 5, startY + 120, 95, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Draw title
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw current values
        int centerX = this.width / 2;
        int startY = this.height / 2 - 80;
        guiGraphics.drawString(this.font, "Adjust the sliders to position the figure", centerX - 80, startY, 0xAAAAAA);
    }

    @Override
    public void removed() {
        super.removed();
        // Updates are already sent in real-time by the sliders
    }

    private void sendUpdate() {
        // Send packet to server with new values
        LOGGER.info("Sending update - Position: {}, Offsets: X={}, Y={}, Z={}, Scale={}",
                    blockPos, offsetX, offsetY, offsetZ, scale);
        FigurePositionPacket packet = new FigurePositionPacket(blockPos, offsetX, offsetY, offsetZ, scale);
        BlockPopsModForge.NETWORK_CHANNEL.sendToServer(packet);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
