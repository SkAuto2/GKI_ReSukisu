package com.abk.kernel.data.db

import com.abk.kernel.data.model.ModuleCatalogItem
import com.abk.kernel.data.model.ModuleCatalogRepository

fun ModuleCatalogRepository.toEntity() = BuildModuleRepositoryEntity(
    id = id,
    url = url,
    indexJsonUrl = indexJsonUrl,
    name = name,
    lastUpdated = lastUpdated,
    error = error,
    skippedCount = skippedCount
)

fun ModuleCatalogItem.toEntity(repositoryId: String) = BuildModuleCatalogItemEntity(
    repositoryId = repositoryId,
    name = name,
    version = version,
    description = description,
    repoUrl = repoUrl,
    defaultStage = defaultStage,
    supportedStages = supportedStages.joinToString(","),
    recommendedStages = recommendedStages.joinToString(","),
    author = author,
    homepage = homepage
)

fun BuildModuleRepositoryEntity.toDomain(modules: List<ModuleCatalogItem>) = ModuleCatalogRepository(
    id = id,
    url = url,
    indexJsonUrl = indexJsonUrl,
    name = name,
    modules = modules,
    lastUpdated = lastUpdated,
    error = error,
    skippedCount = skippedCount
)

fun BuildModuleCatalogItemEntity.toDomain() = ModuleCatalogItem(
    name = name,
    version = version,
    description = description,
    repoUrl = repoUrl,
    defaultStage = defaultStage,
    supportedStages = supportedStages.split(",").filter { it.isNotBlank() },
    recommendedStages = recommendedStages.split(",").filter { it.isNotBlank() },
    author = author,
    homepage = homepage
)
