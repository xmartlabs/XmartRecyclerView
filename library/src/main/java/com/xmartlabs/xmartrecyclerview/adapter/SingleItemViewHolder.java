package com.xmartlabs.xmartrecyclerview.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * View holder for a single item {@link T}.
 *
 * @param <T> The type of the single item.
 */
public class SingleItemViewHolder<T> extends BaseViewHolder implements BindingItemViewHolder<T> {
  @Nullable
  private final Consumer<T> onClickListener;

  public SingleItemViewHolder(@NonNull View view) {
    this(view, null);
  }

  @SuppressWarnings("WeakerAccess")
  public SingleItemViewHolder(@NonNull View view, @Nullable Consumer<T> onClickListenerConsumer) {
    super(view);
    this.onClickListener = onClickListenerConsumer;
  }

  @CallSuper
  @Override
  public void bindItem(@NonNull T t) {
    if (onClickListener != null) {
      itemView.setOnClickListener(v -> onClickListener.accept(t));
    }
  }
}
