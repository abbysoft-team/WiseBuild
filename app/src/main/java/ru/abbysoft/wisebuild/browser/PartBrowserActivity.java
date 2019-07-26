package ru.abbysoft.wisebuild.browser;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import ru.abbysoft.wisebuild.AddPartActivity;
import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.storage.DBFactory;

public class PartBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add_part_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPartButtonClicked(view);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.partRecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new PartBrowserAdapter(DBFactory.getDatabase(), this);
        recyclerView.setAdapter(adapter);
    }

    public void addPartButtonClicked(View view) {
        AddPartActivity.createIntentFrom(this, getClass());
    }
}
