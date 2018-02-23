package com.xmartlabs.xmartrecyclerview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder {
  public BaseViewHolder(@NonNull View view) {
    super(view);
  }

  /** @return a Resources instance for the application's package */
  @NonNull
  @SuppressWarnings("unused")
  protected final Resources getResources() {
    return getContext().getResources();
  }

  /**
   * Returns the context the view is running in, through which it can
   * access the current theme, resources, etc.
   *
   * @return The view's Context.
   */
  @NonNull
  protected final Context getContext() {
    return itemView.getContext();
  }
}
