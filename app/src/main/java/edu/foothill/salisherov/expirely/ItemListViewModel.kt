package edu.foothill.salisherov.expirely

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class ItemListViewModel : ViewModel() {

    val items = mutableListOf<Item>()

    private val itemRepository = ItemRepo.get()
    val itemListLiveData = itemRepository.getItems()
//    init {
//        for (i in 0 until 1) {
//            val dtStart = "Jun 4 2021"
//            val format =
//                SimpleDateFormat("MMM d yyyy")
//            val item = Item()
//            item.name = "Item #$i"
//            item.date = format.parse(dtStart)
//            itemRepository.addItem(item)
//        }
//        for (i in 0 until 1) {
//            val dtStart = "Jul 4 2021"
//            val format =
//                SimpleDateFormat("MMM d yyyy")
//            val item = Item()
//            item.name = "Item #$i"
//            item.date = format.parse(dtStart)
//            itemRepository.addItem(item)
//        }
//    }


    fun addItem(item: Item) {
        itemRepository.addItem(item)
    }
    fun deleteItem(item: Item) {
        itemRepository.deleteItem(item)
    }

}