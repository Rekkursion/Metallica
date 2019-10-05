package com.rekkursion.metallica.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatSpinner
import com.rekkursion.metallica.R
import com.rekkursion.metallica.manager.ClassificationManager
import com.rekkursion.metallica.manager.WordsManager
import com.rekkursion.metallica.model.ClassificationItem
import com.rekkursion.metallica.model.WordItem
import com.rekkursion.metallica.view.ClassificationEntryWhenAddingWordView
import com.rekkursion.metallica.view.SpeechAndMeaningWhenAddingView
import java.util.*
import kotlin.collections.ArrayList


class WordAddingActivity: AppCompatActivity(), View.OnClickListener {
    companion object {
        var oldWord: WordItem? = null
    }

    private val mIsNewWord: Boolean = oldWord == null

    private lateinit var mEdtEnglishWord: EditText
    private lateinit var mEdtRemark: EditText
    private lateinit var mSkbDifficulty: AppCompatSeekBar
    private lateinit var mTxtvShowSelectedDifficulty: TextView

    private lateinit var mLlySpeechesAndMeaningsContainer: LinearLayout
    private lateinit var mBtnAddNewSpeechAndMeaning: Button

    private lateinit var mLlySelectedClassificationsContainer: LinearLayout
    private lateinit var mBtnSelectClassifications: Button
    private lateinit var mBtnAddNewClassification: Button
    private var mSelectedClassificationTreeSet = TreeSet<ClassificationItem>()

    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSubmit: Button

