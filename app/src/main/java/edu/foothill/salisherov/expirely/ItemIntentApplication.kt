package edu.foothill.salisherov.expirely

import android.app.Application

class ItemIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ItemRepo.initialize(this)
    }
}