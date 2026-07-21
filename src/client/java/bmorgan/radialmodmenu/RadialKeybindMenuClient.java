package bmorgan.radialmodmenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*/
//?}
//? if >=1.21.6 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*/
//?}
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*/
//?}
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class RadialKeybindMenuClient implements ClientModInitializer {

    public static KeyMapping openMenuKey;
    public static KeyMapping configMenuKey;
    public static RadialMenuHud hud;

    @Override
    public void onInitializeClient() {
        KeyMapping openMenuKeyMapping = new KeyMapping(
                "key.radialkeybindmenu.open",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                //? if >=1.21.9 {
                KeyMapping.Category.MISC);
                //?} else {
                /*"key.categories.misc");
                *///?}

        KeyMapping configMenuKeyMapping = new KeyMapping(
                "key.radialkeybindmenu.config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN, // unbound by default
                //? if >=1.21.9 {
                KeyMapping.Category.MISC);
                //?} else {
                /*"key.categories.misc");
                *///?}

        //? if >=26.1 {
        openMenuKey = KeyMappingHelper.registerKeyMapping(openMenuKeyMapping);
        configMenuKey = KeyMappingHelper.registerKeyMapping(configMenuKeyMapping);
        //?} else {
        /*openMenuKey = KeyBindingHelper.registerKeyBinding(openMenuKeyMapping);
        configMenuKey = KeyBindingHelper.registerKeyBinding(configMenuKeyMapping);
        *///?}

        hud = new RadialMenuHud();
        //? if >=1.21.6 {
        HudElementRegistry.addLast(
                //? if >=1.21.11 {
                Identifier.fromNamespaceAndPath(RadialKeybindMenu.MOD_ID, "radial_menu"),
                //?} else {
                /*ResourceLocation.fromNamespaceAndPath(RadialKeybindMenu.MOD_ID, "radial_menu"),
                *///?}
                hud);
        //?} else {
        /*HudRenderCallback.EVENT.register(hud::renderHudCallback);
        *///?}

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (currentScreen(client) instanceof RadialMenuScreen) {
                    RadialMenuFire.openScreen(client, null);
                } else if (currentScreen(client) == null) {
                    RadialMenuFire.openScreen(client, new RadialMenuScreen(hud));
                }
            }
            while (configMenuKey.consumeClick()) {
                if (currentScreen(client) == null) {
                    RadialMenuFire.openScreen(client, new RadialMenuConfigScreen(hud));
                }
            }
        });
    }

    // Minecraft.screen moved to Minecraft.gui.screen() at 26.2.
    //? if >=26.2 {
    private static Screen currentScreen(Minecraft client) { return client.gui.screen(); }
    //?} else {
    /*private static Screen currentScreen(Minecraft client) { return client.screen; }
    *///?}
}
