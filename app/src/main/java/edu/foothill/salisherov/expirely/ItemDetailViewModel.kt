package edu.foothill.salisherov.expirely

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class ItemDetailViewModel : ViewModel() {
    private val itemRepository = ItemRepo.get()
    private val itemIdLiveData = MutableLiveData<UUID>()

    var itemLiveData: LiveData<Item?> =
        Transformations.switchMap(itemIdLiveData) { itemId ->
            itemRepository.getItem(itemId)
        }

    fun loadItem(itemId: UUID) {
        itemIdLiveData.value = itemId
    }

    fun saveItem(item: Item) {
        itemRepository.updateItem(item)
    }

    fun deleteItem(item: Item) {
        itemRepository.deleteItem(item)
    }


}