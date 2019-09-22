package com.rekkursion.metallica.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import com.rekkursion.metallica.R

class ClassificationEntryWhenAddingWordView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    private val mContext: Context = context

    private lateinit var mTxtvGroupName: TextView
    private lateinit var mImgbtnDeleteGroupName: AppCompatImageButton

    private var mOnDeleteViewButtonClickListener: OnDeleteViewButtonClickListener? = null
    private val mObjectIndex: Int = mObjectCount++
    companion object {
        private var mObjectCount: Int = 0
    }

    init {
        LayoutInflater.from(mContext).inflate(R.layout.layout_classification_entry_when_adding_word, this)
        initViews()
        initEvents()
    }

    constructor(context: Context, attrs: AttributeSet? = null, groupName: String): this(context, attrs) {
        mTxtvGroupName.text = groupName
    }

    private fun initViews() {
        mTxtvGroupName = findViewById(R.id.txtv_classification_group_name_when_adding_word)
        mImgbtnDeleteGroupName = findViewById(R.id.imgbtn_delete_classification_group_name_when_adding_word)
    }

    private fun initEvents() {
        // delete the view
        mImgbtnDeleteGroupName.setOnClickListener { mOnDeleteViewButtonClickListener?.onDeleteViewButtonClick(mObjectIndex) }
    }

    fun getObjectIndex(): Int = mObjectIndex

    fun setOnDeleteViewButtonClickListener(listener: OnDeleteViewButtonClickListener) {
        mOnDeleteViewButtonClickListener = listener
    }

    interface OnDeleteViewButtonClickListener {
        fun onDeleteViewButtonClick(objectIndex: Int)
    }
}