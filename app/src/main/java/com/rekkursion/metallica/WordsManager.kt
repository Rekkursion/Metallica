package com.rekkursion.metallica

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.adapter.WordItemAdapter
import com.rekkursion.metallica.model.WordItem

object WordsManager {
    const val NEW_WORD_FIELD: String = "new_word"

    private val mWordList = ArrayList<WordItem>()

    // add the new word into word-list
    fun addNewWord(context: Context, item: WordItem) {
        mWordList.add(item)
    }

    // load all words from the database
    fun loadAllWordsFromDatabase(context: Context, clearFirst: Boolean) {
        if (clearFirst)
            mWordList.clear()
        // TODO: serialize in
    }

    // get the word-list
    fun getWordList(): ArrayList<WordItem> = mWordList

    // set adapter on recv
    fun setAdapterOnWordRecyclerView(context: Context, recv: RecyclerView) {
        val adapter = WordItemAdapter(getWordList(), context)
        recv.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}