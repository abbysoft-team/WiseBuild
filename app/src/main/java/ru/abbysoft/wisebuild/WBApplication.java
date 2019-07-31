package ru.abbysoft.wisebuild;

import android.app.Application;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.vk.api.sdk.auth.VKAccessToken;

import ru.abbysoft.wisebuild.utils.MiscUtils;

/**
 * Main application class
 *
 * @author apopov
 */
public class WBApplication extends Application {

    private final VKTokenExpiredHandler tokenTracker;

    /**
     * Default constructor
     */
    public WBApplication() {
        tokenTracker = new VKTokenExpiredHandler() {
            @Override
            public void onTokenExpired() {
                // expired token
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();

        VK.initialize(this);
    }
}
