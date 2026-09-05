import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

configure<JavaPluginExtension> {
    withJavadocJar()
}

// TODO: Add publishing logic here.