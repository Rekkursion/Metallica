package com.rekkursion.metallica.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import com.rekkursion.metallica.R
import com.rekkursion.metallica.model.WordItem

class SpeechAndMeaningWhenAddingView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    private val mContext: Context = context

    private lateinit var txtvSpeechAndMeaning: TextView
    private lateinit var spnSpeech: AppCompatSpinner
    private lateinit var edtMeaning: EditText

    constructor(context: Context, attrs: AttributeSet? = null, labelName: String): this(context, attrs) {
        txtvSpeechAndMeaning.text = labelName
    }

    init {
        LayoutInflater.from(mContext).inflate(R.layout.layout_speech_and_meaning_when_adding, this)
        initViews()
        initEvents()
    }

    private fun initViews() {
        txtvSpeechAndMeaning = findViewById(R.id.txtv_part_of_speech_and_chinese_meaning_when_adding)
        spnSpeech = findViewById(R.id.spn_part_of_speech_when_adding)
        edtMeaning = findViewById(R.id.edt_chinese_meaning_when_adding)
    }

    private fun initEvents() {
        // set adapter on speech-selecting spinner
        spnSpeech.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, WordItem.PartOfSpeech.values().map { it.abbr })
    }
}