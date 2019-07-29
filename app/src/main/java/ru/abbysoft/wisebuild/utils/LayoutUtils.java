package ru.abbysoft.wisebuild.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

/**
 * Helper functions related to layout and structure of views
 */
public class LayoutUtils {

    /**
     * Remove specified view from layout
     *
     * @param view view to be removed
     */
    public static void removeViewFromLayout(View view) {
        ((ViewManager)view.getParent()).removeView(view);
    }

    /**
     * Add view to layout and make visible
     *
     * @param view component to be added
     * @param layout new parent of view
     */
    public static void addViewToLayout(View view, ViewGroup layout) {
        layout.addView(view);
    }
}
