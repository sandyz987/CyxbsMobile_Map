package com.mredrock.cyxbs.discover.map.ui.activity.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.InfoItem
import kotlinx.android.synthetic.main.map_recycle_item_symbol_places.view.*

class SymbolRvAdapter(val context: Context, private var mList: List<InfoItem>?) : RecyclerView.Adapter<SymbolRvAdapter.ViewHolder>() {

    lateinit var curSelectorItem: AppCompatCheckedTextView

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
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            curSelectorItem = holder.symbol
            if (mList!![position].isHot) {
                holder.hot.visibility = View.VISIBLE
            } else {
                holder.hot.visibility = View.INVISIBLE
            }
            holder.symbol.text = mList!![position].title
            holder.symbol.setOnClickListener { v ->
                /**
                 * 二次点击取消选择
                 */
                if (v == curSelectorItem) {
                    if (curSelectorItem.isChecked) {
                        val animator2 = ValueAnimator.ofFloat(1f, 0.8f, 1.2f, 1f)
                        animator2.duration = 500
                        animator2.addUpdateListener {
                            val currentValue: Float = it.animatedValue as Float
                            curSelectorItem.scaleX = currentValue
                            curSelectorItem.scaleY = currentValue
                            if (currentValue > 1f) {
                                curSelectorItem.isChecked = false
                            }
                        }
                        animator2.start()
                        return@setOnClickListener
                    }

                }
                /**
                 * 选择动画
                 */
                val lastSelect = curSelectorItem
                curSelectorItem = v as AppCompatCheckedTextView
                val animator = ValueAnimator.ofFloat(1f, 1.2f, 0.8f, 1f)
                animator.duration = 500
                animator.addUpdateListener {
                    val currentValue: Float = it.animatedValue as Float
                    curSelectorItem.scaleX = currentValue
                    curSelectorItem.scaleY = currentValue
                    if (currentValue < 1f) {
                        lastSelect.isChecked = false
                        curSelectorItem.isChecked = true
                    }
                }
                animator.start()


            }
        }
    }

    fun setList(list: List<InfoItem>) {
        mList = list
    }

}