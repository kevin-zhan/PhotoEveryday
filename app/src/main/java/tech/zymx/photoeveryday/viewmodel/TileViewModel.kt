package tech.zymx.photoeveryday.viewmodel

import android.app.Application
import android.os.AsyncTask
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.zymx.photoeveryday.MyApplication
import tech.zymx.photoeveryday.R
import tech.zymx.photoeveryday.UIActionType
import tech.zymx.photoeveryday.model.TileModel
import tech.zymx.photoeveryday.model.UIActionModel
import tech.zymx.photoeveryday.repo.TileRepo
import java.util.*

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */


class TileListViewModel(application: Application) : AndroidViewModel(application) {
    val tileListViewModel = MutableLiveData<MutableList<TileViewModel>>(mutableListOf())
    val uiAction = MutableLiveData<UIActionModel>()

    fun refreshData() {
        AsyncTask.execute {
            TileRepo.tileDatabase?.tileDao()?.getAll()?.map { model ->
                TileViewModel(model)
            }?.let {
                tileListViewModel.postValue(it.toMutableList())
            }
        }
    }

    fun addData() {
        uiAction.postValue(UIActionModel(UIActionType.UI_ACTION_ADD_PAGE))
    }

    fun tryDeleteTile(position: Int) {
        uiAction.postValue(object : UIActionModel(UIActionType.UI_ACTION_SHOW_DELETE_DIALOG) {
            override fun getExtraData(): Int {
                return position
            }
        })
    }

    fun confirmDeleteTile(position: Int) {
        AsyncTask.execute {
            tileListViewModel.value?.let {
                TileRepo.tileDatabase?.tileDao()?.delete(it[position].model)
                it.removeAt(position)

                tileListViewModel.postValue(it)
                uiAction.postValue(object : UIActionModel(UIActionType.UI_ACTION_SHOW_TOAST) {
                    override fun getExtraData(): String {
                        return getApplication<MyApplication>().getString(R.string.delete_done)
                    }
                })
            }
        }

    }

}

class TileViewModel(model: TileModel) : ViewModel() {
    val model = model

    val coverUrl = MutableLiveData<String>(model.coverUrl)
    val tileTitle: MutableLiveData<String> = MutableLiveData(model.title)
    val tileUpdateTime: MutableLiveData<String> = MutableLiveData("2019-12-2 23:22")

}


class TileAddViewModel(application: Application) : AndroidViewModel(application) {
    val tileTitle = MutableLiveData<String>()
    val coverPath = MutableLiveData<String>()

    val uiAction = MutableLiveData<UIActionModel>()

    fun openPhotoLibrary() {
        uiAction.postValue( UIActionModel(UIActionType.UI_ACTION_PHOTO_LIBRARY))
    }

    fun saveTile() {
        if (!isInfoComplete()) {
            uiAction.postValue(object : UIActionModel(UIActionType.UI_ACTION_SHOW_TOAST) {
                override fun getExtraData(): String {
                    return getApplication<MyApplication>().getString(R.string.pls_complete_info)
                }
            })
            return
        }
        AsyncTask.execute {
            val tileModel = TileModel()
            tileModel.title = tileTitle.value!!
            tileModel.coverUrl = coverPath.value!!
            tileModel.createTime = Date().time
            tileModel.updateTime = Date().time
            TileRepo.tileDatabase?.tileDao()?.insert(tileModel)

            uiAction.postValue(UIActionModel(UIActionType.UI_ACTION_ADD_DONE))

            uiAction.postValue(object : UIActionModel(UIActionType.UI_ACTION_SHOW_TOAST) {
                override fun getExtraData(): String {
                    return getApplication<MyApplication>().getString(R.string.suc_add)
                }
            })
        }
    }

    private fun isInfoComplete(): Boolean {
        return !TextUtils.isEmpty(tileTitle.value) && !TextUtils.isEmpty(coverPath.value)
    }

}