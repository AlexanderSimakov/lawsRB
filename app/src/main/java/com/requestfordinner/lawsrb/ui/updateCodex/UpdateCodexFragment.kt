package com.requestfordinner.lawsrb.ui.updateCodex

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.Preferences
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.requestfordinner.lawsrb.databinding.FragmentUpdateCodexBinding
import com.requestfordinner.lawsrb.databinding.UpdateCodexButtonBinding
import com.requestfordinner.lawsrb.ui.updateCodex.UpdateCodexUiState.ButtonState
import com.requestfordinner.lawsrb.ui.NotificationBadge
import com.requestfordinner.lawsrb.utils.ImprovedToast

/**
 * This class is a child of [Fragment] which represents **Update Codex Page** where user can:
 * 1. Update codexes.
 * 2. See codex change date.
 * 3. Check for updates.
 *
 * @see Fragment
 * @see UpdateCodexViewModel
 */
class UpdateCodexFragment : Fragment() {

    /**
     * This variable is responsible for enabling and disabling some debug functions such as
     * clear database button.
     *
     * **Attention:** make sure that you set it to `false` after using.
     */
    private val IS_DEBUG: Boolean = true

    private lateinit var model: UpdateCodexViewModel
    private lateinit var binding: FragmentUpdateCodexBinding
    private lateinit var toast: ImprovedToast

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateCodexBinding.inflate(inflater, container, false)
        model = ViewModelProviders.of(this)[UpdateCodexViewModel::class.java]
        toast = ImprovedToast(requireContext())

        // Allows the AppBarLayout to open with animation if it was hidden on transition to the fragment
        val toolbarLayout = requireActivity().findViewById<AppBarLayout>(R.id.app_bar_layout)
        toolbarLayout.setExpanded(true, true)

        clearMenuOptions()
        fabVisibility(false)

        setUpObservers()
        setUpUpdateButtons()
        setUpListeners()

        if (IS_DEBUG) {
            setUpClearAllButton()
        }

        return binding.root
    }

    /** Hides unnecessary menu items in the current fragment */
    private fun clearMenuOptions() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            //Remove all existing items from the menu,
            //leaving it empty as if it had just been created.
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)
    }

    /** This method set up *observes* for view model's ui state. */
    private fun setUpObservers() {
        model.uiState.observe(viewLifecycleOwner) {
            it.messageToShow?.let { messageId ->
                toast.show(messageId)
                it.messageToShow = null
            }

            Codex.forEach { codex -> setUpUpdateButton(binding.getButton(codex), codex) }
            binding.checkUpdatesButton.isEnabled =
                it.checkUpdatesButtonState == ButtonState.ENABLED

            NotificationBadge.isVisible = model.uiState.value?.isUpdateEnabled() ?: false
        }
    }

    /**
     * This method call [setUpUpdateButton] for each codex update button.
     *
     * @see setUpUpdateButton
     */
    private fun setUpUpdateButtons() {
        Codex.forEach { codex -> setUpUpdateButton(binding.getButton(codex), codex) }
    }

    /** This method configure **Update [button]** using given [codex]. */
    private fun setUpUpdateButton(button: UpdateCodexButtonBinding, codex: Codex) {
        when (model.uiState.value!!.getState(codex)) {
            ButtonState.ENABLED -> button.makeEnabled(requireContext(), codex).cancelAnimation()
            ButtonState.DISABLED -> button.makeDisabled(requireContext(), codex).cancelAnimation()
            ButtonState.UPDATING -> button.makeDisabled(requireContext(), codex).continueAnimation()
        }
    }

    /** This method set up *OnClickListener* for buttons. */
    private fun setUpListeners() {
        binding.checkUpdatesButton.setOnClickListener { model.checkCodexUpdates() }
        Codex.forEach { codex ->
            binding.getButton(codex).updateCodexButton.setOnClickListener {
                model.executeCodexUpdating(codex)
            }
        }
    }

    /**
     * This method set up and show button which clear all databases.
     *
     * **Note:** This is a *debug* method which will be executed only if [IS_DEBUG] equals `true`.
     */
    private fun setUpClearAllButton() {
        binding.debugClearAllButton.visibility = View.VISIBLE
        binding.debugClearAllButton.setOnClickListener {
            BaseCodexDatabase.clearAll()
            BaseCodexProvider.setDefaultPageItems()
            Codex.forEach { codex -> Preferences.setCodexChangesCount(codex, -1) }
            model.updateIsUpdateEnabled()

            toast.show("Базы данных очищены")
        }
    }

    /**
     * The method is responsible for hiding and showing the [FloatingActionButton].
     * @param visibility button display state, pass false to hide, pass true to show
     */
    private fun fabVisibility(visibility: Boolean) {
        requireActivity().findViewById<FloatingActionButton>(R.id.fab)
            ?.isVisible = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fabVisibility(true)
    }

    /** This method returns [UpdateCodexButtonBinding] for given [codex]. */
    private fun FragmentUpdateCodexBinding.getButton(codex: Codex): UpdateCodexButtonBinding {
        return when (codex) {
            Codex.UK -> updateUk
            Codex.UPK -> updateUpk
            Codex.KoAP -> updateKoap
            Codex.PIKoAP -> updatePikoap
        }
    }
}
