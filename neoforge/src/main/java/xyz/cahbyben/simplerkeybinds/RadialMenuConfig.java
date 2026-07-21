package xyz.cahbyben.simplerkeybinds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class RadialMenuConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simplerkeybinds.MODID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("simplerkeybinds.json");

    // Up to 3 wheels, each with up to 4 slots
    public String[][] wheelBinds       = new String[3][4];
    public String[][] wheelSlotNames   = new String[3][4]; // custom display names, null = use binding name
    // The key name (e.g. "key.jump") that opens each wheel; null = unset
    public String[] wheelActivationKeys = new String[3];

    public static RadialMenuConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                return GSON.fromJson(r, RadialMenuConfig.class);
            } catch (IOException e) {
                LOGGER.warn("Failed to load config, using defaults", e);
            }
        }
        return new RadialMenuConfig();
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, w);
        } catch (IOException e) {
            LOGGER.warn("Failed to save config", e);
        }
    }
}
