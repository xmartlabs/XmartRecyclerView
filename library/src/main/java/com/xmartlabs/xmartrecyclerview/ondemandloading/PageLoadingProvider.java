package com.xmartlabs.xmartrecyclerview.ondemandloading;

/** Provides the necessary operations to allow on demand loading while scrolling a {@link android.support.v7.widget.RecyclerView} */
public interface PageLoadingProvider {
  /**
   * Called when the scroll position of the RecyclerView reaches the end of the current page.
   *
   * @param page The next page to be loaded.
   */
  void loadPage(int page);

  boolean hasMorePages();

  int getFirstPage();
}
