package com.xmartlabs.xmartrecyclerview.multipleitems;

import android.support.annotation.NonNull;

import com.xmartlabs.xmartrecyclerview.common.Brand;
import com.xmartlabs.xmartrecyclerview.common.ListActivity;

import java.util.List;

public class MultipleItemActivity extends ListActivity<BrandCarAdapter> {
  @Override
  public BrandCarAdapter createAdapter() {
    return new BrandCarAdapter();
  }

  public void setItems(@NonNull List<Brand> brands) {
    getAdapter().setItems(brands);
  }
}
