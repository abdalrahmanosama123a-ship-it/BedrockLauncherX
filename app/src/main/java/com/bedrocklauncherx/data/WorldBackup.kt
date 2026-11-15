package com.bedrocklauncherx.data

import org.json.JSONArray
import org.json.JSONObject

/** Backup format for launcher metadata only; it never contains Minecraft world files. */
object WorldBackup {
    private const val FORMAT = "bedrocklauncherx-world-profiles"
    private const val VERSION = 1

    fun encode(worlds: List<World>): String {
        val entries = JSONArray()
        worlds.forEach { world ->
            entries.put(JSONObject().apply {
                put("name", world.name)
                put("seed", world.seed)
                put("gameMode", world.gameMode)
                put("difficulty", world.difficulty)
                put("createdAt", world.createdAt)
            })
        }
        return JSONObject().apply {
            put("format", FORMAT)
            put("version", VERSION)
            put("worlds", entries)
        }.toString(2)
    }

    /** Parses and validates the complete document before returning any rows for insertion. */
    fun decode(json: String): List<World> {
        require(json.length <= 2_000_000) { "Backup file is too large" }
        val root = JSONObject(json)
        require(root.optString("format") == FORMAT) { "This is not a BedrockLauncherX profiles backup" }
        require(root.optInt("version", -1) == VERSION) { "Unsupported backup format version" }
        val entries = root.optJSONArray("worlds") ?: throw IllegalArgumentException("Missing worlds list")
        require(entries.length() <= 10_000) { "Backup contains too many profiles" }
        return buildList(entries.length()) {
            for (index in 0 until entries.length()) {
                val item = entries.optJSONObject(index) ?: throw IllegalArgumentException("Invalid profile at index $index")
                val name = WorldValidation.normalizedName(item.optString("name"))
                    ?: throw IllegalArgumentException("Profile at index $index has an empty name")
                val mode = item.optString("gameMode", "survival")
                val difficulty = item.optString("difficulty", "normal")
                require(mode in setOf("survival", "creative")) { "Invalid game mode in profile $name" }
                require(difficulty in setOf("peaceful", "easy", "normal", "hard")) { "Invalid difficulty in profile $name" }
                add(World(id = 0, name = name, seed = item.optString("seed"), gameMode = mode,
                    difficulty = difficulty, createdAt = item.optLong("createdAt", System.currentTimeMillis())))
            }
        }
    }
}
