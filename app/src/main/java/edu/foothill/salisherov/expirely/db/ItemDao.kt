package edu.foothill.salisherov.expirely.db

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.foothill.salisherov.expirely.Item
import java.util.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM item ORDER BY date ASC")
    fun getItems(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE id=(:id)")
    fun getItem(id: UUID): LiveData<Item?>

    @Update
    fun updateItem(item: Item)

    @Insert
    fun addItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

}