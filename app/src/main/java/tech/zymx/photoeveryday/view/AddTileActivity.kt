package tech.zymx.photoeveryday.view

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import tech.zymx.photoeveryday.R
import tech.zymx.photoeveryday.REQUEST_CODE_PHOTO_LIBRARY
import tech.zymx.photoeveryday.UIActionType
import tech.zymx.photoeveryday.databinding.ActivityAddTileBinding
import tech.zymx.photoeveryday.viewmodel.TileAddViewModel


/**
 * Created by kevinzhan@tencent.com on 2019-12-11
 */
class AddTileActivity : AppCompatActivity() {

    lateinit var viewModel: TileAddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TileAddViewModel::class.java)

        viewModel.uiAction.observe(this, Observer { actionModel ->
            when {
                actionModel.actionType == UIActionType.UI_ACTION_PHOTO_LIBRARY -> {
                    // jump to photo library
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(intent, REQUEST_CODE_PHOTO_LIBRARY)
                }
                actionModel.actionType == UIActionType.UI_ACTION_SHOW_TOAST -> Toast.makeText(
                    this,
                    actionModel.getExtraData() as String,
                    Toast.LENGTH_SHORT
                )
                    .show()
                actionModel.actionType == UIActionType.UI_ACTION_ADD_DONE -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })

        DataBindingUtil.setContentView<ActivityAddTileBinding>(this, R.layout.activity_add_tile)
            .let { binding ->
                binding.viewModel = viewModel

                binding.toolbar.title = getString(R.string.insist_on)
                setSupportActionBar(binding.toolbar)
                binding.toolbar.setNavigationIcon(R.drawable.ic_nav_back)
                binding.toolbar.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.action_ok) {
                        viewModel.saveTile()
                        return@setOnMenuItemClickListener true
                    }
                    false
                }

                viewModel.coverPath.observe(this, Observer {
                    binding.ivAddLogo.visibility = View.GONE
                    binding.ivCoverPreview.setImageDrawable(Drawable.createFromPath(it))
                })

                binding.ivCoverPreview.setOnClickListener {
                    viewModel.openPhotoLibrary()
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PHOTO_LIBRARY) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let { selectedImage ->
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    contentResolver.query(
                        selectedImage,
                        filePathColumn, null, null, null
                    )?.also {
                        it.moveToFirst()
                        val columnIndex = it.getColumnIndex(filePathColumn[0])
                        val picturePath = it.getString(columnIndex)
                        viewModel.coverPath.postValue(picturePath)
                        it.close()
                    }
                }
            }
        }
    }
}