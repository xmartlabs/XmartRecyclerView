package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/** Provides a base implementation of {@link PageLoadingProvider} */
public abstract class BasePageLoadingProvider implements PageLoadingProvider {
  public final static int DEFAULT_FIRST_PAGE = 1;

  @Nullable
  private Integer totalEntities;

  @NonNull
  private final ItemContainer itemContainer;

  public BasePageLoadingProvider(@NonNull ItemContainer itemContainer) {
    this.itemContainer = itemContainer;
  }

  /**
   * Sets the number of entities.
   * The number will be used to calculate if there ate more pages or not.
   *
   * @param totalEntities The number of entities
   */
  public void setTotalEntities(@Nullable Integer totalEntities) {
    this.totalEntities = totalEntities;
  }

  @Override
  public boolean hasMorePages() {
    return totalEntities == null || itemContainer.getItemCount() < totalEntities;
  }

  /** By default the first page value is {@link #DEFAULT_FIRST_PAGE} */
  @Override
  public int getFirstPage() {
    return DEFAULT_FIRST_PAGE;
  }
}
