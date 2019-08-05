package ru.abbysoft.wisebuild.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.Toast;

import ru.abbysoft.wisebuild.PartListFragment;
import ru.abbysoft.wisebuild.PartParametersActivity;
import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;

public class BrowserActivity extends AppCompatActivity implements PartListFragment.OnPartListItemClickedListener {

    private static final int PICK_PART_REQUEST = 0;
    private static final String PART_TYPE_EXTRA = "PART_TYPE";
    private static final String PART_ID_EXTRA = "PART_ID";

    private ViewPager viewPager;
    private TabLayout tabs;

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

        ActionBar actionBar = getSupportActionBar();
        if (isLaunchedForPart()) {
            actionBar.setTitle(R.string.select_part);
        } else {
            actionBar.setTitle(R.string.part_browser);
        }
    }

    private void setUpViewPager() {
        this.viewPager = findViewById(R.id.browserViewPager);

        String partType = getIntent().getStringExtra(PART_TYPE_EXTRA);
        if (partType != null) {
            this.viewPager.setCurrentItem(ComputerPart.ComputerPartType.valueOf(partType).getId());
        }

        BrowserPagerAdapter adapter = new BrowserPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        this.tabs = findViewById(R.id.browserTabs);
        tabs.setupWithViewPager(viewPager);
    }

    private boolean isLaunchedForPart() {
        return getIntent().hasExtra(PART_TYPE_EXTRA) && getCallingActivity() != null;
    }

    public void addPartButtonClicked(View view) {
        PartParametersActivity.launch(this,
                ComputerPart.ComputerPartType.values()[viewPager.getCurrentItem()]);
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
        intent.putExtra(PART_TYPE_EXTRA, type.toString());

        activity.startActivityForResult(intent, PICK_PART_REQUEST);
    }

    @Override
    public void onPartListItemClicked(long id) {
        if (!isLaunchedForPart()) {
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(PART_ID_EXTRA, id);
        setResult(RESULT_OK);

        finish();
    }
}
