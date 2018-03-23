package com.xmartlabs.xmartrecyclerview.ondemandloading.nestedscroll;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;

import com.xmartlabs.xmartrecyclerview.common.ListActivity;
import com.xmartlabs.xmartrecyclerview.singleitem.CarAdapter;
import com.xmartlabs.xmartrecyclerview.test.R;

public class OnDemandLoadingActivity extends ListActivity<CarAdapter> {
  NestedScrollView nestedScrollView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    nestedScrollView = findViewById(R.id.nestedScrollView);
    getRecyclerView().setNestedScrollingEnabled(false);
  }

  @NonNull
  public NestedScrollView getNestedScrollView() {
    return nestedScrollView;
  }

  @LayoutRes
  protected int getLayoutRes() {
    return R.layout.activity_with_nested_scroll_view_and_list;
  }

  @Override
  public CarAdapter createAdapter() {
    return new CarAdapter();
  }
}
