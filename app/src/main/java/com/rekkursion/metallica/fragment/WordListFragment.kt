package com.rekkursion.metallica.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.R
import com.rekkursion.metallica.listener.RecyclerViewItemClickListener
import com.rekkursion.metallica.manager.ClassificationManager
import com.rekkursion.metallica.manager.OperationManager
import com.rekkursion.metallica.manager.WordsManager

private var mGroupName: String? = null

class WordListFragment: Fragment() {
    companion object {
        val TAG: String = WordListFragment::class.java.simpleName

        // new instance of word-list-fragment
        fun newInstance(groupName: String): WordListFragment {
            mGroupName = groupName
            return WordListFragment()
        }
    }

    private lateinit var mRecvWordList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        initViews(rootView)
        initEvents()
    }

    private fun initViews(rootView: View) {
        mRecvWordList = rootView.findViewById(R.id.recv_word_list)
    }

    private fun initEvents() {
        // region set layout-manager on recv
        val layoutManager = LinearLayoutManager(this.context!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecvWordList.layoutManager = layoutManager
        // endregion

        // region set click and long-click listener on recv
        mRecvWordList.addOnItemTouchListener(
            RecyclerViewItemClickListener(
                mRecvWordList,
                object: RecyclerViewItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {

                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        OperationManager.buildItemOperatingDialog(
                            this@WordListFragment,
                            mRecvWordList,
                            OperationManager.OperatedObject.WORD,
                            ClassificationManager.getClassificationByGroupName(mGroupName!!)?.getWordList()?.get(position)?.getEnglishWord()!!,
                            ClassificationManager.getClassificationByGroupName(mGroupName!!),
                            position
                        )
                    }
                }
            )
        )
        // endregion

        // region load all existed words in this classification by serialization and set adapter on recv
        mGroupName?.let { groupName ->
            val wordList = ClassificationManager.getClassificationByGroupName(groupName)?.getWordList()
            wordList?.let {
                WordsManager.setAdapterOnWordRecyclerView(this.context!!, mRecvWordList, it)
            }
        }
//        WordsManager.loadAllWordsBySerialization(this.context!!, true, mGroupName)
//        WordsManager.setAdapterOnWordRecyclerView(this.context!!, mRecvWordList, ClassificationManager.getClassificationByGroupName(mGroupName!!)?.getWordList() ?: arrayListOf())
        // endregion
    }

    fun setAdapterOnWordRecyclerView() {
        mGroupName?.let {
            WordsManager.setAdapterOnWordRecyclerView(this.context!!, mRecvWordList, ClassificationManager.getClassificationByGroupName(it)?.getWordList() ?: arrayListOf())
        }
    }
}
