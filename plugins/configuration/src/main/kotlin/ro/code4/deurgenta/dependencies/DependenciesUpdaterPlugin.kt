package ro.code4.deurgenta.dependencies

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import java.util.Locale

class DependenciesUpdaterPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.run {
        if (this == rootProject) {
            configureDependencyUpdates()
        } else {
            logger.warn("You should apply this plugin only to root project")
        }
    }

    private fun Project.configureDependencyUpdates() {
        apply<VersionsPlugin>()
        tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
            rejectVersionIf {
                isNonStable(candidate.version) && !isNonStable(currentVersion)
            }

            checkForGradleUpdate = true
            outputFormatter = "plain,json"
            outputDir = "build/dependencyUpdates"
            reportfileName = "report"
        }
    }

    private fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA", "RC", "BETA").any {
            version.toUpperCase(Locale.US).contains(it)
        }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return !isStable
    }
}
