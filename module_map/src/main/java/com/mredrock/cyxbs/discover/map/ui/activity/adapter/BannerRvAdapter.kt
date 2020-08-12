package com.mredrock.cyxbs.discover.map.ui.activity.adpter

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

class BannerRvAdapter(val context: Context, private val mList: MutableList<String>) : RecyclerView.Adapter<BannerRvAdapter.ViewHolder>() {

    lateinit var curSelectorItem: AppCompatCheckedTextView

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: RoundRectImageView = view.map_iv_banner_item_detail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.map_banner_item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (mList.size > 10)
            return 10
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