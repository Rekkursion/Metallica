package com.rekkursion.metallica.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatSpinner
import com.rekkursion.metallica.R
import com.rekkursion.metallica.manager.WordsManager
import com.rekkursion.metallica.model.WordItem
import com.rekkursion.metallica.view.SpeechAndMeaningWhenAddingView


class WordAddingActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var mEdtEnglishWord: EditText
    private lateinit var mEdtRemark: EditText
    private lateinit var mSkbDifficulty: AppCompatSeekBar
    private lateinit var mTxtvShowSelectedDifficulty: TextView

    private lateinit var mLlySpeechesAndMeaningsContainer: LinearLayout
    private lateinit var mBtnAddNewSpeechAndMeaning: Button

    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSubmit: Button

    private val mOnDifficultySeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, isFromUser: Boolean) {
            mTxtvShowSelectedDifficulty.text =
                    when (progress) {
                        0 -> this@WordAddingActivity.getString(R.string.str_difficulty_0star)
                        1 -> this@WordAddingActivity.getString(R.string.str_difficulty_1star)
                        2 -> this@WordAddingActivity.getString(R.string.str_difficulty_2stars)
                        3 -> this@WordAddingActivity.getString(R.string.str_difficulty_3stars)
                        4 -> this@WordAddingActivity.getString(R.string.str_difficulty_4stars)
                        else -> this@WordAddingActivity.getString(R.string.str_difficulty_5stars)
                    }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

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

            // region add the word by words-manager
            WordsManager.addNewWord(this, WordItem(
                mEdtEnglishWord.text.toString(),
                speechList,
                meaningList,
                mEdtRemark.text.toString(),
                mSkbDifficulty.progress
            ))
            // endregion
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
        mSkbDifficulty = findViewById(R.id.skb_difficulty_when_adding)
        mTxtvShowSelectedDifficulty = findViewById(R.id.txtv_show_selected_difficulty_when_adding)

        mLlySpeechesAndMeaningsContainer = findViewById(R.id.lly_speeches_and_meanings_container_when_adding_word)
        mBtnAddNewSpeechAndMeaning = findViewById(R.id.btn_add_new_speech_and_meaning)

        mBtnCancel = findViewById(R.id.btn_cancel_when_adding)
        mBtnSubmit = findViewById(R.id.btn_submit_when_adding)
    }

    private fun initEvents() {
        // cancel & submit
        mBtnCancel.setOnClickListener(this)
        mBtnSubmit.setOnClickListener(this)

        // listener of deleting the speech-and-meaning view
        val onDeleteSpeechAndMeaningWhenAddingViewListener = object: SpeechAndMeaningWhenAddingView.OnDeleteViewButtonClickListener {
            override fun onDeleteViewButtonClick(objectIndex: Int) {
                var k = 0
                while (k < mLlySpeechesAndMeaningsContainer.childCount) {
                    if ((mLlySpeechesAndMeaningsContainer.getChildAt(k) as SpeechAndMeaningWhenAddingView).getObjectIndex() == objectIndex) {
                        mLlySpeechesAndMeaningsContainer.removeViewAt(k)
                        break
                    }
                    ++k
                }
            }
        }

        // add the first speech-and-meaning-view
        val firstSpeechAndMeaningView = SpeechAndMeaningWhenAddingView(this, labelName = this.getString(R.string.str_part_of_speech_and_chinese_meaning), showDeleteButton = false)
        firstSpeechAndMeaningView.setOnDeleteViewButtonClickListener(onDeleteSpeechAndMeaningWhenAddingViewListener)
        mLlySpeechesAndMeaningsContainer.addView(firstSpeechAndMeaningView)

        // event of adding new speech and meaning
        mBtnAddNewSpeechAndMeaning.setOnClickListener {
            val speechAndMeaningView = SpeechAndMeaningWhenAddingView(this, labelName = "")
            speechAndMeaningView.setOnDeleteViewButtonClickListener(onDeleteSpeechAndMeaningWhenAddingViewListener)
            mLlySpeechesAndMeaningsContainer.addView(speechAndMeaningView)
        }

        // seek-bar of difficulty changed
        mSkbDifficulty.setOnSeekBarChangeListener(mOnDifficultySeekBarChangeListener)
    }
}
