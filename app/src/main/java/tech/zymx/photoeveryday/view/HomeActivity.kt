package tech.zymx.photoeveryday.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import tech.zymx.photoeveryday.PERMISSION_REQ_WRITE_EXT
import tech.zymx.photoeveryday.R
import tech.zymx.photoeveryday.REQUEST_CODE_ADD_PAGE
import tech.zymx.photoeveryday.UIActionType
import tech.zymx.photoeveryday.databinding.ActivityHomeBinding
import tech.zymx.photoeveryday.viewmodel.TileListViewModel
import tech.zymx.photoeveryday.viewmodel.TileViewModel

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

        configUIAction()

        DataBindingUtil.setContentView<ActivityHomeBinding>(
            this,
            R.layout.activity_home
        ).let { binding ->
            binding.rvTileList.layoutManager = LinearLayoutManager(this)
            binding.rvTileList.adapter = tileAdapter

            binding.tbNavBar.title = getString(R.string.app_name)
            setSupportActionBar(binding.tbNavBar)

            binding.tbNavBar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_add) {
                    viewModel.addData()
                    return@setOnMenuItemClickListener true
                }
                false
            }
        }

        viewModel.refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_PAGE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.refreshData()
            }
        }
    }

    private fun configUIAction() {
        viewModel.uiAction.observe(this, Observer { actionModel ->
            when (actionModel.actionType) {
                UIActionType.UI_ACTION_ADD_PAGE -> {
                    if (!permissionCheck()) {
                        return@Observer
                    }
                    val intent = Intent(this, AddTileActivity::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_PAGE)
                }
                UIActionType.UI_ACTION_DETAIL_PAGE -> {
                    // todo
                }
                UIActionType.UI_ACTION_SHOW_TOAST -> {
                    Toast.makeText(this, actionModel.getExtraData() as String, Toast.LENGTH_SHORT)
                        .show()
                }
                UIActionType.UI_ACTION_SHOW_DELETE_DIALOG -> {
                    AlertDialog.Builder(this).setTitle(getString(R.string.confirm_delete_wording))
                        .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                            viewModel.confirmDeleteTile(actionModel.getExtraData() as Int)
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.dismiss()
                        }.create().show()
                }
                else -> {
                }
            }
        })
    }

    private fun permissionCheck(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                //用户拒绝过改权限时，执行此处
                AlertDialog.Builder(this).setTitle(getString(R.string.permission_apply)).setMessage(
                    getString(
                        R.string.write_file_permission
                    )
                )
                    .setPositiveButton(getString(R.string.word_ok)) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_REQ_WRITE_EXT
                        )
                    }.create().show()

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQ_WRITE_EXT
                )

            }
            return false
        }
        return true
    }
}
