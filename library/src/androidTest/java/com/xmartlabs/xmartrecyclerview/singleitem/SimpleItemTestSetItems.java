package com.xmartlabs.xmartrecyclerview.singleitem;

import com.xmartlabs.xmartrecyclerview.common.Car;

import org.junit.Test;

import java.util.List;

public class SimpleItemTestSetItems extends SimpleItemRecyclerViewTest {
  @Test
  public void testSetItems() {
    List<Car> cars = getCarList();

    SingleItemActivity activity = activityRule.getActivity();
    activity.runOnUiThread(() -> activity.getAdapter().setItems(cars));

    checkItems(cars);
  }
}
