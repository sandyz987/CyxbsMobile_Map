package com.mredrock.cyxbs.discover.map.ui.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.common.utils.extensions.setImageFromUrl
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.component.RoundRectImageView
import kotlinx.android.synthetic.main.map_banner_item_detail.view.*
import kotlinx.android.synthetic.main.map_recycle_item_all_picture.view.*

class AllPictureRvAdapter(val context: Context, private val mList: MutableList<String>) : RecyclerView.Adapter<AllPictureRvAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: RoundRectImageView = view.map_iv_recycle_item_all_picture
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.map_recycle_item_all_picture, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageFromUrl(mList[position])

    }


    fun setList(list: List<String>) {
        mList.clear()
        mList.addAll(list)
    }

}