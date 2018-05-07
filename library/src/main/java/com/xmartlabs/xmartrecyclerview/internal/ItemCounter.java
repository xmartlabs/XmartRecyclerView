package com.xmartlabs.xmartrecyclerview.internal;

/**
 * Provides an operation to get the amount of items in an {@link android.support.v7.widget.RecyclerView.Adapter}
 * instance or pixels in a {@link android.support.v4.widget.NestedScrollView} instance.
 */
public interface ItemCounter {
  /** Returns the item count. */
  int getItemCount();
}
