package com.xmartlabs.xmartrecyclerview.paging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xmartlabs.xmartrecyclerview.internal.ItemCounter;

/** Provides a base implementation of {@link PageLoader} */
public abstract class BasePageLoader implements PageLoader {
  @SuppressWarnings("WeakerAccess")
  public final static int DEFAULT_FIRST_PAGE = 1;

  @Nullable
  private Integer entityCount;

  @NonNull
  private final ItemCounter itemCounter;

  public BasePageLoader(@NonNull ItemCounter itemCounter) {
    this.itemCounter = itemCounter;
  }

  /**
   * Sets the number of entities.
   *
   * The number will be used to calculate if there are more pages or not.
   *
   * @param entityCount The number of entities.
   */
  public void setEntityCount(@Nullable Integer entityCount) {
    this.entityCount = entityCount;
  }

  @Override
  public boolean hasMorePages() {
    return entityCount == null || itemCounter.getItemCount() < entityCount;
  }

  /** Returns the first page index, which is {@link #DEFAULT_FIRST_PAGE}. */
  @Override
  public int getFirstPageIndex() {
    return DEFAULT_FIRST_PAGE;
  }
}
