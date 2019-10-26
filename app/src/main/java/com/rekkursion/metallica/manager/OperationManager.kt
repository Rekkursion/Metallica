package com.rekkursion.metallica.manager

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rekkursion.metallica.R
import com.rekkursion.metallica.activity.WordAddingActivity
import com.rekkursion.metallica.fragment.WordListFragment
import com.rekkursion.metallica.model.ClassificationItem
import com.rekkursion.metallica.model.WordItem
import com.rekkursion.metallica.view.ClassificationDeletingDialog
import java.lang.StringBuilder

object OperationManager {
    enum class OperatedObject {
        WORD,
        CLASSIFICATION
    }

    // 當使用者長按某個列表元素，則跳出操作該元素的 dialog。
    fun buildItemOperatingDialog(fragment: Fragment, recv: RecyclerView, operatedObject: OperatedObject, classificationGroupNameOrWordName: String, classificationForDeletingWord: ClassificationItem? = null, wordPositionForDeletingWord: Int? = null) {
        val context = fragment.context!!
        val operationArray =
                if (operatedObject == OperationManager.OperatedObject.WORD)
                    context.resources.getStringArray(R.array.strarr_word_operations)
                else {
                    if (classificationGroupNameOrWordName == context.getString(R.string.str_unclassified))
                        context.resources.getStringArray(R.array.strarr_classification_operations)
                            .filter { it != "重新命名" }.toTypedArray()
                    else
                        context.resources.getStringArray(R.array.strarr_classification_operations)
                }

        AlertDialog.Builder(context)
            .setTitle(classificationGroupNameOrWordName)
            .setItems(operationArray) { _, position ->
                when (operationArray[position]) {
                    // region 分類的操作
                    "進入分類" -> enterClassification(fragment, classificationGroupNameOrWordName)
                    "分類信息" -> ClassificationManager.getClassificationByGroupName(classificationGroupNameOrWordName)?.let {
                        buildInformationDialog(fragment.context!!, it)
                    }
                    "重新命名" -> ClassificationManager.getClassificationByGroupName(classificationGroupNameOrWordName)?.let {
                        buildRenamingDialog(fragment.context!!, it, recv)
                    }
                    "刪除分類" -> ClassificationManager.getClassificationByGroupName(classificationGroupNameOrWordName)?.let {
                        buildDeletingDialog(fragment.context!!, it, recv)
                    }
                    // endregion

                    // region 單字的操作
                    "詳細內容" -> {

                    }
                    "編輯單字" -> {
                        classificationForDeletingWord?.let { classification ->
                            wordPositionForDeletingWord?.let { position ->
                                goToWordAddingActivityForEditingTheWord(fragment, classification, position)
                            }
                        }
                    }
                    "刪除單字" -> {
                        classificationForDeletingWord?.let { classification ->
                            wordPositionForDeletingWord?.let { position ->
                                buildDeletingDialog(context, classification, position, recv)
                            }
                        }
                    }
                    // endregion
                }

            }
            .create()
            .show()
    }

    // 分類操作：進入分類
    fun enterClassification(fragment: Fragment, classificationGroupName: String) {
        val wordListFragment = WordListFragment.newInstance(classificationGroupName)
        fragment.fragmentManager
            ?.beginTransaction()
            ?.detach(fragment)
            ?.add(R.id.lly_root_at_main, wordListFragment)
            ?.commit()
    }

    // 分類操作：分類信息
    private fun buildInformationDialog(context: Context, classification: ClassificationItem) {
        val sBuf = StringBuilder()
        sBuf.append("建立於 ", classification.getLocalDateTimeStr(), "\n")
        sBuf.append("內有 ", classification.getWordList().size, " 個單字\n")

        AlertDialog.Builder(context)
            .setTitle(classification.getGroupName())
            .setMessage(sBuf.toString())
            .setPositiveButton(context.getString(R.string.str_confirm), null)
            .create()
            .show()
    }

