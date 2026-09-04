import org.gradle.api.file.CopySpec
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSetContainer

plugins {
    id("matthiesen-common")
}

val commonMain = project(":common").extensions.getByType<SourceSetContainer>().named("main")

sourceSets.named("main") {
    java.srcDirs(commonMain.map { it.allJava.srcDirs })
    resources.srcDirs(commonMain.map { it.resources.srcDirs })
}

tasks.named("processResources") {
    this as CopySpec
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
