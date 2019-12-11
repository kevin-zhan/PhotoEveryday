package tech.zymx.photoeveryday.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import tech.zymx.photoeveryday.GlideApp
import tech.zymx.photoeveryday.R
import tech.zymx.photoeveryday.databinding.ItemTileBinding
import tech.zymx.photoeveryday.viewmodel.TileListViewModel
import tech.zymx.photoeveryday.viewmodel.TileViewModel

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */
class TileAdapter : RecyclerView.Adapter<TileViewHolder>() {

    private var tileViewModelList = mutableListOf<TileViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        DataBindingUtil.inflate<ItemTileBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_tile,
            parent,
            false
        ).let {
            return TileViewHolder(it.root)
        }
    }

    fun setData(tileList: MutableList<TileViewModel>) {
        tileViewModelList = tileList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return tileViewModelList.size
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        DataBindingUtil.findBinding<ItemTileBinding>(holder.itemView).let {
            if (it == null) {
                return
            }
            it.viewModel = tileViewModelList[position]

            it.root.setOnLongClickListener { view ->
                ViewModelProviders.of(view.context as FragmentActivity)
                    .get(TileListViewModel::class.java).tryDeleteTile(position)
                true
            }
            it.executePendingBindings()

            GlideApp.with(holder.itemView)
                .load(tileViewModelList[position].coverUrl.value)
                .into(it.imageView)
        }
    }

}

class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)