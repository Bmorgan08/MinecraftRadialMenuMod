pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.7"
    // Applies the correct fabric-loom / fabric-loom-remap variant per Minecraft version automatically.
    id("dev.kikugie.loom-back-compat") version "0.4.1"
}

stonecutter {
    create(rootProject) {
        // versions("<name>") is "<minecraft version>", grouped by API tier:
        //   tier A  (1.20.0-1.21.5):  old HudRenderCallback HUD, old Screen input methods,
        //                              string KeyMapping category, ResourceLocation
        //   tier B  (1.21.6-1.21.8):  HudElementRegistry HUD, old Screen input methods,
        //                              string KeyMapping category, ResourceLocation
        //   tier C  (1.21.9-1.21.11): HudElementRegistry HUD, KeyEvent/MouseButtonEvent/
        //                              CharacterEvent, KeyMapping.Category, Identifier
        //                              (Identifier specifically only from 1.21.11)
        //   tier D1 (26.1.x):         KeyMappingHelper (renamed from KeyBindingHelper), unobfuscated
        //                              (no more Mojang mappings needed), GuiGraphics renamed to
        //                              GuiGraphicsExtractor with render*->extract* across Screen/
        //                              HudElement/AbstractWidget, drawString->text
        //   tier D2 (26.2.x):         same as D1, plus Minecraft.screen/setScreen moved to
        //                              Minecraft.gui.screen()/Minecraft.gui.setScreen()
        // Stops before 26.3, which replaces GLFW with SDL3 — a separate rewrite, not a version branch.
        // 26.1/26.1.1 are hotfixes of 26.1.2 with the same API surface; only the latest is built.
        // One representative pin per supported minor line; add more with `stonecutter create <version>`.
        versions("1.20.4", "1.21.5", "1.21.6", "1.21.8", "1.21.9", "1.21.11", "26.1.2", "26.2")
        vcsVersion = "26.2"
    }
}
