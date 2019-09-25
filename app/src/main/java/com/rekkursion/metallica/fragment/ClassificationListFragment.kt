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


class ClassificationListFragment: Fragment() {
    companion object {
        val TAG: String = ClassificationListFragment::class.java.simpleName

        // new instance of classification-list-fragment
        fun newInstance(): ClassificationListFragment {
            return ClassificationListFragment()
        }
    }

    private lateinit var mRecvClassificationList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_classification_list, container, false)
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        initViews(rootView)
        initEvents()
    }

    private fun initViews(rootView: View) {
        mRecvClassificationList = rootView.findViewById(R.id.recv_classification_list)
    }

    private fun initEvents() {
        // region set layout-manager on recv
        val layoutManager = LinearLayoutManager(this.context!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecvClassificationList.layoutManager = layoutManager
        // endregion

        // region set click and long-click listener on recv
        mRecvClassificationList.addOnItemTouchListener(
            RecyclerViewItemClickListener(
                mRecvClassificationList,
                object: RecyclerViewItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val wordListFragment = WordListFragment.newInstance(ClassificationManager.getClassificationGroupNames()[position])
                        fragmentManager
                            ?.beginTransaction()
                            ?.detach(this@ClassificationListFragment)
                            ?.add(R.id.lly_root_at_main, wordListFragment)
                            ?.commit()
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        // TODO: classification item long-click
                    }
                }
            )
        )
        // endregion

        // region load all existed classifications by serialization and set adapter on recv
        ClassificationManager.loadAllClassificationsBySerialization(this.context!!, true)
        ClassificationManager.setAdapterOnClassificationRecyclerView(this.context!!, mRecvClassificationList)

//        ClassificationManager.getClassificationList().forEach {
//            Log.e("name", it.getGroupName())
//            it.getWordList().forEach {
//                Log.e("word", it.getEnglishWord() ?: "null")
//            }
//        }

        // endregion
    }

    fun setAdapterOnClassificationRecyclerView() {
        ClassificationManager.setAdapterOnClassificationRecyclerView(this.context!!, mRecvClassificationList)
    }
}