    // listener of changing the word's difficulty
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
        // init-data 必須放在 init-events 後面
        initData()
    }

    // btn-cancel and btn-submit allowed only
    override fun onClick(view: View?) {
        // illegal buttons clicked, return directly
        if (view?.id != R.id.btn_cancel_when_adding_word && view?.id != R.id.btn_submit_when_adding_word)
            return

        // if submitting
        if (view.id == R.id.btn_submit_when_adding_word) {
            // region build the speeches and meanings lists
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
            // endregion

            // 沒有加入任何分類，則加入「未分類」分類
            if (mSelectedClassificationTreeSet.isEmpty()) {
                // 還沒有「未分類」分類，新增「未分類」分類
                if (ClassificationManager.getClassificationGroupNames().none { it == getString(R.string.str_unclassified) })
                    ClassificationManager.addNewClassification(this, ClassificationItem(getString(R.string.str_unclassified), arrayListOf()))
                // 將單字加入「未分類」分類
                val unclassifiedClassification = ClassificationManager.getClassificationByGroupName(getString(R.string.str_unclassified))
                unclassifiedClassification?.let { mSelectedClassificationTreeSet.add(it) }
            }

            // region 建立新的 word-item
            val newWordItem = WordItem(
                mEdtEnglishWord.text.toString(),
                speechList,
                meaningList,
                mEdtRemark.text.toString(),
                mSkbDifficulty.progress,
                mSelectedClassificationTreeSet.toCollection(ArrayList())
            )
            // endregion

            // 編輯舊單字
            if (!mIsNewWord && oldWord != null && oldWord?.getClassificationList() != null && oldWord?.getClassificationList()?.size!! > 0) {
                val classification = oldWord!!.getClassificationList()!![0]
                ClassificationManager.editWord(this, classification, oldWord!!, newWordItem)
            }

            // 新增新單字
            else if (mIsNewWord) {
                // add the word into the selected classifications
                mSelectedClassificationTreeSet.forEach {
                    ClassificationManager.addWordIntoClassification(this, it, newWordItem)
                }
            }
        }

        // set result and finish
        val dataIntent = Intent()
        dataIntent.putExtra(WordsManager.NEW_WORD_OR_EDITED_WORD_FIELD, mEdtEnglishWord.text.toString())
        setResult(if (view.id == R.id.btn_cancel_when_adding_word) Activity.RESULT_CANCELED else Activity.RESULT_OK, dataIntent)
        finish()
    }

    private fun initViews() {
        mEdtEnglishWord = findViewById(R.id.edt_english_word_when_adding_word)
        mEdtRemark = findViewById(R.id.edt_remark_when_adding_word)
        mSkbDifficulty = findViewById(R.id.skb_difficulty_when_adding_word)
        mTxtvShowSelectedDifficulty = findViewById(R.id.txtv_show_selected_difficulty_when_adding_word)

        mLlySpeechesAndMeaningsContainer = findViewById(R.id.lly_speeches_and_meanings_container_when_adding_word)
        mBtnAddNewSpeechAndMeaning = findViewById(R.id.btn_add_new_speech_and_meaning_when_adding_word)

        mLlySelectedClassificationsContainer = findViewById(R.id.lly_added_classifications_container_when_adding_word)
        mBtnSelectClassifications = findViewById(R.id.btn_select_classifications_when_adding_word)
        mBtnAddNewClassification = findViewById(R.id.btn_add_new_classification_when_adding_word)

        mBtnCancel = findViewById(R.id.btn_cancel_when_adding_word)
        mBtnSubmit = findViewById(R.id.btn_submit_when_adding_word)
    }

    private fun initData() {
        // is editing the old word
        if (!mIsNewWord) {
            // word name (english)
            mEdtEnglishWord.setText(oldWord?.getEnglishWord() ?: "")

            // remarks
            mEdtRemark.setText(oldWord?.getRemark() ?: "")

            // difficulty
            mSkbDifficulty.progress = oldWord?.getDifficulty() ?: 1

            // region part-of-speeches and meanings
            val numOfMeanings = oldWord?.getChineseMeaningList()?.size ?: 0
            val numOfSpeechAndMeaningWhenAddingViews = mLlySpeechesAndMeaningsContainer.childCount
            var noViewExistsYet = numOfSpeechAndMeaningWhenAddingViews == 0

            for (counter in 0 until numOfMeanings) {
                val speech = oldWord?.getPartOfSpeechList()?.get(counter)
                val meaning = oldWord?.getChineseMeaningList()?.get(counter)

                // speech-and-meaning-when-adding-view 已經存在，修改 UI 的值
                if (counter < numOfSpeechAndMeaningWhenAddingViews) {
                    val speechAndMeaningWhenAddingView = mLlySpeechesAndMeaningsContainer.getChildAt(counter) as SpeechAndMeaningWhenAddingView
                    speech?.let { it_speech ->
                        meaning?.let { it_meaning ->
                            speechAndMeaningWhenAddingView.setSpeechAndMeaning(it_speech, it_meaning)
                        }
                    }
                }

                // 沒有了，需要新增
                else {
                    if (noViewExistsYet) {
                        addNewSpeechAndMeaningWhenAddingView(this.getString(R.string.str_part_of_speech_and_chinese_meaning), false, speech, meaning)
                        noViewExistsYet = false
                    }
                    else
                        addNewSpeechAndMeaningWhenAddingView("", true, speech, meaning)
                }
            }
            // endregion

            // region classification
            oldWord?.getClassificationList()
                ?.filter { it.getGroupName() != getString(R.string.str_unclassified) }
                ?.forEach { addNewClassificationView(it.getGroupName()) }
            // endregion
        }
    }

    private fun initEvents() {
        // cancel & submit
        mBtnCancel.setOnClickListener(this)
        mBtnSubmit.setOnClickListener(this)

        // add the first speech-and-meaning-view
        addNewSpeechAndMeaningWhenAddingView(this.getString(R.string.str_part_of_speech_and_chinese_meaning), false)

        // event of adding new speech and meaning
        mBtnAddNewSpeechAndMeaning.setOnClickListener {
            addNewSpeechAndMeaningWhenAddingView("", true)
        }

        // seek-bar of difficulty changed
        mSkbDifficulty.setOnSeekBarChangeListener(mOnDifficultySeekBarChangeListener)

        // select classifications
        mBtnSelectClassifications.setOnClickListener {
            val groupNameArray = ClassificationManager.getClassificationGroupNames().filter {
                it != getString(R.string.str_unclassified)
            }.toTypedArray()
//            val groupNameIsCheckedArray = BooleanArray(groupNameArray.size) { idx ->
//                mSelectedClassificationTreeSet.contains(ClassificationManager.getClassificationByGroupName(groupNameArray[idx]))
//            }
            val initialCheckedGroupNameIndex =
                    if (mSelectedClassificationTreeSet.isEmpty()) 0
                    else {
                        val checkedGroupName = mSelectedClassificationTreeSet.first().getGroupName()
                        groupNameArray.indexOf(checkedGroupName)
                    }
            var checkedGroupNameIndex = initialCheckedGroupNameIndex

            // region build and show dialog
            val builder =
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.str_select_classifications_when_adding_word))
            if (groupNameArray.isEmpty())
                builder
                    .setMessage(getString(R.string.str_no_classification_yet))
                    .setPositiveButton(getString(R.string.str_confirm), null)
            else
                builder
                    .setSingleChoiceItems(groupNameArray, initialCheckedGroupNameIndex) { _, position ->
                        checkedGroupNameIndex = position
                    }
