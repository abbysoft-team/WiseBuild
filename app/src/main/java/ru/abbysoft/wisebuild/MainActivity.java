package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * MainActivity class
 *
 * Here you can choose action to do
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when addPartButton clicked
     * @param view addPartButton
     */
    public void addPartButtonClicked(View view) {
        Intent intent = new Intent(this, AddPartActivity.class);

        startActivity(intent);
    }
}
