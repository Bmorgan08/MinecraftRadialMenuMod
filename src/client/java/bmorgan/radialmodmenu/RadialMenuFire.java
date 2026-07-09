package bmorgan.radialmodmenu;

import net.minecraft.client.KeyMapping;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RadialMenuFire {

    private static final Set<KeyMapping> pending = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void queue(KeyMapping km) {
        pending.add(km);
    }

    // Called from mixin — returns true once per queued mapping
    public static boolean consume(KeyMapping km) {
        return pending.remove(km);
    }
}
