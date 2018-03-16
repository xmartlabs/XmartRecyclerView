package com.xmartlabs.xmartrecyclerview.sample;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.xmartlabs.xmartrecyclerview.adapter.SingleItemTypeRecyclerViewAdapter;
import com.xmartlabs.xmartrecyclerview.adapter.SingleItemViewHolder;

public class MainAdapter extends SingleItemTypeRecyclerViewAdapter<Person, SingleItemViewHolder<Person>> {
  @NonNull
  @Override
  public SingleItemViewHolder<Person> onCreateViewHolder(@NonNull ViewGroup parent) {
    return new SingleItemViewHolder<>(inflateView(parent, R.layout.item));
  }
}
