package com.github.sib_energy_craft.farming.feeding_station.screen;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.screen.ScreenSquareArea;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class FeedingStationScreen extends HandledScreen<FeedingStationScreenHandler> {
    private static final Identifier TEXTURE = Identifiers.of("textures/gui/container/feeding_station.png");

    private static final ScreenSquareArea CHARGE = new ScreenSquareArea(45, 36, 7, 13);
    private static final ScreenSquareArea MODE_BUTTON = new ScreenSquareArea(40, 16, 18, 18);
    private static final ScreenSquareArea MODE_ICON = new ScreenSquareArea(40, 16, 18, 18);

    public FeedingStationScreen(@NotNull FeedingStationScreenHandler handler,
                                @NotNull PlayerInventory inventory,
                                @NotNull Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(@NotNull MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.x;
        int y = this.y;
        drawTexture(matrixStack, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int progress = this.handler.getChargeProgress();
        drawTexture(matrixStack, x + CHARGE.x(), y + CHARGE.y(), 176, 0, CHARGE.width(), progress);

        var energyMachineState = handler.getEnergyMachineState();
        var mode = energyMachineState.getFeedingStationMode();
        var modeCode = mode.name().toLowerCase();
        drawTexture(matrixStack, x + MODE_BUTTON.x(), y  + MODE_BUTTON.y(), 183, MODE_BUTTON.height(),
                MODE_BUTTON.width(), MODE_BUTTON.height());
        drawTexture(matrixStack, x + MODE_ICON.x(), y  + MODE_ICON.y(), 201, MODE_ICON.height() * mode.ordinal(),
                MODE_ICON.width(), MODE_ICON.height());

        if(CHARGE.in(x, y, mouseX, mouseY)) {
            int charge = this.handler.getCharge();
            int maxCharge = this.handler.getMaxCharge();
            var charging = Text.translatable("energy.range.text", charge, maxCharge);
            renderTooltip(matrixStack, charging, mouseX, mouseY);
        }
        if (MODE_BUTTON.in(x, y, mouseX, mouseY)) {
            drawTexture(matrixStack, x + MODE_BUTTON.x(), y  + MODE_BUTTON.y(),
                    183, MODE_BUTTON.height() * 2,
                    MODE_BUTTON.width(), MODE_BUTTON.height());
            var key = "screen.sib_energy_craft.feeding_station.button.mode.%s".formatted(modeCode);
            var modeText = Text.translatable(key);
            renderTooltip(matrixStack, modeText, mouseX, mouseY);
        }
    }

    @Override
    public void render(@NotNull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var client = this.client;
        if (client == null) {
            return false;
        }
        var interactionManager = client.interactionManager;
        if (interactionManager == null) {
            return false;
        }

        if (MODE_BUTTON.in(x, y, mouseX, mouseY)) {
            if(client.player != null) {
                this.handler.onButtonClick(FeedingStationButtons.CHANGE_MODE);
            }
            interactionManager.clickButton(this.handler.syncId, FeedingStationButtons.CHANGE_MODE.ordinal());
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}