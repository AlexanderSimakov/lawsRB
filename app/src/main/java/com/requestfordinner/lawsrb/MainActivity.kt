package com.requestfordinner.lawsrb

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.requestfordinner.lawsrb.basic.NetworkCheck
import com.requestfordinner.lawsrb.basic.Preferences
import com.requestfordinner.lawsrb.basic.dataProviders.*
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.htmlParser.CodexVersionParser
import com.requestfordinner.lawsrb.basic.roomDatabase.*
import com.requestfordinner.lawsrb.databinding.ActivityMainBinding
import com.requestfordinner.lawsrb.ui.FragmentNavigation
import com.requestfordinner.lawsrb.ui.NotificationBadge
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    //Class name keywords used in log, tag separation required
    private val TAG = "MainActivityLog"

    private val coroutine = CoroutineScope(Dispatchers.Main)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var _savedInstanceState: Bundle? = null

    //variables for state instance
    private val FAVORITES_KEY = "is_favorites_showing"
    private val SEARCH_KEY = "is_search_showing"
    private val SEARCH_STRING = "search_string"
    private val SENT_REQUEST_KEY = "is_sent_request"
    private var searchableString = ""
    private var isFavoritesShowing = false
    private var isSearchShowing = false
    private var isSentRequest = false

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // init NetworkAvailable class
        val coroutineContext = Dispatchers.Main
        val mScope = CoroutineScope(coroutineContext + SupervisorJob())
        mScope.launch {
            val networkCheck = NetworkCheck(applicationContext)
            networkCheck.subscribeForUpdates()
        }

        super.onCreate(savedInstanceState)
        //Initialize database
        BaseCodexDatabase.init(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_criminal_code, R.id.nav_code_of_criminal_procedure,
                R.id.nav_koap, R.id.nav_pikoap
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (NetworkCheck.isAvailable) {
            CodexVersionParser.update()
        }

        if (savedInstanceState != null) {
            _savedInstanceState = savedInstanceState
        }

        // init Preferences and setup dark/light mode
        Preferences.update(applicationContext)
        if (Preferences.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Highlighter.isDarkMode = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Highlighter.isDarkMode = false
        }

        // init codex info
        Preferences.apply {
            if (isRunFirst) {
                setCodexInfo(Codex.UK, 82, "От 13 мая 2022")
                setCodexInfo(Codex.UPK, 61, "От 20 июля 2022")
                setCodexInfo(Codex.KoAP, 1, "От 4 января 2022")
                setCodexInfo(Codex.PIKoAP, 1, "От 4 января 2022")
                isRunFirst = false
            }
        }

        // update notification badge
        val item = binding.navView.menu.findItem(R.id.nav_update_codex)
        val notificationImage = item.actionView as ImageView
        notificationImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.notification_badge,
                applicationContext.theme
            )
        )
        NotificationBadge.image = notificationImage

        // show notification if have changes
        notificationImage.postDelayed({
            NotificationBadge.isVisible = CodexVersionParser.isHaveChanges()
        }, 3000)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isFavoritesShowing = savedInstanceState.getBoolean(FAVORITES_KEY)
        isSearchShowing = savedInstanceState.getBoolean(SEARCH_KEY)
        isSentRequest = savedInstanceState.getBoolean(SENT_REQUEST_KEY)
        searchableString = savedInstanceState.getString(SEARCH_STRING) ?: ""

        Log.d(
            TAG, "Restored state: isFavoritesShowing=$isFavoritesShowing, " +
                    "isSearchShowing=$isSearchShowing, " +
                    "searchableString=$searchableString"
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val toolbarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)

        val searchFab = findViewById<FloatingActionButton>(R.id.fab)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchItem.isVisible = isSearchShowing
        searchView.queryHint = getString(R.string.action_search)
        searchView.isIconified = !isSearchShowing

        if (searchableString.isNotEmpty()) {
            searchView.setQuery(searchableString, false)
            searchView.clearFocus()
        }

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                searchableString = text ?: ""
                return false
            }

            override fun onQueryTextSubmit(text: String): Boolean {
                BaseCodexProvider.search = text
                searchableString = text
                isSentRequest = true
                // Allows the AppBarLayout to open with animation if it was hidden
                toolbarLayout.setExpanded(true, true)
                return false
            }
        })

        searchView.setOnCloseListener {
            isSearchShowing = false
            searchItem.isVisible = false
            if (isSentRequest) {
                BaseCodexProvider.search = ""
                isSentRequest = false
            }
            searchableString = ""

            // hide keyboard
            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            false
        }

        // --- Search button ---
        searchFab.setOnClickListener {
            searchItem.isVisible = true
            searchView.isIconified = false
            isSearchShowing = true
        }

        // --- Favorites button ---
        val favoritesItem = menu.findItem(R.id.action_favorites)
        val favoritesCheckBox = favoritesItem.actionView as CheckBox

        favoritesCheckBox.buttonDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.card_checkbox_selector,
            applicationContext.theme
        )

        favoritesCheckBox.scaleX = 0.8F
        favoritesCheckBox.scaleY = 0.8F

        // TODO: remove this crutch
        // Crutch: The main purpose of this line is to move favorites icon left :)
        favoritesCheckBox.text = "   "

        if (isFavoritesShowing && !favoritesCheckBox.isChecked) {
            favoritesCheckBox.toggle()
        }

        favoritesCheckBox.setOnClickListener {
            val isChecked = (it as CheckBox).isChecked
            BaseCodexProvider.showFavorites = isChecked
            isFavoritesShowing = isChecked
        }

        // --- Theme switcher ---
        val themeSwitcher = findViewById<ToggleButton>(R.id.theme_switcher)
        themeSwitcher.isChecked = Preferences.isDarkTheme
        themeSwitcher.setOnCheckedChangeListener { _, isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Preferences.isDarkTheme = true
                Highlighter.isDarkMode = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Preferences.isDarkTheme = false
                Highlighter.isDarkMode = false
            }
            recreate()
        }

        return true
    }

    /** This method returns true if the current fragment is the first navigation fragment, in other false. */
    private fun isFirstNavHostFragment(): Boolean {
        val fragmentNavigation = FragmentNavigation(this)

        return fragmentNavigation.getOpenedCode() == Codex.UK
    }

    /** Method fires a dialog box if the user clicked the BACK button from the main navigation fragment. */
    private fun openExitDialog() {
        if (isFirstNavHostFragment()) {
            if (doubleBackToExitPressedOnce) {
                System.gc()
                super.onBackPressed()
                return
            }

            coroutine.launch {
                doubleBackToExitPressedOnce = true
                Toast.makeText(this@MainActivity, "Нажмите снова, чтобы выйти", Toast.LENGTH_SHORT)
                    .show()
                delay(2000)
                doubleBackToExitPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }

    /** This method is used to handle pressing the BACK button when the favorites tab is enabled. */
    private fun toDefaultFavoritesItemState() {
        val favoritesItem = findViewById<CheckBox>(R.id.action_favorites)

        BaseCodexProvider.showFavorites = false

        coroutine.launch {
            delay(300)
            favoritesItem.toggle()
            favoritesItem.isChecked = false
            isFavoritesShowing = false
        }
    }

    /** This method is used to handle pressing the BACK button when search is enabled. */
    private fun toDefaultSearchViewState() {
        val searchView = findViewById<SearchView>(R.id.action_search)

        if (searchView.isShown) {
            //hide keyboard
            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            searchView.setQuery("", false)
            searchView.isIconified = true

            if (!searchView.isShown) {
                isSentRequest = false
                isSearchShowing = false
            }
        }
    }

    /** This method handles all situations when the BACK button is pressed. */
    override fun onBackPressed() {
        val fragmentNavigation = FragmentNavigation(this)
        val currentFragment = fragmentNavigation.getOpenedFragment()
        val containingAttribute = currentFragment.toString()

        if (containingAttribute.contains("UpdateCodexFragment")) {
            super.onBackPressed()
            isSearchShowing = false
            isFavoritesShowing = false
        } else {
            if (!isSearchShowing && !isFavoritesShowing) {
                openExitDialog()
            }
            if (isSearchShowing) {
                toDefaultSearchViewState()
            }
            if (!isSearchShowing && isFavoritesShowing) {
                toDefaultFavoritesItemState()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchableString)
        outState.putBoolean(FAVORITES_KEY, isFavoritesShowing)
        outState.putBoolean(SEARCH_KEY, isSearchShowing)
        outState.putBoolean(SENT_REQUEST_KEY, isSentRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        Log.i(TAG, "Activity has been destroyed")
        super.onDestroy()
    }
}
