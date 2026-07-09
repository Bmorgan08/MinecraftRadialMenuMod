package bmorgan.radialmodmenu;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class RadialMenuScreen extends Screen {

    private final RadialMenuHud hud;
    private int lastMouseX, lastMouseY;

    public RadialMenuScreen(RadialMenuHud hud) {
        super(Component.empty());
        this.hud = hud;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        hud.renderWheel(gfx, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        // Keep the game world visible behind the wheel
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean handled) {
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            fireHoveredSlot((int) event.x(), (int) event.y());
            return true;
        }
        return super.mouseClicked(event, handled);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();
        if (key == GLFW.GLFW_KEY_1) { hud.switchWheel(0); return true; }
        if (key == GLFW.GLFW_KEY_2) { hud.switchWheel(1); return true; }
        if (key == GLFW.GLFW_KEY_3) { hud.switchWheel(2); return true; }
        if (key == GLFW.GLFW_KEY_C) {
            minecraft.setScreen(new RadialMenuConfigScreen(hud));
            return true;
        }
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            onClose();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        // Fire and close when the open-menu key is released
        if (RadialKeybindMenuClient.openMenuKey.matches(event)) {
            fireHoveredSlot(lastMouseX, lastMouseY);
            return true;
        }
        return false;
    }

    private void fireHoveredSlot(int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        int cx = mc.getWindow().getGuiScaledWidth() / 2;
        int cy = mc.getWindow().getGuiScaledHeight() / 2;
        int slot = hud.getHoveredSlot(mouseX, mouseY, cx, cy);
        if (slot >= 0) {
            String bindName = RadialMenuHud.config.wheelBinds[hud.getActiveWheel()][slot];
            if (bindName != null && !bindName.isEmpty()) {
                KeyMapping km = KeyMapping.get(bindName);
                if (km != null) RadialMenuFire.queue(km);
            }
        }
        onClose();
    }
}
