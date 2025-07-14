plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight.userdev)
}

group = "me.moros"
version = "1.0.1"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
    implementation(libs.tinyloki)
}

tasks {
    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    shadowJar {
        archiveClassifier = ""
        archiveBaseName = project.name
        from(rootDir.resolve("LICENSE")) {
            rename { "META-INF/${it}_${rootProject.name.uppercase()}" }
        }
    }
    assemble {
        dependsOn(shadowJar)
    }
    named<Copy>("processResources") {
        filesMatching("paper-plugin.yml") {
            expand(mapOf("version" to project.version, "mcVersion" to libs.versions.minecraft.get()))
        }
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
