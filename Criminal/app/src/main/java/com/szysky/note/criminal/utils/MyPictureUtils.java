package com.szysky.note.criminal.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Author :  suzeyu
 * Time   :  2016-09-09  下午4:03
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 进行图片处理的工具类
 */

public class MyPictureUtils {

    /**
     * 从本地文件获得一个和手机窗口大小相近的图片
     * @param act   使用此方法的Activity对象
     * @param path  图片的存储路径
     * @return  返回一个BitmapDrawable可直接设置图片
     */
    public static BitmapDrawable getScaleDrawable(Activity act, String path){

        Display display = act.getWindowManager().getDefaultDisplay();
        int destWidth = display.getWidth();
        int destHeight = display.getHeight();

        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int srcHeight = options.outHeight;
        int srcWidth = options.outWidth;

        int sampleSize = 1;

        if (srcHeight > destHeight || srcWidth > destWidth){
            if (srcWidth > srcHeight){
                sampleSize = srcHeight / destHeight;
            }else{
                sampleSize = srcWidth / destWidth;
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(act.getResources(), bitmap);

    }


    /**
     * 提供一个 卸载ImageView控件的前景上的图片, 并手动释放bitmap
     */
    public static void cleanImageView(ImageView imageView){
        if (!(imageView.getDrawable() instanceof BitmapDrawable)){
            return ;
        }

        // clean up the view's image for the sake of memory
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        imageView.setImageDrawable(null);
        drawable.getBitmap().recycle();
    }
}
