plugins {
    id("dev.kikugie.stonecutter")
    id("dev.kikugie.loom-back-compat")
    id("maven-publish")
}

version = "${project.property("mod_version")}+${stonecutter.current.version}"
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("radialkeybindmenu") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    // 26.1+ ships unobfuscated with parameter names already included; nothing to map.
    if (stonecutter.current.parsed < "26.1") {
        mappings(loom.officialMojangMappings())
    }
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
}

tasks.processResources {
    val mcVersion = project.property("minecraft_version") as String
    val mcDepend = "~$mcVersion" // exact patch line for this build node; each MC version gets its own jar
    val modVersion = project.version.toString()
    val loaderVersion = project.property("loader_version") as String
    inputs.property("version", modVersion)
    inputs.property("mc_depend", mcDepend)
    inputs.property("loader_version", loaderVersion)
    filesMatching("fabric.mod.json") {
        expand(
            "version" to modVersion,
            "mc_depend" to mcDepend,
            "loader_version" to loaderVersion
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
}

// Collects this version's built jar into build/libs/<mod_version>/.
// Uses task name lookup (not the typed `tasks.remapJar` accessor) since pre-26.1 nodes
// remap an obfuscated jar (task "remapJar") while 26.1+ nodes are already unobfuscated (task "jar").
val collectJar by tasks.registering(Copy::class) {
    group = "project"
    from(tasks.findByName("remapJar") ?: tasks.named("jar").get())
    into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod_version")}"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }
    repositories {
        // Add repositories to publish to here.
    }
}
