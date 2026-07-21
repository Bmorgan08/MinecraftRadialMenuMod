package xyz.cahbyben.simplerkeybinds;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
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

@Mod(Simplerkeybinds.MODID)
public class Simplerkeybinds {

    public static final String MODID = "simplerkeybinds";

    public static KeyMapping openMenuKey;
    public static KeyMapping configMenuKey;
    public static RadialMenuHud hud;

    public Simplerkeybinds(IEventBus modBus) {
        modBus.addListener(this::registerKeyMappings);
        modBus.addListener(this::registerGuiLayers);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        openMenuKey = new KeyMapping(
                "key.simplerkeybinds.open",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                //? if >=1.21.9 {
                KeyMapping.Category.MISC);
                //?} else {
                /*"key.categories.misc");
                *///?}

        configMenuKey = new KeyMapping(
                "key.simplerkeybinds.config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN, // unbound by default
                //? if >=1.21.9 {
                KeyMapping.Category.MISC);
                //?} else {
                /*"key.categories.misc");
                *///?}

        event.register(openMenuKey);
        event.register(configMenuKey);
    }

    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        hud = new RadialMenuHud();
        event.registerAboveAll(
                //? if >=1.21.11 {
                Identifier.fromNamespaceAndPath(MODID, "radial_menu"),
                //?} else {
                /*ResourceLocation.fromNamespaceAndPath(MODID, "radial_menu"),
                *///?}
                hud);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        while (openMenuKey.consumeClick()) {
            if (currentScreen(client) instanceof RadialMenuScreen) {
                setScreen(client, null);
            } else if (currentScreen(client) == null) {
                setScreen(client, new RadialMenuScreen(hud));
            }
        }
        while (configMenuKey.consumeClick()) {
            if (currentScreen(client) == null) {
                setScreen(client, new RadialMenuConfigScreen(hud));
            }
        }
    }

    // Minecraft.screen / Minecraft.setScreen moved to Minecraft.gui.screen() / Minecraft.gui.setScreen() at 26.2.
    //? if >=26.2 {
    static Screen currentScreen(Minecraft client) { return client.gui.screen(); }
    static void setScreen(Minecraft client, Screen screen) { client.gui.setScreen(screen); }
    //?} else {
    /*static Screen currentScreen(Minecraft client) { return client.screen; }
    static void setScreen(Minecraft client, Screen screen) { client.setScreen(screen); }
    *///?}
}
