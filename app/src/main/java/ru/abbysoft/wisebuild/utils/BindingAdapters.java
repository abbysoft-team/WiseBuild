package ru.abbysoft.wisebuild.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import ru.abbysoft.wisebuild.R;

public class BindingAdapters {

    @BindingAdapter("app:hideIfNotZero")
    public static void hideIfNotZero(View view, int number) {
        hide(view, number != 0);
    }

    @BindingAdapter("app:hideIfZero")
    public static void hideIfZero(View view, int number) {
        hide(view, number == 0);
    }

    @BindingAdapter("app:hide")
    public static void hide(View view, boolean hide) {
        if (hide) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("app:setImageOrPlaceholder")
    public static void setImageOrPlaceholder(ImageView view, Bitmap bitmap) {
        if (bitmap == null) {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(),
                            R.mipmap.ic_launcher_round));
        } else {
            view.setImageBitmap(bitmap);
        }
    }
}
