package com.bandits.badmintonmanager.util

object Constants {
    // Database
    const val DATABASE_NAME = "bandits_database"
    const val DATABASE_VERSION = 1

    // Rating System
    const val DEFAULT_RATING = 1000.0
    const val K_FACTOR_BASE = 32.0
    const val SCORE_DIFF_MULTIPLIER = 0.1
    const val RECENCY_DECAY_FACTOR = 0.95
    const val MAX_MATCHES_FOR_RECENCY = 10

    // Duplicate Detection
    const val DUPLICATE_TIME_WINDOW_MINUTES = 30

    // Pagination
    const val PAGE_SIZE = 25
    const val RECENT_MATCHES_LIMIT = 10
    const val TOP_PLAYERS_LIMIT = 5

    // Export/Import
    const val EXPORT_VERSION = "1.0"
    const val EXPORT_FILE_NAME = "bandits_backup.json"

    // Date Format
    const val DATE_FORMAT_DISPLAY = "MMM dd, yyyy"
    const val DATETIME_FORMAT_DISPLAY = "MMM dd, yyyy h:mm a"
    const val TIME_FORMAT_DISPLAY = "h:mm a"
}
