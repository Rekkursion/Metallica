package com.rekkursion.metallica.manager

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.adapter.WordItemAdapter
import com.rekkursion.metallica.model.WordItem
import java.io.File

object WordsManager {
    const val NEW_WORD_FIELD: String = "new_word"

    private const val SERIALIZATION_WORDS_FILENAME: String = ".metallica_serialization_words"

    private val mWordList = ArrayList<WordItem>()

    // add the new word into word-list
    fun addNewWord(context: Context, item: WordItem) {
        mWordList.add(item)

        // serial out the words
        val serialOutFile = File(context.filesDir,
            SERIALIZATION_WORDS_FILENAME
        )
        if (!serialOutFile.exists())
            serialOutFile.createNewFile()
        val serialOutSuccessOrNot = SerializationManager.save(getWordList(), serialOutFile.path)
        if (!serialOutSuccessOrNot)
            Log.e("add-new-word", "serial-out failed")
    }

    // load all words by serialization
    fun loadAllWordsBySerialization(context: Context, clearFirst: Boolean) {
        if (clearFirst)
            mWordList.clear()

        // load from the serialized file
        val serialOutFile = File(context.filesDir,
            SERIALIZATION_WORDS_FILENAME
        )
        if (!serialOutFile.exists())
            return
        val loaded = SerializationManager.load<ArrayList<WordItem> >(serialOutFile.path)
        mWordList.addAll(loaded ?: arrayListOf())
    }

    // get the word-list
    private fun getWordList(): ArrayList<WordItem> = mWordList

    // set adapter on recv
    fun setAdapterOnWordRecyclerView(context: Context, recv: RecyclerView) {
        val adapter = WordItemAdapter(getWordList(), context)
        recv.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}