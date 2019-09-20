package com.rekkursion.metallica.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatSpinner
import com.rekkursion.metallica.R
import com.rekkursion.metallica.WordsManager
import com.rekkursion.metallica.model.WordItem
import com.rekkursion.metallica.view.SpeechAndMeaningWhenAddingView


class WordAddingActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var mEdtEnglishWord: EditText
    private lateinit var mEdtRemark: EditText

    private lateinit var mLlySpeechesAndMeaningsContainer: LinearLayout
    private lateinit var mBtnAddNewSpeechAndMeaning: Button

    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_adding)

        initViews()
        initEvents()
    }

    override fun onClick(view: View?) {
        // illegal buttons clicked, return directly
        if (view?.id != R.id.btn_cancel_when_adding && view?.id != R.id.btn_submit_when_adding)
            return

        // if submitting
        if (view.id == R.id.btn_submit_when_adding) {
            // build the speeches and meanings lists
            val speechList = ArrayList<WordItem.PartOfSpeech>()
            val meaningList = ArrayList<String>()

            // get speeches and meanings
            val cnt = mLlySpeechesAndMeaningsContainer.childCount
            var k = 0
            while (k < cnt) {
                val child = mLlySpeechesAndMeaningsContainer.getChildAt(k)
                val spnSpeech: AppCompatSpinner = child.findViewById(R.id.spn_part_of_speech_when_adding)
                val edtMeaning: EditText = child.findViewById(R.id.edt_chinese_meaning_when_adding)

                speechList.add(WordItem.PartOfSpeech.values()[spnSpeech.selectedItemPosition])
                meaningList.add(edtMeaning.text.toString())

                ++k
            }

            // add the word by words-manager
            WordsManager.addNewWord(this, WordItem(
                mEdtEnglishWord.text.toString(),
                speechList,
                meaningList,
                mEdtRemark.text.toString()
            ))
        }

        // set result and finish
        val dataIntent = Intent()
        dataIntent.putExtra(WordsManager.NEW_WORD_FIELD, mEdtEnglishWord.text.toString())
        setResult(if (view.id == R.id.btn_cancel_when_adding) Activity.RESULT_CANCELED else Activity.RESULT_OK, dataIntent)
        finish()
    }

    private fun initViews() {
        mEdtEnglishWord = findViewById(R.id.edt_english_word_when_adding)
        mEdtRemark = findViewById(R.id.edt_remark_when_adding)

        mLlySpeechesAndMeaningsContainer = findViewById(R.id.lly_speeches_and_meanings_container)
        mBtnAddNewSpeechAndMeaning = findViewById(R.id.btn_add_new_speech_and_meaning)

        mBtnCancel = findViewById(R.id.btn_cancel_when_adding)
        mBtnSubmit = findViewById(R.id.btn_submit_when_adding)
    }

    private fun initEvents() {
        // cancel & submit
        mBtnCancel.setOnClickListener(this)
        mBtnSubmit.setOnClickListener(this)

        // add the first speech-and-meaning-view
        val firstSpeechAndMeaningView = SpeechAndMeaningWhenAddingView(this)
        mLlySpeechesAndMeaningsContainer.addView(firstSpeechAndMeaningView)

        // event of adding new speech and meaning
        mBtnAddNewSpeechAndMeaning.setOnClickListener {
            val speechAndMeaningView = SpeechAndMeaningWhenAddingView(this, labelName = "")
            mLlySpeechesAndMeaningsContainer.addView(speechAndMeaningView)
        }
    }

    private fun getAllSpeechesList(): List<String> = WordItem.PartOfSpeech.values().map { it.abbr }
}
