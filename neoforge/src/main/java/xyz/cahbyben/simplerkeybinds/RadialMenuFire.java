package xyz.cahbyben.simplerkeybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RadialMenuFire {

    private static final Set<KeyMapping> pending = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void queue(KeyMapping km) {
        pending.add(km);
    }

    public static boolean consume(KeyMapping km) {
        return pending.remove(km);
    }

    // KeyMapping.get(String) doesn't exist before 1.21.9 (the backing ALL/MAP lookup was private),
    // so look the binding up by name from the public keyMappings array instead. Works on every version.
    public static KeyMapping findByName(String name) {
        for (KeyMapping km : Minecraft.getInstance().options.keyMappings) {
            if (km.getName().equals(name)) return km;
        }
        return null;
    }

    // Minecraft.setScreen moved to Minecraft.gui.setScreen() at 26.2.
    //? if >=26.2 {
    public static void openScreen(Minecraft client, Screen screen) { client.gui.setScreen(screen); }
    //?} else {
    /*public static void openScreen(Minecraft client, Screen screen) { client.setScreen(screen); }
    *///?}
}
