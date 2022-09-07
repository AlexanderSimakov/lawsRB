package com.team.lawsrb.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SearchView
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team.lawsrb.R
import com.google.android.material.snackbar.Snackbar
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexParser
import com.team.lawsrb.basic.roomDatabase.BaseCodexDatabase
import androidx.lifecycle.ViewModelProviders
import com.team.lawsrb.basic.Preferences
import com.team.lawsrb.basic.htmlParser.CodexLists
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.update_codex_button.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"

    private lateinit var model: SettingsViewModel

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

        model = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

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
        binding.settingsFragment.apply {
            update_uk.title.text = getString(R.string.menu_criminal_code)
            update_upk.title.text = getString(R.string.menu_code_of_criminal_proсedure)
            update_koap.title.text = getString(R.string.menu_KoAP)
            update_pikoap.title.text = getString(R.string.menu_PIKoAP)

            update_uk.subtitle.text = Preferences.getCodexUpdateDate(Codex.UK)
            update_upk.subtitle.text = Preferences.getCodexUpdateDate(Codex.UPK)
            update_koap.subtitle.text = Preferences.getCodexUpdateDate(Codex.KoAP)
            update_pikoap.subtitle.text = Preferences.getCodexUpdateDate(Codex.PIKoAP)
        }
    }

    private fun setOnClickListenerForUpdateButtons(){
        binding.settingsFragment.update_uk.update_codex_button
            .setOnClickListener {
                if (model.isUKParserWorked){
                    Snackbar.make(requireView(), "УК уже обновляется", Snackbar.LENGTH_SHORT).show()
                }else{
                    GlobalScope.launch(Dispatchers.Default) {
                        model.isUKParserWorked = true

                        Snackbar.make(requireView(), "Обновление УК", Snackbar.LENGTH_SHORT).show()
                        Log.i(TAG, "Start parse UK")
                        val codexLists = CodexParser().get(Codex.UK)
                        Log.d(TAG, "End parse UK and start insert")

                        insertCodexLists(BaseCodexDatabase.UK, codexLists)
                        Log.d(TAG, "End UK insert")

                        BaseCodexProvider.update()
                        Log.d(TAG, "Update CodexProvider (UK)")

                        // Bug: then click button and change page to UK (or others),
                        // after parsing app crash, because cannot find view(below)
                        //Snackbar.make(requireView(), "УК обновлен", Snackbar.LENGTH_SHORT).show()
                        model.isUKParserWorked = false
                    }
                }
            }

        binding.settingsFragment.update_upk.update_codex_button
            .setOnClickListener {
                if (model.isUPKParserWorked){
                    Snackbar.make(requireView(), "УПК уже обновляется", Snackbar.LENGTH_SHORT).show()
                }else{
                    GlobalScope.launch(Dispatchers.Default) {
                        model.isUPKParserWorked = true

                        Snackbar.make(requireView(), "Обновление УПК ", Snackbar.LENGTH_SHORT).show()
                        Log.i(TAG, "Start parse UPK")
                        val codexLists = CodexParser().get(Codex.UPK)
                        Log.d(TAG, "End parse UPK and start insert")

                        insertCodexLists(BaseCodexDatabase.UPK, codexLists)
                        Log.d(TAG, "End UPK insert")

                        BaseCodexProvider.update()
                        Log.d(TAG, "Update CodexProvider (UPK)")

                        model.isUPKParserWorked = false

                        // See first
                        //Snackbar.make(requireView(), "УПК обновлен", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        binding.settingsFragment.update_koap.update_codex_button
            .setOnClickListener {
                if (model.isKoAPParserWorked){
                    Snackbar.make(requireView(), "КоАП уже обновляется", Snackbar.LENGTH_SHORT).show()
                }else{
                    GlobalScope.launch(Dispatchers.Default) {
                        model.isKoAPParserWorked = true

                        Snackbar.make(requireView(), "Обновление КоАП", Snackbar.LENGTH_SHORT).show()
                        Log.i(TAG, "Start parse KoAP")
                        val codexLists = CodexParser().get(Codex.KoAP)
                        Log.d(TAG, "End parse KoAP and start insert")

                        insertCodexLists(BaseCodexDatabase.KoAP, codexLists)
                        Log.d(TAG, "End KoAP insert")

                        BaseCodexProvider.update()
                        Log.d(TAG, "Update CodexProvider (KoAP)")

                        model.isKoAPParserWorked = false

                        // See first
                        //Snackbar.make(requireView(), "КоАП обновлен", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        binding.settingsFragment.update_pikoap.update_codex_button
            .setOnClickListener {
                if (model.isPIKoAPParserWorked){
                    Snackbar.make(requireView(), "ПИКоАП уже обновляется", Snackbar.LENGTH_SHORT).show()
                }else{
                    GlobalScope.launch(Dispatchers.Default) {
                        model.isPIKoAPParserWorked = true

                        Snackbar.make(requireView(), "Обновление ПИКоАП", Snackbar.LENGTH_SHORT).show()
                        Log.i(TAG, "Start parse PIKoAP")
                        val codexLists = CodexParser().get(Codex.PIKoAP)
                        Log.d(TAG, "End parse PIKoAP and start insert")

                        insertCodexLists(BaseCodexDatabase.PIKoAP, codexLists)
                        Log.d(TAG, "End PIKoAP insert")

                        BaseCodexProvider.update()
                        Log.d(TAG, "Update CodexProvider (PIKoAP)")

                        model.isPIKoAPParserWorked = false

                        // See first
                        //Snackbar.make(requireView(), "ПИКоАП обновлен", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
    }

    // TODO move to BaseCodexDatabase?
    private fun insertCodexLists(database: CodexDatabase, codexLists: CodexLists){
        database.partsDao().insert(codexLists.parts)
        database.sectionsDao().insert(codexLists.sections)
        database.chaptersDao().insert(codexLists.chapters)
        database.articlesDao().insert(codexLists.articles)
    }

    // debug function
    private fun setUpClearAllButton(){
        binding.settingsFragment.debug_clear_all_button.setOnClickListener {
            clearDatabase(BaseCodexDatabase.UK)
            clearDatabase(BaseCodexDatabase.UPK)
            clearDatabase(BaseCodexDatabase.KoAP)
            clearDatabase(BaseCodexDatabase.PIKoAP)

            BaseCodexProvider.update()

            Snackbar.make(requireView(), "Базы данных очищены", Snackbar.LENGTH_SHORT).show()
        }
    }

    // debug function
    // TODO move to BaseCodexDatabase?
    private fun clearDatabase(database: CodexDatabase){
        database.articlesDao().clearAll()
        database.chaptersDao().clearAll()
        database.sectionsDao().clearAll()
        database.partsDao().clearAll()
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