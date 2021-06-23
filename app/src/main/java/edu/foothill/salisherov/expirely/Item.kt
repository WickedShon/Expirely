package edu.foothill.salisherov.expirely

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Item(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var name: String = "Default item",
                var date: Date = Date(),
                var notified: Boolean = false,
                var notificationTime: Int = 1,
                var expired: Boolean = false)