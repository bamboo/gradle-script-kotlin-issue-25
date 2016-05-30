import org.gradle.api.artifacts.dsl.*
import org.gradle.api.plugins.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.ZipEntryCompression
import org.gradle.jvm.tasks.Jar
import org.gradle.script.lang.kotlin.*
import java.util.concurrent.Callable

apply { it.plugin("kotlin") }

val kotlinVersion = extra["kotlinVersion"]

dependencies {
    "compile"("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
}

repositories {
    maven {
        it.setUrl("https://repo.gradle.org/gradle/repo")
    }
}

task<Zip>("repackageKotlinCompilerEmbeddable") {
    baseName = "kotlin-compiler-embeddable"
    version = "${kotlinVersion}a"
    extension = "jar"
    entryCompression = ZipEntryCompression.STORED
    from(Callable {
        val files = configurations.getByName("compile").files
        zipTree(files.single { it.name.startsWith(baseName) })
    })
    exclude("META-INF/services/java.nio.charset.spi.CharsetProvider")
    destinationDir = buildDir
    description = "Repackages '$baseName:$version' to remove broken META-INF/services files"
}