//                    .setMultiChoiceItems(groupNameArray, groupNameIsCheckedArray) { _, position, isChecked ->
//                        groupNameIsCheckedArray[position] = isChecked
//                    }
                    .setPositiveButton(this.getString(R.string.str_submit)) { _, _ ->
                        // 新增 classification-entry view
                        addNewClassificationView(groupNameArray[checkedGroupNameIndex])
                    }
                    .setNegativeButton(this.getString(R.string.str_cancel), null)
            builder.create().show()
            // endregion
        }

        // add new classification
        mBtnAddNewClassification.setOnClickListener {
            // TODO: add new classification when adding new word
        }
    }

    // 新增一個用來「新增詞性及意義」的 view
    private fun addNewSpeechAndMeaningWhenAddingView(labelName: String, showDeleteButton: Boolean, speech: WordItem.PartOfSpeech? = null, meaning: String? = null) {
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

        val speechAndMeaningView = SpeechAndMeaningWhenAddingView(this, labelName = labelName, showDeleteButton = showDeleteButton)
        speechAndMeaningView.setOnDeleteViewButtonClickListener(onDeleteSpeechAndMeaningWhenAddingViewListener)

        if (speech != null && meaning != null)
            speechAndMeaningView.setSpeechAndMeaning(speech, meaning)

        mLlySpeechesAndMeaningsContainer.addView(speechAndMeaningView)
    }

    // 新增分類的 entry view
    private fun addNewClassificationView(selectedGroupName: String) {
        // remove all items and views
        mSelectedClassificationTreeSet.clear()
        mLlySelectedClassificationsContainer.removeAllViews()

        val classificationItem = ClassificationManager.getClassificationByGroupName(selectedGroupName)
        classificationItem?.let {
            val classificationEntryView = ClassificationEntryWhenAddingWordView(this, groupName = selectedGroupName)

            classificationEntryView.setOnDeleteViewButtonClickListener(object: ClassificationEntryWhenAddingWordView.OnDeleteViewButtonClickListener {
                override fun onDeleteViewButtonClick(objectIndex: Int) {
                    mSelectedClassificationTreeSet.remove(it)
                    mLlySelectedClassificationsContainer.removeView(classificationEntryView)
                }
            })

            mSelectedClassificationTreeSet.add(it)
            mLlySelectedClassificationsContainer.addView(classificationEntryView)
        }

        // region add views which are checked
//                        groupNameIsCheckedArray.forEachIndexed { index, isChecked ->
//                            if (isChecked) {
//                                val classificationItem = ClassificationManager.getClassificationByGroupName(groupNameArray[index])
//
//                                classificationItem?.let {
//                                    val groupName = it.getGroupName()
//                                    val classificationEntryView = ClassificationEntryWhenAddingWordView(this, groupName = groupName)
//
//                                    classificationEntryView.setOnDeleteViewButtonClickListener(object: ClassificationEntryWhenAddingWordView.OnDeleteViewButtonClickListener {
//                                        override fun onDeleteViewButtonClick(objectIndex: Int) {
//                                            mSelectedClassificationTreeSet.remove(it)
//                                            mLlySelectedClassificationsContainer.removeView(classificationEntryView)
//                                        }
//                                    })
//
//                                    mSelectedClassificationTreeSet.add(it)
//                                    mLlySelectedClassificationsContainer.addView(classificationEntryView)
//                                }
//                            }
//                        }
        // endregion
    }
}
