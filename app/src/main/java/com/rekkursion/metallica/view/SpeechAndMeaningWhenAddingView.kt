package com.rekkursion.metallica.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.forEach
import com.rekkursion.metallica.R
import com.rekkursion.metallica.model.WordItem

class SpeechAndMeaningWhenAddingView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    private val mContext: Context = context

    private lateinit var mTxtvSpeechAndMeaning: TextView
    private lateinit var mSpnSpeech: AppCompatSpinner
    private lateinit var mEdtMeaning: EditText
    private lateinit var mImgbtnDeleteSpeechAndMeaning: AppCompatImageButton

    private var mOnDeleteViewButtonClickListener: OnDeleteViewButtonClickListener? = null
    private val mObjectIndex: Int = mObjectCount++
    companion object {
        private var mObjectCount: Int = 0
    }

    init {
        LayoutInflater.from(mContext).inflate(R.layout.layout_speech_and_meaning_when_adding, this)
        initViews()
        initEvents()
    }

    constructor(context: Context, attrs: AttributeSet? = null, labelName: String, showDeleteButton: Boolean = true): this(context, attrs) {
        mTxtvSpeechAndMeaning.text = labelName
        if (!showDeleteButton)
            mImgbtnDeleteSpeechAndMeaning.visibility = View.GONE
    }

    private fun initViews() {
        mTxtvSpeechAndMeaning = findViewById(R.id.txtv_part_of_speech_and_chinese_meaning_when_adding)
        mSpnSpeech = findViewById(R.id.spn_part_of_speech_when_adding)
        mEdtMeaning = findViewById(R.id.edt_chinese_meaning_when_adding)
        mImgbtnDeleteSpeechAndMeaning = findViewById(R.id.imgbtn_delete_part_of_speech_and_chinese_meaning_when_adding)
    }

    private fun initEvents() {
        // set adapter on speech-selecting spinner
        mSpnSpeech.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, WordItem.PartOfSpeech.values().map { it.abbr })

        // delete the view
        mImgbtnDeleteSpeechAndMeaning.setOnClickListener { mOnDeleteViewButtonClickListener?.onDeleteViewButtonClick(mObjectIndex) }
    }

    fun getObjectIndex(): Int = mObjectIndex

    fun setSpeechAndMeaning(speech: WordItem.PartOfSpeech, meaning: String) {
        mSpnSpeech.setSelection(WordItem.PartOfSpeech.values().indexOf(speech))
        mEdtMeaning.setText(meaning)
    }

    fun setOnDeleteViewButtonClickListener(listener: OnDeleteViewButtonClickListener) {
        mOnDeleteViewButtonClickListener = listener
    }

    interface OnDeleteViewButtonClickListener {
        fun onDeleteViewButtonClick(objectIndex: Int)
    }
}