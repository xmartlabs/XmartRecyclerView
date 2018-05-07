package com.xmartlabs.xmartrecyclerview.paging.nestedscroll;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.xmartlabs.xmartrecyclerview.common.NestedScrollViewScrollToAction;
import com.xmartlabs.xmartrecyclerview.paging.BaseOnDemandLoadingRecyclerViewTest;
import com.xmartlabs.xmartrecyclerview.paging.BasePageLoader;
import com.xmartlabs.xmartrecyclerview.paging.MetricsHelper;
import com.xmartlabs.xmartrecyclerview.paging.OnDemandPageNestedScrollViewListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class NestedScrollViewOnDemandLoadingRecyclerViewTest extends BaseOnDemandLoadingRecyclerViewTest<OnDemandLoadingActivity> {
  @Rule
  public ActivityTestRule<OnDemandLoadingActivity> activityRule = new ActivityTestRule<>(OnDemandLoadingActivity.class);

  @Override
  protected OnDemandLoadingActivity getActivity() {
    return activityRule.getActivity();
  }

  @Test
  public void testNestedScroll() {
    OnDemandLoadingActivity activity = activityRule.getActivity();
    List<Integer> pagesRequested = new ArrayList<>();
    int visibleThreshold = MetricsHelper.dpToPxInt(activity.getResources(), 100);
    int visibleThresholdCount = 5; // TextView view size is 20dp -> 100 / 20 = 5

    BasePageLoader provider = createLoaderProvider(pagesRequested, activity);
    OnDemandPageNestedScrollViewListener scrollListener = new OnDemandPageNestedScrollViewListener(activity, provider);
    scrollListener.setVisibleThreshold(visibleThreshold);

    activity.getNestedScrollView().setOnScrollChangeListener(scrollListener);

    sleep(100);
    assertThat(pagesRequested, is(Collections.singletonList(1)));

    scrollToPosition(PAGE_SIZE - visibleThresholdCount - 1);

    sleep(100);
    assertThat(pagesRequested, is(Collections.singletonList(1)));

    scrollToPosition(PAGE_SIZE - visibleThresholdCount);

    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2)));

    scrollToPosition(PAGE_SIZE * 2 - visibleThresholdCount);

    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2, 3)));

    scrollToTheBottom(activity);
    sleep(100);
    assertThat(pagesRequested, is(Arrays.asList(1, 2, 3)));
  }

  @Override
  protected void scrollToPosition(int position) {
    onView(withText(getModelName(position)))
        .perform(NestedScrollViewScrollToAction.scrollTo());
  }

  private void scrollToTheBottom(@NonNull OnDemandLoadingActivity activity) {
    activity.getWindow().getDecorView().getHandler()
        .post(() -> activity.getNestedScrollView().fullScroll(View.FOCUS_DOWN));
  }
}
