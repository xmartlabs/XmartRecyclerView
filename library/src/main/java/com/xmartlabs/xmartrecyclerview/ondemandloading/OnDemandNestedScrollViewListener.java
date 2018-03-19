package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;

/** An OnDemandNestedScrollViewListener for {@link RecyclerView} pagination in {@link NestedScrollView}'s */
public class OnDemandNestedScrollViewListener implements NestedScrollView.OnScrollChangeListener {
  private static final int DEFAULT_VISIBLE_THRESHOLD_DP = 100;

  @NonNull
  private final RecyclerView recyclerView;

  private boolean enabled = true;
  private boolean loading = true;
  private int page = 1;
  private int previousRecyclerViewHeight;

  @Dimension(unit = Dimension.PX)
  private int visibleThreshold;

  private PageLoadingProvider pageLoadingProvider;

  public OnDemandNestedScrollViewListener(@NonNull RecyclerView recyclerView, @NonNull PageLoadingProvider pageLoadingProvider) {
    this.recyclerView = recyclerView;
    this.pageLoadingProvider = pageLoadingProvider;
    visibleThreshold = MetricsHelper.dpToPxInt(recyclerView.getResources(), DEFAULT_VISIBLE_THRESHOLD_DP);
    pageLoadingProvider.loadPage(page);
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
   * Sets the value of the visible threshold for the on demand loading.
   *
   * @param threshold A value that specifies the visible threshold for the on demand loading to happen.
   */
  public void setVisibleThreshold(@Dimension(unit = Dimension.PX) int threshold) {
    this.visibleThreshold = threshold;
  }

  /**
   * Sets a {@link PageLoadingProvider} that provides the on demand loading capabilities.
   *
   * @param pageLoadingProvider The {@link PageLoadingProvider} to be set.
   */
  public void setPageLoadingProvider(@NonNull PageLoadingProvider pageLoadingProvider) {
    this.pageLoadingProvider = pageLoadingProvider;
  }

  /**
   * Resets to the initial nested scroll view values.
   */
  public void resetStatus() {
    enabled = true;
    visibleThreshold = MetricsHelper.dpToPxInt(recyclerView.getResources(), DEFAULT_VISIBLE_THRESHOLD_DP);
    loading = false;
    previousRecyclerViewHeight = recyclerView.getMeasuredHeight();
    page = 1;
  }

  /**
   * Called when the scroll position of the nested scroll view changes.
   *
   * @param nestedScrollView The view whose scroll position has changed.
   * @param scrollX          Current horizontal scroll origin.
   * @param scrollY          Current vertical scroll origin.
   * @param oldScrollX       Previous horizontal scroll origin.
   * @param oldScrollY       Previous vertical scroll origin.
   */
  @Override
  public void onScrollChange(@NonNull NestedScrollView nestedScrollView, int scrollX, int scrollY,
                             int oldScrollX, int oldScrollY) {
    if (previousRecyclerViewHeight < recyclerView.getMeasuredHeight()) {
      loading = false;
      page++;
      previousRecyclerViewHeight = recyclerView.getMeasuredHeight();
    }

    if ((scrollY + visibleThreshold >= (recyclerView.getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) &&
        scrollY > oldScrollY && !loading && enabled) {
      loading = true;
      pageLoadingProvider.loadPage(page);
    }
  }
}
