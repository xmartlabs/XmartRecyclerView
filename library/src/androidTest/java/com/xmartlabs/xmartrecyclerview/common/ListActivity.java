package com.xmartlabs.xmartrecyclerview.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xmartlabs.xmartrecyclerview.test.R;

public abstract class ListActivity<T extends RecyclerView.Adapter> extends Activity {
  private RecyclerView recyclerView;

  @NonNull
  private final T adapter = createAdapter();

  @NonNull
  public T getAdapter() {
    return adapter;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutRes());
    recyclerView = findViewById(R.id.recycler_view);
    setupRecyclerViewAdapter();
  }

  @LayoutRes
  protected int getLayoutRes() {
    return R.layout.activity_with_list;
  }

  @NonNull
  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  private void setupRecyclerViewAdapter() {
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }

  public abstract T createAdapter();
}
