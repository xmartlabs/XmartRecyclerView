package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.content.Context;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/** An OnDemandRecyclerViewScrollListener for {@link RecyclerView} pagination in {@link NestedScrollView}'s */
public class OnDemandNestedScrollViewListener implements NestedScrollView.OnScrollChangeListener, ItemContainer,
    OnDemandLoader {
  @Dimension(unit = Dimension.DP)
  private static final int DEFAULT_VISIBLE_THRESHOLD_DP = 100;

  private int totalItemCount = 0;

  @NonNull
  private final BaseOnDemandLoader onDemandLoader;

  public OnDemandNestedScrollViewListener(@NonNull Context context, @NonNull PageLoadingProvider loadingProvider) {
    onDemandLoader = new BaseOnDemandLoader(loadingProvider, this);
    onDemandLoader.setVisibleThreshold(MetricsHelper.dpToPxInt(context.getResources(), DEFAULT_VISIBLE_THRESHOLD_DP));
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    onDemandLoader.setEnabled(isEnabled);
  }

  @Override
  public void setVisibleThreshold(@Dimension int threshold) {
    onDemandLoader.setVisibleThreshold(threshold);
  }

  @Override
  public void setLoadingProvider(@NonNull PageLoadingProvider loadingProvider) {
    onDemandLoader.setLoadingProvider(loadingProvider);
  }

  @Override
  public void resetStatus() {
    onDemandLoader.resetStatus();
  }

  /**
   * Resets to the initial view values.
   *
   * @param nestedScrollView It's used to get the child size
   */
  public void resetStatus(@NonNull NestedScrollView nestedScrollView) {
    View lastView = getLastNestedScrollChildView(nestedScrollView);
    totalItemCount = lastView == null ? 0 :lastView.getMeasuredHeight();
    onDemandLoader.resetStatus();
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
    View lastView = getLastNestedScrollChildView(nestedScrollView);
    if (lastView != null) {
      totalItemCount = lastView.getMeasuredHeight();
    }

    // -1 is because some devices scroll 1 pixel more
    onDemandLoader.onItemConsumed(scrollY + nestedScrollView.getMeasuredHeight() - 1);
  }

  @Nullable
  private View getLastNestedScrollChildView(@NonNull NestedScrollView nestedScrollView) {
    return nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
  }

  @Override
  public int getItemCount() {
    return totalItemCount;
  }
}
