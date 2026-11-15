package com.bedrocklauncherx.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/** Public read-only Modrinth API client. It does not authenticate users or install files. */
class ModrinthRepository {
    suspend fun gameVersions(): List<String> = withContext(Dispatchers.IO) {
        val array = JSONArray(get("https://api.modrinth.com/v2/tag/game_version"))
        buildList {
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                val version = item.optString("version", "")
                if (version.isNotBlank()) add(version)
            }
        }
    }

    suspend fun searchMods(query: String, gameVersion: String?, loader: String?): List<ModrinthProject> = withContext(Dispatchers.IO) {
        val facets = mutableListOf("[\"project_type:mod\"]")
        if (!gameVersion.isNullOrBlank()) facets.add("[\"versions:${jsonEscape(gameVersion)}\"]")
        if (!loader.isNullOrBlank()) facets.add("[\"categories:${jsonEscape(loader)}\"]")
        val params = buildList {
            if (query.isNotBlank()) add("query=${encode(query.trim())}")
            add("facets=${encode("[${facets.joinToString(",")}]")}")
            add("index=relevance")
            add("limit=30")
        }.joinToString("&")
        val root = JSONObject(get("https://api.modrinth.com/v2/search?$params"))
        val hits = root.optJSONArray("hits") ?: JSONArray()
        buildList {
            for (i in 0 until hits.length()) {
                val hit = hits.getJSONObject(i)
                add(
                    ModrinthProject(
                        id = hit.optString("project_id"),
                        slug = hit.optString("slug"),
                        title = hit.optString("title", "Untitled"),
                        description = hit.optString("description"),
                        author = hit.optString("author"),
                        downloads = hit.optLong("downloads"),
                        iconUrl = hit.optString("icon_url").takeIf { it.startsWith("https://") },
                        gameVersions = hit.optJSONArray("versions").toStringList()
                    )
                )
            }
        }
    }

    private fun get(address: String): String {
        val connection = URL(address).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 12_000
        connection.readTimeout = 15_000
        connection.setRequestProperty("User-Agent", "abdalrahmanosama123a-ship-it/BedrockLauncherX/0.2.0 (https://github.com/abdalrahmanosama123a-ship-it/BedrockLauncherX)")
        return try {
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (status !in 200..299) throw IllegalStateException("Modrinth API returned HTTP $status")
            body
        } finally {
            connection.disconnect()
        }
    }

    private fun JSONArray?.toStringList(): List<String> = this?.let { array ->
        buildList { for (i in 0 until array.length()) add(array.optString(i)) }
    }.orEmpty()

    private fun encode(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())
    private fun jsonEscape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"")
}

data class ModrinthProject(
    val id: String,
    val slug: String,
    val title: String,
    val description: String,
    val author: String,
    val downloads: Long,
    val iconUrl: String?,
    val gameVersions: List<String>
)
