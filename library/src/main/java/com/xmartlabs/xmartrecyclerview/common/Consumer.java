package com.xmartlabs.xmartrecyclerview.common;

import android.support.annotation.NonNull;

/**
 * A functional interface (callback) that accepts a single value.
 *
 * @param <T> the value type
 */
public interface Consumer<T> {
  /**
   * Consume the given value.
   *
   * @param t the value
   */
  void accept(@NonNull T t);
}
