package com.lumengrid.oritechthings.client.screen;

import static com.lumengrid.oritechthings.main.OritechThings.MOD_ID;

import com.lumengrid.oritechthings.client.screen.component.ToggleButton;
import com.lumengrid.oritechthings.main.OritechThings;
import com.lumengrid.oritechthings.menu.AcceleratorSpeedSensorMenu;
import com.lumengrid.oritechthings.network.packet.UpdateSpeedSensorC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("null")
public class AcceleratorSpeedSensorScreen extends AbstractContainerScreen<AcceleratorSpeedSensorMenu> {
    private final Component title = Component.translatable("gui.oritechthings.particle_accelerator_speed_sensor.title");
    public static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(OritechThings.MOD_ID,
            "textures/gui/speed_sensor.png");

    public AcceleratorSpeedSensorScreen(AcceleratorSpeedSensorMenu pMenu, Inventory pPlayerInventory,
            Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        gui.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0XFFFFFF);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelY = 10000;

        // < > TOGGLE
        addRenderableWidget(new ToggleButton(
                leftPos + 40, topPos + 46, 17, 17,
                Component.literal(menu.be.isCheckGreater() ? ">" : "<"),
                button -> {
                    boolean newState = !menu.be.isCheckGreater();
                    PacketDistributor.sendToServer(new UpdateSpeedSensorC2SPacket(menu.be.getBlockPos(),
                            menu.be.getSpeedLimit(), menu.be.isEnabled(), newState));
                    button.setMessage(Component.literal(newState ? ">" : "<"));
                },
                menu.be.isEnabled(),
                0xFF000000,
                0xFF000000));

        // SPEED INPUT
        EditBox speedInput = new EditBox(this.font, this.leftPos + 60, this.topPos + 45,
                45, 20, Component.translatable("gui.oritechthings.particle_accelerator_speed_sensor.speed_input"));
        speedInput.setMaxLength(5);
        speedInput.setFilter(s -> s.matches("\\d*") && !s.isEmpty());
        speedInput.setValue(String.valueOf(menu.be.getSpeedLimit()));
        speedInput.setResponder(this::onSpeedEntered);
        addRenderableWidget(speedInput);

        // ON OFF TOGGLE
        MutableComponent toggleButton = Component
                .translatable("tooltip." + MOD_ID + ".state." + (menu.be.isEnabled() ? "on" : "off"));

        addRenderableWidget(new ToggleButton(
                leftPos + 130, topPos + 45, 30, 18,
                toggleButton,
                button -> {
                    boolean newState = !menu.be.isEnabled();
                    PacketDistributor.sendToServer(new UpdateSpeedSensorC2SPacket(menu.be.getBlockPos(),
                            menu.be.getSpeedLimit(), newState, menu.be.isCheckGreater()));
                    button.setMessage(toggleButton);
                },
                menu.be.isEnabled(),
                0xFF93c47d,
                0xFFe06666));
    }

    private void onSpeedEntered(String input) {
        try {
            int value = Integer.parseInt(input);
            value = Math.max(0, Math.min(value, 20000));
            PacketDistributor.sendToServer(new UpdateSpeedSensorC2SPacket(menu.be.getBlockPos(), value,
                    menu.be.isEnabled(), menu.be.isCheckGreater()));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(
                BACKGROUND,
                x,
                y,
                0,
                0,
                176,
                166,
                256,
                256);
        renderParticleAcceleratorMessage(guiGraphics);
    }

    private void renderParticleAcceleratorMessage(GuiGraphics guiGraphics) {
        guiGraphics.drawString(
                this.font,
                Component.translatable("gui.oritechthings.particle_accelerator_speed_sensor.controller"),
                leftPos + 10,
                topPos + 20,
                0xFFFFFF);
        if (menu.be.getTargetDesignator() == null) {
            guiGraphics.drawString(
                    this.font,
                    Component.translatable("gui.oritechthings.particle_accelerator_speed_sensor.controller_not_set")
                            .withStyle(style -> style.withColor(0xFF5555).withBold(true)),
                    leftPos + 100,
                    topPos + 32,
                    0xFF5555);
        } else {
            BlockPos target = menu.be.getTargetDesignator();
            guiGraphics.drawString(
                    this.font,
                    Component.literal(target.toShortString()).withStyle(style -> style.withColor(0x0000FF)),
                    leftPos + 40,
                    topPos + 32,
                    0x0000FF);
        }
    }
}
