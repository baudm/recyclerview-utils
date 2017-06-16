package com.github.baudm.util.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.baudm.R;

import java.util.List;

/**
 * Wraps an existing {@link android.support.v7.widget.RecyclerView.Adapter} to provide
 * "load more when scrolled to bottom" functionality.
 */
public final class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADING_INDICATOR_VIEW_TYPE = -0xDEADBEEF;
    private static final long LOADING_INDICATOR_ITEM_ID = -0xDEADBEEF;

    private final RecyclerView.Adapter delegate;
    private final OnLoadMoreListener listener;

    private int totalCount;
    private boolean loading;

    public LoadMoreAdapter(RecyclerView.Adapter adapter, OnLoadMoreListener listener) {
        this.delegate = adapter;
        setHasStableIds(this.delegate.hasStableIds());
        this.listener = listener;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setLoadingFinished() {
        loading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOADING_INDICATOR_VIEW_TYPE) {
            return new LoadingViewHolder(parent);
        }
        return delegate.onCreateViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof LoadingViewHolder)) {
            delegate.onBindViewHolder(holder, position);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (!(holder instanceof LoadingViewHolder)) {
            delegate.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < delegate.getItemCount()) {
            return delegate.getItemViewType(position);
        }
        return LOADING_INDICATOR_VIEW_TYPE;
    }

    @Override
    public long getItemId(int position) {
        if (position < delegate.getItemCount()) {
            return delegate.getItemId(position);
        }
        return LOADING_INDICATOR_ITEM_ID;
    }

    @Override
    public int getItemCount() {
        int itemCount = delegate.getItemCount();
        // Add 1 to the count for the loading view holder
        if (itemCount < totalCount) {
            itemCount++;
        }
        return itemCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (!(holder instanceof LoadingViewHolder)) {
            delegate.onViewRecycled(holder);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (holder instanceof LoadingViewHolder) {
            return super.onFailedToRecycleView(holder);
        }
        return delegate.onFailedToRecycleView(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof LoadingViewHolder) {
            if (!loading) {
                loading = true;
                listener.onLoadMore();
            }
        } else {
            delegate.onViewAttachedToWindow(holder);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (!(holder instanceof LoadingViewHolder)) {
            delegate.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        delegate.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        delegate.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        delegate.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        delegate.onDetachedFromRecyclerView(recyclerView);
    }


    private static final class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item_load_more, parent, false));
        }
    }


    public interface OnLoadMoreListener {
        /**
         * Called by {@link LoadMoreAdapter} when the bottom of the list is shown.
         */
        void onLoadMore();
    }
}
