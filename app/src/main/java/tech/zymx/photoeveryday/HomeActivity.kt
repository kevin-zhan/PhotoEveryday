package tech.zymx.photoeveryday

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import tech.zymx.photoeveryday.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {

    lateinit var viewModel: TileListViewModel
    lateinit var tileAdapter: TileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TileListViewModel::class.java)
        tileAdapter = TileAdapter()

        viewModel.tileListViewModel.observe(this, Observer<MutableList<TileViewModel>> {
            tileAdapter.setData(it)
        })

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).let { binding ->
            binding.rvTileList.layoutManager = LinearLayoutManager(this)
            binding.rvTileList.adapter = tileAdapter

            binding.tbNavBar.title = getString(R.string.app_name)
            setSupportActionBar(binding.tbNavBar)

            binding.tbNavBar.setOnMenuItemClickListener { item ->
                if (item.itemId != R.id.action_add) {
                    return@setOnMenuItemClickListener false
                }
                viewModel.addData()

                true
            }
        }

        viewModel.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}
