package com.rekkursion.metallica.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.rekkursion.metallica.R
import com.rekkursion.metallica.fragment.ClassificationListFragment
import com.rekkursion.metallica.fragment.WordListFragment
import com.rekkursion.metallica.manager.ClassificationManager
import com.rekkursion.metallica.manager.WordsManager
import com.rekkursion.metallica.view.WhatToAddDialog

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {
    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        private const val RC_TO_WORD_ADDING_ACTIVITY: Int = 4731
        private const val RC_TO_CLASSIFICATION_ADDING_ACTIVITY: Int = 5371
    }

    private lateinit var mFabAddNewWordOrClassification: FloatingActionButton
    private lateinit var mLlyRoot: LinearLayout

    private var mClassificationListFragment: ClassificationListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initEvents()

        if (savedInstanceState == null) {
            mClassificationListFragment = ClassificationListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.lly_root_at_main, mClassificationListFragment!!)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_TO_WORD_ADDING_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(mFabAddNewWordOrClassification, getString(R.string.str_add_new_word_or_classification_successfully) + " " + data?.getStringExtra(WordsManager.NEW_WORD_FIELD), Snackbar.LENGTH_SHORT).show()
                    mClassificationListFragment?.setAdapterOnClassificationRecyclerView()
                }
            }

            RC_TO_CLASSIFICATION_ADDING_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(mFabAddNewWordOrClassification, getString(R.string.str_add_new_word_or_classification_successfully) + " " + data?.getStringExtra(ClassificationManager.NEW_CLASSIFICATION_FIELD), Snackbar.LENGTH_SHORT).show()
                    mClassificationListFragment?.setAdapterOnClassificationRecyclerView()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            if (supportFragmentManager.fragments[0]::class.java.simpleName == WordListFragment::class.java.simpleName) {
                mClassificationListFragment = ClassificationListFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .detach(supportFragmentManager.fragments[0])
                    .add(R.id.lly_root_at_main, mClassificationListFragment!!)
                    .commit()
            }
        }
    }

    private fun initViews() {
        mFabAddNewWordOrClassification = findViewById(R.id.fab_add_new_word_or_classification)
        mLlyRoot = findViewById(R.id.lly_root_at_main)
    }

    private fun initEvents() {
        // click for adding new word
        mFabAddNewWordOrClassification.setOnClickListener {
            val whatToAddDialog = WhatToAddDialog(this)
            whatToAddDialog
                .setOnAddNewClassificationListener(object: WhatToAddDialog.OnAddNewClassificationListener {
                    override fun onAddNewClassificationClick() {
                        val toClassificationAddingIntent = Intent(this@MainActivity, ClassificationAddingActivity::class.java)
                        startActivityForResult(toClassificationAddingIntent, RC_TO_CLASSIFICATION_ADDING_ACTIVITY)
                    }
                })
                .setOnAddNewWordListener(object: WhatToAddDialog.OnAddNewWordListener {
                    override fun onAddNewWordClick() {
                        val toWordAddingIntent = Intent(this@MainActivity, WordAddingActivity::class.java)
                        startActivityForResult(toWordAddingIntent, RC_TO_WORD_ADDING_ACTIVITY)
                    }
                })
                .show()
        }
    }
}
