package com.miss.meet.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.miss.meet.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dell on 2017/6/17.
 */

public class ImageTools {
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.loading)
            .showImageForEmptyUri(R.drawable.ic_empty)
            .showImageOnFail(R.drawable.error).cacheInMemory(true)
            .cacheOnDisk(true)
            // .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
            // ````````````````````````````````````
            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类 ?
            .build();

    public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ImageTools() {
        // TODO Auto-generated constructor stub
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
