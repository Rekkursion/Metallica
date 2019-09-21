package com.rekkursion.metallica.manager

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.adapter.ClassificationItemAdapter
import com.rekkursion.metallica.model.ClassificationItem
import java.io.File

object ClassificationManager {
    const val NEW_CLASSIFICATION_FIELD: String = "new_classification"

    private const val SERIALIZATION_CLASSIFICATIONS_FILENAME: String = ".metallica_serialization_classifications"

    private val mClassificationList = ArrayList<ClassificationItem>()

    // add the new classification
    fun addNewClassification(context: Context, item: ClassificationItem) {
        mClassificationList.add(item)

        // serial out the words
        val serialOutFile = File(context.filesDir, SERIALIZATION_CLASSIFICATIONS_FILENAME)
        if (!serialOutFile.exists())
            serialOutFile.createNewFile()
        val serialOutSuccessOrNot = SerializationManager.save(mClassificationList, serialOutFile.path)
        if (!serialOutSuccessOrNot)
            Log.e("add-new-classification", "serial-out failed")
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
        Log.e("loaded size", loaded?.size.toString())
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
}