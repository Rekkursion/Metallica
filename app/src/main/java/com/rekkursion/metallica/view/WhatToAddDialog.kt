package com.rekkursion.metallica.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.rekkursion.metallica.R

class WhatToAddDialog(context: Context): Dialog(context) {
    private var mContext: Context = context

    private var mOnAddNewClassificationListener: OnAddNewClassificationListener? = null
    private var mOnAddNewWordListener: OnAddNewWordListener? = null

    private lateinit var mBtnAddNewClassification: Button
    private lateinit var mBtnAddNewWord: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_what_to_add_dialog)

        setCanceledOnTouchOutside(true)

        initViews()
        initEvents()
    }

    private fun initViews() {
        mBtnAddNewClassification = findViewById(R.id.btn_add_new_classification)
        mBtnAddNewWord = findViewById(R.id.btn_add_new_word)
    }

    private fun initEvents() {
        // user intents to add new classification
        mBtnAddNewClassification.setOnClickListener {
            mOnAddNewClassificationListener?.onAddNewClassificationClick()
            dismiss()
        }

        // user intents to add new word
        mBtnAddNewWord.setOnClickListener {
            mOnAddNewWordListener?.onAddNewWordClick()
            dismiss()
        }
    }

    // set add-new-classification listener
    fun setOnAddNewClassificationListener(onAddNewClassificationListener: OnAddNewClassificationListener): WhatToAddDialog {
        mOnAddNewClassificationListener = onAddNewClassificationListener
        return this
    }

    // set add-new-word listener
    fun setOnAddNewWordListener(onAddNewWordListener: OnAddNewWordListener): WhatToAddDialog {
        mOnAddNewWordListener = onAddNewWordListener
        return this
    }

    // interface of add-new-classification
    interface OnAddNewClassificationListener {
        fun onAddNewClassificationClick()
    }

    // interface of add-new-word
    interface OnAddNewWordListener {
        fun onAddNewWordClick()
    }
}