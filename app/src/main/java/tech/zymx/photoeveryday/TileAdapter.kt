package tech.zymx.photoeveryday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tech.zymx.photoeveryday.databinding.ItemTileBinding

/**
 * Created by kevinzhan@tencent.com on 2019-12-10
 */
class TileAdapter : RecyclerView.Adapter<TileViewHolder>() {

    private var tileViewModelList = mutableListOf<TileViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        DataBindingUtil.inflate<ItemTileBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_tile,
            null,
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
            it.executePendingBindings()


            GlideApp.with(holder.itemView)
                .load(tileViewModelList[position].coverUrl.value)
                .into(it.imageView)
        }
    }

}

class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}