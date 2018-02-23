package com.xmartlabs.xmartrecyclerview.singleitem;

import com.xmartlabs.xmartrecyclerview.common.ListActivity;

public class SingleItemActivity extends ListActivity<CarAdapter> {
  @Override
  public CarAdapter createAdapter() {
    return new CarAdapter();
  }
}
