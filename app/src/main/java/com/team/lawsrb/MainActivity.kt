package com.team.lawsrb

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.ToggleButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.team.lawsrb.basic.dataProviders.*
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexLists
import com.team.lawsrb.basic.htmlParser.Parser
import com.team.lawsrb.basic.roomDatabase.*
import com.team.lawsrb.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
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

        // Saving state of app
        // using SharedPreferences
        sharedPref = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        if (sharedPref.getBoolean("isDarkModeOn", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        //Initialize database
        BaseCodexDatabase.init(applicationContext)

        /*
        clearAll()

        GlobalScope.launch {
            Log.i("MainActivity", "Start parsing UK")
            val allUK: CodexLists = Parser.get(Codex.UK)
            Log.i("MainActivity", allUK.toString())
            Log.i("MainActivity", "Start adding UK")
            allUK.parts.forEach { BaseCodexDatabase.UK.partsDao().insert(it) }
            allUK.sections.forEach { BaseCodexDatabase.UK.sectionsDao().insert(it) }
            allUK.chapters.forEach { BaseCodexDatabase.UK.chaptersDao().insert(it) }
            allUK.articles.forEach { BaseCodexDatabase.UK.articlesDao().insert(it) }
            Log.i("MainActivity", "End with UK")

            Log.i("MainActivity", "Start parsing UPK")
            val allUPK: CodexLists = Parser.get(Codex.UPK)
            Log.i("MainActivity", allUPK.toString())
            Log.i("MainActivity", "Start adding UPK")
            allUPK.parts.forEach { BaseCodexDatabase.UPK.partsDao().insert(it) }
            allUPK.sections.forEach { BaseCodexDatabase.UPK.sectionsDao().insert(it) }
            allUPK.chapters.forEach { BaseCodexDatabase.UPK.chaptersDao().insert(it) }
            allUPK.articles.forEach { BaseCodexDatabase.UPK.articlesDao().insert(it) }
            Log.i("MainActivity", "End with UPK")

            Log.i("MainActivity", "Start parsing KoAP")
            val allKoAP: CodexLists = Parser.get(Codex.KoAP)
            Log.i("MainActivity", "Start adding KoAP")
            allKoAP.parts.forEach { BaseCodexDatabase.KoAP.partsDao().insert(it) }
            allKoAP.sections.forEach { BaseCodexDatabase.KoAP.sectionsDao().insert(it) }
            allKoAP.chapters.forEach { BaseCodexDatabase.KoAP.chaptersDao().insert(it) }
            allKoAP.articles.forEach { BaseCodexDatabase.KoAP.articlesDao().insert(it) }
            Log.i("MainActivity", "End with KoAP")

            Log.i("MainActivity", "Start parsing PIKoAP")
            val allPIKoAP: CodexLists = Parser.get(Codex.PIKoAP)
            Log.i("MainActivity", "Start adding PIKoAP")
            allPIKoAP.parts.forEach { BaseCodexDatabase.PIKoAP.partsDao().insert(it) }
            allPIKoAP.sections.forEach { BaseCodexDatabase.PIKoAP.sectionsDao().insert(it) }
            allPIKoAP.chapters.forEach { BaseCodexDatabase.PIKoAP.chaptersDao().insert(it) }
            allPIKoAP.articles.forEach { BaseCodexDatabase.PIKoAP.articlesDao().insert(it) }
            Log.i("MainActivity", "End with PIKoAP")
        }
        */
    }

    /*
    private fun clearAll(){
        Log.i("MainActivity", "Clear all start")
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
        Log.i("MainActivity", "Clear all end")
    }
    */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

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

        searchView.setOnCloseListener {
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

        // --- Theme switcher ---
        val themeSwitcher = findViewById<ToggleButton>(R.id.theme_switcher)
        themeSwitcher.isChecked = sharedPref.getBoolean("isDarkModeOn", false)
        val editor = sharedPref.edit()
        themeSwitcher.setOnCheckedChangeListener { _, isDarkMode ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkModeOn", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkModeOn", false)
            }
            editor.apply()
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
