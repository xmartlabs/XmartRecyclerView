package com.xmartlabs.xmartrecyclerview.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Consumer;

/**
 * View holder for a single item {@link T}.
 *
 * @param <T> The type of the single item.
 */
public class SingleItemBaseViewHolder<T> extends BaseViewHolder implements BindingItemViewHolder<T> {
  @NonNull
  private Optional<Consumer<T>> onClickListener;

  public SingleItemBaseViewHolder(@NonNull View view) {
    this(view, null);
  }

  public SingleItemBaseViewHolder(@NonNull View view, @Nullable Consumer<T> onClickListenerConsumer) {
    super(view);
    this.onClickListener = Optional.ofNullable(onClickListenerConsumer);
  }

  @CallSuper
  @Override
  public void bindItem(@NonNull T item) {
    onClickListener.executeIfPresent(listener ->
        itemView.setOnClickListener(v -> listener.accept(item))
    );
  }
}
