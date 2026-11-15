package com.bedrocklauncherx.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldDao {
    @Query("SELECT * FROM worlds ORDER BY createdAt DESC") fun observeAll(): Flow<List<World>>
    @Query("SELECT * FROM worlds ORDER BY createdAt DESC") suspend fun getAll(): List<World>
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(world: World): Long
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertAll(worlds: List<World>)
    @Update suspend fun update(world: World)
    @Query("DELETE FROM worlds WHERE id = :id") suspend fun deleteById(id: Long)
}

@Database(entities = [World::class], version = 1, exportSchema = false)
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun worldDao(): WorldDao

    companion object {
        @Volatile private var INSTANCE: LauncherDatabase? = null
        fun get(context: Context): LauncherDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, LauncherDatabase::class.java, "bedrock-launcher.db")
                .build().also { INSTANCE = it }
        }
    }
}
