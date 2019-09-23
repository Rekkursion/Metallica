package com.rekkursion.metallica.listener

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemClickListener(recyclerView: RecyclerView, listener: OnItemClickListener): RecyclerView.SimpleOnItemTouchListener() {
    private var clickListener: OnItemClickListener? = listener
    private var gestureDetector: GestureDetectorCompat? = null

    init {
        gestureDetector = GestureDetectorCompat(recyclerView.context,
            object: GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && clickListener != null)
                        clickListener?.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && clickListener != null)
                        clickListener?.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector?.onTouchEvent(e)
        return false
    }

    // interface
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }
}