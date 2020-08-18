package com.mredrock.cyxbs.discover.map.ui.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.utils.extensions.doPermissionAction
import com.mredrock.cyxbs.common.utils.extensions.toast
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.ui.adapter.AllPictureRvAdapter
import com.mredrock.cyxbs.discover.map.util.SubsamplingScaleImageViewShowPictureTarget
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import com.mredrock.cyxbs.discover.map.widget.GlideApp
import com.mredrock.cyxbs.discover.map.widget.ProgressInterceptor
import kotlinx.android.synthetic.main.map_fragment_all_picture.*
import kotlinx.android.synthetic.main.map_fragment_show_picture.*
import org.jetbrains.anko.longToast
import java.io.File
import java.io.FileOutputStream


class ShowPictureFragment(val url: String) : Fragment() {
    private lateinit var allPictureAdapter: AllPictureRvAdapter
    private val imageData = mutableListOf<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment_show_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = android.app.ProgressDialog(context)
        dialog.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL)
        dialog.setMessage("图片加载中")
        dialog.setCancelable(false)
        dialog.show()
        ProgressInterceptor.addListener(url) { progress ->
            dialog.progress = progress
        }
        context?.let {
            GlideApp.with(it)
                    .download(GlideUrl(url))
                    .into(SubsamplingScaleImageViewShowPictureTarget(it, map_iv_show_picture, dialog, url))
        }
        map_iv_show_picture.setOnLongClickListener {
            (context as AppCompatActivity).doPermissionAction(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                doAfterGranted {
                    Glide.with(BaseApp.context).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            saveImage(resource)
                        }
                    }
                    )
                }
            }
            true
        }
    }

    private fun saveImage(resource: Bitmap) {
        val file = File(BaseApp.context.externalMediaDirs[0]?.absolutePath
                + File.separator
                + "Map"
                + File.separator
                + System.currentTimeMillis()
                + ".jpg")
        val parentDir = file.parentFile
        if (parentDir.exists()) parentDir.delete()
        parentDir.mkdir()
        file.createNewFile()
        val fos = FileOutputStream(file)
        resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        galleryAddPic(file.path)
        context?.longToast("图片已保存在" + file.absolutePath)
    }

    //更新相册
    private fun galleryAddPic(imagePath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        BaseApp.context.sendBroadcast(mediaScanIntent)
    }

}