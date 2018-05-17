package com.example.geotracker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

public class DrawableUtils {
    @Nullable
    public static Drawable getTintedDrawable(@NonNull Context context, @DrawableRes int drawable, @ColorInt int tintColor) {
        Drawable result = AppCompatResources.getDrawable(context, drawable);
        if (result != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Drawable resultWrap = (DrawableCompat.wrap(result));
                result = resultWrap.mutate();
            }
            DrawableCompat.setTint(result, tintColor);
        }
        return result;
    }

    public static Bitmap setDrawableHeightWithKeepRatio(@NonNull Drawable drawable, @Dimension int finalHeightPx) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        float ratio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        int drawableWidth = Math.round(((float)finalHeightPx) * ratio);
        Bitmap bitmap = Bitmap.createBitmap(Math.round(drawableWidth), Math.round(finalHeightPx), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
