package com.mredrock.cyxbs.discover.map.component;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

public class ClickView extends androidx.appcompat.widget.AppCompatImageView {
    public ClickView(Context context) {
        super(context);
        //        必须设置setClickable(true), 要不然像ImageView 默认是点击不了的，它就收不到Action_Up 事件
//        如果一个view 的onTouchEvent 的Action_Down 返回false ，那么view的 Action_Up 是不会执行的
        this.setClickable(true);
    }

    public ClickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);
    }

    public ClickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
    }

    /**
     * 顶点判断【0：中间】【1：上】【2：右】【3：下】【4：左】
     **/
    private int pivot = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                getX() 和 getY() 获取的是距离自身的坐标位置
                startAnimDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                endAnimal();
                break;

        }
        boolean b = super.onTouchEvent(event);

        return b;
    }

    /**
     * 顶点判断【0：中间】【1：上】【2：右】【3：下】【4：左】
     **/
    public OvershootInterpolator interpolator = new OvershootInterpolator(3f);

    public void startAnimDown(float touchX, float touchY) {
//        获取的是自身的相对坐标位置
        if (width / 5 * 2 < touchX && touchX < width / 5 * 3 && height / 5 * 2 < touchY && touchY < height / 5 * 3) {
//            代表中心位置
            pivot = 0;
        } else if (touchX < width / 2 && touchY < height / 2) {
            if (touchX > touchY) {
                pivot = 1;// 代表用户点击的上面
            } else {
                pivot = 4; // 代表用户点击的左边
            }
        } else if (touchX < width / 2 && touchY > height / 2) {
            if (touchX < (height - touchY)) {//
                pivot = 4;// 代表用户点击左边
            } else {
                pivot = 3; // 代表用户点击下面
            }
        } else if (touchX > width / 2 && touchY < height / 2) {
            if (width - touchX < touchY) {//点击右边
                pivot = 2;// 点击右边
            } else {
                pivot = 1; // 代表用户点击上面
            }
        } else if (touchX > width / 2 && touchY > height / 2) {
            if (width - touchX > height - touchY) {//点击下面
                pivot = 3;//点击下面
            } else {
                pivot = 2; // 代表用户点击右边
            }
        }
//        上面这种算法只适合正方形的图片
        String myAnim = "";
        switch (pivot) {
            case 0:
                startCenterSmallAnimal();
                return;
            case 1:
            case 3:
                myAnim = "rotationX";
                break;
            case 2:
            case 4:
                myAnim = "rotationY";
                break;
        }
//        设置动画的基准点
        this.setPivotX(width / 2);
        this.setPivotY(height / 2);
//        【0：中间】【1：上】【2：右】【3：下】【4：左】
        if (pivot == 4) {
            endRotationValue = -17f;
        } else if (pivot == 2) {
            endRotationValue = 17f;
        } else if (pivot == 1) {
            endRotationValue = 17f;
        } else if (pivot == 3) {
            endRotationValue = -17f;
        }
        ObjectAnimator animObject = ObjectAnimator.ofFloat(this, myAnim.toString(), 0, endRotationValue)
                .setDuration(300);
        animObject.setInterpolator(interpolator);
        animObject.start();
    }

    private void startCenterSmallAnimal() {
        int tzStart = (int) this.getTranslationZ();
        this.setTag(tzStart);

        PropertyValuesHolder tz = PropertyValuesHolder.ofFloat("translationZ", tzStart, 0);
        PropertyValuesHolder tX = PropertyValuesHolder.ofFloat("scaleX", this.getScaleX(), 0.9f);
        PropertyValuesHolder tY = PropertyValuesHolder.ofFloat("scaleY", this.getScaleY(), 0.9f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, tz, tX, tY).setDuration(300);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.start();
    }

    private void startCenterBigAnimal() {
        int tzValue = (int) this.getTag();
        PropertyValuesHolder tz = PropertyValuesHolder.ofFloat("translationZ", 0, tzValue);
        PropertyValuesHolder tX = PropertyValuesHolder.ofFloat("scaleX", this.getScaleX(), 1f);
        PropertyValuesHolder tY = PropertyValuesHolder.ofFloat("scaleY", this.getScaleY(), 1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, tz, tX, tY).setDuration(300);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.start();
    }

    //    结束动画
    public void endAnimal() {
        String anim = "";
        switch (pivot) {
            case 0:
                startCenterBigAnimal();
                return;
//            【0：中间】【1：上】【2：右】【3：下】【4：左】
            case 1:
            case 3:
                anim = "rotationX";
                break;
            case 2:
            case 4:
                anim = "rotationY";
                break;
        }

        if (pivot == 2 || pivot == 4) {
            startRotationValue = (int) this.getRotationY();
        } else {
            startRotationValue = (int) this.getRotationX();
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, anim.toString(), startRotationValue, 0);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.start();
    }

    float endRotationValue = 0;
    int startRotationValue = 0;

    //    当宽高发生变化时进行的回调
    int height;
    int width;

    //    System.out: touchX :552.0touchY :492.0
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        width = w;
        height = h;
    }
}