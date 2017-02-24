package com.enihsyou.shane.packagetracker.helper;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 跳转到UI线程设置图片
 */
public interface OnDownloadCallback {
    void SetBitmapCallBack(Bitmap bitmap, ImageView view);
}
