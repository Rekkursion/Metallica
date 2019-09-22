package com.rekkursion.metallica.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rekkursion.metallica.R
import com.rekkursion.metallica.manager.ClassificationManager
import com.rekkursion.metallica.model.ClassificationItem
import kotlin.contracts.contract


class ClassificationListFragment(context: Context): Fragment() {
    companion object {
        val TAG: String = ClassificationListFragment::class.java.simpleName

        // new instance of classification-list-fragment
        fun newInstance(context: Context): ClassificationListFragment {
            return ClassificationListFragment(context)
        }
    }

    private var mContext: Context = context
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
        // set layout-manager on recv
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecvClassificationList.layoutManager = layoutManager

        ClassificationManager.loadAllClassificationsBySerialization(mContext, true)
        ClassificationManager.setAdapterOnClassificationRecyclerView(mContext, mRecvClassificationList)
    }

    fun setAdapterOnClassificationRecyclerView() {
        ClassificationManager.setAdapterOnClassificationRecyclerView(mContext, mRecvClassificationList)
    }
}
