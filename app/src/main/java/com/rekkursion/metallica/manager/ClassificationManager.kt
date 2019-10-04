package com.rekkursion.metallica.manager

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.R
import com.rekkursion.metallica.adapter.ClassificationItemAdapter
import com.rekkursion.metallica.model.ClassificationItem
import com.rekkursion.metallica.model.WordItem
import com.rekkursion.metallica.view.ClassificationDeletingDialog
import java.io.File

object ClassificationManager {
    const val NEW_CLASSIFICATION_FIELD: String = "new_classification"

    private const val SERIALIZATION_CLASSIFICATIONS_FILENAME: String = ".metallica_serialization_classifications"

    private val mClassificationList = ArrayList<ClassificationItem>()

    // add the new classification
    fun addNewClassification(context: Context, item: ClassificationItem) {
        mClassificationList.add(item)
        serialOut(context)
    }

    // add the word into certain classification
    fun addWordIntoClassification(context: Context, classification: ClassificationItem, word: WordItem): Boolean {
        mClassificationList.forEach {
            if (it == classification)
                it.addWord(word)
        }

        return serialOut(context)
    }

    // change the group name of the classification
    fun changeClassificationGroupName(context: Context, origGroupName: String, newGroupName: String) {
        if (origGroupName == newGroupName || origGroupName == context.getString(R.string.str_unclassified))
            return

        val idx = mClassificationList.indexOf(getClassificationByGroupName(origGroupName))
        if (idx >= 0) {
            mClassificationList[idx].setGroupName(newGroupName)
            serialOut(context)
        }
    }

    // delete certain classification
    fun deleteClassification(context: Context, classification: ClassificationItem, wordsDeletingMethod: ClassificationDeletingDialog.WordsDeletingMethod, moveToWhere: String?) {
        val wordsDestinationGroupName: String? =
            when (wordsDeletingMethod) {
                ClassificationDeletingDialog.WordsDeletingMethod.MOVE_TO_UNCLASSIFIED -> context.getString(R.string.str_unclassified)
                ClassificationDeletingDialog.WordsDeletingMethod.MOVE_TO_ANOTHER_CLASSIFICATION -> moveToWhere ?: classification.getGroupName()
                else -> null
            }

        // 確定不要永久刪除，且目的地與原來的不一樣，才執行移動單字的動作
        if (wordsDestinationGroupName != null && wordsDestinationGroupName != classification.getGroupName()) {
            var destinationItem = getClassificationByGroupName(wordsDestinationGroupName)
            if (destinationItem == null)
                addNewClassification(context, ClassificationItem(wordsDestinationGroupName))
            destinationItem = getClassificationByGroupName(wordsDestinationGroupName)
            
            destinationItem?.getWordList()?.addAll(classification.getWordList())
        }

        // delete the classification from the list
        mClassificationList.remove(classification)

        // serial out
        serialOut(context)
    }

    // delete all words in certain classification
    fun deleteAllWordsInCertainClassification(context: Context, classification: ClassificationItem) {
        classification.getWordList().clear()
        serialOut(context)
    }

    // load all classifications by serialization
    fun loadAllClassificationsBySerialization(context: Context, clearFirst: Boolean) {
        if (clearFirst)
            mClassificationList.clear()

        // load from the serialized file
        val serialOutFile = File(context.filesDir, SERIALIZATION_CLASSIFICATIONS_FILENAME)
        if (!serialOutFile.exists())
            return
        val loaded = SerializationManager.load<ArrayList<ClassificationItem> >(serialOutFile.path)
        mClassificationList.addAll(loaded ?: arrayListOf())
    }

    // set adapter on recv
    fun setAdapterOnClassificationRecyclerView(context: Context, recv: RecyclerView) {
        val adapter = ClassificationItemAdapter(mClassificationList, context)
        recv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    // get count of classification
    fun getClassificationCount(): Int = mClassificationList.size

    // get classifications
    fun getClassificationList(): ArrayList<ClassificationItem> = mClassificationList

    // get the certain classification by its group name
    fun getClassificationByGroupName(groupName: String): ClassificationItem? {
        val filtered = mClassificationList.filter { it.getGroupName() == groupName }
        return if (filtered.isEmpty()) null else filtered[0]
    }

    // get group names of classifications
    fun getClassificationGroupNames(): List<String> = mClassificationList.map { it.getGroupName() }

    private fun serialOut(context: Context): Boolean {
        // serial out the words
        val serialOutFile = File(context.filesDir, SERIALIZATION_CLASSIFICATIONS_FILENAME)
        if (!serialOutFile.exists())
            serialOutFile.createNewFile()
        val serialOutSuccessOrNot = SerializationManager.save(mClassificationList, serialOutFile.path)
        if (!serialOutSuccessOrNot)
            Log.e("add-new-classification", "serial-out failed")
        return serialOutSuccessOrNot
    }
}