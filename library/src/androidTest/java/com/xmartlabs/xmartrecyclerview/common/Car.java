package com.xmartlabs.xmartrecyclerview.common;

import android.support.annotation.NonNull;

public class Car {
  @NonNull
  private final String model;

  public Car(@NonNull String model) {
    this.model = model;
  }

  @NonNull
  public String getModel() {
    return model;
  }
}
