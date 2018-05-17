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

/**
 * Utility class including all static method dealing with repeating operations involving {@link Drawable} or {@link Bitmap}
 * objects.
 */
public class DrawableUtils {
    /**
     * Static method which handles tinting of any kind of {@link Drawable} (including {@link android.graphics.drawable.VectorDrawable})
     * in a backwards-compatible fashion.
     * @param context the {@link Context} instance.
     * @param drawable the {@link Drawable} resource we need to apply tint on, specified as a res Id integer.
     * @param tintColor the color we want to apply over our {@link Drawable}, must be specified as a parsed Color integer.
     * @return an instance of the input {@link Drawable} after tint has been applied.
     */
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

    /**
     * Static method handling resizing an input {@link Drawable} object to a target height: width is calculated respecting
     * the original Drawable's ratio.
     * @param drawable the {@link Drawable} we want to resize
     * @param finalHeightPx an integer value representing the requested height value in pixels
     * @return a {@link Bitmap} representing the resized input object.
     */
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
