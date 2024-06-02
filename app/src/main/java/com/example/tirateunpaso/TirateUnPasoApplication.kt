package com.example.tirateunpaso

import android.app.Application
import com.example.tirateunpaso.database.AppContainer
import com.example.tirateunpaso.database.AppDataContainer

class TirateUnPasoApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
