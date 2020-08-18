package com.mredrock.cyxbs.discover.map.widget

import android.app.ProgressDialog
import android.content.Context

//转圈圈的弹窗

object GlideProgressDialog {
    private var dialog: ProgressDialog? = null

    fun show(context: Context, message: String, cancelable: Boolean) {
        dialog = ProgressDialog(context)
        dialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog?.setMessage(message)
        dialog?.setCancelable(cancelable)
        dialog?.show()
    }

    fun hide() {
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    fun setProcess(process: Int) {
        if (dialog != null) {
            dialog?.progress = process
        }
    }

    fun dismiss(){
        dialog?.dismiss()
    }
}