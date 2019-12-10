package tech.zymx.photoeveryday

import android.content.Context
import android.os.AsyncTask
import androidx.room.*

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */
@Entity(tableName = TileModel.TABLE_NAME)
class TileModel {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var title: String = ""

    @ColumnInfo(
        name = "create_time",
        typeAffinity = ColumnInfo.TEXT
    ) var createTime: String = ""

    @ColumnInfo(
        name = "update_time",
        typeAffinity = ColumnInfo.TEXT
    ) var updateTime: String = ""

    var coverUrl: String = ""


    companion object {
        const val TABLE_NAME = "TileModel"
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


object TileRepo {
    private var tileDatabase: TileDatabase? = null

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