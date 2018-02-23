package com.xmartlabs.xmartrecyclerview.singleitem;

import com.annimon.stream.Stream;
import com.xmartlabs.xmartrecyclerview.common.Car;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleItemTestAddItems extends SimpleItemRecyclerViewTest {
  private void testAddItem(List<Car> initialItems, List<Car> itemsToAdd) {
    SingleItemActivity activity = activityRule.getActivity();
    CarAdapter activityCarAdapter = activity.getAdapter();
    activity.runOnUiThread(() -> activityCarAdapter.setItems(initialItems));

    checkItems(initialItems);
    activity.runOnUiThread(() -> activityCarAdapter.addItems(itemsToAdd));

    List<Car> resultList = Stream
        .concat(
            Stream.of(initialItems),
            Stream.of(itemsToAdd))
        .toList();
    checkItems(resultList);
  }

  @Test
  public void testAddOneItem() {
    testAddItem(getCarList(), Collections.singletonList(new Car("New car")));
  }

  @Test
  public void testAddOneItemToAnEmptyList() {
    testAddItem(Collections.emptyList(), Collections.singletonList(new Car("New car")));
  }

  @Test
  public void testAddMultipleItem() {
    List<Car> cars = getCarList();
    Car car1 = new Car("New Car");
    Car car2 = new Car("New Car2");
    List<Car> carList = Arrays.asList(car1, car2);
    testAddItem(cars, carList);
  }

  @Test
  public void testAddOneItemTwice() {
    List<Car> cars = getCarList();
    Car car1 = new Car("New Car");
    List<Car> carList = Arrays.asList(car1, car1);
    testAddItem(cars, carList);
  }
}
