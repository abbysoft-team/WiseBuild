package ru.abbysoft.wisebuild.assembly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.exception.SlotLimitException;
import ru.abbysoft.wisebuild.model.AssembledPC;
import ru.abbysoft.wisebuild.model.CPU;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.MemoryModule;
import ru.abbysoft.wisebuild.model.Motherboard;
import ru.abbysoft.wisebuild.storage.DBFactory;
import ru.abbysoft.wisebuild.utils.MiscUtils;

public class CreateAssemblyActivity extends AppCompatActivity implements PartListener {

    private static final String LOG_TAG = CreateAssemblyActivity.class.toString();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

    private TextView totalPriceView;
    private long totalPrice;

    private Motherboard motherboard;
    private CPU cpu;
    private MemoryModule memoryModule;

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

        totalPriceView = findViewById(R.id.assembly_total_price);
        totalPriceView.setText("$0");

        configureRecyclerView();
    }

    private void configureRecyclerView() {
        recyclerView = findViewById(R.id.assembly_parts_recycler);
        recyclerView.setHasFixedSize(true);

        recyclerLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayout);

        recyclerAdapter =
                new CreateAssemblyAdapter(getInitialParts(), this, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private Map<ComputerPart.ComputerPartType, Set<ComputerPart>> getInitialParts() {
        Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts = new HashMap<>();
        for (ComputerPart.ComputerPartType type :
                ComputerPart.ComputerPartType.getEntriesWithoutAssembly()) {

            parts.put(type, new HashSet<>());
        }

        return parts;
    }

    @Override
    public void partReceived(ComputerPart part) {
        totalPrice += part.getPriceUsd();

        String text = "$" + totalPrice;
        totalPriceView.setText(text);

        if (part instanceof MemoryModule) {
            memoryModule = (MemoryModule) part;
        } else if (part instanceof CPU) {
            cpu = (CPU) part;
        } else if (part instanceof Motherboard) {
            motherboard = (Motherboard) part;
        }
    }

    /**
     * Save assembly action
     *
     * @param view view from which action was accessed
     */
    public void saveAssembly(View view) {
        if (!validateView()) {
            return;
        }

        AssembledPC pc = new AssembledPC();
        pc.setName(new Date().toString());
        pc.setCpu(cpu);

        try {
            pc.addMemoryModule(memoryModule);
        } catch (SlotLimitException e) {
            Log.e(LOG_TAG, "Cannot add more memory modules");
            e.printStackTrace();
        }

        pc.setMotherboard(motherboard);

        DBFactory.getDatabase().storePart(pc);

        MiscUtils.showMessageDialogAndFinish("Success",
                "Assembly saved successfully", this);
    }

    private boolean validateView() {
        if (memoryModule == null) {
            showErrorDialog("Please add at least one plank of memory");
            return false;
        }
        if (cpu == null) {
            showErrorDialog("Please choose CPU");
            return false;
        }
        if (motherboard == null) {
            showErrorDialog("Please choose motherboard");
            return false;
        }

        return true;
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(getString(R.string.cannot_continue));
        builder.setMessage(message);

        builder.setPositiveButton(android.R.string.ok, null);

        builder.create().show();
    }


}
