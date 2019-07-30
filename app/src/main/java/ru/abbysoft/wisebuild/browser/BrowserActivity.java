package ru.abbysoft.wisebuild.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import ru.abbysoft.wisebuild.AddPartActivity;
import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;

public class BrowserActivity extends AppCompatActivity {

    private static final int PICK_PART_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add_part_fab);
        fab.setOnClickListener(this::addPartButtonClicked);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpViewPager();
    }

    private void setUpViewPager() {
        ViewPager viewPager = findViewById(R.id.browserViewPager);
        viewPager.setAdapter(new BrowserPagerAdapter(getSupportFragmentManager()));

        TabLayout tabs = findViewById(R.id.browserTabs);
        tabs.setupWithViewPager(viewPager);
    }

    public void addPartButtonClicked(View view) {
        AddPartActivity.createIntentFrom(this, getClass());
    }

    /**
     * Launch this activity from given context in order to recieve
     * part back from part browser_content
     *
     * @param activity activity from which to launch this activity
     * @param type type of part to be picked
     */
    public static void launchForPickPartFrom(Activity activity,
                                             ComputerPart.ComputerPartType type) {

        Intent intent = new Intent(activity, BrowserActivity.class);
        activity.startActivityForResult(intent, PICK_PART_REQUEST);
    }
}
