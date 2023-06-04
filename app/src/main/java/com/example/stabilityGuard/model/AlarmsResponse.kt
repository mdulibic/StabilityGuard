package com.example.stabilityGuard.model

import com.squareup.moshi.Json

data class AlarmsResponse(
    @Json(name = "data") val data: List<AlarmResponse>,
    @Json(name = "totalPages") val totalPages: Int,
    @Json(name = "totalElements") val totalElements: Int,
    @Json(name = "hasNext") val hasNext: Boolean,
)

data class AlarmResponse(
    @Json(name = "id") val id: Id,
    @Json(name = "createdTime") val createdTime: Long,
    @Json(name = "tenantId") val tenantId: Id,
    @Json(name = "customerId") val customerId: Id,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "originator") val originator: Id,
    @Json(name = "severity") val severity: String,
    @Json(name = "status") val status: String,
    @Json(name = "startTs") val startTs: Long,
    @Json(name = "endTs") val endTs: Long,
    @Json(name = "ackTs") val ackTs: Long,
    @Json(name = "clearTs") val clearTs: Long,
    @Json(name = "details") val details: Map<String, Any>?,
    @Json(name = "propagate") val propagate: Boolean,
    @Json(name = "propagateToOwner") val propagateToOwner: Boolean,
    @Json(name = "propagateToTenant") val propagateToTenant: Boolean,
    @Json(name = "propagateRelationTypes") val propagateRelationTypes: List<String>,
    @Json(name = "originatorName") val originatorName: String?,
)

data class Id(
    @Json(name = "id") val id: String,
    @Json(name = "entityType") val entityType: String,
)
