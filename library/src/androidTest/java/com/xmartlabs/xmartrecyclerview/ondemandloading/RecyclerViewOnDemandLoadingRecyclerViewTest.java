package com.xmartlabs.xmartrecyclerview.ondemandloading;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.xmartlabs.xmartrecyclerview.singleitem.SingleItemActivity;
import com.xmartlabs.xmartrecyclerview.test.R;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RecyclerViewOnDemandLoadingRecyclerViewTest extends BaseOnDemandLoadingRecyclerViewTest<SingleItemActivity> {
  @Rule
  public ActivityTestRule<SingleItemActivity> activityRule = new ActivityTestRule<>(SingleItemActivity.class);

  @Override
  protected SingleItemActivity getActivity() {
    return activityRule.getActivity();
  }

  @Test
  public void checkRecyclerViewLoader() {
    List<Integer> pagesRequested = new ArrayList<>();
    SingleItemActivity activity = getActivity();
    int visibleThreshold = 10;

    BasePageLoadingProvider provider = createLoaderProvider(pagesRequested, activity);
    OnDemandRecyclerViewScrollListener scrollListener = new OnDemandRecyclerViewScrollListener(provider);
    scrollListener.setVisibleThreshold(visibleThreshold);

    activity.getRecyclerView().addOnScrollListener(scrollListener);

    sleep(100);
    assertThat(pagesRequested, is(Collections.singletonList(1)));

    scrollToPosition(activity.getAdapter().getItemCount() - visibleThreshold - 1);
    sleep(100);
    assertThat(pagesRequested, is(Collections.singletonList(1)));

    scrollToPosition(activity.getAdapter().getItemCount() - visibleThreshold);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2)));

    scrollToPosition(activity.getAdapter().getItemCount() - visibleThreshold);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2, 3)));

    scrollToPosition(activity.getAdapter().getItemCount() - 1);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2, 3)));
  }

  @Override
  protected void scrollToPosition(int position) {
    onView(withId(R.id.recycler_view))
        .perform(RecyclerViewActions.scrollToPosition(position));
  }

  @Test
  public void checkAdapterLoader() {
    List<Integer> pagesRequested = new ArrayList<>();
    SingleItemActivity activity = getActivity();
    int visibleThreshold = 10;

    BasePageLoadingProvider provider = createLoaderProvider(pagesRequested, activity);
    activity.getAdapter().setLoader(provider);
    sleep(100);
    assertThat(pagesRequested, is(Collections.singletonList(1)));

    OnDemandLoader loader = activity.getAdapter().getOnDemandLoader();
    //noinspection ConstantConditions
    loader.setVisibleThreshold(visibleThreshold);

    scrollToPosition(activity.getAdapter().getItemCount() - visibleThreshold - 1);
    sleep(100);
    assertThat(pagesRequested, is(Collections.singletonList(1)));

    scrollToPosition(activity.getAdapter().getItemCount() - visibleThreshold);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2)));

    scrollToPosition(activity.getAdapter().getItemCount() - visibleThreshold);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2, 3)));

    scrollToPosition(activity.getAdapter().getItemCount() - 1);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2, 3)));
  }
}
