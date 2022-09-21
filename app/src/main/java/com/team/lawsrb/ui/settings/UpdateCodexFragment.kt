package com.team.lawsrb.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.CheckBox
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.team.lawsrb.R
import com.team.lawsrb.basic.NetworkCheck
import com.team.lawsrb.basic.Preferences
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexParser
import com.team.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.team.lawsrb.basic.htmlParser.CodexVersionParser
import com.team.lawsrb.databinding.FragmentUpdateCodexBinding
import com.team.lawsrb.databinding.UpdateCodexButtonBinding
import com.team.lawsrb.ui.NotificationBadge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UpdateCodexFragment : Fragment() {
    private val TAG = "UpdateCodexFragment"

    /**
     * This variable is responsible for enabling and disabling some debug functions such as
     * clear database button.
     *
     * **Attention:** make sure that you set it to `false` after using.
     */
    private val IS_DEBUG: Boolean = false

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
        hideHeaderAndSearchButtons()

        if (IS_DEBUG){
            setUpClearAllButton()
        }

        return root
    }

    private fun setUpCheckCodexUpdatesButton(){
        binding.checkUpdatesButton.setOnClickListener {
            if (NetworkCheck.isNotAvailable){
                Snackbar.make(requireView(), "Нет доступа в Интернет", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
                binding.updateUk,
                Codex.UK,
                getString(R.string.menu_criminal_code)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.UPK).observe(viewLifecycleOwner) {
            updateRefreshButton(
                binding.updateUpk,
                Codex.UPK,
                getString(R.string.menu_code_of_criminal_proсedure)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.KoAP).observe(viewLifecycleOwner) {
            updateRefreshButton(
                binding.updateKoap,
                Codex.KoAP,
                getString(R.string.menu_KoAP)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.PIKoAP).observe(viewLifecycleOwner) {
            updateRefreshButton(
                binding.updatePikoap,
                Codex.PIKoAP,
                getString(R.string.menu_PIKoAP)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }
    }

    private fun setUpRefreshButtons(){
        binding.apply {
            updateRefreshButton(updateUk, Codex.UK, getString(R.string.menu_criminal_code))
            updateRefreshButton(updateUpk, Codex.UPK, getString(R.string.menu_code_of_criminal_proсedure))
            updateRefreshButton(updateKoap, Codex.KoAP, getString(R.string.menu_KoAP))
            updateRefreshButton(updatePikoap, Codex.PIKoAP, getString(R.string.menu_PIKoAP))
        }
    }

    private fun updateRefreshButton(button: UpdateCodexButtonBinding, codex: Codex, title: String?){
        if (model.isUpdateEnabled(codex).value == true){
            button.updateCodexButton.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background_active)
            )

            val activeRefreshButtonImageColor = ContextCompat.getColor(requireContext(), R.color.refresh_image_active)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = "Обновить $title"
            button.subtitle.text = CodexVersionParser.getChangeDate(codex)
            button.updateCodexButton.isEnabled = true
            button.updateCodexButton.cardElevation = 20F
        }else{
            button.updateCodexButton.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background)
            )

            val activeRefreshButtonImageColor = ContextCompat.getColor(requireContext(), R.color.refresh_image)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = title
            button.subtitle.text = Preferences.getCodexUpdateDate(codex)
            button.updateCodexButton.isEnabled = false
            button.updateCodexButton.cardElevation = 5F
        }
    }

    private fun setOnClickListenerForUpdateButtons(){
        binding.apply {
            updateUk.updateCodexButton.setOnClickListener { onUpdateButtonClick(Codex.UK) }
            updateUpk.updateCodexButton.setOnClickListener { onUpdateButtonClick(Codex.UPK) }
            updateKoap.updateCodexButton.setOnClickListener { onUpdateButtonClick(Codex.KoAP) }
            updatePikoap.updateCodexButton.setOnClickListener { onUpdateButtonClick(Codex.PIKoAP) }
        }
    }

    private fun onUpdateButtonClick(codex: Codex){
        if (NetworkCheck.isNotAvailable){
            Snackbar.make(requireView(), "Нет доступа в Интернет", Snackbar.LENGTH_SHORT).show()
            return
        }

        val rotateAnimation = RotateAnimation(0F, 360F,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.apply {
            duration = 2000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
        }

        getCodexImage(codex)?.startAnimation(rotateAnimation)

        GlobalScope.launch(Dispatchers.Default) {
            Snackbar.make(requireView(), "Обновление ${codex.rusName}", Snackbar.LENGTH_SHORT).show()

            val codexLists = CodexParser().get(codex)
            BaseCodexDatabase.update(codex, codexLists)
            BaseCodexProvider.setDefaultPageItems()
            Preferences.setCodexInfo(
                codex,
                CodexVersionParser.getChangesCount(codex),
                CodexVersionParser.getChangeDate(codex)
            )

            view?.let {
                Snackbar.make(it, "${codex.rusName} обновлен", Snackbar.LENGTH_SHORT).show()
            }

            getCodexImage(codex)?.animation?.cancel()
        }

        model.isUpdateEnabled(codex).value = false
    }

    private fun getCodexImage(codex: Codex): View?{
        return if (_binding != null){
            when(codex){
                Codex.UK -> binding.updateUk.image
                Codex.UPK -> binding.updateUpk.image
                Codex.KoAP -> binding.updateKoap.image
                Codex.PIKoAP -> binding.updatePikoap.image
            }
        }else{
            null
        }
    }

    // debug function
    private fun setUpClearAllButton(){
        binding.debugClearAllButton.visibility = View.VISIBLE
        binding.debugClearAllButton.setOnClickListener {
            BaseCodexDatabase.clearAll()

            BaseCodexProvider.setDefaultPageItems()
            Preferences.setCodexChangesCount(Codex.UK, -1)
            Preferences.setCodexChangesCount(Codex.UPK, -1)
            Preferences.setCodexChangesCount(Codex.KoAP, -1)
            Preferences.setCodexChangesCount(Codex.PIKoAP, -1)

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
