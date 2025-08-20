package org.koaks.springstarter

import org.assertj.core.api.Assertions.assertThat
import org.koaks.framework.api.chat.completions.ChatClient
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import kotlin.test.Test

class KoaksAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(KoaksAutoConfiguration::class.java))
        .withPropertyValues(
            "koaks.apikey=apikey",
            "koaks.baseurl=baseurl",
            "koaks.modelName=qwen3-235b",
            "koaks.name=test-client"
        )

    @Test
    fun testAutoConfiguration() {
        contextRunner.run { context ->
            assertThat(context).hasSingleBean(ChatClient::class.java)
            val client = context.getBean(ChatClient::class.java)
            client.generate("what is the meaning of life?")
        }
    }

}