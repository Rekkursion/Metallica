package com.rekkursion.metallica.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
import com.rekkursion.metallica.R
import com.rekkursion.metallica.WordsManager
import com.rekkursion.metallica.model.WordItem


class WordAddingActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var mEdtEnglishWord: EditText
    private lateinit var mSpnPartOfSpeech: AppCompatSpinner
    private lateinit var mEdtChineseMeaning: EditText
    private lateinit var mEdtRemark: EditText

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
            // add the word by words-manager
            WordsManager.addNewWord(this, WordItem(
                mEdtEnglishWord.text.toString(),
                WordItem.PartOfSpeech.values()[mSpnPartOfSpeech.selectedItemPosition],
                mEdtChineseMeaning.text.toString(),
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
        mSpnPartOfSpeech = findViewById(R.id.spn_part_of_speech_when_adding)
        mEdtChineseMeaning = findViewById(R.id.edt_chinese_meaning_when_adding)
        mEdtRemark = findViewById(R.id.edt_remark_when_adding)

        mBtnCancel = findViewById(R.id.btn_cancel_when_adding)
        mBtnSubmit = findViewById(R.id.btn_submit_when_adding)
    }

    private fun initEvents() {
        // set the adapter on speeches spinner
        mSpnPartOfSpeech.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getAllSpeechesList())

        // cancel & submit
        mBtnCancel.setOnClickListener(this)
        mBtnSubmit.setOnClickListener(this)
    }

    private fun getAllSpeechesList(): List<String> = WordItem.PartOfSpeech.values().map { it.abbr }
}
