package tech.zymx.photoeveryday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import tech.zymx.photoeveryday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: TileListViewModel
    lateinit var tileAdapter: TileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TileListViewModel::class.java)
        tileAdapter = TileAdapter()

        viewModel.tileListViewModel.observe(this, Observer<MutableList<TileViewModel>> {
            tileAdapter.setData(it)
        })

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).let {
            it.rvTileList.layoutManager = LinearLayoutManager(this)
            it.rvTileList.adapter = tileAdapter
        }

        viewModel.loadData()
    }
}