    // 分類操作：重新命名
    private fun buildRenamingDialog(context: Context, classification: ClassificationItem, recv: RecyclerView) {
        // input-method-manager for showing the keyboard automatically
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        // edit-text for entering new group name
        val edtNewGroupName = EditText(context)

        // dialog
        val dialog = AlertDialog.Builder(context)
            .setTitle("原分類名：${classification.getGroupName()}")
            .setView(edtNewGroupName)
            .setPositiveButton(context.getString(R.string.str_submit), null)
            .setNegativeButton(context.getString(R.string.str_cancel), null)
            .create()
        dialog.setOnShowListener {
            // submit
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val trimmed = edtNewGroupName.text.trim(' ').toString()

                if (trimmed.isEmpty()) {
                    Snackbar.make(edtNewGroupName, "分類名稱不可以為空。", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (trimmed == context.getString(R.string.str_unclassified)) {
                    Snackbar.make(edtNewGroupName, "分類名稱不可以為「${context.getString(R.string.str_unclassified)}」。", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (trimmed == classification.getGroupName()) {
                    Snackbar.make(edtNewGroupName, "分類名稱沒有變化。", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (ClassificationManager.getClassificationByGroupName(trimmed) != null) {
                    Snackbar.make(edtNewGroupName, "該分類已存在。", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // do renaming
                ClassificationManager.changeClassificationGroupName(context, classification.getGroupName(), trimmed)
                ClassificationManager.setAdapterOnClassificationRecyclerView(context, recv)

                // tell user that renamed successfully through snack-bar
                Snackbar.make(recv, "重新命名成功。", Snackbar.LENGTH_SHORT).show()

                // dismiss the dialog
                inputMethodManager.hideSoftInputFromWindow(edtNewGroupName.windowToken, 0)
                dialog.dismiss()
            }

            // cancel
            val negativeButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setOnClickListener {
                inputMethodManager.hideSoftInputFromWindow(edtNewGroupName.windowToken, 0)
                dialog.dismiss()
            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    // 分類操作：刪除分類
    private fun buildDeletingDialog(context: Context, classification: ClassificationItem, recv: RecyclerView) {
        // dialog
        val dialog = ClassificationDeletingDialog(
            context,
            classification,
            "確定要刪除「${classification.getGroupName()}」嗎？"
        )

        // set submit-listener and show
        dialog
            .setOnSubmitClickListener(object: ClassificationDeletingDialog.OnSubmitClickListener {
                override fun onSubmitClick(wordsDeletingMethod: ClassificationDeletingDialog.WordsDeletingMethod, moveToWhere: String?) {
                    val groupName = classification.getGroupName()

                    ClassificationManager.deleteClassification(context, classification, wordsDeletingMethod, moveToWhere)
                    ClassificationManager.setAdapterOnClassificationRecyclerView(context, recv)

                    // tell user that deleted successfully through snack-bar
                    Snackbar.make(recv, "分類「$groupName」刪除成功。", Snackbar.LENGTH_SHORT).show()
                }
            })
            .show()
    }

    // 單字操作：詳細內容
    private fun goToWordDetailsActivity(fragment: Fragment, classification: ClassificationItem, wordPosition: Int) {
        //val toWordDetailsIntent = Intent(fragment.context, WordAddingActivity::class.java)
        // TODO: word details
    }

    // 單字操作：編輯單字
    private fun goToWordAddingActivityForEditingTheWord(fragment: Fragment, classification: ClassificationItem, wordPosition: Int) {
        val toWordAddingIntent = Intent(fragment.context, WordAddingActivity::class.java)
        WordAddingActivity.oldWord = classification.getWordList()[wordPosition]
        fragment.startActivityForResult(toWordAddingIntent, WordListFragment.RC_TO_WORD_ADDING_ACTIVITY_AT_WORD_LIST)
    }

    // 單字操作：刪除單字
    private fun buildDeletingDialog(context: Context, classification: ClassificationItem, wordPosition: Int, recv: RecyclerView) {
        val wordName = classification.getWordList()[wordPosition].getEnglishWord() ?: return

        // dialog
        AlertDialog.Builder(context)
            .setTitle("即將刪除單字：$wordName")
            .setMessage("確定要永久刪除該單字嗎？此動作將無法復原。")
            .setPositiveButton(context.getString(R.string.str_submit)) { _, _ ->
                ClassificationManager.deleteWordInCertainClassification(context, classification, wordPosition)
                WordsManager.setAdapterOnWordRecyclerView(context, recv, classification.getWordList())

                // tell user that deleted successfully through snack-bar
                Snackbar.make(recv, "單字 $wordName 刪除成功。", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton(context.getString(R.string.str_cancel), null)
            .create()
            .show()
    }
}