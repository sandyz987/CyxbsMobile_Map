package com.mredrock.cyxbs.discover.map.widget

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.mredrock.cyxbs.discover.map.R
import kotlinx.android.synthetic.main.map_dialog_choose.view.*
import org.jetbrains.anko.layoutInflater

/**
 *@author zhangzhe
 *@date 2020/8/18
 *@description
 */

interface OnUpdateSelectListener {
    fun onDeny()
    fun onPositive()
}

object UpdateMapDialog {
    fun show(context: Context, title: String, content: String, listener: OnUpdateSelectListener) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.map_transparent_dialog)
        val view = context.layoutInflater.inflate(R.layout.map_dialog_choose, null, false)
        builder.setView(view)
        builder.setCancelable(true)
        view.map_tv_tip_title.text = title
        view.map_tv_tip_text.text = content
        view.map_tv_tip_deny.text = "下次再说"
        view.map_tv_tip_positive.text = "立即更新"
        val dialog = builder.create()
        dialog.show()

        view.map_tv_tip_deny.setOnClickListener {
            listener.onDeny()
            dialog.dismiss()
        }
        view.map_tv_tip_positive.setOnClickListener {
            listener.onPositive()
            dialog.dismiss()
        }
    }
}