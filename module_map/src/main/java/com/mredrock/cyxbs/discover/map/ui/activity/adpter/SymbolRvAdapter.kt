package com.mredrock.cyxbs.discover.map.ui.activity.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.SymbolBean
import kotlinx.android.synthetic.main.map_recycle_item_symbol_places.view.*

class SymbolRvAdapter(val context: Context, val mList: List<SymbolBean>?) : RecyclerView.Adapter<SymbolRvAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hot: ImageView = view.map_iv_recycle_item_hot
        val symbol: AppCompatCheckedTextView = view.map_tv_recycle_item_symbol
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.map_recycle_item_symbol_places, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (mList == null)
            return 1
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            if (mList[position].isHot){
                holder.hot.visibility = View.VISIBLE
            } else{
                holder.hot.visibility = View.INVISIBLE
            }
            holder.symbol.text = mList[position].name
            holder.symbol.setOnClickListener {
                holder.symbol.isChecked = !holder.symbol.isChecked
            }
        }
    }
}