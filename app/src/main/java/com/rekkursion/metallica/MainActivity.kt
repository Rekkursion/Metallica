package com.rekkursion.metallica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.rekkursion.metallica.activity.WordAddingActivity
import com.rekkursion.metallica.adapter.WordItemAdapter
import com.rekkursion.metallica.model.WordItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val RC_TO_WORD_ADDING_ACTIVITY: Int = 4731
    }

    private lateinit var mFabAddNewWord: FloatingActionButton
    private lateinit var mRecvWordList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initEvents()
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

        if (requestCode == RC_TO_WORD_ADDING_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(mFabAddNewWord, getString(R.string.str_add_new_word_successfully) + " " + data?.getStringExtra(WordsManager.NEW_WORD_FIELD), Snackbar.LENGTH_SHORT).show()
                WordsManager.setAdapterOnWordRecyclerView(this, mRecvWordList)
            }
        }
    }

    private fun initViews() {
        mFabAddNewWord = findViewById(R.id.fab_add_new_word)
        mRecvWordList = findViewById(R.id.recv_word_list)
    }

    private fun initEvents() {
        // click for adding new word
        mFabAddNewWord.setOnClickListener {
            val toWordAddingIntent = Intent(this, WordAddingActivity::class.java)
            startActivityForResult(toWordAddingIntent, RC_TO_WORD_ADDING_ACTIVITY)
        }

        // set layout-manager on recv
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecvWordList.layoutManager = layoutManager

        // initially load all words from the database
        WordsManager.loadAllWordsFromDatabase(this, true)

        // set adapter on recv
        WordsManager.setAdapterOnWordRecyclerView(this, mRecvWordList)
    }
}
