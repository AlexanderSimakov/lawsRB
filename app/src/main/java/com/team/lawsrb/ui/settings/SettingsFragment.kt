package com.team.lawsrb.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.Parser
import com.team.lawsrb.basic.roomDatabase.BaseCodexDatabase
import android.widget.CheckBox
import android.widget.SearchView
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team.lawsrb.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.update_codex_button.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"

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

        val actionSearch = requireActivity().findViewById<SearchView>(R.id.action_search)
        val actionsFavorites = requireActivity().findViewById<CheckBox>(R.id.action_favorites)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false
        actionSearch.isVisible = false
        actionsFavorites.isVisible = false

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
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление УК", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse UK")
                    val codexLists = Parser().get(Codex.UK)
                    Log.d(TAG, "End parse UK and start insert")

                    BaseCodexDatabase.UK.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.UK.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.UK.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.UK.articlesDao().insert(codexLists.articles)
                    Log.d(TAG, "End UK insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (UK)")

                    // Bug: then click button and change page to UK (or others),
                    // after parsing app crash, because cannot find view(below)
                    //Snackbar.make(requireView(), "УК обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }

        binding.settingsFragment.update_upk.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление УПК ", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse UPK")
                    val codexLists = Parser().get(Codex.UPK)
                    Log.d(TAG, "End parse UPK and start insert")

                    BaseCodexDatabase.UPK.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.UPK.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.UPK.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.UPK.articlesDao().insert(codexLists.articles)
                    Log.d(TAG, "End UPK insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (UPK)")

                    // See first
                    //Snackbar.make(requireView(), "УПК обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }

        binding.settingsFragment.update_koap.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление КоАП", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse KoAP")
                    val codexLists = Parser().get(Codex.KoAP)
                    Log.d(TAG, "End parse KoAP and start insert")

                    BaseCodexDatabase.KoAP.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.KoAP.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.KoAP.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.KoAP.articlesDao().insert(codexLists.articles)
                    Log.d(TAG, "End KoAP insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (KoAP)")

                    // See first
                    //Snackbar.make(requireView(), "КоАП обновлен", Snackbar.LENGTH_SHORT).show()
                }
            }

        binding.settingsFragment.update_pikoap.findViewById<MaterialCardView>(R.id.update_codex_button)
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление ПИКоАП", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse PIKoAP")
                    val codexLists = Parser().get(Codex.PIKoAP)
                    Log.d(TAG, "End parse PIKoAP and start insert")

                    BaseCodexDatabase.PIKoAP.partsDao().insert(codexLists.parts)
                    BaseCodexDatabase.PIKoAP.sectionsDao().insert(codexLists.sections)
                    BaseCodexDatabase.PIKoAP.chaptersDao().insert(codexLists.chapters)
                    BaseCodexDatabase.PIKoAP.articlesDao().insert(codexLists.articles)
                    Log.d(TAG, "End PIKoAP insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (PIKoAP)")

                    // See first
                    //Snackbar.make(requireView(), "ПИКоАП обновлен", Snackbar.LENGTH_SHORT).show()
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

            BaseCodexProvider.update()

            Snackbar.make(requireView(), "Базы данных очищены", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        val actionSearch = requireActivity().findViewById<SearchView>(R.id.action_search)
        val actionsFavorites = requireActivity().findViewById<CheckBox>(R.id.action_favorites)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = true
        actionSearch.isVisible = true
        actionsFavorites.isVisible = true

        super.onDestroyView()
        _binding = null
    }

}