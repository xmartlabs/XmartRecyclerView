package com.xmartlabs.xmartrecyclerview.decoration;

import android.graphics.Rect;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.ItemDecoration} subclass designed to add spacing to item controlled by a {@link
 * GridLayoutManager}.
 *
 * This decorator relies on both the {@link GridLayoutManager#mSpanCount} set and the
 * {@link GridLayoutManager.SpanSizeLookup}, so both must be defined.
 *
 * This decorator allows setting spacing for every item, and different spacing for:
 * <ul>
 * <li>First row items</li>
 * <li>Last row items</li>
 * <li>First column items</li>
 * <li>Last column items</li>
 * </ul>
 *
 * There's another option for which you can set the spacing individually for every item, using the
 * {@link #setItemOffsetConsumer} consumer function.
 *
 * Note that calculating the first and last column for each row involves some processing and can hurt performance.
 * Because of that, those values are calculated once and then cached for faster access.
 * If new items are added to the {@link RecyclerView}, you must invalidate the cache for the decoration to work
 * properly, using one of the following methods:
 * <ul>
 * <li>{@link #invalidateCache()} to invalidate the whole cache</li>
 * <li>{@link #invalidateCacheFromPosition(int)} to invalidate the cache from a given position (if you append items
 * to the latest position, using this will yield better performance)</li>
 * </ul>
 *
 * For even faster performance, consider enabling {@link GridLayoutManager.SpanSizeLookup#setSpanIndexCacheEnabled(boolean)}.
 */
@SuppressWarnings("unused")
public class RecyclerViewGridSpacingDecoration extends RecyclerView.ItemDecoration {
  private static final int FIRST_ROW_GROUP = 0;

  /** Top spacing for the first row. If null, {@link #itemSpacing} will be used. */
  @Dimension
  @Nullable
  private Integer firstRowTopSpacing;
  /** Bottom spacing for the last row. If null, {@link #itemSpacing} will be used. */
  @Dimension
  @Nullable
  private Integer lastRowBottomSpacing;
  /** Left spacing for the first column. If null, {@link #itemSpacing} will be used. */
  @Dimension
  @Nullable
  private Integer firstColumnLeftSpacing;
  /** Right spacing for the last column. If null, {@link #itemSpacing} will be used. */
  @Dimension
  @Nullable
  private Integer lastColumnRightSpacing;
  /**
   * Used to manually set the offset for every item.
   * This will override the automatic calculations.
   * The {@link Rect} top, right, bottom, left parameters must be modified to set the offset.
   */
  @Nullable
  private BiConsumer<Rect, RecyclerView> setItemOffsetConsumer;
  /** The default spacing for every item (top, right, bottom, left), unless one of the above spacings apply. */
  @Dimension
  private int itemSpacing;

  @NonNull
  private final List<Integer> firstColumns = new ArrayList<>();
  private int biggestFirstColumn;
  @NonNull
  private final List<Integer> lastColumns = new ArrayList<>();
  private int biggestLastColumn;

  @Override
  public void getItemOffsets(@NonNull Rect outRect, View view, @NonNull RecyclerView parent,
                             @NonNull RecyclerView.State state) {
    if (!(parent.getLayoutManager() instanceof GridLayoutManager)) {
      throw new IllegalArgumentException("This Item Decoration can only be used with GridLayoutManager");
    }
    GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
    if (setItemOffsetConsumer == null) {
      setOffsetForItem(outRect, view, parent, layoutManager);
    } else {
      setItemOffsetConsumer.accept(outRect, parent);
    }
  }

  /**
   * Sets the offset (spacing) for the {@code view}.
   *
   * @param outRect           the bounds of the view. The spacing must be set to this object
   * @param view              the view to add the spacing
   * @param recyclerView      the recycler view that holds the {@code view}
   * @param gridLayoutManager the layout manager of the recycler view
   */
  private void setOffsetForItem(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView recyclerView,
                                @NonNull GridLayoutManager gridLayoutManager) {
    int position = recyclerView.getChildLayoutPosition(view);
    int spanCount = gridLayoutManager.getSpanCount();
    int numberOfItems = recyclerView.getAdapter().getItemCount();
    GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

    if (setItemOffsetConsumer == null) {
      int firstRowTopSpacing = this.firstRowTopSpacing == null ? itemSpacing : this.firstRowTopSpacing;
      int firstColumnLeftSpacing = this.firstColumnLeftSpacing == null ? itemSpacing : this.firstColumnLeftSpacing;
      int lastColumnRightSpacing = this.lastColumnRightSpacing == null ? itemSpacing : this.lastColumnRightSpacing;
      int lastRowBottomSpacing = this.lastRowBottomSpacing == null ? itemSpacing : this.lastRowBottomSpacing;

      outRect.top = isFirstRow(position, spanCount, spanSizeLookup) ? firstRowTopSpacing : itemSpacing;
      outRect.left = isFirstColumn(position, spanCount, spanSizeLookup) ? firstColumnLeftSpacing : itemSpacing;
      outRect.right = isLastColumn(position, spanCount, numberOfItems, spanSizeLookup) ? lastColumnRightSpacing : itemSpacing;
      outRect.bottom = isLastRow(position, spanCount, numberOfItems, spanSizeLookup) ? lastRowBottomSpacing : itemSpacing;
    } else {
      setItemOffsetConsumer.accept(outRect, recyclerView);
    }
  }

  /**
   * Calculates whether or not the item at {@code position} belongs to the first row.
   *
   * @param position       the item position
   * @param spanCount      the maximum number of items a row can hold
   * @param spanSizeLookup the object that defines how much space an item can take
   * @return whether or not the item belong to the first row
   */
  private boolean isFirstRow(int position, int spanCount, @NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
    return spanSizeLookup.getSpanGroupIndex(position, spanCount) == FIRST_ROW_GROUP;
  }

  /**
   * Calculates whether or not the item at {@code position} belongs to the last row.
   *
   * @param position       the item position
   * @param spanCount      the maximum number of items a row can hold
   * @param numberOfItems  the total number of items held by the {@link RecyclerView}
   * @param spanSizeLookup the object that defines how much space an item can take
   * @return whether or not the item belongs to the last row
   */
  private boolean isLastRow(int position, int spanCount, int numberOfItems,
                            @NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
    while (position < numberOfItems - 1) {
      int spanSize = spanSizeLookup.getSpanSize(numberOfItems - 1);
      spanCount -= spanSize;
      numberOfItems -= 1;
      if (spanCount < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns whether the item at {@code position} belongs to the first column.
   *
   * @param position       the item position
   * @param spanCount      the maximum number of items a row can hold
   * @param spanSizeLookup the object that defines how much space an item can take
   * @return whether or not the item belongs to the first column
   */
  private boolean isFirstColumn(int position, int spanCount, @NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
    if (position == 0 || firstColumns.contains(position)) {
      biggestFirstColumn = biggestFirstColumn < position ? position : biggestFirstColumn;
      return true;
    }
    if (position < biggestFirstColumn) {
      return false;
    }

    boolean isFirstColumn = spanSizeLookup.getSpanGroupIndex(position, spanCount)
        > spanSizeLookup.getSpanGroupIndex(position - 1, spanCount);
    if (isFirstColumn) {
      biggestFirstColumn = biggestFirstColumn < position ? position : biggestFirstColumn;
    }
    return isFirstColumn;
  }

  /**
   * Returns whether the item at {@code position} belongs to the last column.
   *
   * @param position       the item position
   * @param spanCount      the maximum number of items a row can hold
   * @param numberOfItems  the total number of items held by the {@link RecyclerView}
   * @param spanSizeLookup the object that defines how much space an item can take
   * @return whether or not the item belongs to the last column
   */
  private boolean isLastColumn(int position, int spanCount, int numberOfItems,
                               @NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
    if (position == numberOfItems - 1 || lastColumns.contains(position)) {
      biggestLastColumn = biggestLastColumn < position ? position : biggestLastColumn;
      return true;
    }
    if (position < biggestLastColumn) {
      return false;
    }

    boolean isLastColumn = spanSizeLookup.getSpanGroupIndex(position, spanCount)
        < spanSizeLookup.getSpanGroupIndex(position + 1, spanCount);
    if (isLastColumn) {
      biggestLastColumn = biggestLastColumn < position ? position : biggestLastColumn;
    }
    return isLastColumn;
  }

  /**
   * Invalidates the cache holding the information about which items belong to the first or last column.
   *
   * If {@link GridLayoutManager.SpanSizeLookup#setSpanIndexCacheEnabled(boolean)} is enabled and the recycler view
   * adapter did not suffer any change, then you must invalidate the {@link GridLayoutManager.SpanSizeLookup} cache
   * calling {@link GridLayoutManager.SpanSizeLookup#invalidateSpanIndexCache()}.
   */
  public void invalidateCache() {
    firstColumns.clear();
    biggestFirstColumn = 0;
    lastColumns.clear();
    biggestLastColumn = 0;
  }

  /**
   * Invalidates the cache holding the information about which items belong to the first or last column from the
   * specified {@code position}.
   *
   * If {@link GridLayoutManager.SpanSizeLookup#setSpanIndexCacheEnabled(boolean)} is enabled and the recycler view
   * adapter did not suffer any change, then you must invalidate the {@link GridLayoutManager.SpanSizeLookup} cache
   * calling {@link GridLayoutManager.SpanSizeLookup#invalidateSpanIndexCache()}.
   *
   * @param position the position from which the cache should be invalidated
   */
  public void invalidateCacheFromPosition(int position) {
    List<Integer> itemsToRemove = new ArrayList<>();
    for (Integer item : firstColumns) {
      if (item <= position) {
        itemsToRemove.add(item);
      }
    }
    firstColumns.removeAll(itemsToRemove);

    biggestFirstColumn = firstColumns.get(firstColumns.size() - 1);

    itemsToRemove.clear();
    for (Integer item : lastColumns) {
      if (item <= position) {
        itemsToRemove.add(item);
      }
    }
    lastColumns.removeAll(itemsToRemove);
    biggestLastColumn = lastColumns.get(lastColumns.size() - 1);
  }

  public static final class Builder {
    @Dimension
    @Nullable
    private Integer firstRowTopSpacing;
    @Dimension
    @Nullable
    private Integer lastRowBottomSpacing;
    @Dimension
    @Nullable
    private Integer firstColumnLeftSpacing;
    @Dimension
    @Nullable
    private Integer lastColumnRightSpacing;
    @Nullable
    private BiConsumer<Rect, RecyclerView> setItemOffsetConsumer;
    private int itemSpacing;
    private int biggestFirstColumn;
    private int biggestLastColumn;

    public Builder() {}

    @NonNull
    public Builder firstRowTopSpacing(@Dimension @Nullable Integer val) {
      firstRowTopSpacing = val;
      return this;
    }

    public Builder lastRowBottomSpacing(@Dimension @Nullable Integer val) {
      lastRowBottomSpacing = val;
      return this;
    }

    @NonNull
    public Builder firstColumnLeftSpacing(@Dimension @Nullable Integer val) {
      firstColumnLeftSpacing = val;
      return this;
    }

    @NonNull
    public Builder lastColumnRightSpacing(@Dimension @Nullable Integer val) {
      lastColumnRightSpacing = val;
      return this;
    }

    @NonNull
    public Builder setItemOffsetConsumer(@Nullable BiConsumer<Rect, RecyclerView> val) {
      setItemOffsetConsumer = val;
      return this;
    }

    @NonNull
    public Builder itemSpacing(int val) {
      itemSpacing = val;
      return this;
    }

    @NonNull
    public Builder biggestFirstColumn(int val) {
      biggestFirstColumn = val;
      return this;
    }

    @NonNull
    public Builder biggestLastColumn(int val) {
      biggestLastColumn = val;
      return this;
    }

    @NonNull
    public RecyclerViewGridSpacingDecoration build() {
      return new RecyclerViewGridSpacingDecoration(this);
    }
  }

  private RecyclerViewGridSpacingDecoration(@NonNull Builder builder) {
    firstRowTopSpacing = builder.firstRowTopSpacing;
    lastRowBottomSpacing = builder.lastRowBottomSpacing;
    firstColumnLeftSpacing = builder.firstColumnLeftSpacing;
    lastColumnRightSpacing = builder.lastColumnRightSpacing;
    setItemOffsetConsumer = builder.setItemOffsetConsumer;
    itemSpacing = builder.itemSpacing;
    biggestFirstColumn = builder.biggestFirstColumn;
    biggestLastColumn = builder.biggestLastColumn;
  }
}
