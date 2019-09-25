package com.rekkursion.metallica.manager

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.R
import com.rekkursion.metallica.adapter.WordItemAdapter
import com.rekkursion.metallica.model.WordItem
import java.io.File

object WordsManager {
    const val NEW_WORD_FIELD: String = "new_word"

    // set adapter on recv
    fun setAdapterOnWordRecyclerView(context: Context, recv: RecyclerView, wordList: ArrayList<WordItem>) {
        val adapter = WordItemAdapter(wordList, context)
        recv.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}