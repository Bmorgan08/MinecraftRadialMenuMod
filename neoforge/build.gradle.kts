plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version "2.0.142"
}

version = "${project.property("mod_version")}+${stonecutter.current.version}"
group = project.property("mod_group_id") as String

java.toolchain.languageVersion.set(JavaLanguageVersion.of((project.property("java_version") as String).toInt()))

base {
    archivesName.set(project.property("mod_id") as String)
}

neoForge {
    version = project.property("neo_version") as String

    runs {
        register("client") {
            client()
        }
    }

    mods {
        create(project.property("mod_id") as String) {
            sourceSet(sourceSets["main"])
        }
    }
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to project.property("minecraft_version"),
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "neo_version" to project.property("neo_version"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.version
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    // Stonecutter preprocesses src/main/templates into this generated copy; read from there,
    // not the raw src/main/templates, so the //? if branches are actually resolved first.
    dependsOn("stonecutterGenerate")
    from(layout.buildDirectory.dir("generated/stonecutter/main/templates"))
    into(layout.buildDirectory.dir("generated/sources/modMetadata"))
}
sourceSets.main {
    resources.srcDir(generateModMetadata)
}
neoForge.ideSyncTask(generateModMetadata)

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
