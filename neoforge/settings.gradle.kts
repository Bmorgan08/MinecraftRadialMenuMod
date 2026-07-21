pluginManagement {
    repositories {
        maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.7"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        // versions("<name>") is "<minecraft version>", grouped by API tier:
        //   tier N-B (1.21.1-1.21.5):  RegisterGuiLayersEvent (vanilla LayeredDraw.Layer,
        //                               render(GuiGraphics, DeltaTracker)), old Screen input
        //                               methods, string KeyMapping category
        //   tier N-C (1.21.8-1.21.10): same GUI layer API, KeyEvent/MouseButtonEvent/
        //                               CharacterEvent, KeyMapping.Category (registered via
        //                               RegisterKeyMappingsEvent#registerCategory)
        //   tier N-D (1.21.11-26.0.x): adds Identifier (renamed from ResourceLocation),
        //                               NeoForge's own GuiLayer interface
        //   tier N-E (26.1-26.2):      GuiGraphicsExtractor rename, Minecraft.gui.screen()/
        //                               setScreen() at 26.2 (26.2 NeoForge build is beta-only
        //                               as of writing; bump versions/26.2/gradle.properties
        //                               once a stable release ships)
        // Floor is 1.21.1, not 1.20.2/1.20.4 (NeoForge's true fork point): those old NeoForge
        // builds don't publish the artifact variant metadata current ModDevGradle needs to
        // resolve at all (confirmed via direct build failure, not a plugin-version issue) -
        // supporting them would need a separate, older toolchain investigated later if wanted.
        // Stops before 26.3 (GLFW->SDL3), same ceiling as the Fabric build.
        versions("1.21.1", "1.21.5", "1.21.8", "1.21.11", "26.1", "26.2")
        vcsVersion = "26.2"
    }
}

rootProject.name = "simplerkeybinds"
