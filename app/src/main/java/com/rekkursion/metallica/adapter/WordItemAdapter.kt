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
            holder.setData(eng, item.getPartOfSpeech(), item.getChineseMeaning())
    }

    // inner class: view-holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtvEnglishWord: TextView = itemView.findViewById(R.id.txtv_english_word_at_word_item)
        private var txtvPartOfSpeech: TextView = itemView.findViewById(R.id.txtv_part_of_speech_at_word_item)
        private var txtvChineseMeaning: TextView = itemView.findViewById(R.id.txtv_chinese_meaning_at_word_item)
        private var imgbtnMore: ImageButton = itemView.findViewById(R.id.imgbtn_more_at_word_item)

        fun setData(eng: String, speech: WordItem.PartOfSpeech?, chi: String?) {
            txtvEnglishWord.text = eng
            txtvPartOfSpeech.text = speech?.abbr ?: "-"
            txtvChineseMeaning.text = chi ?: ""
            imgbtnMore.setOnClickListener {
                // TODO: click on more-button on item
            }
        }
    }
}