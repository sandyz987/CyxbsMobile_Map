package com.mredrock.cyxbs.discover.map.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author zhangzhe
 * @date 2020/8/8
 * @description 控制软键盘的显示和隐藏
 */

public class KeyboardController {

    /**
     * 隐藏键盘
     * 弹窗弹出的时候把键盘隐藏掉
     *
     * @param v 要获取焦点的view，一般填editText，隐藏时无所谓
     */
    public static void hideInputKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    /**
     * 弹起键盘
     */
    public static void showInputKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }
}
