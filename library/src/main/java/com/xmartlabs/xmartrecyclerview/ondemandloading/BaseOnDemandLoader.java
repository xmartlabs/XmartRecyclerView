package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.NonNull;
/** Provides the necessary operations to allow on demand loading while scrolling a {@link android.support.v7.widget.RecyclerView} */
@SuppressWarnings("unused")
public final class BaseOnDemandLoader implements OnDemandLoader {
  private static final int VISIBLE_THRESHOLD_DEFAULT = 5;

  private boolean enabled = true;
  private boolean loading = true;

  private int page;
  private int previousTotal;
  private int totalItemCount;
  private int lastItemConsumed;
  private int visibleThreshold = VISIBLE_THRESHOLD_DEFAULT;

  @NonNull
  private PageLoadingProvider loadingProvider;
  @NonNull
  private final ItemContainer itemContainer;

  public BaseOnDemandLoader(@NonNull PageLoadingProvider loadingProvider, @NonNull ItemContainer itemContainer) {
    this.itemContainer = itemContainer;
    this.loadingProvider = loadingProvider;
    page = loadingProvider.getFirstPage();
    loadingProvider.loadPage(page);
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    this.enabled = isEnabled;
  }

  @Override
  public void setVisibleThreshold(int threshold) {
    this.visibleThreshold = threshold;
  }

  @Override
  public void setLoadingProvider(@NonNull PageLoadingProvider loadingProvider) {
    this.loadingProvider = loadingProvider;
  }

  @Override
  public void resetStatus() {
    enabled = true;
    previousTotal = 0;
    loading = false;
    lastItemConsumed = 0;
    totalItemCount = itemContainer.getItemCount();
    page = loadingProvider.getFirstPage();
    checkIfLoadingIsNeeded();
  }

  /**
   * Called when an item is consumed.
   * This method could invoke a new page request.
   *
   * @param index The index of the consumed element.
   * */
  public void onItemConsumed(int index) {
    totalItemCount = itemContainer.getItemCount();
    lastItemConsumed = index;
    checkIfLoadingIsNeeded();
  }

  private void checkIfLoadingIsNeeded() {
    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false;
        page++;
        previousTotal = totalItemCount;
      }
    }
    if (!loading
        && enabled
        && loadingProvider.hasMorePages()
        && totalItemCount - visibleThreshold <= lastItemConsumed) {
      loadingProvider.loadPage(page);
      loading = true;
    }
  }
}
