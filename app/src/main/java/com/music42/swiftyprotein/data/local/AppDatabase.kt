package com.music42.swiftyprotein.data.local

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.music42.swiftyprotein.data.local.entity.FavoriteLigand
import com.music42.swiftyprotein.data.local.entity.User

@Database(entities = [User::class, FavoriteLigand::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `favorite_ligands` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `ligandId` TEXT NOT NULL,
                        `createdAtMs` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_favorite_ligands_ligandId` ON `favorite_ligands` (`ligandId`)"
                )
            }
        }
    }
}
