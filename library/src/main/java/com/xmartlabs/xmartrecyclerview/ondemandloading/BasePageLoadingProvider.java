package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BasePageLoadingProvider implements PageLoadingProvider {
  private final static int DEFAULT_FIRST_PAGE = 1;

  @Nullable
  private Integer totalEntities;
  @NonNull
  private final ItemContainer itemContainer;

  public BasePageLoadingProvider(@NonNull ItemContainer itemContainer) {
    this.itemContainer = itemContainer;
  }

  public void setTotalEntities(@Nullable Integer totalEntities) {
    this.totalEntities = totalEntities;
  }

  @Override
  public boolean hasMorePages() {
    return totalEntities == null || itemContainer.getItemCount() < totalEntities;
  }

  @Override
  public int getFirstPage() {
    return DEFAULT_FIRST_PAGE;
  }
}
