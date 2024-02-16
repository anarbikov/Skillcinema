package com.skillcinema

import android.app.Application
import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
class TestApplication:Application(){
//    private lateinit var db: AppDatabase
//    override fun onCreate() {
//        super.onCreate()
//        db = Room
//            .inMemoryDatabaseBuilder(
//                applicationContext,
//                AppDatabase::class.java,
//            )
//            .fallbackToDestructiveMigration()
//            .build()
//    }
}