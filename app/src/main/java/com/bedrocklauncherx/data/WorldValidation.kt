package com.bedrocklauncherx.data

object WorldValidation {
    fun normalizedName(input: String): String? = input.trim().takeIf(String::isNotEmpty)
}
