package com.github.baudm.util.recyclerview;

import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

/**
 * Provides an easy way for getting notified about item clicks in a {@link RecyclerView}
 */
public final class ItemClickDetector extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener {

    private final OnItemClickListener listener;

    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetector;

    public ItemClickDetector(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (this.recyclerView == recyclerView) {
            return;
        }
        if (this.recyclerView != null) {
            this.recyclerView.removeOnItemTouchListener(this);
        }
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            recyclerView.addOnItemTouchListener(this);
            initGestureDetector();
        }
    }

    private void initGestureDetector() {
        if (gestureDetector == null) {
            gestureDetector = new GestureDetectorCompat(recyclerView.getContext(), this);
            gestureDetector.setIsLongpressEnabled(false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) {
            final View view = rv.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                listener.onItemClick(rv, rv.findContainingViewHolder(view));
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // Not implemented
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // Not implemented
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView rv, RecyclerView.ViewHolder viewHolder);
    }
}
