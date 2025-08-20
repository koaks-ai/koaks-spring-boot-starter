package org.koaks.springstarter

import jakarta.annotation.PostConstruct
import org.koaks.framework.Koaks
import org.koaks.framework.api.chat.completions.ChatClient
import org.koaks.framework.api.dsl.createChatClient
import org.koaks.framework.memory.IMemoryStorage
import org.springframework.beans.factory.BeanFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigurationPackages
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean


@AutoConfiguration
@EnableConfigurationProperties(KoaksProperties::class)
class KoaksAutoConfiguration(
    private val beanFactory: BeanFactory,
    private val koaksProperties: KoaksProperties,
    private val applicationContext: ApplicationContext
) {

    @PostConstruct
    fun initKoaks() {
        val packages = try {
            AutoConfigurationPackages.get(beanFactory)
        } catch (_: IllegalStateException) {
            emptyList<String>()
        }

        if (packages.isNotEmpty()) {
            Koaks.init(*packages.toTypedArray())
        }
    }

    @Bean
    fun chatClients(): Map<String, ChatClient> {
        if (koaksProperties.clients.isEmpty()) {
            return emptyMap()
        }

        return koaksProperties.clients.associate { clientProps ->
            clientProps.name to createChatClient {
                model {
                    baseUrl = clientProps.baseurl
                    apiKey = clientProps.apikey
                    modelName = clientProps.modelName
                }
                memory {
                    val beans = applicationContext.getBeansOfType(IMemoryStorage::class.java)
                    if (beans.isNotEmpty()) {
                        default()
                    } else {
                        custom(beans.values.first())
                    }
                }
                tools {
                    default()
                    val toolGroups = clientProps.toolGroups
                    if (toolGroups != null) {
                        groups(*toolGroups.toTypedArray())
                    }
                }
            }
        }
    }

}
