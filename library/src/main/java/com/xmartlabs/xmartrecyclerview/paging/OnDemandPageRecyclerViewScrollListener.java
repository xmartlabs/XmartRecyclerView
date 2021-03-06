package com.xmartlabs.xmartrecyclerview.paging;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xmartlabs.xmartrecyclerview.internal.ItemCounter;
import com.xmartlabs.xmartrecyclerview.internal.paging.BaseOnDemandPageLoader;

/** An OnDemandPageRecyclerViewScrollListener for {@link RecyclerView} pagination */
public class OnDemandPageRecyclerViewScrollListener
    extends RecyclerView.OnScrollListener implements ItemCounter,
    OnDemandPageLoader {
  @NonNull
  private final BaseOnDemandPageLoader onDemandLoader;
  private int totalItemCount = 0;

  public OnDemandPageRecyclerViewScrollListener(@NonNull PageLoader loadingProvider) {
    onDemandLoader = new BaseOnDemandPageLoader(loadingProvider, this);
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    onDemandLoader.setEnabled(isEnabled);
  }

  @Override
  public void setVisibleThreshold(int threshold) {
    onDemandLoader.setVisibleThreshold(threshold);
  }

  @Override
  public void setPageLoader(@NonNull PageLoader pageLoader) {
    onDemandLoader.setPageLoader(pageLoader);
  }

  @Override
  public void resetStatus() {
    totalItemCount = 0;
    onDemandLoader.resetStatus();
  }

  /**
   * Resets to the initial view values.
   *
   * @param layoutManager The {@link LinearLayoutManager} of the RecyclerView.
   */
  public void resetStatus(@NonNull LinearLayoutManager layoutManager) {
    totalItemCount = layoutManager.getItemCount();
    onDemandLoader.resetStatus();
  }

  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
      totalItemCount = layoutManager.getItemCount();
      int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
      onDemandLoader.onItemConsumed(lastVisibleItemPosition);
    } else {
      throw new IllegalStateException("The RecyclerView's LayoutManager should be of the LinearLayoutManager type");
    }
  }

  @Override
  public int getItemCount() {
    return totalItemCount;
  }
}
