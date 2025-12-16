package com.bandits.badmintonmanager.data.repository

import android.net.Uri
import com.bandits.badmintonmanager.domain.repository.ExportRepository
import com.bandits.badmintonmanager.util.Resource
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    // TODO: Add context and DAOs when implementing
) : ExportRepository {

    override suspend fun exportToJson(): Resource<String> {
        // Placeholder implementation
        return Resource.Error("Export not yet implemented")
    }

    override suspend fun importFromJson(json: String): Resource<Unit> {
        // Placeholder implementation
        return Resource.Error("Import not yet implemented")
    }

    override suspend fun saveJsonToFile(json: String, uri: Uri): Resource<Unit> {
        // Placeholder implementation
        return Resource.Error("Save to file not yet implemented")
    }

    override suspend fun readJsonFromFile(uri: Uri): Resource<String> {
        // Placeholder implementation
        return Resource.Error("Read from file not yet implemented")
    }
}
