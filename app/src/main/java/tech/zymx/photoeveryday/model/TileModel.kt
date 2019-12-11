package tech.zymx.photoeveryday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by kevinzhan@tencent.com on 2019-12-11
 */

@Entity(tableName = TileModel.TABLE_NAME)
class TileModel {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var title: String = ""

    var coverUrl: String = ""

    var createTime: Long = 0

    var updateTime: Long = 0

    companion object {
        const val TABLE_NAME = "TileModel"
    }
}
