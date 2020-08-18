package com.mredrock.cyxbs.discover.map.util

import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.mredrock.cyxbs.common.utils.extensions.defaultSharedPreferences
import com.mredrock.cyxbs.common.utils.extensions.editor
import com.mredrock.cyxbs.common.utils.extensions.toast
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.widget.ProgressInterceptor
import java.io.File

class SubsamplingScaleImageViewShowPictureTarget(val context: Context, view: SubsamplingScaleImageView, val dialog:ProgressDialog, val url:String)
    : CustomViewTarget<SubsamplingScaleImageView, File>(view) {
    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
        view.setImage(ImageSource.uri(Uri.fromFile(resource)))
        dialog.dismiss()
        ProgressInterceptor.removeListener(url)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        // Ignore
        dialog.dismiss()
        context.toast("图片加载失败")
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        // Ignore
    }

    override fun onResourceLoading(placeholder: Drawable?) {
        super.onResourceLoading(placeholder)
    }
}