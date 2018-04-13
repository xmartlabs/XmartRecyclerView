package com.xmartlabs.xmartrecyclerview.paging;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.xmartlabs.xmartrecyclerview.common.Car;
import com.xmartlabs.xmartrecyclerview.common.ListActivity;
import com.xmartlabs.xmartrecyclerview.singleitem.CarAdapter;

import java.util.List;

public abstract class BaseOnDemandLoadingRecyclerViewTest<T extends ListActivity<CarAdapter>> {
  protected static final int PAGE_SIZE = 60;

  @NonNull
  private List<Car> getCars(int page) {
    return Stream.range(PAGE_SIZE * page, PAGE_SIZE * (page + 1))
        .map(integer -> new Car(getModelName(integer)))
        .toList();
  }

  @NonNull
  protected String getModelName(Integer index) {
    return "Model " + index;
  }

  protected abstract T getActivity();

  protected abstract void scrollToPosition(int position);

  @NonNull
  protected BasePageLoader createLoaderProvider(@NonNull List<Integer> pagesRequested, @NonNull T activity) {
    BasePageLoader provider = new BasePageLoader(() -> activity.getAdapter().getItemCount()) {
      @Override
      public void loadPage(int page) {
        activity.getWindow().getDecorView().getHandler()
            .post(() -> {
              pagesRequested.add(page);
              activity.getAdapter().addItems(getCars(page - 1));
            });
      }
    };
    provider.setEntityCount((int) (2.5 * PAGE_SIZE));
    return provider;
  }

  protected void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
