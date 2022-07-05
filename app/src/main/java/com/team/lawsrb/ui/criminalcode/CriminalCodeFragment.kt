package com.team.lawsrb.ui.criminalcode

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.team.lawsrb.databinding.FragmentCriminalCodeBinding
import com.team.lawsrb.databinding.FragmentGalleryBinding
import com.team.lawsrb.ui.gallery.GalleryViewModel

class CriminalCodeFragment : Fragment() {

    private var _binding: FragmentCriminalCodeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val criminalCodeViewModel =
            ViewModelProvider(this).get(CriminalCodeViewModel::class.java)

        _binding = FragmentCriminalCodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.exampleText
        criminalCodeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}