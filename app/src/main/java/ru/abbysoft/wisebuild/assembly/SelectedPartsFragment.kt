package ru.abbysoft.wisebuild.assembly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.abbysoft.wisebuild.R
import ru.abbysoft.wisebuild.model.ComputerPart


/**
 * Fragment with list of selected parts with specific type
 *
 */
class SelectedPartsFragment(partType: ComputerPart.ComputerPartType): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_part_overview, container, false)
    }

    companion object {
        /**
         * Create new SelectedPartsFragment for display part of specified parameter
         */
        fun newInstance(partType: ComputerPart.ComputerPartType) : SelectedPartsFragment {
            return SelectedPartsFragment(partType)
        }
    }
}
