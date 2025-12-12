package com.example.financetrackerapplication.features.aset.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentAsetBinding
import com.example.financetrackerapplication.features.aset.add.AddAsetActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AsetFragment : Fragment() {

    private var _binding: FragmentAsetBinding? = null
    private val binding get() = _binding!!

    private lateinit var asetAdapter: AsetAdapter

    private val viewModel: AsetViewModel by viewModels()

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

        setupRecyclerView()
        setupListener()
        observer()
    }

    private fun observer() {
        val orderKeys =
            requireContext().resources.getStringArray(R.array.group_option_aset).toList()
        viewModel.setOrder(orderKeys)
//        viewModel.getListAset(orderKeys).observe(viewLifecycleOwner) { listGroupAset ->
//            asetAdapter = AsetAdapter(
//                onChildSelect = { aset ->
//                    viewModel.toggleSelect(aset)
//                }
//            )
//            binding.asetRvAset.adapter = asetAdapter
//            asetAdapter.setExpandableParentItemList(listGroupAset)
//
//            // setup selection rv
////            tracker = createLongTracker(
////                binding.asetRvAset,
////                asetAdapter,
////                keySelector = {position -> position.toLong()},
////                selectionId = "my_tracker"
////            )
////
////            asetAdapter.tracker = this.tracker
////
////            // Observer untuk detect perubahan selection
////            tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
////                override fun onSelectionChanged() {
////                    super.onSelectionChanged()
////                    val count = tracker.selection.size()
////                    Toast.makeText(requireActivity(), "$count item selected", Toast.LENGTH_SHORT).show()
////                }
////            })
//        }

        lifecycleScope.launch {
            viewModel.childAsetList.collect{ list ->
                asetAdapter = AsetAdapter(
                    onChildSelect = { aset ->
                        viewModel.toggleSelect(aset)
                    }
                )
                binding.asetRvAset.adapter = asetAdapter
                asetAdapter.setExpandableParentItemList(list)

            }
        }


    }

    private fun setupRecyclerView() {
        asetAdapter = AsetAdapter(
            onChildSelect = { aset ->
                viewModel.toggleSelect(aset)
            }
        )
        binding.asetRvAset.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun setupListener() {
        binding.apply {
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

}