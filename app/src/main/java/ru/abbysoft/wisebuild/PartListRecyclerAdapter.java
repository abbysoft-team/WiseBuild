package ru.abbysoft.wisebuild;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.storage.DBInterface;

/**
 * Adapter for computer parts list
 */
class PartListRecyclerAdapter extends RecyclerView.Adapter<PartListRecyclerAdapter.ViewHolder> {
    private DBInterface db;
    private List<ComputerPart> parts;
    private Context context;
    private ComputerPart.ComputerPartType type;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView name;
        TextView price;
        ImageView photo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            name = view.findViewById(R.id.assembly_part_name);
            price = view.findViewById(R.id.part_price);
            photo = view.findViewById(R.id.part_photo);
        }
    }

    PartListRecyclerAdapter(DBInterface database, Context context) {
        db = database;
        this.context = context;
        parts = database.getAllComponents();
        type = null;
    }

    PartListRecyclerAdapter(DBInterface database, Context context, ComputerPart.ComputerPartType type) {
        this.db = database;
        this.context = context;
        this.type = type;

        if (type != null) {
            this.parts = database.getComponentsOfType(type);
        } else {
            this.parts = database.getAllComponents();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.part_view, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComputerPart part = parts.get(position);
        holder.name.setText(part.getTrimmedName());

        String unknownString = this.context.getString(R.string.unknown);
        String priceText = part.getPriceUsd() != 0 ?
                Integer.toString((int) part.getPriceUsd()) : unknownString;

        holder.price.setText(String.format(context.getString(R.string.price_value),priceText));

        if (part.getPhoto() != null) {
            holder.photo.setImageBitmap(part.getPhoto());
        } else {
            holder.photo.setImageResource(getDrawableResourceForPartType(part.getType()));
        }

        holder.view.setOnClickListener((View view) -> {
            onPartClicked(position);
        });
    }

    private void onPartClicked(int position) {
        ComputerPart part = parts.get(position);

        Toast toast = Toast.makeText(
                context,
                "Part " + position + " clicked! Part info not implemented yet.",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private int getDrawableResourceForPartType(ComputerPart.ComputerPartType type) {
        switch (type) {
            case CPU:
                return R.drawable.default_cpu_image;
            case MEMORY_MODULE:
                return R.drawable.default_ram_image;
            case MOTHERBOARD:
                return R.drawable.default_motherboard_image;
        }

        return R.drawable.default_cpu_image;
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

}