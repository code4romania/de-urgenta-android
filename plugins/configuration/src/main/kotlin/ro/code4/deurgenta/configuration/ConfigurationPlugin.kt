package ro.code4.deurgenta.configuration

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import java.io.File
import java.util.Properties

class ConfigurationPlugin : Plugin<Project> {
    private val propertiesCache = mutableMapOf<String, Properties>()
    override fun apply(target: Project) {
        target.run {
            val fieldsExtension = extensions.create(
                "variantConfigFields",
                VariantConfigFieldsExtension::class
            )
            extensions.findByType(BaseAppModuleExtension::class)?.apply {
                buildTypes {
                    applicationVariants.all {
                        val variantName = buildType.name
                        fieldsExtension.fields.forEach {
                            buildConfigField(
                                it.type,
                                it.name,
                                getValue(variantName, it.sourceProperty).escape(it.type)
                            )
                        }
                        val productFlavor = productFlavors.firstOrNull()?.name?.capitalize().orEmpty()
                        val buildType = variantName.capitalize()
                        tasks.named(
                            "compile${productFlavor}${buildType}UnitTestSources"
                        ).dependsOn("merge${productFlavor}${buildType}Assets")
                    }
                }
            }
        }
    }

    private fun getProperties(env: String): Properties =
        propertiesCache.getOrPut(env) {
            Properties().apply {
                load(File("$env.properties").inputStream())
            }
        }

    private fun getValue(env: String, key: String) = getProperties(env)[key]?.toString().orEmpty()
    private fun String.escape(type: String) = if (type == "String") """"$this"""" else this
}
