package ru.abbysoft.wisebuild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ru.abbysoft.wisebuild.model.ComputerPart;

public class PartCreationActivity extends AppCompatActivity {

    public static final String PART_TYPE_EXTRA = "PART_TYPE";

    private volatile ComputerPart.ComputerPartType partType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_creation);

        getPassedExtras();
    }

    private void getPassedExtras() {
        Intent intent = getIntent();

        partType = (ComputerPart.ComputerPartType) intent.getSerializableExtra(PART_TYPE_EXTRA);

        TextView headerMessage = findViewById(R.id.part_creation_label);
        headerMessage.setText("Creating new " + partType.getReadableName());
    }
}
