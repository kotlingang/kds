package `fun`.kotlingang.deploy

import DeployException
import kotlin.reflect.KProperty0
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

open class DeployProperties(
    var host: String? = null,
    var port: Int = 22,
    var username: String? = null,
    var password: String? = null,
    var deployPath: String? = null
)

open class DeployEntity(
    var group: String? = null,
    var artifactId: String? = null,
    var version: String? = null,
    var name: String? = null,
    var description: String? = null
)

/**
 * Checks properties for null. If it is null, throws [DeployException].
 * @param fields - fields to check.
 */
internal fun <T> notNullPropertiesOrException(vararg fields: KProperty0<T?>) = fields.forEach { field ->
    if(field.get() == null)
        throw DeployException("Field for name `${field.name}` is null. Should be provided for deploy task.")
}

/**
 * `fun`.kotlingang.deploy.Deploy task.
 * To applying should be also specified [DeployEntity] and [DeployProperties].
 */
class Deploy : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply(plugin = "maven-publish")

        val config = target.extensions.create<DeployProperties>(name = "deploy")
        val entity = target.extensions.create<DeployEntity>(name = "deploy entity")

        target.afterEvaluate {

            notNullPropertiesOrException(
                config::host,
                config::username,
                config::password,
                config::deployPath,
                entity::name,
                entity::group,
                entity::description,
                entity::artifactId
            )

            project.the<PublishingExtension>().apply {
                publications {
                    create<MavenPublication>("deploy") {
                        group = entity.group ?: error("shouldn't be null")
                        artifactId = entity.artifactId ?: error("shouldn't be null")
                        version = entity.version ?: error("shouldn't be null")

                        pom {
                            name.set(entity.name ?: error("shouldn't be null"))
                            description.set(entity.description ?: error("shouldn't be null"))
                        }

                        from(components["kotlin"])
                    }
                }

                repositories {
                    maven {
                        name = entity.name ?: error("shouldn't be null")
                        version = entity.version ?: error("shouldn't be null")

                        url = uri(
                            "sftp://${config.host}:${config.port}/${config.deployPath}"
                        )

                        credentials {
                            username = config.username
                            password = config.password
                        }
                    }
                }
            }
        }
    }

}