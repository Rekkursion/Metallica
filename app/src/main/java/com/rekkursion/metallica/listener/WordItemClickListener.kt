package com.rekkursion.metallica.listener

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class WordItemClickListener(recyclerView: RecyclerView, listener: OnWordItemClickListener): RecyclerView.SimpleOnItemTouchListener() {
    private var clickListener: OnWordItemClickListener? = listener
    private var gestureDetector: GestureDetectorCompat? = null

    init {
        gestureDetector = GestureDetectorCompat(recyclerView.context,
            object: GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && clickListener != null)
                        clickListener?.onWordItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && clickListener != null)
                        clickListener?.onWordItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector?.onTouchEvent(e)
        return false
    }

    // interface
    interface OnWordItemClickListener {
        fun onWordItemClick(view: View?, position: Int)
        fun onWordItemLongClick(view: View?, position: Int)
    }
}