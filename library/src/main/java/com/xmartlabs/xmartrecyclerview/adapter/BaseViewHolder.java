package com.xmartlabs.xmartrecyclerview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * A Base RecyclerView.ViewHolder with already implemented functions such as
 * get ViewHolder context.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
  @SuppressWarnings("WeakerAccess")
  public BaseViewHolder(@NonNull View view) {
    super(view);
  }

  /** Returns a Resources instance for the application's package */
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
  @SuppressWarnings("WeakerAccess")
  protected final Context getContext() {
    return itemView.getContext();
  }
}
