package tech.zymx.photoeveryday

import android.app.Application
import tech.zymx.photoeveryday.repo.TileRepo

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        TileRepo.initDatabase(this)
    }

    companion object {
        private lateinit var sInstance: MyApplication

        fun g(): MyApplication {
            return sInstance
        }
    }
}