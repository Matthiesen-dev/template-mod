plugins {
    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforged.moddev) apply false
}

allprojects {
    group = providers.gradleProperty("group").get()
    version = providers.environmentVariable("RELEASE_VERSION")
        .orElse(providers.gradleProperty("version"))
        .get()
}