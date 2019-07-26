package ru.abbysoft.wisebuild.assembly;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;
import java.util.Set;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.browser.PartBrowserActivity;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.utils.LayoutUtils;

/**
 * Adapter for create assembly activity
 *
 * @author apopov
 */
public class CreateAssemblyAdapter
        extends RecyclerView.Adapter<CreateAssemblyAdapter.AssemblyPartViewHolder> {

    private final Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts;
    private final Activity activity;

    /**
     * ViewHolder for assembly part
     */
    public static class AssemblyPartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partName;
        private final TextView partPrice;
        private final TextView partType;
        private final ViewGroup noPartContainer;
        private final ViewGroup partContainer;
        private Set<ComputerPart> parts;
        private ComputerPart.ComputerPartType type;
        private ComputerPart currentPart;
        private Activity activity;

        public AssemblyPartViewHolder(@NonNull View itemView) {
            super(itemView);

            partName = itemView.findViewById(R.id.assembly_part_name);
            partPrice = itemView.findViewById(R.id.assembly_part_price);
            partType = itemView.findViewById(R.id.assembly_part_type);
            partContainer = itemView.findViewById(R.id.assembly_card);
            noPartContainer = itemView.findViewById(R.id.assembly_no_part_card);

            // hide main card unless part is loaded, until that moment
            // show card with add button
            LayoutUtils.removeViewFromLayout(partContainer);

            AppCompatImageButton addPart = itemView.findViewById(R.id.assembly_add_part_button);
            addPart.setOnClickListener((view -> addNewPart()));
        }

        private void addNewPart() {
            assert (activity != null);

            PartBrowserActivity.launchForPickPartFrom(activity, type);
        }

        /**
         * Set data for this view
         *
         * @param parts computer parts set
         * @param type type of computer parts
         */
        public void setData(Set<ComputerPart> parts,
                            ComputerPart.ComputerPartType type,
                            Activity activity) {
            this.parts = parts;
            this.type = type;
            this.activity = activity;

            partType.setText(type.getReadableName());

            if (parts == null || parts.isEmpty()) {
                setEmptyView();
            } else {
                currentPart = parts.iterator().next();
                configureView();
            }
        }

        private void setEmptyView() {
            currentPart = null;


        }

        private void configureView() {

        }
    }

    /**
     * Create new assembly adapter
     * @param parts set of parts by type
     * @param activity activity from which behalf would be generated intents
     */
    public CreateAssemblyAdapter(Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts,
                                 Activity activity) {
        this.parts = parts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AssemblyPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assembly_part_view, parent, false);

        return new AssemblyPartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssemblyPartViewHolder holder, int position) {
        ComputerPart.ComputerPartType type =
                (ComputerPart.ComputerPartType) parts.keySet().toArray()[position];

        holder.setData(parts.get(type), type, activity);
    }

    @Override
    public int getItemCount() {
        return parts.keySet().size();
    }

    /**
     * Add new part of specified type
     * @param type type of part
     * @param part new part
     */
    public void addNewPart(ComputerPart.ComputerPartType type, ComputerPart part) {
        Set<ComputerPart> partsOfType = parts.get(type);
        partsOfType.add(part);
    }
}
