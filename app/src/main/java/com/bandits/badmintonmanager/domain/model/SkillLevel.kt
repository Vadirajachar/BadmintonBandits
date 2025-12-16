package com.bandits.badmintonmanager.domain.model

enum class SkillLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    companion object {
        fun fromString(value: String): SkillLevel {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: BEGINNER
        }
    }

    fun displayName(): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }
}
