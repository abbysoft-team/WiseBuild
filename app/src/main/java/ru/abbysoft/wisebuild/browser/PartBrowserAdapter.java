package ru.abbysoft.wisebuild.browser;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.storage.DBInterface;

/**
 * Adapter for computer parts list
 */
class PartBrowserAdapter extends RecyclerView.Adapter<PartBrowserAdapter.ViewHolder> {
    private DBInterface db;
    private List<ComputerPart> parts;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView name;
        TextView price;
        ImageView photo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            name = view.findViewById(R.id.part_name);
            price = view.findViewById(R.id.part_price);
            photo = view.findViewById(R.id.part_photo);
        }
    }

    PartBrowserAdapter(DBInterface database) {
        db = database;
        parts = database.getAllComponents();
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
        holder.name.setText(part.getName());
        holder.price.setText(Integer.toString((int) part.getPriceUsd()));
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

}
