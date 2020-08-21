package com.mredrock.cyxbs.discover.map.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.utils.extensions.defaultSharedPreferences
import com.mredrock.cyxbs.common.utils.extensions.editor
import com.mredrock.cyxbs.common.utils.extensions.toast
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.component.MapToast
import com.mredrock.cyxbs.discover.map.model.DataSet
import com.mredrock.cyxbs.discover.map.widget.GlideProgressDialog
import com.mredrock.cyxbs.discover.map.widget.ProgressInterceptor
import java.io.File

class SubsamplingScaleImageViewTarget(val context: Context, view: SubsamplingScaleImageView, val url: String)
    : CustomViewTarget<SubsamplingScaleImageView, File>(view) {
    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
        view.setImage(ImageSource.uri(Uri.fromFile(resource)))
        GlideProgressDialog.hide()
        ProgressInterceptor.removeListener(url)
        DataSet.savePath(resource.absolutePath)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        // Ignore
        view.setImage(ImageSource.resource(R.drawable.map_ic_high))
        GlideProgressDialog.hide()
        MapToast.makeText(context, BaseApp.context.getString(R.string.map_map_load_failed), Toast.LENGTH_SHORT).show()
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        // Ignore
    }

    override fun onResourceLoading(placeholder: Drawable?) {
        super.onResourceLoading(placeholder)
    }
}