import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

plugins {
    id("maven-publish")
}

configure<JavaPluginExtension> {
    withJavadocJar()
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "devMatthiesenMaven"
            url = uri(if (version.toString().endsWith("SNAPSHOT"))
                "https://maven.matthiesen.dev/snapshots"
            else "https://maven.matthiesen.dev/releases")
            credentials(org.gradle.api.credentials.PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${rootProject.property("archives_base_name")}-${project.name}"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = project.property("mod_name").toString()
                description = project.property("description").toString()
                url = project.property("github_url").toString()
                licenses {
                    license {
                        name = project.property("license").toString()
                        url = project.property("license_url").toString()
                    }
                }
                developers {
                    developer {
                        id = project.property("mod_author_id").toString()
                        name = project.property("mod_author").toString()
                        url = project.property("mod_author_url").toString()
                    }
                }
                scm {
                    connection = "scm:git:git://${project.property("git_url").toString()}"
                    developerConnection = "scm:git:ssh://git@${project.property("git_url").toString()}"
                    url = project.property("github_url").toString()
                }
            }
        }
    }
}

tasks.withType<Javadoc> {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}