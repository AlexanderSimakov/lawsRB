package com.requestfordinner.lawsrb.ui.updateCodex

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.Preferences
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.htmlParser.CodexVersionParser
import com.requestfordinner.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.requestfordinner.lawsrb.databinding.FragmentUpdateCodexBinding
import com.requestfordinner.lawsrb.databinding.UpdateCodexButtonBinding
import com.requestfordinner.lawsrb.ui.updateCodex.UpdateCodexUiState.ButtonState
import com.requestfordinner.lawsrb.ui.NotificationBadge

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
    private val TAG = "UpdateCodexFragment"

    /**
     * This variable is responsible for enabling and disabling some debug functions such as
     * clear database button.
     *
     * **Attention:** make sure that you set it to `false` after using.
     */
    private val IS_DEBUG: Boolean = true

    private lateinit var model: UpdateCodexViewModel
    private lateinit var binding: FragmentUpdateCodexBinding

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateCodexBinding.inflate(inflater, container, false)
        model = ViewModelProviders.of(this)[UpdateCodexViewModel::class.java]

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

    /**
     * Menu overriding.
     *
     * Allows to hide unnecessary menu items in the current fragment.
     */
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

    /** This method set up *observes* for view model's button state variables. */
    private fun setUpObservers() {
        model.uiState.observe(viewLifecycleOwner) {
            it.messageToShow?.let { messageId ->
                showMessage(messageId)
                it.messageToShow = null
            }

            setUpUpdateButton(binding.updateUk, Codex.UK, getString(R.string.menu_UK))
            setUpUpdateButton(binding.updateUpk, Codex.UPK, getString(R.string.menu_UPK))
            setUpUpdateButton(binding.updateKoap, Codex.KoAP, getString(R.string.menu_KoAP))
            setUpUpdateButton(binding.updatePikoap, Codex.PIKoAP, getString(R.string.menu_PIKoAP))
            binding.checkUpdatesButton.isEnabled = it.checkUpdatesButtonState == ButtonState.ENABLED

            NotificationBadge.isVisible = model.uiState.value?.isUpdateEnabled() ?: false
        }
    }

    private fun showMessage(messageId: Int) {
        toast?.cancel()
        toast = Toast.makeText(
            requireContext(),
            messageId,
            Toast.LENGTH_SHORT
        )
        toast?.show()
    }

    /**
     * This method call [setUpUpdateButton] for each codex update button.
     *
     * @see setUpUpdateButton
     */
    private fun setUpUpdateButtons() {
        binding.apply {
            setUpUpdateButton(updateUk, Codex.UK, getString(R.string.menu_UK))
            setUpUpdateButton(updateUpk, Codex.UPK, getString(R.string.menu_UPK))
            setUpUpdateButton(updateKoap, Codex.KoAP, getString(R.string.menu_KoAP))
            setUpUpdateButton(updatePikoap, Codex.PIKoAP, getString(R.string.menu_PIKoAP))
        }
    }

    /** This method configure **Update [button]** using given [codex] and [title]. */
    private fun setUpUpdateButton(button: UpdateCodexButtonBinding, codex: Codex, title: String?) {
        if (model.uiState.value!!.getState(codex) == ButtonState.ENABLED) {
            button.updateCodexButton.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background_active)
            )

            val activeRefreshButtonImageColor = ContextCompat
                .getColor(requireContext(), R.color.refresh_image_active)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = "Обновить $title"
            button.subtitle.text = CodexVersionParser.getChangeDate(codex)
            button.updateCodexButton.isEnabled = true
            button.updateCodexButton.cardElevation = 20F
            getCodexImage(codex).animation?.cancel()
        } else if (model.uiState.value!!.getState(codex) == ButtonState.DISABLED) {
            button.updateCodexButton.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background)
            )

            val activeRefreshButtonImageColor = ContextCompat
                .getColor(requireContext(), R.color.refresh_image)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = title
            button.subtitle.text = Preferences.getCodexUpdateDate(codex)
            button.updateCodexButton.isEnabled = false
            button.updateCodexButton.cardElevation = 5F
            getCodexImage(codex).animation?.cancel()
        } else if (model.uiState.value!!.getState(codex) == ButtonState.UPDATING) {
            button.updateCodexButton.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.refresh_card_background)
            )

            val activeRefreshButtonImageColor = ContextCompat
                .getColor(requireContext(), R.color.refresh_image)

            button.image.setColorFilter(activeRefreshButtonImageColor)
            button.title.setTextColor(activeRefreshButtonImageColor)
            button.subtitle.setTextColor(activeRefreshButtonImageColor)

            button.title.text = title
            button.subtitle.text = Preferences.getCodexUpdateDate(codex)
            button.updateCodexButton.isEnabled = false
            button.updateCodexButton.cardElevation = 5F

            getCodexImage(codex).let {
                if (it.animation == null) {
                    it.startAnimation(getInfinityRotateAnimation())
                }
            }
        }
    }

    /**
     * This method set up *OnClickListener* for each update button using [executeUpdatingFor] method.
     *
     * @see executeUpdatingFor
     */
    private fun setUpListeners() {
        binding.checkUpdatesButton.setOnClickListener { model.checkCodexUpdates() }
        binding.apply {
            updateUk.updateCodexButton.setOnClickListener { executeUpdatingFor(Codex.UK) }
            updateUpk.updateCodexButton.setOnClickListener { executeUpdatingFor(Codex.UPK) }
            updateKoap.updateCodexButton.setOnClickListener { executeUpdatingFor(Codex.KoAP) }
            updatePikoap.updateCodexButton.setOnClickListener { executeUpdatingFor(Codex.PIKoAP) }
        }
    }

    /**
     * This method execute given [codex]'s database updating.
     *
     * Also it manage state of given [codex]'s update button.
     */
    private fun executeUpdatingFor(codex: Codex) {
        model.executeCodexUpdating(codex)
    }

    /** This method returns **Update Codex Button** image by given [codex]. */
    private fun getCodexImage(codex: Codex): View {
        return when (codex) {
            Codex.UK -> binding.updateUk.image
            Codex.UPK -> binding.updateUpk.image
            Codex.KoAP -> binding.updateKoap.image
            Codex.PIKoAP -> binding.updatePikoap.image
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
            Preferences.setCodexChangesCount(Codex.UK, -1)
            Preferences.setCodexChangesCount(Codex.UPK, -1)
            Preferences.setCodexChangesCount(Codex.KoAP, -1)
            Preferences.setCodexChangesCount(Codex.PIKoAP, -1)

            model.updateIsUpdateEnabled()

            Toast.makeText(
                requireContext(),
                "Базы данных очищены",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * The method is responsible for hiding and showing the Floating Action Button.
     * @param visibility button display state, pass false to hide, pass true to show
     */
    private fun fabVisibility(visibility: Boolean) {
        val fab: FloatingActionButton? = requireActivity().findViewById(R.id.fab)

        if (fab != null) {
            fab.isVisible = visibility
        } else {
            Log.e(TAG, "FAB is null: Cannot change it visibility")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fabVisibility(true)
    }

    private fun getInfinityRotateAnimation(): RotateAnimation {
        return RotateAnimation(
            0F, 360F,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
        }
    }
}

