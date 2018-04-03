package com.xmartlabs.xmartrecyclerview.ondemandloading;

/** Provides the necessary operations to take control of the loading on demand  */
public interface PageLoadingProvider {
  /**
   * Called when the scroll position of the RecyclerView reaches the end of the current page.
   *
   * @param page The next page to be loaded.
   */
  void loadPage(int page);

  /**
   * Called to know if there are more pages to request.
   * The result will be used before call {@link #loadPage(int)} method.
   *
   * @return If there are more pages to use.
   */
  boolean hasMorePages();

  /**
   * Called in the initialization to setup the first page to be requested.
   *
   * @return the number of the first page to be requested.
   */
  int getFirstPage();
}
