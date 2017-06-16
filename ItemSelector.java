package com.github.baudm.util.recyclerview;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.widget.Checkable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

/**
 * Adds item selection behavior to any {@link RecyclerView.Adapter}
 *
 * To use this in your Adapter, do something like
 *
 * Adapter() {
 *     itemSelector = new ItemSelector(this);
 * }
 *
 * public void setSelection(int position) {
 *     // delegate
 *     itemSelector.setSelection(position);
 * }
 *
 * public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
 *      // delegate
 *      itemSelector.onBindViewHolder(holder, position);
 *      // do your stuff
 * }
 */
public final class ItemSelector {

    public static final int MODE_SINGLE = 1;

    public static final int MODE_MULTIPLE = 2;

    @IntDef({MODE_SINGLE, MODE_MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectionMode {}

    @SelectionMode
    private final int selectionMode;

    private int selectedPosition = RecyclerView.NO_POSITION;

    private Set<Long> selectedItemIds = new HashSet<>();

    public ItemSelector(@SelectionMode int selectionMode) {
        this.selectionMode = selectionMode;
    }

    public void setSelection(RecyclerView.Adapter<?> adapter, int position) {
        if (selectionMode == MODE_SINGLE) {
            setSelectionSingle(adapter, position);
        } else {
            setSelectionMultiple(adapter, position);
        }
    }

    private void setSelectionSingle(RecyclerView.Adapter<?> adapter, int position) {
        if (selectedPosition == position) {
            return;
        }
        // Refresh previously selected item
        if (selectedPosition != RecyclerView.NO_POSITION) {
            adapter.notifyItemChanged(selectedPosition);
        }
        selectedPosition = position;
        // Refresh current selected item
        adapter.notifyItemChanged(position);
    }

    private void setSelectionMultiple(RecyclerView.Adapter<?> adapter, int position) {
        final long id = adapter.getItemId(position);
        if (selectedItemIds.contains(id)) {
            selectedItemIds.remove(id);
        } else {
            selectedItemIds.add(id);
        }
        adapter.notifyItemChanged(position);
    }

    public int getSelection() {
        return selectedPosition;
    }

    public void setSelections(Set<Long> selections) {
        selectedItemIds = new HashSet<>(selections);
    }

    public Set<Long> getSelections() {
        return new HashSet<>(selectedItemIds);
    }

    /**
     * Hook this to {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     *
     * @param adapter
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.Adapter<?> adapter, RecyclerView.ViewHolder holder, int position) {
        if (selectionMode == MODE_SINGLE) {
            onBindViewHolderSingle(holder, position);
        } else {
            onBindViewHolderMultiple(adapter, holder, position);
        }
    }

    private void onBindViewHolderSingle(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setSelected(position == selectedPosition);
    }

    private void onBindViewHolderMultiple(RecyclerView.Adapter<?> adapter, RecyclerView.ViewHolder holder, int position) {
        final long id = adapter.getItemId(position);
        final boolean selected = selectedItemIds.contains(id);
        if (holder.itemView instanceof Checkable) {
            ((Checkable) holder.itemView).setChecked(selected);
        } else {
            holder.itemView.setSelected(selected);
        }
    }
}
