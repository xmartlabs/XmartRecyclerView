package com.xmartlabs.xmartrecyclerview.adapter;

import android.support.annotation.Nullable;

import java.util.Collection;

final class Utils {
  /**
   * Checks whether or not a collection is null or empty.
   *
   * @param collection the {@link Collection} instance to be checked
   * @return true if the {@link Collection} is null or empty
   */
  static boolean isNullOrEmpty(@Nullable Collection collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Returns {@code true} if the arguments are equal to each other
   * and {@code false} otherwise.
   * Consequently, if both arguments are {@code null}, {@code true}
   * is returned and if exactly one argument is {@code null}, {@code
   * false} is returned.  Otherwise, equality is determined by using
   * the {@link Object#equals equals} method of the first
   * argument.
   *
   * @param a an object
   * @param b an object to be compared with {@code a} for equality
   * @return {@code true} if the arguments are equal to each other
   * and {@code false} otherwise
   * @see Object#equals(Object)
   */
  static boolean equals(@Nullable Object a, @Nullable Object b) {
    return (a == b) || (a != null && a.equals(b));
  }
}
