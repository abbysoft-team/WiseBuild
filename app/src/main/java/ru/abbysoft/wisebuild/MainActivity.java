package ru.abbysoft.wisebuild;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.exceptions.VKApiExecutionException;
import com.vk.api.sdk.utils.VKUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import ru.abbysoft.wisebuild.assembly.CreateAssemblyActivity;
import ru.abbysoft.wisebuild.browser.BrowserActivity;
import ru.abbysoft.wisebuild.model.CPU;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.MemoryModule;
import ru.abbysoft.wisebuild.model.Motherboard;
import ru.abbysoft.wisebuild.model.VkUser;
import ru.abbysoft.wisebuild.storage.DBFactory;
import ru.abbysoft.wisebuild.storage.DBInterface;
import ru.abbysoft.wisebuild.ui.login.LoginActivity;
import ru.abbysoft.wisebuild.utils.MiscUtils;
import ru.abbysoft.wisebuild.utils.ModelUtils;

/**
 * MainActivity class
 *
 * Here you can choose action to do
 */
public class MainActivity extends AppCompatActivity {

    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeMessage = findViewById(R.id.main_activity_header);
        welcomeMessage.setVisibility(View.INVISIBLE);

        String[] fingerPrints = VKUtils.getCertificateFingerprint(this, this.getPackageName());
        Log.v("MainActivity", "FingerPrints: " + Arrays.toString(fingerPrints));

        createTestData();
    }

    /**
     * Temporary method for populating default DB
     */
    private void createTestData() {
        DBInterface db = DBFactory.getDatabase();

        for (int i = 0; i < 50; i++) {
            db.storePart(ModelUtils.generateRandomPart());
        }
    }

    /**
     * This method is called when addPartButton clicked
     * @param view addPartButton
     */
    public void addPartButtonClicked(View view) {
        AddPartActivity.createIntentFrom(this, getClass());
    }

    public void browsePartsButtonClicked(View view) {
//        Intent intent = new Intent(this, BrowserActivity.class);
//        startActivity(intent);
        BrowserActivity.launchForPickPartFrom(this, ComputerPart.ComputerPartType.CPU);
    }

    public void createAssemblyButtonClicked(View view) {
        CreateAssemblyActivity.launchActivityFrom(this);
    }

    public void openRandomPartButtonClicked(View view) {
        ComputerPart randomOne = ModelUtils.generateRandomPart();
        DBFactory.getDatabase().storePart(randomOne);

        PartParametersActivity.launchForViewParametersOf(randomOne.getId(), this);
    }

    public void signInButtonClicked(View view) {
        LoginActivity.startFrom(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {
            loginActivityFinished();
        }
    }

    private void loginActivityFinished() {
        if (!VK.isLoggedIn()) {
            return;
        }

        MiscUtils.getUserNameAsync(new VKApiCallback<VkUser>() {
            @Override
            public void success(VkUser vkUser) {
                loginSuccess(vkUser);
            }

            @Override
            public void fail(@NotNull VKApiExecutionException e) {
                MiscUtils.showErrorDialogAndFinish("sign in error",
                        "Cannot receive user info. Please try relogin",
                        MainActivity.this);
                welcomeMessage.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
        });
    }

    private void loginSuccess(VkUser user) {
        welcomeMessage.setText(getString(R.string.welcome_message, user.getFirstName()));
        welcomeMessage.setVisibility(View.VISIBLE);
    }
}
