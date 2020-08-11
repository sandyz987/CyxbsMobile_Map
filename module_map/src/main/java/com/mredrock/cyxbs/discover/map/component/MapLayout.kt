package com.mredrock.cyxbs.discover.map.component

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.mredrock.cyxbs.common.utils.extensions.dp2px
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.widget.ProgressDialog

/**
 * 创建者：林潼
 * 时间：2020/8/6
 * 内容：基于Sub-samplingScaleImageView，继承自FrameLayout的MapLayout
 */

const val FOCUS_ANIMATION_DURATION = 800L

class MapLayout : FrameLayout, View.OnClickListener {
    /** Sub-samplingScaleImageView第三方控件 */
    private val subsamplingScaleImageView = SubsamplingScaleImageView(context)

    /** 图片来源 */
    private var imageSource = ImageSource.resource(R.mipmap.map_high)

    /** 标签array list */
    private val iconList = mutableListOf<ImageView>()

    private var onIconClickListener: OnIconClickListener? = null

    private var onPlaceClickListener: OnPlaceClickListener? = null

    /**
     *下面四个为继承FrameLayout的构造器方法
     */
    constructor(c: Context) :
            this(c, null) {
    }

    constructor(context: Context?, attrs: AttributeSet?) :
            this(context, attrs, 0) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context!!, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    /**
     *初始化
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        ProgressDialog.show(context, "提示", "加载中...", false)
        val rootParams =
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        subsamplingScaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
        subsamplingScaleImageView.setDoubleTapZoomScale(1f)
        subsamplingScaleImageView.setImage(
                imageSource.dimensions(7460, 13268),
                ImageSource.resource(R.mipmap.map)
        )
        addView(subsamplingScaleImageView, rootParams)

        subsamplingScaleImageView.setOnImageEventListener(object :
                SubsamplingScaleImageView.OnImageEventListener {
            override fun onImageLoaded() {
                subsamplingScaleImageView.animateScaleAndCenter(1f, PointF(1560f, 8640f))
                        ?.withDuration(FOCUS_ANIMATION_DURATION)
                        ?.withInterruptible(true)?.start()
            }

            /**
             * 务必在此方法后再addView
             */
            override fun onReady() {
                iconList.forEach { icon ->
                    val layoutParams = LayoutParams(
                            context.dp2px(45f),
                            context.dp2px(48f)
                    )
                    addView(icon, layoutParams)
                }
                ProgressDialog.hide()
            }

            override fun onTileLoadError(e: Exception?) {

            }

            override fun onPreviewReleased() {

            }

            override fun onImageLoadError(e: Exception?) {

            }

