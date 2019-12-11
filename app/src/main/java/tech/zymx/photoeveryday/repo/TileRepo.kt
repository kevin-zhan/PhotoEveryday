package tech.zymx.photoeveryday.repo

import android.content.Context
import android.os.AsyncTask
import androidx.room.*
import tech.zymx.photoeveryday.model.TileModel

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */

object TileRepo {
    var tileDatabase: TileDatabase? = null

    fun initDatabase(applicationContext: Context) {
        AsyncTask.execute {
            tileDatabase = Room.databaseBuilder(
                applicationContext,
                TileDatabase::class.java,
                TileDatabase.DATABASE_NAME
            ).build()
        }
    }
}


@Dao
interface TileDao {

    @Query("select * from " + TileModel.TABLE_NAME)
    fun getAll(): List<TileModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: TileModel): Long

    @Update
    fun update(item: TileModel): Int


    @Delete
    fun delete(item: TileModel): Int
}

@Database(entities = [TileModel::class], version = 1)
abstract class TileDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "tile_database"
    }

    abstract fun tileDao(): TileDao
}


