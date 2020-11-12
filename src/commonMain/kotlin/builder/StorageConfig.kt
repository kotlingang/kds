package com.kotlingang.kds.builder

import com.kotlingang.kds.storage.joinPath
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

class StorageConfig(private val defaultPath: String) {
    internal var path: String? = null

    fun path(absolutePath: String) { path = absolutePath }
    fun name(name: String) = path(defaultPath.joinPath("$name.json"))


    internal var json: Json = Json

    fun json(json: Json) { this.json = json }
    fun buildJson(builder: JsonBuilder.() -> Unit) = json(Json { builder() })
}
