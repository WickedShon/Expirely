package edu.foothill.salisherov.expirely.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.foothill.salisherov.expirely.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
@TypeConverters(ItemTypeConverters::class)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}