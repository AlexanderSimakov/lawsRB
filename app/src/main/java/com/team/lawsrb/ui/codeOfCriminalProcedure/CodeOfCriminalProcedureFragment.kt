package com.team.lawsrb.ui.codeOfCriminalProcedure

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.team.lawsrb.R
import com.team.lawsrb.databinding.FragmentCodeOfCriminalProcedureBinding
import com.team.lawsrb.databinding.FragmentCriminalCodeBinding
import com.team.lawsrb.ui.criminalcode.CriminalCodeViewModel

class CodeOfCriminalProcedureFragment : Fragment() {

    private var _binding: FragmentCodeOfCriminalProcedureBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val codeOfCriminalProcedureViewModel =
            ViewModelProvider(this).get(CodeOfCriminalProcedureViewModel::class.java)

        _binding = FragmentCodeOfCriminalProcedureBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.exampleText
        codeOfCriminalProcedureViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}