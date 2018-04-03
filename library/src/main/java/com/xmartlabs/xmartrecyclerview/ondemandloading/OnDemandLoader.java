package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.NonNull;

/**
 * Provides the necessary operations to setup the on demand loading while scrolling a
 * {@link android.support.v7.widget.RecyclerView} or a {@link android.support.v4.widget.NestedScrollView}
 */
public interface OnDemandLoader {
  /**
   * Enables or disables the loading on demand when scrolling.
   *
   * @param isEnabled A boolean specifying if loading on demand should be enabled or not.
   */
  void setEnabled(boolean isEnabled);

  /**
   * Sets the value of the visible threshold for the on demand loading.
   *
   * @param threshold A value that specifies the visible threshold for the on demand loading to happen.
   */
  void setVisibleThreshold(int threshold);

  /**
   * Sets a {@link PageLoadingProvider} that provides the on demand loading capabilities.
   *
   * @param loadingProvider The {@link PageLoadingProvider} to be set.
   */
  void setLoadingProvider(@NonNull PageLoadingProvider loadingProvider);

  /** Resets to the initial nested scroll view values. */
  void resetStatus();
}
