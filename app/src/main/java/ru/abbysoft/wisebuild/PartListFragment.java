package ru.abbysoft.wisebuild;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.storage.DBFactory;


/**
 * List of computer parts fragment
 */
public class PartListFragment extends Fragment {
    private static final String ARG_PART_TYPE = "ARG_PART_TYPE";

    private ComputerPart.ComputerPartType type;

    public PartListFragment() {
        // Required empty public constructor
    }

    /**
     * Construct a new part list for a given category
     * @param type Part category
     * @return New part list fragment
     */
    public static PartListFragment newInstance(ComputerPart.ComputerPartType type) {
        PartListFragment fragment = new PartListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PART_TYPE, type.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = ComputerPart.ComputerPartType.valueOf(getArguments().getString(ARG_PART_TYPE));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpRecyclerView(view.findViewById(R.id.partListRecyclerView));
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new PartListRecyclerAdapter(DBFactory.getDatabase(), getActivity(), type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_part_list, container, false);
    }
}
