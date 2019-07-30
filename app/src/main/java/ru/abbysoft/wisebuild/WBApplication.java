package ru.abbysoft.wisebuild;

import android.app.Application;

import com.vk.api.sdk.VK;

/**
 * Main application class
 *
 * @author apopov
 */
public class WBApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VK.initialize(this);
    }
}
