package tech.zymx.photoeveryday

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */

class TileListViewModel(application: Application) : AndroidViewModel(application) {
    var tileListViewModel = MutableLiveData<MutableList<TileViewModel>>(mutableListOf())

    fun loadData() {
        val model = TileModel()
        model.coverUrl = "https://qzonestyle.gtimg.cn/aoi/sola/20191105163912_ONRjyZUKh8.png"
        // https://qzonestyle.gtimg.cn/aoi/sola/20181127110559_k8L8vATJwx.png
        model.title = "每天读书"
        model.updateTime = "2019-12-10 21:00"
        val viewModel = TileViewModel(model)
        tileListViewModel.postValue(tileListViewModel.value.also {
            it?.add(viewModel)
        })
    }

    fun addData() {
        val model = TileModel()
        model.coverUrl = "https://qzonestyle.gtimg.cn/aoi/sola/20191105163912_ONRjyZUKh8.png"
        // https://qzonestyle.gtimg.cn/aoi/sola/20181127110559_k8L8vATJwx.png
        model.title = "每天读书"
        model.updateTime = "2019-12-10 21:00"
        val viewModel = TileViewModel(model)
        tileListViewModel.postValue(tileListViewModel.value.also {
            it?.add(viewModel)
        })
    }

}

class TileViewModel(model: TileModel) : ViewModel() {
    val coverUrl = MutableLiveData<String>(model.coverUrl)
    val tileTitle: MutableLiveData<String> = MutableLiveData(model.title)
    val tileUpdateTime: MutableLiveData<String> = MutableLiveData(model.updateTime)
}