            override fun onPreviewLoadError(e: Exception?) {

            }

        })
        /**点击的位置 */
        var clickPoint = PointF(1660f, 7200f)

        /**通过此方法获得点击的位置 */
        val gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                        if (subsamplingScaleImageView.isReady) {
                            clickPoint = subsamplingScaleImageView.viewToSourceCoord(e.x, e.y)!!
                        }
                        return true
                    }
                })
        /**监听点击事件 */
        subsamplingScaleImageView.setOnClickListener {
            iconList.forEach { icon ->
                val iconBean = icon.tag as IconBean
                if ((clickPoint.x > iconBean.leftX
                                && clickPoint.x < iconBean.rightX
                                && clickPoint.y > iconBean.bottomY
                                && clickPoint.y < iconBean.topY) ||
                        (clickPoint.x > iconBean.tagLeftX
                                && clickPoint.x < iconBean.tagRightX
                                && clickPoint.y > iconBean.tagBottomY
                                && clickPoint.y < iconBean.tagTopY)) {
                    subsamplingScaleImageView.animateScaleAndCenter(
                            1f,
                            PointF(iconBean.sx, iconBean.sy)
                    )?.withDuration(FOCUS_ANIMATION_DURATION)
                            ?.withInterruptible(true)?.start()
                    showIcon(icon)
                    onPlaceClickListener?.onPlaceClick(icon)
                }
                if (!(clickPoint.x > 732 && clickPoint.x < 6530 && clickPoint.y > 5640 && clickPoint.y < 9000)) {
                    closeAllIcon()
                }
            }

        }
        /**监听触摸事件 */
        subsamplingScaleImageView.setOnTouchListener { v, event ->
            return@setOnTouchListener gestureDetector.onTouchEvent(event)
        }
        /**监听移动变化，保持icon不变 */
        subsamplingScaleImageView.setOnStateChangedListener(object :
                SubsamplingScaleImageView.OnStateChangedListener {
            override fun onCenterChanged(newCenter: PointF?, origin: Int) {
                iconList.forEach { icon ->
                    val iconBean = icon.tag as IconBean
                    val screenPoint =
                            subsamplingScaleImageView.sourceToViewCoord(iconBean.sx, iconBean.sy)
                    if (screenPoint != null) {
                        icon.x = screenPoint.x - context.dp2px(45f) / 2
                        icon.y = screenPoint.y - context.dp2px(48f)
                    }
                }

            }

            override fun onScaleChanged(newScale: Float, origin: Int) {
            }

        })

    }

    /**
     * 添加一个标签
     * bean：要添加的标签bean类
     */
    fun addIcon(bean: IconBean) {
        val icon = ImageView(context)
        icon.setImageResource(R.drawable.map_ic_local)
        icon.tag = bean
        val screenPoint = subsamplingScaleImageView.sourceToViewCoord(bean.sx, bean.sy)
        if (screenPoint != null) {
            icon.x = screenPoint.x - context.dp2px(45f) / 2
            icon.y = screenPoint.y - context.dp2px(48f)
        }
        icon.setOnClickListener(this)
        icon.visibility = View.GONE
        iconList.add(icon)
        if (subsamplingScaleImageView.isReady) {
            val layoutParams = LayoutParams(
                    context.dp2px(45f),
                    context.dp2px(48f)
            )
            addView(icon, layoutParams)
        }

    }

    /**
     * 添加多个标签
     * beans：要添加的标签bean类list
     */
    fun addSomeIcons(beans: List<IconBean>) {
        beans.forEach { bean ->
            val icon = ImageView(context)
            icon.setImageResource(R.drawable.map_ic_local)
            icon.tag = bean
            val screenPoint = subsamplingScaleImageView.sourceToViewCoord(bean.sx, bean.sy)
            if (screenPoint != null) {
                icon.x = screenPoint.x - context.dp2px(45f) / 2
                icon.y = screenPoint.y - context.dp2px(48f)
            }
            icon.setOnClickListener(this)
            icon.visibility = View.GONE
            iconList.add(icon)
        }

    }


    /**
     * 关闭一个标签
     * icon：要关闭的标签
     */
    private fun closeIcon(icon: ImageView) {
        val animator = ValueAnimator.ofFloat(1f, 1.5f, 0f, 0.5f, 0f)
        animator.duration = 500
        animator.addUpdateListener {
            val currentValue: Float = it.animatedValue as Float
            icon.scaleX = currentValue
            icon.scaleY = currentValue
            if (currentValue == 0f) {
                icon.visibility = View.GONE
            }
        }
        animator.start()
    }

    /**
     * 展示一个标签
     * icon：要展示的标签
     */
    private fun showIcon(icon: ImageView) {
        icon.visibility = View.VISIBLE
        val animator = ValueAnimator.ofFloat(0f, 1.2f, 0.8f, 1f)
        animator.duration = 500
        animator.addUpdateListener {
            val currentValue: Float = it.animatedValue as Float
            icon.scaleX = currentValue
            icon.scaleY = currentValue
        }
        animator.start()
    }

    /**
     *关闭所有的标签
     */
    fun closeAllIcon() {
        var delayTime = 0
        iconList.forEach { icon ->
            val animator = ValueAnimator.ofFloat(1f, 1.5f, 0f, 0.5f, 0f)
            animator.duration = 500
            animator.addUpdateListener {
                val currentValue: Float = it.animatedValue as Float
                icon.scaleX = currentValue
                icon.scaleY = currentValue
                if (currentValue == 0f) {
                    icon.visibility = View.GONE
                }
            }
            animator.startDelay = delayTime.toLong()
            animator.start()
            delayTime += 200
        }

    }

    /**
     *展示所有的标签
     */
    fun showAllIcon() {
        iconList.forEach { icon ->
            icon.visibility = View.VISIBLE
            val animator = ValueAnimator.ofFloat(0f, 1.2f, 0.8f, 1f)
            animator.duration = 500
            animator.addUpdateListener {
                val currentValue: Float = it.animatedValue as Float
                icon.scaleX = currentValue
                icon.scaleY = currentValue
            }
            animator.start()
        }
    }

    /**
     * 展示指定范围的标签
     * start：起始位置
     * end：结束位置
     */
    private fun showSomeIcons(start: Int, end: Int) {
        if (start >= 0 && end <= iconList.size) {
            for (i in start..end) {
                val icon = iconList[i]
                icon.visibility = View.VISIBLE
                val animator = ValueAnimator.ofFloat(0f, 1.2f, 0.8f, 1f)
                animator.duration = 500
                animator.addUpdateListener {
                    val currentValue: Float = it.animatedValue as Float
                    icon.scaleX = currentValue
                    icon.scaleY = currentValue
                }
                animator.start()
            }

        }
    }

    /**
     * 从资源文件中设置
     */
    fun setImageFromRes(resId: Int) {
        imageSource = ImageSource.resource(resId)
    }

    /**
     * 设置预览图
     */
    fun setImagePreviewFromRes(resId: Int, dimenX: Int, dimenY: Int) {

    }

    /**
     * 放大并平移到某点
     */
    fun focusToPoint(sx: Float, sy: Float) {
        subsamplingScaleImageView.animateScaleAndCenter(
                1f,
                PointF(sx, sy)
        )?.withDuration(FOCUS_ANIMATION_DURATION)
                ?.withInterruptible(true)?.start()
    }

    override fun onClick(v: View) {
        onIconClickListener?.onIconClick(v)
    }

    interface OnIconClickListener {
        fun onIconClick(v: View)
    }

    /**
     * 地图点击回调
     */
    interface OnPlaceClickListener {
        fun onPlaceClick(v: View)
    }

    fun setMyOnIconClickListener(onIconClickListener: OnIconClickListener) {
        this.onIconClickListener = onIconClickListener
    }

    fun setMyOnPlaceClickListener(onPlaceClickListener: OnPlaceClickListener) {
        this.onPlaceClickListener = onPlaceClickListener
    }

    /**
     * public的方法，传入icon的id就可以展示此icon
     */
    fun showIcon(id: String) {
        iconList.forEach {
            val bean = it.tag as IconBean
            val beanId = bean.id.toString()
            if (id == beanId) {
                showIcon(it)
                return
            }
        }
    }
}