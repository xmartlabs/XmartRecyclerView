package com.xmartlabs.xmartrecyclerview.paging;

/**
 * Provides a way to load {@link android.support.v7.widget.RecyclerView} pages sequentially.
 *
 * This class is used to load new pages sequentially as the RecyclerView is scrolled.
 *
 * The pages could be loaded from any source (e.g. a REST API or a local database) and its items must be added to the
 * RecyclerView adapter.
 *
 * Note that this class' methods will typically be called from the main thread.
 */
public interface PageLoader {
  /**
   * Loads a page, whose items must end up in the RecyclerView adapter.
   *
   * @param page The page to be loaded.
   */
  void loadPage(int page);

  /**
   * Returns true if there are more pages to load.
   *
   * Typically, the implementation should track the current page to know if there are more pages to load.
   *
   * @return If there are more pages to use.
   */
  boolean hasMorePages();

  /**
   * Returns the index of the first page.
   *
   * Typically this method should return 1.
   *
   * @return the index of the first page.
   */
  int getFirstPageIndex();
}
