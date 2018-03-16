package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/** An OnDemandNestedScrollViewListener for {@link RecyclerView} pagination */
public class OnDemandNestedScrollViewListener extends RecyclerView.OnScrollListener {
  public static final int VISIBLE_THRESHOLD_DEFAULT = 5;

  private boolean enabled = true;
  private boolean loading = true;

  private int firstVisibleItem;
  private int firstPage = 1;
  private int page = firstPage;
  private int previousTotal;
  private int totalItemCount;
  private int visibleItemCount;
  private int visibleThreshold = VISIBLE_THRESHOLD_DEFAULT;

  private LoadingProvider loadingProvider;

  public OnDemandNestedScrollViewListener(@NonNull LoadingProvider loadingProvider) {
    loadingProvider.loadNextPage(page);
  }

  /**
   * Sets the first page of the RecyclerView
   *
   * @param firstPage First page of the RecyclerView
   */
  public void setFirstPage(int firstPage) {
    this.firstPage = firstPage;
  }

  /**
   * Enables or disables the loading on demand when scrolling.
   *
   * @param isEnabled A boolean specifying if loading on demand should be enabled or not.
   */
  public void setEnabled(boolean isEnabled) {
    this.enabled = isEnabled;
  }

  /**
   * Sets the visible threshold, in items, for the on demand loading to happen.
   *
   * @param threshold Visible threshold, in items, for the on demand loading to happen.
   */
  public void setVisibleThreshold(int threshold) {
    this.visibleThreshold = threshold;
  }

  /**
   * Sets a {@link LoadingProvider} that provides the on demand loading capabilities.
   *
   * @param loadingProvider The {@link LoadingProvider} to be set.
   */
  public void setLoadingProvider(@NonNull LoadingProvider loadingProvider) {
    this.loadingProvider = loadingProvider;
  }

  /**
   * Resets to the initial view values.
   *
   * @param layoutManager The {@link LinearLayoutManager} of the RecyclerView.
   */
  public void resetStatus(@NonNull LinearLayoutManager layoutManager) {
    enabled = true;
    previousTotal = 0;
    loading = false;
    firstVisibleItem = 0;
    visibleItemCount = getFirstVisibleItemPosition(layoutManager);
    totalItemCount = getTotalItemCount(layoutManager);
    page = firstPage;
    checkIfLoadingIsNeeded();
  }

  /**
   * Called when the scroll position of the nested scroll view changes.
   *
   * @param recyclerView The RecyclerView whose scroll position has changed.
   * @param dx          The amount of horizontal scroll.
   * @param dy          The amount of vertical scroll.
   */
  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    visibleItemCount = recyclerView.getChildCount();

    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
      totalItemCount = getTotalItemCount(layoutManager);
      firstVisibleItem = getFirstVisibleItemPosition(layoutManager);

      checkIfLoadingIsNeeded();
    } else {
      throw new IllegalStateException("The RecyclerView's LayoutManager should be of the LinearLayoutManager type");
    }
  }

  private void checkIfLoadingIsNeeded() {
    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false;
        page++;
        previousTotal = totalItemCount;
      }
    } else if (enabled && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
      loadingProvider.loadNextPage(page);
      loading = true;
    }
  }

  private int getFirstVisibleItemPosition(@NonNull LinearLayoutManager layoutManager) {
    return layoutManager.findFirstVisibleItemPosition();
  }

  private int getTotalItemCount(@NonNull LinearLayoutManager layoutManager) {
    return layoutManager.getItemCount();
  }
}
