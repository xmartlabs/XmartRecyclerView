package com.xmartlabs.xmartrecyclerview.common;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Brand {
  @NonNull
  private List<Car> cars = new ArrayList<>();
  @NonNull
  private String name;

  public Brand(@NonNull String name, @NonNull List<Car> cars) {
    this.cars = cars;
    this.name = name;
  }

  @NonNull
  public List<Car> getCars() {
    return cars;
  }

  @NonNull
  public String getName() {
    return name;
  }
}
