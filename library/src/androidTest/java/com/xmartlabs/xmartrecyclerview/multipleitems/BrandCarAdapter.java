package com.xmartlabs.xmartrecyclerview.multipleitems;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.xmartlabs.xmartrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.xmartlabs.xmartrecyclerview.adapter.SingleItemRecyclerViewItemType;
import com.xmartlabs.xmartrecyclerview.adapter.SingleItemViewHolder;
import com.xmartlabs.xmartrecyclerview.common.Brand;
import com.xmartlabs.xmartrecyclerview.common.Car;
import com.xmartlabs.xmartrecyclerview.test.R;

import java.util.List;

public class BrandCarAdapter extends BaseRecyclerViewAdapter<Object, RecyclerView.ViewHolder> {
  private final SingleItemRecyclerViewItemType<Car, CarViewHolder> carItemType =
      new SingleItemRecyclerViewItemType<Car, CarViewHolder>() {
        @NonNull
        @Override
        public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
          return new CarViewHolder(inflateView(parent, R.layout.item_single));
        }
      };

  private final SingleItemRecyclerViewItemType<Brand, BrandViewHolder> brandItemType =
      new SingleItemRecyclerViewItemType<Brand, BrandViewHolder>() {
        @NonNull
        @Override
        public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
          return new BrandViewHolder(inflateView(parent, R.layout.item_single));
        }
      };

  @MainThread
  void setItems(@NonNull List<Brand> brands) {
    Stream.of(brands)
        .forEach(brand -> {
          addItem(brandItemType, brand);
          Stream.ofNullable(brand.getCars())
              .forEach(car -> addItem(carItemType, car));
        });
    notifyDataSetChanged();
  }

  static final class BrandViewHolder extends SingleItemViewHolder<Brand> {
    private final TextView title;

    BrandViewHolder(@NonNull View view) {
      super(view);
      title = view.findViewById(android.R.id.title);
    }

    @Override
    public void bindItem(@NonNull Brand item) {
      super.bindItem(item);

      title.setText(item.getName());
    }
  }

  static final class CarViewHolder extends SingleItemViewHolder<Car> {
    private final TextView title;

    CarViewHolder(@NonNull View view) {
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
