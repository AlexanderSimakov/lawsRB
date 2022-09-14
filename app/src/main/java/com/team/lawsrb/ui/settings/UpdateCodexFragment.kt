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
import com.team.lawsrb.basic.htmlParser.CodexVersionParser
import com.team.lawsrb.databinding.FragmentUpdateCodexBinding
import com.team.lawsrb.ui.NotificationBadge
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

        setUpCheckCodexUpdatesButton()
        setUpObservers()
        setUpRefreshButtons()
        setOnClickListenerForUpdateButtons()
        setUpClearAllButton()
        hideHeaderAndSearchButtons()

        return root
    }

    private fun setUpCheckCodexUpdatesButton(){
        binding.checkUpdatesButton.setOnClickListener {
            GlobalScope.launch {
                model.isCheckUpdateButtonEnabled.postValue(false)
                Snackbar.make(requireView(), "Проверка обновлений", Snackbar.LENGTH_SHORT).show()

                CodexVersionParser.update().join()

                if (CodexVersionParser.isHaveChanges()) {
                    model.updateIsUpdateEnabled()

                    view?.let {
                        Snackbar.make(it, "Доступны обновления кодексов", Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    view?.let {
                        Snackbar.make(it, "На данный момент обновлений нет", Snackbar.LENGTH_SHORT).show()
                    }
                }

                model.isCheckUpdateButtonEnabled.postValue(true)
            }
        }
    }

    private fun hideHeaderAndSearchButtons() {
        val actionSearch = requireActivity().findViewById<SearchView>(R.id.action_search)
        val actionsFavorites = requireActivity().findViewById<CheckBox>(R.id.action_favorites)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false
        actionSearch.isVisible = false
        actionsFavorites.isVisible = false
    }

    private fun showHeaderAndSearchButtons() {
        val actionSearch = requireActivity().findViewById<SearchView>(R.id.action_search)
        val actionsFavorites = requireActivity().findViewById<CheckBox>(R.id.action_favorites)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = true
        actionSearch.isVisible = true
        actionsFavorites.isVisible = true
    }

    private fun setUpObservers(){
        model.isCheckUpdateButtonEnabled.observe(viewLifecycleOwner) {
            binding.checkUpdatesButton.isEnabled = it
        }

        model.isUpdateEnabled(Codex.UK).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_uk,
                Codex.UK,
                getString(R.string.menu_criminal_code)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.UPK).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_upk,
                Codex.UPK,
                getString(R.string.menu_code_of_criminal_proсedure)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.KoAP).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_koap,
                Codex.KoAP,
                getString(R.string.menu_KoAP)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.PIKoAP).observe(viewLifecycleOwner) {
            updateRefreshButton(
                update_pikoap,
                Codex.PIKoAP,
                getString(R.string.menu_PIKoAP)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
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
        binding.updateCodexFragment.apply {
            update_uk.update_codex_button.setOnClickListener { onUpdateButtonClick(Codex.UK) }
            update_upk.update_codex_button.setOnClickListener { onUpdateButtonClick(Codex.UPK) }
            update_koap.update_codex_button.setOnClickListener { onUpdateButtonClick(Codex.KoAP) }
            update_pikoap.update_codex_button.setOnClickListener { onUpdateButtonClick(Codex.PIKoAP) }
        }
    }

    private fun onUpdateButtonClick(codex: Codex){
        GlobalScope.launch(Dispatchers.Default) {
            Snackbar.make(requireView(), "Обновление ${codex.rusName}", Snackbar.LENGTH_SHORT).show()

            Log.i(TAG, "Start parse ${codex.name}")
            val codexLists = CodexParser().get(codex)
            Log.d(TAG, "End parse ${codex.name} and start insert")

            BaseCodexDatabase.insertCodexLists(codex, codexLists)
            BaseCodexProvider.update()
            Preferences.setCodexInfo(
                codex,
                CodexVersionParser.getChangesCount(codex),
                CodexVersionParser.getChangeDate(codex)
            )

            view?.let {
                Snackbar.make(it, "${codex.rusName} обновлен", Snackbar.LENGTH_SHORT).show()
            }
        }

        model.isUpdateEnabled(codex).value = false
    }

    // debug function
    private fun setUpClearAllButton(){
        binding.updateCodexFragment.debug_clear_all_button.setOnClickListener {
            BaseCodexDatabase.clearDatabases()

            BaseCodexProvider.update()
            Preferences.setCodexVersion(Codex.UK, -1)
            Preferences.setCodexVersion(Codex.UPK, -1)
            Preferences.setCodexVersion(Codex.KoAP, -1)
            Preferences.setCodexVersion(Codex.PIKoAP, -1)

            model.updateIsUpdateEnabled()

            Snackbar.make(requireView(), "Базы данных очищены", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        hideHeaderAndSearchButtons()
    }

    override fun onDestroyView() {
        showHeaderAndSearchButtons()
        super.onDestroyView()
        _binding = null
    }
}
