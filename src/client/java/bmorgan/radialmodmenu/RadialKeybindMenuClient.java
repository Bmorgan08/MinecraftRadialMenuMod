package bmorgan.radialmodmenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class RadialKeybindMenuClient implements ClientModInitializer {

    public static KeyMapping openMenuKey;
    public static KeyMapping configMenuKey;
    public static RadialMenuHud hud;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.radialkeybindmenu.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KeyMapping.Category.MISC
        ));

        configMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.radialkeybindmenu.config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,  // unbound by default
            KeyMapping.Category.MISC
        ));

        hud = new RadialMenuHud();
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(RadialKeybindMenu.MOD_ID, "radial_menu"),
            hud
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen instanceof RadialMenuScreen) {
                    client.setScreen(null);
                } else if (client.screen == null) {
                    client.setScreen(new RadialMenuScreen(hud));
                }
            }
            while (configMenuKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new RadialMenuConfigScreen(hud));
                }
            }
        });
    }
}
