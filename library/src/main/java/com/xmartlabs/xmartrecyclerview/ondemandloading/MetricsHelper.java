package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.content.res.Resources;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;

class MetricsHelper {
  /**
   * Converts the {@code dp} value to pixels dimension.
   *
   * @param resources to obtain the density value
   * @param dp the value in dp dimension
   * @return the converted {@code dp} value to pixels
   */
  @Dimension(unit = Dimension.PX)
  public static float dpToPx(@NonNull Resources resources, @Dimension(unit = Dimension.DP) float dp) {
    return dp * resources.getDisplayMetrics().density;
  }

  /**
   * Converts the {@code dp} value to pixels dimension.
   *
   * @param resources to obtain the density value
   * @param dp the value in dp dimension
   * @return the converted {@code dp} value to integer pixels
   */
  @Dimension(unit = Dimension.PX)
  public static int dpToPxInt(@NonNull Resources resources, @Dimension(unit = Dimension.DP) float dp) {
    return (int) dpToPx(resources, dp);
  }
}
