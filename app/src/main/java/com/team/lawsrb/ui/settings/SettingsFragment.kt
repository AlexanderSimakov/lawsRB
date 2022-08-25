package com.team.lawsrb.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.team.lawsrb.R
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.Parser
import com.team.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.team.lawsrb.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.update_codex_button.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        setOnClickListenerForUpdateButtons()
        setUpClearAllButton()

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

    private fun setOnClickListenerForUpdateButtons(){
        binding.settingsFragment.update_uk.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch {
                    val codexLists = Parser.get(Codex.UK)
                    BaseCodexDatabase.UK.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.UK.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.UK.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.UK.articlesDao().insert(codexLists.articles)

                    Snackbar.make(requireView(), "УК обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }

        binding.settingsFragment.update_upk.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch {
                    val codexLists = Parser.get(Codex.UPK)
                    BaseCodexDatabase.UPK.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.UPK.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.UPK.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.UPK.articlesDao().insert(codexLists.articles)

                    Snackbar.make(requireView(), "УПК обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }

        binding.settingsFragment.update_koap.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch {
                    val codexLists = Parser.get(Codex.KoAP)
                    BaseCodexDatabase.KoAP.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.KoAP.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.KoAP.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.KoAP.articlesDao().insert(codexLists.articles)

                    Snackbar.make(requireView(), "КоАП обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }

        binding.settingsFragment.update_pikoap.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch {
                    val codexLists = Parser.get(Codex.PIKoAP)
                    BaseCodexDatabase.PIKoAP.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.PIKoAP.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.PIKoAP.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.PIKoAP.articlesDao().insert(codexLists.articles)

                    Snackbar.make(requireView(), "ПИКоАП обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    // debug function
    private fun setUpClearAllButton(){
        binding.settingsFragment.debug_clear_all_button.setOnClickListener {
            BaseCodexDatabase.UK.articlesDao().clearAll()
            BaseCodexDatabase.UK.chaptersDao().clearAll()
            BaseCodexDatabase.UK.sectionsDao().clearAll()
            BaseCodexDatabase.UK.partsDao().clearAll()

            BaseCodexDatabase.UPK.articlesDao().clearAll()
            BaseCodexDatabase.UPK.chaptersDao().clearAll()
            BaseCodexDatabase.UPK.sectionsDao().clearAll()
            BaseCodexDatabase.UPK.partsDao().clearAll()

            BaseCodexDatabase.KoAP.articlesDao().clearAll()
            BaseCodexDatabase.KoAP.chaptersDao().clearAll()
            BaseCodexDatabase.KoAP.sectionsDao().clearAll()
            BaseCodexDatabase.KoAP.partsDao().clearAll()

            BaseCodexDatabase.PIKoAP.articlesDao().clearAll()
            BaseCodexDatabase.PIKoAP.chaptersDao().clearAll()
            BaseCodexDatabase.PIKoAP.sectionsDao().clearAll()
            BaseCodexDatabase.PIKoAP.partsDao().clearAll()

            Snackbar.make(requireView(), "Базы данных очищены, перезагрузите приложение", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}