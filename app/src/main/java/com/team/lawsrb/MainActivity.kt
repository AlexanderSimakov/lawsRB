package com.team.lawsrb

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.ToggleButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.app.TaskStackBuilder
import com.team.lawsrb.basic.Preferences
import com.team.lawsrb.basic.dataProviders.*
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexVersionParser
import com.team.lawsrb.basic.roomDatabase.*
import com.team.lawsrb.databinding.ActivityMainBinding
import com.team.lawsrb.ui.codexPageFragments.Highlighter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_criminal_code, R.id.nav_code_of_criminal_procedure,
                R.id.nav_koap, R.id.nav_pikoap,
                R.id.nav_settings), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        CodexVersionParser.update()

        // Saving state of app
        // using SharedPreferences
        Preferences.update(applicationContext)
        if (Preferences.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Highlighter.isDarkMode = true
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Highlighter.isDarkMode = false
        }

        // init codex info
        Preferences.apply {
            if (isRunFirst){
                setCodexInfo(Codex.UK, 82, "От 13 мая 2022")
                setCodexInfo(Codex.UPK, 61, "От 20 июля 2022")
                setCodexInfo(Codex.KoAP, 1, "От 4 января 2022")
                setCodexInfo(Codex.PIKoAP, 1, "От 4 января 2022")
                isRunFirst = false
            }
        }

        //Initialize database
        BaseCodexDatabase.init(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val searchFab = findViewById<FloatingActionButton>(R.id.fab)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.action_search)

        searchView.setOnQueryTextListener( object : OnQueryTextListener{
            override fun onQueryTextChange(text: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(text: String): Boolean {
                BaseCodexProvider.sentQuery(text)
                return false
            }
        })

        searchView.setOnSearchClickListener {
            searchFab.hide()
        }

        searchView.setOnCloseListener {
            searchFab.show()
            BaseCodexProvider.sentQuery("")
            false
        }

        // --- Favorites button ---
        val favoritesItem = menu.findItem(R.id.action_favorites)
        val favoritesCheckBox = favoritesItem.actionView as CheckBox

        favoritesCheckBox.buttonDrawable = applicationContext.getDrawable(R.drawable.card_checkbox_selector)
        favoritesCheckBox.scaleX = 0.8F
        favoritesCheckBox.scaleY = 0.8F

        favoritesCheckBox.setOnClickListener {
            val isChecked = (it as CheckBox).isChecked
            BaseCodexProvider.setFavorite(isChecked)
        }

        // --- Search button ---
        searchFab.setOnClickListener {
            searchView.isIconified = false
            searchFab.hide()
        }

        // --- Theme switcher ---
        val themeSwitcher = findViewById<ToggleButton>(R.id.theme_switcher)
        themeSwitcher.isChecked = Preferences.isDarkTheme
        themeSwitcher.setOnCheckedChangeListener { _, isDarkMode ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Preferences.isDarkTheme = true
                Highlighter.isDarkMode = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Preferences.isDarkTheme = false
                Highlighter.isDarkMode = false
            }
            TaskStackBuilder.create(applicationContext)
                .addNextIntent(Intent(applicationContext, MainActivity::class.java))
                .addNextIntent(intent)
                .startActivities()
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
