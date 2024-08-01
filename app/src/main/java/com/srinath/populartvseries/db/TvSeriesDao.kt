package com.srinath.populartvseries.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.srinath.populartvseries.models.TvSeriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvSeriesDao {
    @Query("SELECT * FROM series ORDER BY popularity DESC")
    fun getAllSeries(): Flow<List<TvSeriesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: List<TvSeriesEntity>)

    @Query("DELETE FROM series")
    suspend fun clearSeries()
}

@Database(entities = [TvSeriesEntity::class], version = 2)
abstract class TvSeriesDatabase : RoomDatabase() {
    abstract fun tvSeriesDao(): TvSeriesDao
}

object DatabaseModule {
    private var INSTANCE: TvSeriesDatabase? = null

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Rename the old table
            database.execSQL("ALTER TABLE series RENAME TO series_old")

            // Create the new table with the correct schema
            database.execSQL(
                """
            CREATE TABLE series (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                overview TEXT NOT NULL,
                popularity REAL NOT NULL,
                posterPath TEXT,
                firstAirDate TEXT, -- Note: This is nullable now
                voteAverage REAL NOT NULL,
                voteCount INTEGER NOT NULL
            )
            """
            )

            // Copy the data from the old table to the new table
            database.execSQL(
                """
            INSERT INTO series (id, name, overview, popularity, posterPath, firstAirDate, voteAverage, voteCount)
            SELECT id, name, overview, popularity, posterPath, firstAirDate, voteAverage, voteCount
            FROM series_old
            """
            )

            // Drop the old table
            database.execSQL("DROP TABLE series_old")
        }
    }

    fun getDatabase(context: Context): TvSeriesDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TvSeriesDatabase::class.java,
                "tv_series_database"
            ).fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}
