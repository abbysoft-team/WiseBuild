package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.abbysoft.wisebuild.model.ComputerPart;

public class AddPartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part);
    }

    /**
     * Callback for "add new motherboard" button
     *
     * @param view button itself
     */
    public void addMotherboard(View view) {
        launchPartCreationActivity(ComputerPart.ComputerPartType.MOTHERBOARD);
    }

    /**
     * Callback for "add new CPU" button
     *
     * @param view button itself
     */
    public void addCPU(View view) {
        launchPartCreationActivity(ComputerPart.ComputerPartType.CPU);
    }

    /**
     * Callback for "add new memory" button
     *
     * @param view button itself
     */
    public void addMemory(View view) {
        launchPartCreationActivity(ComputerPart.ComputerPartType.MEMORY_MODULE);
    }

    private void launchPartCreationActivity(ComputerPart.ComputerPartType partType) {
        Intent intent = new Intent(this, PartCreationActivity.class);
        intent.putExtra(PartCreationActivity.PART_TYPE_EXTRA, partType);
        startActivity(intent);
    }
}
