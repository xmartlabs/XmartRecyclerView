package com.xmartlabs.xmartrecyclerview.listener;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * An OnDemandLoadingScrollListener for recycler view pagination
 */
public abstract class OnDemandLoadingScrollListener extends RecyclerView.OnScrollListener {
  public static final int VISIBLE_THRESHOLD_DEFAULT = 5;

  private static int firstPage = 1;

  private boolean enabled = true;
  private boolean loading = true;

  private int firstVisibleItem;
  private int page = firstPage;
  private int previousTotal;
  private int totalItemCount;
  private int visibleItemCount;
  private int visibleThreshold = VISIBLE_THRESHOLD_DEFAULT;

  public OnDemandLoadingScrollListener() {
    loadNextPage(page);
  }

  /**
   * Sets the first page of the recycler view
   *
   * @param firstPage First page of the recycler view
   */
  public void setFirstPage(int firstPage) {
    OnDemandLoadingScrollListener.firstPage = firstPage;
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
   * Resets to the initial view values.
   *
   * @param layoutManager The {@link LinearLayoutManager} of the recycler view.
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
   * @param recyclerView The recycler view whose scroll position has changed.
   * @param dx          The amount of horizontal scroll.
   * @param dy          The amount of vertical scroll.
   */
  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    visibleItemCount = recyclerView.getChildCount();
    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    totalItemCount = getTotalItemCount(layoutManager);
    firstVisibleItem = getFirstVisibleItemPosition(layoutManager);

    checkIfLoadingIsNeeded();
  }

  private void checkIfLoadingIsNeeded() {
    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false;
        page++;
        previousTotal = totalItemCount;
      }
    } else if (enabled && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
      loadNextPage(page);
      loading = true;
    }
  }

  private int getFirstVisibleItemPosition(@NonNull LinearLayoutManager layoutManager) {
    return layoutManager.findFirstVisibleItemPosition();
  }

  private int getTotalItemCount(@NonNull LinearLayoutManager layoutManager) {
    return layoutManager.getItemCount();
  }

  /**
   * Called when the scroll position of the recycler view reaches the end of the current page.
   *
   * @param page The next page to be loaded.
   */
  protected abstract void loadNextPage(int page);
}
