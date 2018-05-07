package com.xmartlabs.xmartrecyclerview.internal.paging;

import android.support.annotation.NonNull;

import com.xmartlabs.xmartrecyclerview.internal.ItemCounter;
import com.xmartlabs.xmartrecyclerview.paging.OnDemandPageLoader;
import com.xmartlabs.xmartrecyclerview.paging.PageLoader;

/** Provides the necessary operations to allow on demand loading while scrolling a {@link android.support.v7.widget.RecyclerView} */
@SuppressWarnings("unused")
public final class BaseOnDemandPageLoader implements OnDemandPageLoader {
  private static final int VISIBLE_THRESHOLD_DEFAULT = 5;

  private boolean enabled = true;
  private boolean loading = true;

  private int page;
  private int previousTotal;
  private int totalItemCount;
  private int lastItemConsumed;
  private int visibleThreshold = VISIBLE_THRESHOLD_DEFAULT;

  @NonNull
  private PageLoader loadingProvider;
  @NonNull
  private final ItemCounter itemCounter;

  public BaseOnDemandPageLoader(@NonNull PageLoader loadingProvider, @NonNull ItemCounter itemCounter) {
    this.itemCounter = itemCounter;
    this.loadingProvider = loadingProvider;
    page = loadingProvider.getFirstPageIndex();
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
  public void setPageLoader(@NonNull PageLoader pageLoader) {
    this.loadingProvider = pageLoader;
  }

  @Override
  public void resetStatus() {
    enabled = true;
    previousTotal = 0;
    loading = false;
    lastItemConsumed = 0;
    totalItemCount = itemCounter.getItemCount();
    page = loadingProvider.getFirstPageIndex();
    checkIfLoadingIsNeeded();
  }

  /**
   * Called when an item is consumed.
   * This method could invoke a new page request.
   *
   * @param index The index of the consumed element.
   * */
  public void onItemConsumed(int index) {
    totalItemCount = itemCounter.getItemCount();
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
