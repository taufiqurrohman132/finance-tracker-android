package com.example.financetrackerapplication.features.aset.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.Action
import com.example.financetrackerapplication.MainActivity
import com.example.financetrackerapplication.MainSharedViewModel
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentAsetBinding
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.features.aset.add.AddAsetActivity
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AsetFragment : Fragment() {

    private var _binding: FragmentAsetBinding? = null
    private val binding get() = _binding!!

    private lateinit var asetAdapter: AsetAdapter

    private val viewModel: AsetViewModel by viewModels()
    private val sharedViewModel: MainSharedViewModel by activityViewModels()

    private var actionMode: ActionMode? = null

//    private val actionModeCallback = object : ActionMode.Callback{
//        override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
//            return when(menuItem.itemId){
//                R.id.action_cancel -> {
//                    asetAdapter.isSelectionMode = false
//                    actionMode.finish()
//                    true
//                }
//                R.id.action_select_all -> {
//                    viewModel.selectAll()
//                    true
//                }
//
//                else -> false
//            }
//        }

//        override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
//            actionMode.menuInflater.inflate(R.menu.menu_aset_selection, menu)
//            actionMode.title = "Pilih Item"
//            return true
//        }
//
//        override fun onDestroyActionMode(mode: ActionMode) {
//            viewModel.clearSelection()
//            actionMode = null
//        }
//
//        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
//            return false
//       }
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAsetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val activity = requireActivity() as MainActivity
                    activity.showActionMenu(false)

                    isEnabled = false
                    requireActivity()
                        .onBackPressedDispatcher
                        .onBackPressed()

                }
            }
        )
        
        setupRecyclerView()
        setupListener()
        observer()
    }


    private fun observer() {
        val orderKeys =
            requireContext().resources.getStringArray(R.array.group_option_aset).toList()
        viewModel.setOrder(orderKeys)


        viewModel.displayList.observe(viewLifecycleOwner) { list ->

            val totalAset = list.filterIsInstance<GroupAset.Parent>()
                .flatMap { it.childAsetList }
                .filter { it.aset.initialBalance > 0 }
                .sumOf { it.aset.initialBalance }
                .parseLongToMoney()
            val totalLiability = list.filterIsInstance<GroupAset.Parent>()
                .flatMap { it.childAsetList }
                .filter { it.aset.initialBalance < 0 }
                .sumOf { it.aset.initialBalance }
                .parseLongToMoney()
            val totalBalance = list
                .filterIsInstance<GroupAset.Parent>()
                .flatMap { it.childAsetList }
                .sumOf { it.aset.initialBalance }
                .parseLongToMoney()

            binding.apply {
                asetTvAset.text = totalAset
                asetTvLiabilitas.text = totalLiability
                asetTvTotal.text = totalBalance
            }

            Log.d(TAG, "observer: update list is running")

            val activity = requireActivity() as MainActivity
            activity.showActionMenu(viewModel.hasSelection())

            binding.collapsingLayout.collapsedTitleGravity = if (viewModel.selectedCount() == 0)
                Gravity.START else Gravity.CENTER_HORIZONTAL
            binding.collapsingLayout.title = if (viewModel.selectedCount() == 0)
                "Aset" else "${viewModel.selectedCount()} dipilih"
            asetAdapter.submitList(list)
        }

        sharedViewModel.actionEvent.observe(viewLifecycleOwner) { action ->
            val asetEntitiesToDelete = viewModel.displayList.value
                ?.filterIsInstance<GroupAset.Parent>()
                ?.flatMap { it.childAsetList }
                ?.filter { it.isSelected }
                ?.map { it.aset }
                ?: emptyList()

            when (action) {
                Action.DELETE -> {
                    binding.selectionLayout.isVisible = false
                    asetAdapter.isSelectionMode = false
                    viewModel.deleteList(asetEntitiesToDelete)
                }

                Action.PIN -> {
                }
            }
        }
    }

    private fun showActionMenuBottom(isShow: Boolean) {
        val menu = requireActivity()
    }

    private fun setupRecyclerView() {
        asetAdapter = AsetAdapter(
            onExpand = { parent ->
                viewModel.toggleExpand(parent.id)
            },
            onSelect = { child ->
                viewModel.toggleSelect(child)

                asetAdapter.isSelectionMode = viewModel.hasSelection()

            },
            selectionCallback = object : AsetAdapter.SelectionCallback {
                // selection callback
                override fun onSelectionStarted() {
                    // show toolbar custom
                    binding.selectionLayout.isVisible = true
                    // hide bottom nav
                }

                override fun onSelectionUpdated(selectedCount: Int) {
//                    binding.collapsingLayout.title = "$selectedCount dipilih"
                }

                override fun onSelectionEnded() {
                    binding.selectionLayout.isVisible = false
                }
            }
        )

        binding.asetRvAset.apply {
            adapter = asetAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
        }


    }

    private fun setupListener() {
        binding.apply {
            btnBatalkanSelection.setOnClickListener {
                asetAdapter.isSelectionMode = false
                viewModel.clearSelection()
            }
            btnPilihSemuaSelection.setOnClickListener {
                viewModel.selectAll()
            }
            fabAddAset.setOnClickListener {
                val intent = Intent(requireActivity(), AddAsetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = AsetFragment::class.java.simpleName
    }


}