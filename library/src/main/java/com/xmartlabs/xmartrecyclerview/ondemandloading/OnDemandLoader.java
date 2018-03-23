package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.annotation.NonNull;

public interface OnDemandLoader {
  /**
   * Enables or disables the loading on demand when scrolling.
   *
   * @param isEnabled A boolean specifying if loading on demand should be enabled or not.
   */
  void setEnabled(boolean isEnabled);

  /**
   * Sets the visible threshold, in items, for the on demand loading to happen.
   *
   * @param threshold Visible threshold, in items, for the on demand loading to happen.
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
