package com.rekkursion.metallica.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.rekkursion.metallica.R
import com.rekkursion.metallica.model.ClassificationItem
import android.widget.ArrayAdapter
import com.rekkursion.metallica.manager.ClassificationManager


class ClassificationDeletingDialog(context: Context, classification: ClassificationItem, titleText: String, messageText: String = ""): Dialog(context) {
    enum class WordsDeletingMethod {
        MOVE_TO_UNCLASSIFIED,
        MOVE_TO_ANOTHER_CLASSIFICATION,
        DELETE_PERMANENTLY
    }

    private var mContext: Context = context

    private var mClassificationItem: ClassificationItem = classification
    private var mTitleText: String = titleText
    private var mMessageText: String = messageText

    private var mOnCancelClickListener: OnCancelClickListener? = null
    private var mOnSubmitClickListener: OnSubmitClickListener? = null

    private lateinit var mTxtvTitle: TextView
    private lateinit var mTxtvMessage: TextView

    private lateinit var mRdgWordsDeletingMethods: RadioGroup
    private lateinit var mRdbMoveToUnclassified: RadioButton
    private lateinit var mRdbMoveToAnotherClassification: RadioButton
    private lateinit var mRdbDeletePermanently: RadioButton

    private lateinit var mSpnChooseClassificationToMoveTo: AppCompatSpinner

    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_classification_deleting_dialog)

        setCanceledOnTouchOutside(true)

        initViews()
        initEvents()
    }

    private fun initViews() {
        mTxtvTitle = findViewById(R.id.txtv_title_at_classification_deleting_dialog)
        mTxtvMessage = findViewById(R.id.txtv_message_at_classification_deleting_dialog)

        mRdgWordsDeletingMethods = findViewById(R.id.rdg_words_deleting_methods_at_classification_deleting_dialog)
        mRdbMoveToUnclassified = findViewById(R.id.rdb_words_deleting_methods_move_to_unclassified_at_classification_deleting_dialog)
        mRdbMoveToAnotherClassification = findViewById(R.id.rdb_words_deleting_methods_move_to_another_classification_at_classification_deleting_dialog)
        mRdbDeletePermanently = findViewById(R.id.rdb_words_deleting_methods_delete_permanently_at_classification_deleting_dialog)

        mSpnChooseClassificationToMoveTo = findViewById(R.id.spn_choose_classification_to_move_to_at_classification_deleting_dialog)

        mBtnCancel = findViewById(R.id.btn_cancel_at_classification_deleting_dialog)
        mBtnSubmit = findViewById(R.id.btn_submit_at_classification_deleting_dialog)
    }

    private fun initEvents() {
        // region listeners of cancel & submit buttons
        // cancel
        mBtnCancel.setOnClickListener {
            mOnCancelClickListener?.onCancelClick()
            dismiss()
        }

        // submit
        mBtnSubmit.setOnClickListener {
            val method = when {
                mRdbMoveToUnclassified.isChecked -> WordsDeletingMethod.MOVE_TO_UNCLASSIFIED
                mRdbMoveToAnotherClassification.isChecked -> WordsDeletingMethod.MOVE_TO_ANOTHER_CLASSIFICATION
                else -> WordsDeletingMethod.DELETE_PERMANENTLY
            }

            val moveToWhere: String? =
                if (mRdbMoveToAnotherClassification.isChecked) mSpnChooseClassificationToMoveTo.selectedItem.toString()
                else null

            mOnSubmitClickListener?.onSubmitClick(method, moveToWhere)
            dismiss()
        }
        // endregion

        // set spinner's items
        val spinnerArrayAdapter = ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, ClassificationManager.getClassificationGroupNames().filter { it != mClassificationItem.getGroupName() && it != mContext.getString(R.string.str_unclassified) })
        mSpnChooseClassificationToMoveTo.adapter = spinnerArrayAdapter

        // listener of radio-group to control the checked changes
        mRdgWordsDeletingMethods.setOnCheckedChangeListener { _, checkedId ->
            mSpnChooseClassificationToMoveTo.visibility =
                if (checkedId == mRdbMoveToAnotherClassification.id) View.VISIBLE
                else View.GONE
        }

        // region set texts of title and message
        mTxtvTitle.text = mTitleText
        mTxtvMessage.text = mMessageText

        if (mTxtvTitle.text.isEmpty())
            mTxtvTitle.visibility = View.GONE
        if (mTxtvMessage.text.isEmpty())
            mTxtvMessage.visibility = View.GONE
        // endregion

        // 要刪除的分類是「未分類」
        if (mClassificationItem.getGroupName() == mContext.getString(R.string.str_unclassified)) {
            mRdbMoveToUnclassified.visibility = View.GONE
            mRdbMoveToUnclassified.isChecked = false
            mRdbMoveToAnotherClassification.isChecked = true

            mSpnChooseClassificationToMoveTo.visibility = View.VISIBLE
        }
    }

    // set cancel listener
    fun setOnCancelClickListener(onCancelClickListener: OnCancelClickListener): ClassificationDeletingDialog {
        mOnCancelClickListener = onCancelClickListener
        return this
    }

    // set submit listener
    fun setOnSubmitClickListener(onSubmitClickListener: OnSubmitClickListener): ClassificationDeletingDialog {
        mOnSubmitClickListener = onSubmitClickListener
        return this
    }

    // interface of cancelling
    interface OnCancelClickListener {
        fun onCancelClick()
    }

    // interface of submitting
    interface OnSubmitClickListener {
        fun onSubmitClick(wordsDeletingMethod: WordsDeletingMethod, moveToWhere: String? = null)
    }
}