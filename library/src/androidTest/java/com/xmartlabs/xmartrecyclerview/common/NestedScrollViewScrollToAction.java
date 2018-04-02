package com.xmartlabs.xmartrecyclerview.common;

import android.graphics.Rect;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers.Visibility;
import android.support.test.espresso.util.HumanReadables;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

/**
 * Enables scrolling to the given view. View must be a descendant of a ScrollView.
 */
public final class NestedScrollViewScrollToAction implements ViewAction {
  private static final String TAG = NestedScrollViewScrollToAction.class.getSimpleName();

  public static NestedScrollViewScrollToAction scrollTo() {
    return new NestedScrollViewScrollToAction();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Matcher<View> getConstraints() {
    return allOf(withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(anyOf(
        isAssignableFrom(NestedScrollView.class))));
  }

  @Override
  public void perform(UiController uiController, View view) {
    if (isDisplayingAtLeast(90).matches(view)) {
      Log.i(TAG, "View is already displayed. Returning.");
      return;
    }

    View parentScrollView = findScrollView(view);

    parentScrollView.requestLayout();

    uiController.loopMainThreadUntilIdle();

    Rect rect = new Rect();
    view.getDrawingRect(rect);
    if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
      Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.");
    }

    uiController.loopMainThreadUntilIdle();

    if (!isDisplayingAtLeast(90).matches(view)) {
      throw new PerformException.Builder()
          .withActionDescription(this.getDescription())
          .withViewDescription(HumanReadables.describe(view))
          .withCause(new RuntimeException(
              "Scrolling to view was attempted, but the view is not displayed"))
          .build();
    }
  }

  private View findScrollView(View view) {
    View parent = (View) view.getParent();
    if (parent != null) {
      if (parent instanceof NestedScrollView) {
        return parent;
      }
      return findScrollView(parent);
    }
    throw new PerformException.Builder()
        .withActionDescription(this.getDescription())
        .withViewDescription(HumanReadables.describe(view))
        .withCause(new RuntimeException(
            "Scrolling aborted due to not being NestedScrollView child"))
        .build();
  }

  @Override
  public String getDescription() {
    return "scroll to";
  }
}
