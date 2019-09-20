package com.rekkursion.metallica.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.model.WordItem
import com.rekkursion.metallica.R

class WordItemAdapter(pWordItemList: ArrayList<WordItem>, pContext: Context): RecyclerView.Adapter<WordItemAdapter.ViewHolder>() {
    private var mWordItemList: ArrayList<WordItem>? = null
    private var mContext: Context? = null

    // primary constructor
    init {
        mContext = pContext
        if (mWordItemList == null)
            mWordItemList = ArrayList()
        mWordItemList?.clear()
        mWordItemList?.addAll(pWordItemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_word_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mWordItemList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mWordItemList?.get(position)
        val eng = item?.getEnglishWord()
        if (eng != null)
            holder.setData(eng, item.getPartOfSpeechList(), item.getChineseMeaningList())
    }

    // inner class: view-holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtvEnglishWord: TextView = itemView.findViewById(R.id.txtv_english_word_at_word_item)
        private var txtvSpeechesAndMeanings: TextView = itemView.findViewById(R.id.txtv_part_of_speeches_and_chinese_meanings_at_word_item)
        private var imgbtnMore: ImageButton = itemView.findViewById(R.id.imgbtn_more_at_word_item)

        fun setData(eng: String, speechList: ArrayList<WordItem.PartOfSpeech>?, chiList: ArrayList<String>?) {
            txtvEnglishWord.text = eng

            val sBuf = StringBuffer()
            speechList?.forEachIndexed { index, partOfSpeech ->
                sBuf.append("${partOfSpeech.abbr} ${chiList?.get(index) ?: ""} ")
            }
            txtvSpeechesAndMeanings.text = sBuf.toString()

            imgbtnMore.setOnClickListener {
                // TODO: click on more-button on item
            }
        }
    }
}