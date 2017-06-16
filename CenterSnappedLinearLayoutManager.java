package com.github.baudm.util.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * LinearLayoutManager which uses a center-snapped SmoothScroller
 *
 * @see CenteredLinearSmoothScroller
 */
public final class CenterSnappedLinearLayoutManager extends LinearLayoutManager {

    public CenterSnappedLinearLayoutManager(Context context) {
        super(context);
    }

    public CenterSnappedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CenterSnappedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final CenteredLinearSmoothScroller linearSmoothScroller = new CenteredLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }


    /**
     * LinearSmoothScroller which always centers the view inside its parent
     */
    private static final class CenteredLinearSmoothScroller extends LinearSmoothScroller {

        /**
         * Slow the scroll animation by this factor.
         * x2 means twice the animation time per pixel.
         */
        private static final float TIME_FACTOR = 2f;

        CenteredLinearSmoothScroller(Context context) {
            super(context);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return TIME_FACTOR * super.calculateSpeedPerPixel(displayMetrics);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            final int viewCenter = (viewStart + viewEnd) / 2;
            final int boxCenter = (boxStart + boxEnd) / 2;
            return boxCenter - viewCenter;
        }
    }
}
