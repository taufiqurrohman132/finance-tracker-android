package com.example.financetrackerapplication.features.transaction.previewcamera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentAsetBinding
import com.example.financetrackerapplication.databinding.FragmentPreviewBinding
import com.example.financetrackerapplication.features.aset.list.AsetViewModel
import com.example.financetrackerapplication.features.transaction.previewcamera.AddPreviewActivity.Companion.ARG_URI


class PreviewFragment : Fragment() {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCaptureNext.setOnClickListener {
            // intent result
            val uriImg = arguments?.getString(ARG_URI)
            val intent = Intent()
            intent.putExtra(ARG_URI, uriImg)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        }
    }

    companion object{

        fun newInstance(imageUri: String): PreviewFragment{
            val fragment = PreviewFragment()
            val args = Bundle()
            args.putString(ARG_URI, imageUri)
            fragment.arguments = args
            return fragment
        }

        private val TAG = PreviewFragment::class.java.simpleName

    }

}