package com.team.lawsrb.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.lawsrb.R
import com.team.lawsrb.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.update_codex_button.view.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpUpdateButtons()

        return root
    }

    private fun setUpUpdateButtons(){
        binding.settingsFragment.update_uk.title.text = getString(R.string.menu_criminal_code)
        binding.settingsFragment.update_upk.title.text = getString(R.string.menu_code_of_criminal_proсedure)
        binding.settingsFragment.update_koap.title.text = getString(R.string.menu_KoAP)
        binding.settingsFragment.update_pikoap.title.text = getString(R.string.menu_PIKoAP)

        binding.settingsFragment.update_uk.subtitle.text = "От 31.12.1979"
        binding.settingsFragment.update_upk.subtitle.text = "От 31.12.1979"
        binding.settingsFragment.update_koap.subtitle.text = "От 31.12.1979"
        binding.settingsFragment.update_pikoap.subtitle.text = "От 31.12.1979"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}