package ru.abbysoft.wisebuild.assembly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;
import java.util.Set;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;

/**
 * Adapter for create assembly activity
 *
 * @author apopov
 */
public class CreateAssemblyAdapter
        extends RecyclerView.Adapter<CreateAssemblyAdapter.AssemblyPartViewHolder> {

    private final Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts;

    /**
     * ViewHolder for assembly part
     */
    public static class AssemblyPartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partName;
        private final TextView partPrice;
        private Set<ComputerPart> parts;
        private ComputerPart currentPart;

        public AssemblyPartViewHolder(@NonNull View itemView) {
            super(itemView);

            partName = itemView.findViewById(R.id.assembly_part_name);
            partPrice = itemView.findViewById(R.id.assembly_part_price);
        }

        public void setParts(Set<ComputerPart> parts) {
            this.parts = parts;

            if (parts == null || parts.isEmpty()) {
                // empty view layout
            } else {
                currentPart = parts.iterator().next();
            }
        }
    }

    /**
     * Create new assembly adapter
     * @param parts set of parts by type
     */
    public CreateAssemblyAdapter(Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts) {
        this.parts = parts;
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

        holder.setParts(parts.get(type));
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
