package ru.abbysoft.wisebuild.assembly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.abbysoft.wisebuild.R
import ru.abbysoft.wisebuild.databinding.FragmentPartOverviewBinding
import ru.abbysoft.wisebuild.model.ComputerPart


/**
 * Fragment with list of selected parts with specific type
 *
 */
class SelectedPartsFragment(val partType: ComputerPart.ComputerPartType): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding : FragmentPartOverviewBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_part_overview, container, false)

        binding.partType = partType
        binding.containerEmpty = true

        // Inflate the layout for this fragment
        return binding.root
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
