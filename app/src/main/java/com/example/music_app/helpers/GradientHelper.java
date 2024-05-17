package com.example.music_app.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_app.R;

public class GradientHelper {
    public static void applyGradient(Context context, View view, String imageUrl) {
        GradientDrawable gradientDrawable = (GradientDrawable) context.getDrawable(R.drawable.background_gradient);
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int mutedColor = palette.getMutedColor(context.getColor(R.color.neutral2));
                                int darkVibrant = palette.getDarkVibrantColor(context.getColor(R.color.neutral2));
                                if (mutedColor == context.getColor(R.color.neutral2))
                                    gradientDrawable.setColors(new int[]{context.getColor(R.color.neutral0), darkVibrant});
                                else
                                    gradientDrawable.setColors(new int[]{context.getColor(R.color.neutral0), mutedColor});
                                view.setBackground(gradientDrawable);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public static void applyGradient(Context context, View view, int drawableId) {
        GradientDrawable gradientDrawable = (GradientDrawable) context.getDrawable(R.drawable.background_gradient);
        Glide.with(context)
                .asBitmap()
                .load(drawableId)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int mutedColor = palette.getMutedColor(context.getColor(R.color.neutral2));
                                int darkVibrant = palette.getDarkVibrantColor(context.getColor(R.color.neutral2));
                                if (mutedColor == context.getColor(R.color.neutral2))
                                    gradientDrawable.setColors(new int[]{context.getColor(R.color.neutral0), darkVibrant});
                                else
                                    gradientDrawable.setColors(new int[]{context.getColor(R.color.neutral0), mutedColor});
                                view.setBackground(gradientDrawable);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public static void applyDoubleGradient(Context context, View view, String imageUrl) {
        GradientDrawable gradientDrawable = (GradientDrawable) context.getDrawable(R.drawable.linear_background_angle0);
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int vibrantColor = palette.getVibrantColor(context.getColor(R.color.neutral2));
                                int mutedColor = palette.getMutedColor(context.getColor(R.color.neutral2));
                                int darkVibrant = palette.getDarkVibrantColor(context.getColor(R.color.neutral2));
                                if (mutedColor == context.getColor(R.color.neutral2))
                                    gradientDrawable.setColors(new int[]{vibrantColor, darkVibrant});
                                else
                                    gradientDrawable.setColors(new int[]{darkVibrant, mutedColor});
                                view.setBackground(gradientDrawable);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
