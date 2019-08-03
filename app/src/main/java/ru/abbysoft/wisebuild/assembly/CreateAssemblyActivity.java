package ru.abbysoft.wisebuild.assembly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;

public class CreateAssemblyActivity extends AppCompatActivity {

    private SelectedPartsFragment cpuFragment =
            SelectedPartsFragment.Companion.newInstance(ComputerPart.ComputerPartType.CPU);
    private SelectedPartsFragment memoryFragment =
            SelectedPartsFragment.Companion.newInstance(ComputerPart.ComputerPartType.MEMORY_MODULE);
    private SelectedPartsFragment motherboardFragment =
            SelectedPartsFragment.Companion.newInstance(ComputerPart.ComputerPartType.MOTHERBOARD);


    private static final String LOG_TAG = CreateAssemblyActivity.class.toString();

    /**
     * Create intent and launch this activity
     * @param context context of parent activity
     */
    public static void launchActivityFrom(Context context) {
        Intent intent = new Intent(context, CreateAssemblyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assembly);

        populateWithFragments();
    }

    private void populateWithFragments() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .add(R.id.assembly_memory_fragment_container, memoryFragment)
                .add(R.id.assembly_motherboard_fragment_container, motherboardFragment)
                .add(R.id.assembly_cpu_fragment_container, cpuFragment)
                .commit();
    }
}
