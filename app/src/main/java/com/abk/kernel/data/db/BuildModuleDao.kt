package com.abk.kernel.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BuildModuleDao {

    // ── Repository queries ──────────────────────────────────────────────

    @Query("SELECT * FROM build_module_repositories ORDER BY name ASC")
    suspend fun getAllRepositories(): List<BuildModuleRepositoryEntity>

    @Query("SELECT * FROM build_module_repositories WHERE id = :id")
    suspend fun getRepositoryById(id: String): BuildModuleRepositoryEntity?

    @Query("SELECT * FROM build_module_repositories WHERE url = :url COLLATE NOCASE LIMIT 1")
    suspend fun getRepositoryByUrl(url: String): BuildModuleRepositoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(repository: BuildModuleRepositoryEntity)

    @Update
    suspend fun updateRepository(repository: BuildModuleRepositoryEntity)

    @Query("DELETE FROM build_module_repositories WHERE id = :id")
    suspend fun deleteRepositoryById(id: String)

    // ── Catalog item queries ────────────────────────────────────────────

    @Query(
        """
        SELECT * FROM build_module_catalog_items
        WHERE repository_id = :repositoryId
        ORDER BY name ASC
        """
    )
    suspend fun getItemsByRepositoryId(repositoryId: String): List<BuildModuleCatalogItemEntity>

    @Query(
        """
        SELECT * FROM build_module_catalog_items
        ORDER BY name ASC
        """
    )
    suspend fun getAllItems(): List<BuildModuleCatalogItemEntity>

    @Query(
        """
        SELECT * FROM build_module_catalog_items
        WHERE name LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
           OR author LIKE '%' || :query || '%'
           OR repo_url LIKE '%' || :query || '%'
        ORDER BY name ASC
        """
    )
    suspend fun searchItems(query: String): List<BuildModuleCatalogItemEntity>

    @Query(
        """
        SELECT DISTINCT repo_url FROM build_module_catalog_items
        WHERE repo_url = :repoUrl COLLATE NOCASE
        LIMIT 1
        """
    )
    suspend fun findItemByRepoUrl(repoUrl: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<BuildModuleCatalogItemEntity>)

    @Query("DELETE FROM build_module_catalog_items WHERE repository_id = :repositoryId")
    suspend fun deleteItemsByRepositoryId(repositoryId: String)

    // ── Combined operations ─────────────────────────────────────────────

    @Transaction
    suspend fun replaceItemsForRepository(
        repositoryId: String,
        items: List<BuildModuleCatalogItemEntity>
    ) {
        deleteItemsByRepositoryId(repositoryId)
        insertItems(items)
    }

    @Query(
        """
        SELECT i.* FROM build_module_catalog_items i
        INNER JOIN build_module_repositories r ON i.repository_id = r.id
        ORDER BY i.name ASC
        """
    )
    suspend fun getAllItemsWithRepository(): List<BuildModuleCatalogItemEntity>
}
