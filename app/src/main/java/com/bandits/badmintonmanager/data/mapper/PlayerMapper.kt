package com.bandits.badmintonmanager.data.mapper

import com.bandits.badmintonmanager.data.local.entities.PlayerEntity
import com.bandits.badmintonmanager.domain.model.Player
import com.bandits.badmintonmanager.domain.model.SkillLevel
import com.bandits.badmintonmanager.util.toEpochMillis
import com.bandits.badmintonmanager.util.toLocalDateTime

fun PlayerEntity.toDomain(): Player {
    return Player(
        id = id,
        name = name,
        nickname = nickname,
        skillLevel = SkillLevel.fromString(skillLevel),
        joiningDate = joiningDate.toLocalDateTime(),
        currentRating = currentRating,
        isActive = isActive
    )
}

fun Player.toEntity(): PlayerEntity {
    return PlayerEntity(
        id = id,
        name = name,
        nickname = nickname,
        skillLevel = skillLevel.name,
        joiningDate = joiningDate.toEpochMillis(),
        currentRating = currentRating,
        isActive = isActive,
        updatedAt = System.currentTimeMillis()
    )
}
