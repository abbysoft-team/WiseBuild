package ru.abbysoft.wisebuild.assembly

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.abbysoft.wisebuild.R
import ru.abbysoft.wisebuild.browser.BrowserActivity
import ru.abbysoft.wisebuild.databinding.FragmentPartOverviewBinding
import ru.abbysoft.wisebuild.model.ComputerPart
import ru.abbysoft.wisebuild.storage.DBFactory
import ru.abbysoft.wisebuild.utils.ModelUtils
import java.lang.IllegalStateException


/**
 * Fragment with list of selected parts with specific type
 *
 */
class SelectedPartsFragment(private val partType: ComputerPart.ComputerPartType): Fragment() {

    private lateinit var binding : FragmentPartOverviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val model = ViewModelProviders.of(activity as FragmentActivity)[AssemblyViewModel::class.java]

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_part_overview, container, false)

        binding.partType = partType
        binding.containerEmpty = true
        binding.viewModel = model
        binding.lifecycleOwner = viewLifecycleOwner

        subscribeOnActions(model)

        return binding.root
    }

    private fun subscribeOnActions(model : AssemblyViewModel) {
        model.nextAction.observe(this, Observer<AssemblyViewModel.AssemblyAction> {
            when (it) {
                getAssemblyActionFromType() -> {
                    addPart()
                    model.actionDone()
                }
                else -> {}
            }
        })
    }

    private fun getAssemblyActionFromType() : AssemblyViewModel.AssemblyAction = when (partType) {
        ComputerPart.ComputerPartType.CPU -> AssemblyViewModel.AssemblyAction.ADD_CPU
        ComputerPart.ComputerPartType.MEMORY_MODULE -> AssemblyViewModel.AssemblyAction.ADD_MEMORY
        ComputerPart.ComputerPartType.MOTHERBOARD -> AssemblyViewModel.AssemblyAction.ADD_MOTHERBOARD
        ComputerPart.ComputerPartType.VIDEO_CARD -> AssemblyViewModel.AssemblyAction.ADD_VIDEO_CARD
        else -> throw IllegalStateException()
    }

    private fun addPart() {
        BrowserActivity.launchForPickPartFrom(this, partType)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == BrowserActivity.PICK_PART_REQUEST) {

            if (data == null) {
                return
            }

            val model = ViewModelProviders.of(activity as FragmentActivity)[AssemblyViewModel::class.java]
            val partId = data.getLongExtra(BrowserActivity.PART_ID_EXTRA, -1)
            val part = DBFactory.getDatabase().getPart(partId) ?:
            throw IllegalStateException("browser activity returned wrong part id $partId")

            model.newPartAdded(part, partType)

            binding.currentPart = part
            binding.containerEmpty = false
        }
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
