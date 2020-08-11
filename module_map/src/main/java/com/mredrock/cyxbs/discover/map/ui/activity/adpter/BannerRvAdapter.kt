package com.mredrock.cyxbs.discover.map.ui.activity.adpter

import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mredrock.cyxbs.common.utils.extensions.setImageFromUrl
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.InfoItem
import com.mredrock.cyxbs.discover.map.component.RoundRectImageView
import kotlinx.android.synthetic.main.map_banner_item_detail.view.*

class BannerRvAdapter(val context: Context, private val mList: MutableList<String>?) : RecyclerView.Adapter<BannerRvAdapter.ViewHolder>() {

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
        if (mList == null)
            return 1
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
           holder.imageView.setImageFromUrl(mList[position])


        }
    }


    fun setList(list: List<String>) {
        if (mList != null) {
            mList.clear()
            mList.addAll(list)
        }

    }

}