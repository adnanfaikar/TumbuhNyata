package com.example.tumbuhnyata.data.mapper

import com.example.tumbuhnyata.data.local.entity.CsrHistoryEntity
import com.example.tumbuhnyata.data.model.CsrHistoryItem

object CsrHistoryMapper {
    
    fun toEntity(item: CsrHistoryItem, isSynced: Boolean = true): CsrHistoryEntity {
        return CsrHistoryEntity(
            id = item.id,
            userId = item.userId,
            programName = item.programName,
            category = item.category,
            description = item.description,
            location = item.location,
            partnerName = item.partnerName,
            startDate = item.startDate,
            endDate = item.endDate,
            budget = item.budget,
            proposalUrl = item.proposalUrl,
            legalityUrl = item.legalityUrl,
            agreed = item.agreed,
            status = item.status,
            createdAt = item.createdAt,
            isSynced = isSynced,
            isDeleted = false,
            lastModified = System.currentTimeMillis()
        )
    }
    
    fun toItem(entity: CsrHistoryEntity): CsrHistoryItem {
        return CsrHistoryItem(
            id = entity.id,
            userId = entity.userId,
            programName = entity.programName,
            category = entity.category,
            description = entity.description,
            location = entity.location,
            partnerName = entity.partnerName,
            startDate = entity.startDate,
            endDate = entity.endDate,
            budget = entity.budget,
            proposalUrl = entity.proposalUrl,
            legalityUrl = entity.legalityUrl,
            agreed = entity.agreed,
            status = entity.status,
            createdAt = entity.createdAt
        )
    }
    
    fun toItemList(entities: List<CsrHistoryEntity>): List<CsrHistoryItem> {
        return entities.map { toItem(it) }
    }
    
    fun toEntityList(items: List<CsrHistoryItem>, isSynced: Boolean = true): List<CsrHistoryEntity> {
        return items.map { toEntity(it, isSynced) }
    }
} 