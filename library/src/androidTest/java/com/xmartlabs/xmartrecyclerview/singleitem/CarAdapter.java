package com.xmartlabs.xmartrecyclerview.singleitem;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmartlabs.xmartrecyclerview.adapter.SingleItemBaseRecyclerViewAdapter;
import com.xmartlabs.xmartrecyclerview.adapter.SingleItemBaseViewHolder;
import com.xmartlabs.xmartrecyclerview.common.Car;
import com.xmartlabs.xmartrecyclerview.test.R;

public class CarAdapter extends SingleItemBaseRecyclerViewAdapter<Car, CarAdapter.CarViewHolder> {
  @NonNull
  @Override
  public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    return new CarViewHolder(inflateView(parent, R.layout.item_single));
  }

  static final class CarViewHolder extends SingleItemBaseViewHolder<Car> {
    private final TextView title;

    public CarViewHolder(@NonNull View view) {
      super(view);
      title = view.findViewById(android.R.id.title);
    }

    @Override
    public void bindItem(@NonNull Car item) {
      super.bindItem(item);
      title.setText(item.getModel());
    }
  }
}
