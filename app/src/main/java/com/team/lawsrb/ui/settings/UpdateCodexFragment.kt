package com.team.lawsrb.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.team.lawsrb.basic.htmlParser.CodexVersionParser
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.databinding.FragmentUpdateCodexBinding
import kotlinx.android.synthetic.main.fragment_update_codex.*
import kotlinx.android.synthetic.main.fragment_update_codex.view.*
import kotlinx.android.synthetic.main.update_codex_button.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateCodexFragment : Fragment() {
    private val TAG = "UpdateCodexFragment"

    private lateinit var model: UpdateCodexViewModel

    private var _binding: FragmentUpdateCodexBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateCodexBinding.inflate(inflater, container, false)
        val root: View = binding.root

        model = ViewModelProviders.of(this).get(UpdateCodexViewModel::class.java)

        setUpObservers()
        setUpRefreshButtons()
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

    private fun setUpObservers(){
        model.isUpdateEnabled(Codex.UK).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_uk,
                Codex.UK,
                getString(R.string.menu_criminal_code)
            )
        }

        model.isUpdateEnabled(Codex.UPK).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_upk,
                Codex.UPK,
                getString(R.string.menu_code_of_criminal_proсedure)
            )
        }

        model.isUpdateEnabled(Codex.KoAP).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_koap,
                Codex.KoAP,
                getString(R.string.menu_KoAP)
            )
        }

        model.isUpdateEnabled(Codex.PIKoAP).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_pikoap,
                Codex.PIKoAP,
                getString(R.string.menu_PIKoAP)
            )
        }
    }

    private fun setUpRefreshButtons(){
        binding.updateCodexFragment.apply {
            updateRefreshButton(update_uk, Codex.UK, getString(R.string.menu_criminal_code))
            updateRefreshButton(update_upk, Codex.UPK, getString(R.string.menu_code_of_criminal_proсedure))
            updateRefreshButton(update_koap, Codex.KoAP, getString(R.string.menu_KoAP))
            updateRefreshButton(update_pikoap, Codex.PIKoAP, getString(R.string.menu_PIKoAP))
        }
    }

    private fun updateRefreshButton(button: View, codex: Codex, title: String?){
        if (model.isUpdateEnabled(codex).value == true){
            button.update_codex_button.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background_active)
            )

            val activeRefreshButtonImageColor = ContextCompat.getColor(requireContext(), R.color.refresh_image_active)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = "Обновить $title"
            button.subtitle.text = CodexVersionParser.getChangeDate(codex)
            button.update_codex_button.isEnabled = true
            button.update_codex_button.cardElevation = 20F
        }else{
            button.update_codex_button.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background)
            )

            val activeRefreshButtonImageColor = ContextCompat.getColor(requireContext(), R.color.refresh_image)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = title
            button.subtitle.text = Preferences.getCodexUpdateDate(codex)
            button.update_codex_button.isEnabled = false
            button.update_codex_button.cardElevation = 5F
        }
    }

    private fun setOnClickListenerForUpdateButtons(){
        binding.updateCodexFragment.update_uk.update_codex_button
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление УК", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse UK")
                    val codexLists = CodexParser().get(Codex.UK)
                    Log.d(TAG, "End parse UK and start insert")

                    insertCodexLists(BaseCodexDatabase.UK, codexLists)
                    Log.d(TAG, "End UK insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (UK)")

                    Preferences.setCodexInfo(
                        Codex.UK,
                        CodexVersionParser.getChangesCount(Codex.UK),
                        CodexVersionParser.getChangeDate(Codex.UK)
                    )

                    // Bug: then click button and change page to UK (or others),
                    // after parsing app crash, because cannot find view(below)
                    //Snackbar.make(requireView(), "УК обновлен", Snackbar.LENGTH_SHORT).show()
                }

                model.isUpdateEnabled(Codex.UK).value = false
            }

        binding.updateCodexFragment.update_upk.update_codex_button
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление УПК ", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse UPK")
                    val codexLists = CodexParser().get(Codex.UPK)
                    Log.d(TAG, "End parse UPK and start insert")

                    insertCodexLists(BaseCodexDatabase.UPK, codexLists)
                    Log.d(TAG, "End UPK insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (UPK)")

                    Preferences.setCodexInfo(
                        Codex.UPK,
                        CodexVersionParser.getChangesCount(Codex.UPK),
                        CodexVersionParser.getChangeDate(Codex.UPK)
                    )

                    // See first
                    //Snackbar.make(requireView(), "УПК обновлен", Snackbar.LENGTH_SHORT).show()
                }

                model.isUpdateEnabled(Codex.UPK).value = false
            }

        binding.updateCodexFragment.update_koap.update_codex_button
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление КоАП", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse KoAP")
                    val codexLists = CodexParser().get(Codex.KoAP)
                    Log.d(TAG, "End parse KoAP and start insert")

                    insertCodexLists(BaseCodexDatabase.KoAP, codexLists)
                    Log.d(TAG, "End KoAP insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (KoAP)")

                    Preferences.setCodexInfo(
                        Codex.KoAP,
                        CodexVersionParser.getChangesCount(Codex.KoAP),
                        CodexVersionParser.getChangeDate(Codex.KoAP)
                    )

                    // See first
                    //Snackbar.make(requireView(), "КоАП обновлен", Snackbar.LENGTH_SHORT).show()
                }

                model.isUpdateEnabled(Codex.KoAP).value = false
            }

        binding.updateCodexFragment.update_pikoap.update_codex_button
            .setOnClickListener {
                GlobalScope.launch(Dispatchers.Default) {
                    Snackbar.make(requireView(), "Обновление ПИКоАП", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Start parse PIKoAP")
                    val codexLists = CodexParser().get(Codex.PIKoAP)
                    Log.d(TAG, "End parse PIKoAP and start insert")

                    insertCodexLists(BaseCodexDatabase.PIKoAP, codexLists)
                    Log.d(TAG, "End PIKoAP insert")

                    BaseCodexProvider.update()
                    Log.d(TAG, "Update CodexProvider (PIKoAP)")

                    Preferences.setCodexInfo(
                        Codex.PIKoAP,
                        CodexVersionParser.getChangesCount(Codex.PIKoAP),
                        CodexVersionParser.getChangeDate(Codex.PIKoAP)
                    )

                    // See first
                    //Snackbar.make(requireView(), "ПИКоАП обновлен", Snackbar.LENGTH_SHORT).show()
                }

                model.isUpdateEnabled(Codex.PIKoAP).value = false
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
        binding.updateCodexFragment.debug_clear_all_button.setOnClickListener {
            clearDatabase(BaseCodexDatabase.UK)
            clearDatabase(BaseCodexDatabase.UPK)
            clearDatabase(BaseCodexDatabase.KoAP)
            clearDatabase(BaseCodexDatabase.PIKoAP)

            BaseCodexProvider.update()
            Preferences.setCodexVersion(Codex.UK, -1)
            Preferences.setCodexVersion(Codex.UPK, -1)
            Preferences.setCodexVersion(Codex.KoAP, -1)
            Preferences.setCodexVersion(Codex.PIKoAP, -1)

            model.updateIsUpdateEnabled()

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
