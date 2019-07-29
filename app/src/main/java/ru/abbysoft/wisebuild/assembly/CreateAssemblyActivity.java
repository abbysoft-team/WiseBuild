package ru.abbysoft.wisebuild.assembly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;

public class CreateAssemblyActivity extends AppCompatActivity implements PartListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

    private TextView totalPriceView;
    private long totalPrice;

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
    }
}
