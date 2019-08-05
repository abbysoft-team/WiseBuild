package ru.abbysoft.wisebuild.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

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
}
