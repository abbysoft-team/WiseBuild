package ru.abbysoft.wisebuild.assembly;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.PartParameter;
import ru.abbysoft.wisebuild.utils.LayoutUtils;
import ru.abbysoft.wisebuild.utils.ModelUtils;

/**
 * Adapter for create assembly activity
 *
 * @author apopov
 */
public class CreateAssemblyAdapter
        extends RecyclerView.Adapter<CreateAssemblyAdapter.AssemblyPartViewHolder> {

    private final Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts;
    private final Activity activity;
    private int totalPriceUSD;
    private PartListener partListener;

    /**
     * ViewHolder for assembly part
     */
    public class AssemblyPartViewHolder extends RecyclerView.ViewHolder {
        private final int PARAMETER_COUNT = 4;
        private final String LOG_TAG = AssemblyPartViewHolder.class.toString();

        private final ViewGroup layoutContainer;
        private final TextView partName;
        private final TextView partPrice;
        private final TextView partType;
        private final ArrayList<TextView> parameterLabels;
        private final ArrayList<TextView> parameters;
        private final ViewGroup noPartContainer;
        private final ViewGroup partContainer;
        private Set<ComputerPart> parts;
        private ComputerPart.ComputerPartType type;
        private ComputerPart currentPart;
        private Activity activity;

        /**
         * Create view holder
         *
         * @param itemView view
         */
        public AssemblyPartViewHolder(@NonNull View itemView) {
            super(itemView);

            parameterLabels = new ArrayList<>(PARAMETER_COUNT);
            parameters = new ArrayList<>(PARAMETER_COUNT);

            partName = itemView.findViewById(R.id.assembly_part_name);
            partPrice = itemView.findViewById(R.id.assembly_part_price);
            partType = itemView.findViewById(R.id.assembly_part_type);
            partContainer = itemView.findViewById(R.id.assembly_card);
            noPartContainer = itemView.findViewById(R.id.assembly_no_part_card);
            layoutContainer = itemView.findViewById(R.id.assembly_layout);

            try {
                initParameterViews(itemView);
            } catch (Exception e) {
                Log.e(LOG_TAG, "cannot init parameter fields");
                e.printStackTrace();
            }

            // hide main card unless part is loaded, until that moment
            // show card with add button
            LayoutUtils.removeViewFromLayout(partContainer);

            AppCompatImageButton addPart = itemView.findViewById(R.id.assembly_add_part_button);
            addPart.setOnClickListener((view -> addNewPart()));
        }

        private void initParameterViews(View view) throws NoSuchFieldException, IllegalAccessException {
            final String viewIdPrefix = "assembly_part_param";
            final String labelPostfix = "_label";
            int viewId;
            for (int i = 1; i <= PARAMETER_COUNT; i++) {
                viewId = R.id.class.getField(viewIdPrefix + i).getInt(null);
                parameters.add(view.findViewById(viewId));

                viewId = R.id.class.getField(viewIdPrefix + i + labelPostfix).getInt(null);
                parameterLabels.add(view.findViewById(viewId));
            }
        }

        private void addNewPart() {
            assert (activity != null);

            //PartBrowserActivity.launchForPickPartFrom(activity, type);

            // mocking data received from PartBrowser,
            // unless it not implemented
            ComputerPart part = ModelUtils.generateRandomPart(type);
            parts.add(part);
            currentPart = part;

            updateView();

            partListener.partReceived(currentPart);
        }

        private void updateView() {
            LayoutUtils.removeViewFromLayout(noPartContainer);
            LayoutUtils.addViewToLayout(partContainer, layoutContainer);

            partName.setText(currentPart.getName());
            partPrice.setText(String.valueOf(currentPart.getPriceUsd()));

            updateParameters();
        }

        private void updateParameters() {
            removeExtraParametersViews();
            fillParameters();
        }

        private void removeExtraParametersViews() {
            int requiredParameters = currentPart.getParameters().size();
            for (int i = requiredParameters; i < parameterLabels.size(); i++) {
                LayoutUtils.removeViewFromLayout(parameterLabels.get(i));
                LayoutUtils.removeViewFromLayout(parameters.get(i));
            }
        }

        private void fillParameters() {
            List<PartParameter> partParameters = currentPart.getParameters();
            int parameterCount = partParameters.size();

            PartParameter nextParameter;
            for (int i = 0; i < parameterCount; i++) {
                nextParameter = partParameters.get(i);
                parameterLabels.get(i).setText(nextParameter.getName());
                parameters.get(i).setText(nextParameter.getValue().toString());
            }
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
     * @param partListener new part added callback
     */
    public CreateAssemblyAdapter(Map<ComputerPart.ComputerPartType, Set<ComputerPart>> parts,
                                 Activity activity,
                                 PartListener partListener) {
        this.parts = parts;
        this.activity = activity;
        this.partListener = partListener;
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
     * New part added for one of types
     *
     * @param part part being added
     */
    public void partAdded(ComputerPart part) {
        totalPriceUSD += part.getPriceUsd();

    }
}
