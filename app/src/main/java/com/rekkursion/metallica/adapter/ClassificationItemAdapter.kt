package com.rekkursion.metallica.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.R
import com.rekkursion.metallica.model.ClassificationItem

class ClassificationItemAdapter(pClassificationItemList: ArrayList<ClassificationItem>, pContext: Context): RecyclerView.Adapter<ClassificationItemAdapter.ViewHolder>()  {
    private var mClassificationItemList: ArrayList<ClassificationItem>? = null
    private var mContext: Context? = null

    // primary constructor
    init {
        mContext = pContext
        if (mClassificationItemList == null)
            mClassificationItemList = ArrayList()
        mClassificationItemList?.clear()
        mClassificationItemList?.addAll(pClassificationItemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_classification_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mClassificationItemList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mClassificationItemList?.get(position)
        item?.let {
            holder.setData(it.getGroupName(), it.getWordList().size)
        }
    }

    // inner class: view-holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtvGroupName: TextView = itemView.findViewById(R.id.txtv_group_name_at_classification_item)
        private var txtvNumOfWords: TextView = itemView.findViewById(R.id.txtv_num_of_words_at_classification_item)

        @SuppressLint("SetTextI18n")
        fun setData(groupName: String, numOfWords: Int) {
            txtvGroupName.text = groupName
            txtvNumOfWords.text = "($numOfWords)"
        }
    }
}