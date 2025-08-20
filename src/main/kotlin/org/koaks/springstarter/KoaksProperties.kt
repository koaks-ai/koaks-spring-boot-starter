package org.koaks.springstarter

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "koaks")
data class KoaksProperties(
    var clients: List<ClientProperties> = emptyList()
)

data class ClientProperties(
    var name: String = "default",
    var apikey: String? = null,
    var baseurl: String? = null,
    var modelName: String? = null,
    var toolGroups: List<String>? = null,
)