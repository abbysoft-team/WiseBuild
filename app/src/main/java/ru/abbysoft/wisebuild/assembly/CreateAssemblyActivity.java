package ru.abbysoft.wisebuild.assembly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.databinding.ActivityCreateAssemblyBinding;
import ru.abbysoft.wisebuild.exception.SlotLimitException;
import ru.abbysoft.wisebuild.model.AssembledPC;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.storage.DBFactory;
import ru.abbysoft.wisebuild.utils.MiscUtils;

public class CreateAssemblyActivity extends AppCompatActivity {

    private SelectedPartsFragment cpuFragment =
            SelectedPartsFragment.Companion.newInstance(ComputerPart.ComputerPartType.CPU);
    private SelectedPartsFragment memoryFragment =
            SelectedPartsFragment.Companion.newInstance(ComputerPart.ComputerPartType.MEMORY_MODULE);
    private SelectedPartsFragment motherboardFragment =
            SelectedPartsFragment.Companion.newInstance(ComputerPart.ComputerPartType.MOTHERBOARD);

    private ActivityCreateAssemblyBinding binding;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_assembly);

        AssemblyViewModel model = ViewModelProviders.of(this).get(AssemblyViewModel.class);
        binding.setViewModel(model);
        binding.setTotalPrice(0L);

        model.getCurrentParts().observe(this, (parts) -> {
            binding.setTotalPrice(model.calculateTotalPrice());
        });

        model.getNextAction().observe(this, (action) -> {
            if (action == AssemblyViewModel.AssemblyAction.SAVE_ASSEMBLY) {
                try {
                    saveAssembly();
                } catch (SlotLimitException e) {
                    e.printStackTrace();
                }
            }
        });

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

    private void saveAssembly() throws SlotLimitException {
        AssemblyViewModel model = ViewModelProviders.of(this).get(AssemblyViewModel.class);

        if (!validateView(model)) {
            return;
        }

        AssembledPC pc = new AssembledPC();
        pc.setCpu(model.getCurrentCpu().getValue());
        pc.setMotherboard(model.getCurrentMotherboard().getValue());
        pc.addMemoryModule(model.getCurrentMemory().getValue());
        pc.setName(new Date().toString());
        pc.setPriceUsd(binding.getTotalPrice());

        DBFactory.getDatabase().storePart(pc);

        MiscUtils.showMessageDialogAndFinish("Success",
                "Assembly saved successfully", this);
    }

    private boolean validateView(AssemblyViewModel model) {
        if (model.getCurrentMemory().getValue() == null) {
            MiscUtils.showErrorDialog("Please add at least one plank of memory", this);
            return false;
        }
        if (model.getCurrentCpu().getValue() == null) {
            MiscUtils.showErrorDialog("Please choose CPU", this);
            return false;
        }
        if (model.getCurrentMotherboard().getValue() == null) {
            MiscUtils.showErrorDialog("Please choose motherboard", this);
            return false;
        }

        return true;
    }
}
