package com.xmartlabs.xmartrecyclerview.adapter;

import android.support.annotation.NonNull;

/**
 * Represents a function that accepts one argument and produces a result.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 *
 */
interface Function<T, R> {

  /**
   * Applies this function to the given argument.
   *
   *
   * @param t the function argument
   * @return the function result
   */
  @NonNull
  R apply(@NonNull T t);
}
