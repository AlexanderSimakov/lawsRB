package com.team.lawsrb.ui.updateCodex

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.team.lawsrb.R
import com.team.lawsrb.basic.NetworkCheck
import com.team.lawsrb.basic.Preferences
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexParser
import com.team.lawsrb.basic.htmlParser.CodexVersionParser
import com.team.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.team.lawsrb.databinding.FragmentUpdateCodexBinding
import com.team.lawsrb.databinding.UpdateCodexButtonBinding
import com.team.lawsrb.ui.NotificationBadge
import kotlinx.coroutines.*

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
    private val IS_DEBUG: Boolean = false

    private lateinit var model: UpdateCodexViewModel

    private var _binding: FragmentUpdateCodexBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateCodexBinding.inflate(inflater, container, false)
        val root: View = binding.root

        model = ViewModelProviders.of(this)[UpdateCodexViewModel::class.java]

        clearMenuOptions()
        fabVisibility(false)

        setUpCheckCodexUpdatesButton()
        setUpObservers()
        setUpUpdateButtons()
        setOnClickListenerForUpdateButtons()

        if (IS_DEBUG){
            setUpClearAllButton()
        }

        return root
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

    /** This method configure **CheckCodexUpdates** button and set it listener. */
    private fun setUpCheckCodexUpdatesButton(){
        // TODO: use string.xml for text messages
        binding.checkUpdatesButton.setOnClickListener {
            if (NetworkCheck.isNotAvailable){
                Snackbar.make(requireView(), "Нет доступа в Интернет", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val handler = CoroutineExceptionHandler { _, exception ->
                Log.e(TAG, "Internet connection fallen: $exception")
            }
            CoroutineScope(Dispatchers.Default).launch(handler) {
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

    /** This method set up *observes* for view model's button state variables. */
    private fun setUpObservers(){
        model.isCheckUpdateButtonEnabled.observe(viewLifecycleOwner) {
            binding.checkUpdatesButton.isEnabled = it
        }

        model.isUpdateEnabled(Codex.UK).observe(viewLifecycleOwner) {
            setUpUpdateButton(
                binding.updateUk,
                Codex.UK,
                getString(R.string.menu_criminal_code)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.UPK).observe(viewLifecycleOwner) {
            setUpUpdateButton(
                binding.updateUpk,
                Codex.UPK,
                getString(R.string.menu_code_of_criminal_proсedure)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.KoAP).observe(viewLifecycleOwner) {
            setUpUpdateButton(
                binding.updateKoap,
                Codex.KoAP,
                getString(R.string.menu_KoAP)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }

        model.isUpdateEnabled(Codex.PIKoAP).observe(viewLifecycleOwner) {
            setUpUpdateButton(
                binding.updatePikoap,
                Codex.PIKoAP,
                getString(R.string.menu_PIKoAP)
            )
            NotificationBadge.isVisible = model.isUpdateEnabled()
        }
    }

    /**
     * This method call [setUpUpdateButton] for each codex update button.
     *
     * @see setUpUpdateButton
     */
    private fun setUpUpdateButtons(){
        binding.apply {
            setUpUpdateButton(updateUk, Codex.UK, getString(R.string.menu_criminal_code))
            setUpUpdateButton(updateUpk, Codex.UPK, getString(R.string.menu_code_of_criminal_proсedure))
            setUpUpdateButton(updateKoap, Codex.KoAP, getString(R.string.menu_KoAP))
            setUpUpdateButton(updatePikoap, Codex.PIKoAP, getString(R.string.menu_PIKoAP))
        }
    }

    /** This method configure **Update [button]** using given [codex] and [title]. */
    private fun setUpUpdateButton(button: UpdateCodexButtonBinding, codex: Codex, title: String?){
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

    /**
     * This method set up *OnClickListener* for each update button using [executeUpdatingFor] method.
     *
     * @see executeUpdatingFor
     */
    private fun setOnClickListenerForUpdateButtons(){
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
    private fun executeUpdatingFor(codex: Codex){
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

        //The coroutine exception handler that will be called if the coroutine failed
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "Internet connection interrupted: $exception")
            view?.let {
                Snackbar.make(it, "Интернет-соединение прервано", Snackbar.LENGTH_SHORT).show()
            }
            getCodexImage(codex)?.animation?.cancel()
            model.isUpdateEnabled(codex).postValue(true)
        }
        CoroutineScope(Dispatchers.Default).launch(handler) {
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

    /** This method returns **Update Codex Button** image by given [codex]. */
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

    /**
     * This method set up and show button which clear all databases.
     *
     * **Note:** This is a *debug* method which will be executed only if [IS_DEBUG] equals `true`.
     */
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

    private fun fabVisibility(visibility: Boolean) {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fabVisibility(true)
        _binding = null
    }
}
