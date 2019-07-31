package ru.abbysoft.wisebuild.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import org.jetbrains.annotations.NotNull;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.utils.MiscUtils;

public class LoginActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST_CODE = 1;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Login with vk sdk api
     *
     * @param view view
     */
    public void vkLoginButtonClicked(View view) {
        VK.login(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
            @Override
            public void onLoginFailed(int i) {
                MiscUtils.showErrorDialogAndFinish("Auntification failed",
                        "Something went wrong. Try again later",
                        LoginActivity.this);
            }

            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Launch this activity from given
     *
     * @param activity activity from which to launch this one
     */
    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);

        activity.startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }
}
