package com.xmartlabs.xmartrecyclerview.adapter;

import android.support.annotation.Nullable;

import java.util.Collection;

final class Utils {
  /**
   * Compares the two given ints, {@code x} and {@code y}, yielding the following result:
   * <ul>
   * <li>-1 if {@code x} &lt; {@code y}</li>
   * <li>0 if {@code x} == {@code y}</li>
   * <li>1 if {@code x} &gt; {@code y}</li>
   * </ul>
   *
   * @param x the first int to compare.
   * @param y the second int to compare.
   * @return the difference between {@code x} and {@code y} as explained above.
   */
  static int compare(int x, int y) {
    return (x < y) ? -1 : ((x == y) ? 0 : 1);
  }

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
  public static boolean equals(Object a, Object b) {
    return (a == b) || (a != null && a.equals(b));
  }
}
