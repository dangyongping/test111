package com.chinafeisite.tianbu.competition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/*
 *  @项目名：  RK3188-BP 
 *  @包名：    com.chinafeisite.tianbu.competition
 *  @文件名:   CompetitionUtil
 *  @创建者:   Administrator
 *  @创建时间:  2016/12/21 9:44
 *  @描述：    TODO
 */
public class CompetitionUtil {
    private static final String TAG = "CompetitionUtil";
    public  static  void  PicassoMethod(Context context,String url , ImageView view) {
        Picasso.with(context)
               .load(url)
               .transform( new CircleTransform())
               .into(view);


    }
    public static class CircleTransform
            implements Transformation
    {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint  paint  = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                                                   BitmapShader.TileMode.CLAMP,
                                                   BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
