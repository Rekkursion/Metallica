package com.rekkursion.metallica.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.rekkursion.metallica.R
import com.rekkursion.metallica.manager.ClassificationManager
import com.rekkursion.metallica.model.ClassificationItem

class ClassificationAddingActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var mEdtGroupName: EditText

    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classification_adding)

        initViews()
        initEvents()
    }

    override fun onClick(view: View?) {
        // illegal buttons clicked, return directly
        if (view?.id != R.id.btn_cancel_when_adding_classification && view?.id != R.id.btn_submit_when_adding_classification)
            return

        // if submitting
        if (view.id == R.id.btn_submit_when_adding_classification) {
            // region add the classification by classification-manager
            ClassificationManager.addNewClassification(this, ClassificationItem(
                mEdtGroupName.text.toString()
            ))
            // endregion
        }

        // set result and finish
        val dataIntent = Intent()
        dataIntent.putExtra(ClassificationManager.NEW_CLASSIFICATION_FIELD, mEdtGroupName.text.toString())
        setResult(if (view.id == R.id.btn_cancel_when_adding_classification) Activity.RESULT_CANCELED else Activity.RESULT_OK, dataIntent)
        finish()
    }

    private fun initViews() {
        mEdtGroupName = findViewById(R.id.edt_group_name_when_adding_classification)

        mBtnCancel = findViewById(R.id.btn_cancel_when_adding_classification)
        mBtnSubmit = findViewById(R.id.btn_submit_when_adding_classification)
    }

    private fun initEvents() {
        // cancel & submit
        mBtnCancel.setOnClickListener(this)
        mBtnSubmit.setOnClickListener(this)
    }
}
