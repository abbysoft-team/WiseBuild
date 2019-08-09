package ru.abbysoft.wisebuild;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.utils.MiscUtils;
import ru.abbysoft.wisebuild.utils.ModelUtils;

public class AddPartActivity extends AppCompatActivity {
    /**
     * This extra must be put in intent in order to override
     * up button action. Up button will redirect back to specified
     * component instead of one that manifest contains.
     */
    public static final String PARENT_CLASS_COMPONENT_EXTRA = "parentComponent";

    /**
     * Class and package of activity that created intent for this one
     */
    private ComponentName parentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part);

        // get parent component if exists
        parentActivity = getIntent().getParcelableExtra(PARENT_CLASS_COMPONENT_EXTRA);

        addNewComponentButtons();
    }

    private void addNewComponentButtons() {
        List<ComputerPart.ComputerPartType> types
                = ComputerPart.ComputerPartType.Companion.getEntriesWithoutAssembly();

        for (ComputerPart.ComputerPartType type : types) {
            addButtonForType(type);
        }

    }

    private void addButtonForType(ComputerPart.ComputerPartType type) {
        ViewGroup container = findViewById(R.id.add_part_container);
        Button referenceButton = findViewById(R.id.add_part_reference_button);

        Button button = new Button(this);
        button.setLayoutParams(referenceButton.getLayoutParams());
        button.setText(type.getReadableName());
        button.setTextSize(Math.max(15, (referenceButton.getTextSize() / 1.7f)));
        button.setOnClickListener((view -> {
            PartParametersActivity.launch(this, type);
        }));
        button.setTextColor(referenceButton.getTextColors());
        button.setBackground(referenceButton.getBackground());
        container.addView(button);
    }

    /**
     * Callback for "add new motherboard" button
     *
     * @param view button itself
     */
    public void addMotherboard(View view) {
        PartParametersActivity.launch(this, ComputerPart.ComputerPartType.MOTHERBOARD);
    }

    /**
     * Callback for "add new CPU" button
     *
     * @param view button itself
     */
    public void addCPU(View view) {
        PartParametersActivity.launch(this,ComputerPart.ComputerPartType.CPU);
    }

    /**
     * Callback for "add new memory" button
     *
     * @param view button itself
     */
    public void addMemory(View view) {
        PartParametersActivity.launch(this,ComputerPart.ComputerPartType.MEMORY_MODULE);
    }

    /**
     * Create intent from context to this activity
     *
     * Up button functionality will be changed in order to
     * return to creator of this intent
     *
     * @param context context of parent activity
     * @param parentClass parent class
     */
    public static void createIntentFrom(Context context, Class parentClass) {
        Intent intent = new Intent(context, AddPartActivity.class);
        intent.putExtra(PARENT_CLASS_COMPONENT_EXTRA, new ComponentName(context, parentClass));
        context.startActivity(intent);
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent defaultIntent = super.getSupportParentActivityIntent();

        if (parentActivity == null) {
            return defaultIntent;
        }

        // return caller activity intent
        Intent newIntent = new Intent();
        newIntent.setComponent(parentActivity);
        return newIntent;
    }
}
