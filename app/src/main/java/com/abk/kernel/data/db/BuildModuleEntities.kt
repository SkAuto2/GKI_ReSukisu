package com.abk.kernel.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "build_module_repositories",
    indices = [
        Index(value = ["url"], unique = true)
    ]
)
data class BuildModuleRepositoryEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    @ColumnInfo(name = "index_json_url")
    val indexJsonUrl: String,
    val name: String,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long,
    val error: String?,
    @ColumnInfo(name = "skipped_count")
    val skippedCount: Int
)

@Entity(
    tableName = "build_module_catalog_items",
    foreignKeys = [
        ForeignKey(
            entity = BuildModuleRepositoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["repository_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["repository_id"]),
        Index(value = ["name"]),
        Index(value = ["repo_url"]),
        Index(value = ["author"])
    ]
)
data class BuildModuleCatalogItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "repository_id")
    val repositoryId: String,
    val name: String,
    val version: String,
    val description: String,
    @ColumnInfo(name = "repo_url")
    val repoUrl: String,
    @ColumnInfo(name = "default_stage")
    val defaultStage: String,
    @ColumnInfo(name = "supported_stages")
    val supportedStages: String,
    @ColumnInfo(name = "recommended_stages")
    val recommendedStages: String,
    val author: String,
    val homepage: String
)
