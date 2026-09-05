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

tasks.register<Copy>("copyJars") {
    group = "build"
    description = "Copies the JAR files from common, fabric, and neoforge to the output directory"

    for (project in listOf(project(":common"), project(":fabric"), project(":neoforge"))) {
        from("${project.projectDir}/build/libs/") {
            include("*.jar")
        }
    }
    into("./output/")

    doFirst {
        delete(fileTree("./output/") {
            include("**/*")
        })
        file("./output/").mkdirs()
    }
}