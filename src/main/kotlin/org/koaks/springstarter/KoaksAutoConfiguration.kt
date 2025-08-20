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
    private val applicationContext: ApplicationContext,
    private val properties: KoaksProperties
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
    fun chatClient(): ChatClient {
        require(!properties.apikey.isNullOrBlank()) {
            "koaks.clients: 'apikey' must not be empty for client '${properties.name}'"
        }
        require(!properties.baseurl.isNullOrBlank()) {
            "koaks.clients: 'baseurl' must not be empty for client '${properties.name}'"
        }
        require(!properties.modelName.isNullOrBlank()) {
            "koaks.clients: 'modelName' must not be empty for client '${properties.name}'"
        }

        return createChatClient {
            model {
                baseUrl = properties.baseurl
                apiKey = properties.apikey
                modelName = properties.modelName
            }
            memory {
                val beans = applicationContext.getBeansOfType(IMemoryStorage::class.java)
                if (beans.isEmpty()) {
                    default()
                } else {
                    custom(beans.values.first())
                }
            }
            tools {
                default()
                properties.toolGroups?.let {
                    groups(*it.toTypedArray())
                }
            }
        }
    }

}
