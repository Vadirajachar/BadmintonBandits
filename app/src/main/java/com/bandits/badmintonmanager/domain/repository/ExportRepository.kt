package com.bandits.badmintonmanager.domain.repository

import android.net.Uri
import com.bandits.badmintonmanager.util.Resource

interface ExportRepository {

    suspend fun exportToJson(): Resource<String>

    suspend fun importFromJson(json: String): Resource<Unit>

    suspend fun saveJsonToFile(json: String, uri: Uri): Resource<Unit>

    suspend fun readJsonFromFile(uri: Uri): Resource<String>
}
