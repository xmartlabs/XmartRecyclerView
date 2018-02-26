package com.xmartlabs.xmartrecyclerview.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmartlabs.xmartrecyclerview.common.BiFunction;
import com.xmartlabs.xmartrecyclerview.common.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * A Base RecyclerViewAdapter with already implemented functions such as
 * Setting, removing, adding items, getting its count among others.
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  @NonNull
  private final List<Element> items;
  @NonNull
  private final List<RecycleItemType> types = new ArrayList<>();
  private final UpdateItemsQueuedManager updateItemsQueuedManager = new UpdateItemsQueuedManager();

  public BaseRecyclerViewAdapter() {
    this(new ArrayList<>());
  }

  @SuppressWarnings("WeakerAccess")
  public BaseRecyclerViewAdapter(@NonNull List<Element> items) {
    this.items = items;
  }

  @Override
  public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //noinspection unchecked
    return types.get(viewType).onCreateViewHolder(parent);
  }

  /**
   * Removes an item from the data and any registered observers of its removal.
   *
   * @param item the item to be removed.
   */
  @MainThread
  public void removeItem(@NonNull Object item) {
    removeItemsByCondition(element -> Utils.equals(item, element.getItem()));
  }

  /**
   * Removes a list of items from the data and any registered observers of its removal.
   *
   * @param items the list of items to be removed.
   */
  @MainThread
  @SuppressWarnings("WeakerAccess")
  public void removeItems(@NonNull List<Object> items) {
    for (Object item : items) {
      removeItem(item);
    }
  }

  /**
   * Adds an item to the data for the recycler view without notifying any registered observers that an item has been
   * added.
   *
   * @param type            The type of the item.
   * @param item            The item to be added.
   * @param addTypeIfNeeded A boolean specifying if the item type has to be added to the type collection.
   *                        If this parameter is true, the type will be added only if it wasn't added yet.
   */
  @SuppressWarnings("WeakerAccess")
  protected <T extends RecycleItemType> void addItemWithoutNotifying(@NonNull T type, @NonNull Object item,
                                                                     boolean addTypeIfNeeded) {
    addItemWithoutNotifying(items.size(), type, item, addTypeIfNeeded);
  }

  /**
   * Adds an item to the data for the recycler view without notifying any registered observers that an item has been
   * added.
   *
   * @param index           The index at which the specified items are to be inserted.
   * @param type            The type of the item.
   * @param item            The item to be added.
   * @param addTypeIfNeeded A boolean specifying if the item type has to be added to the type collection.
   *                        If this parameter is true, the type will be added only if it wasn't added yet.
   */
  private <T extends RecycleItemType> void addItemWithoutNotifying(int index, @NonNull T type, @Nullable Object item,
                                                                   boolean addTypeIfNeeded) {
    Element element = new Element(type, item);
    items.add(index, element);
    if (addTypeIfNeeded) {
      addItemTypeIfNeeded(type);
    }
  }

  /**
   * Add the type to the collection type only if it is needed.
   *
   * @param type The type to be added.
   */
  private <T extends RecycleItemType> void addItemTypeIfNeeded(@NonNull T type) {
    if (!types.contains(type)) {
      types.add(type);
    }
  }

  /**
   * Adds an item to the data for the recycler view and notifies any registered observers that an item has been added.
   *
   * @param type The type of the item.
   * @param item The item to be added.
   */
  @MainThread
  protected <T extends RecycleItemType> void addItem(@NonNull T type, @NonNull Object item) {
    addItemWithoutNotifying(type, item, true);
    notifyItemInserted(items.size() - 1);
  }

  /**
   * Adds items to the data for the recycler view and notifies any registered observers that the items has been added.
   *
   * @param index The index at which the specified items are to be inserted.
   * @param type  The type of the item.
   * @param items The items that will be the data for the recycler view.
   * @return if items was successfully added.
   */
  @MainThread
  @SuppressWarnings("WeakerAccess")
  protected <T extends RecycleItemType> boolean addItems(int index, @NonNull T type, @Nullable List<?> items) {
    if (Utils.isNullOrEmpty(items)) {
      return false;
    }
    int lastItemCount = getItemCount();
    for (int i = 0; i < items.size(); i++) {
      Object item = items.get(0);
      addItemWithoutNotifying(index + i, type, item, false);
    }
    addItemTypeIfNeeded(type);
    notifyItemRangeInserted(index, getItemCount() - lastItemCount);
    return true;
  }

  /**
   * Adds items to the data for the recycler view and notifies any registered observers that the items has been added.
   *
   * @param type  The type of the items.
   * @param items the items that will be the data for the recycler view.
   * @return if item was successfully added.
   */
  @MainThread
  @SuppressWarnings("WeakerAccess")
  protected <T extends RecycleItemType> boolean addItems(@NonNull T type, @Nullable List<?> items) {
    if (Utils.isNullOrEmpty(items)) {
      return false;
    }
    int lastItemCount = getItemCount();
    for (Object item : items) {
      addItemWithoutNotifying(type, item, false);
    }

    addItemTypeIfNeeded(type);
    if (lastItemCount == 0) {
      notifyDataSetChanged();
    } else {
      notifyItemRangeInserted(lastItemCount, getItemCount() - lastItemCount);
    }
    return true;
  }

  /**
   * Sets the items data for the recycler view and notifying any registered observers that the data set has
   * changed. It uses a function that calculates the difference between the old and the new items
   * in order to improve the update process.
   *
   * @param type                      Type of items.
   * @param newItems                  The items tobe added.
   * @param areItemsTheSameFunction   A function which checks that two items are the same.
   * @param areContentTheSameFunction A function which checks that the content of two items are the same.
   */
  @SuppressWarnings("WeakerAccess")
  protected <T, R extends RecycleItemType> void setItems(@NonNull R type, final @Nullable List<T> newItems,
                                                         @NonNull BiFunction<T, T, Boolean> areItemsTheSameFunction,
                                                         @NonNull BiFunction<T, T, Boolean> areContentTheSameFunction) {
    if (Utils.isNullOrEmpty(newItems)) {
      clearAll();
      return;
    }

    DiffUtil.Callback updateDiffCallback = getUpdateDiffCallback(
        newItems,
        areItemsTheSameFunction,
        areContentTheSameFunction
    );

    updateItemsQueuedManager.update(updateDiffCallback, diffResult -> {
      items.clear();
      for (T item : newItems) {
        addItemWithoutNotifying(type, item, false);
      }

      addItemTypeIfNeeded(type);
      diffResult.dispatchUpdatesTo(BaseRecyclerViewAdapter.this);
    });
  }

  /**
   * Sets the items data for the recycler view and notifying any registered observers that the data set has
   * changed. It uses a function that calculates the difference between the old and the new items
   * in order to improve the update process.
   *
   * @param <T>                       Type of the items to be added.
   * @param newItems                  Items to be added. Each Pair consists of an item and its RecycleItemType.
   * @param areItemsTheSameFunction   A function which checks that two items are the same.
   * @param areContentTheSameFunction A function which checks that the content of two items are the same.
   */
  protected <T> void setMultipleTypeItems(final @Nullable List<Pair<? extends RecycleItemType, T>> newItems,
                                          @NonNull BiFunction<T, T, Boolean> areItemsTheSameFunction,
                                          @NonNull BiFunction<T, T, Boolean> areContentTheSameFunction) {
    if (Utils.isNullOrEmpty(newItems)) {
      clearAll();
      return;
    }


    final List<T> newItemsContent = new ArrayList<>();
    for (Pair<? extends RecycleItemType, T> item : newItems) {
      newItemsContent.add(item.second);
    }

    DiffUtil.Callback updateDiffCallback = getUpdateDiffCallback(
        newItemsContent,
        areItemsTheSameFunction,
        areContentTheSameFunction
    );

    updateItemsQueuedManager.update(updateDiffCallback, diffResult -> {
      items.clear();
      for (Pair<? extends RecycleItemType, T> pair : newItems) {
        addItemWithoutNotifying(pair.first, pair.second, true);
      }
      diffResult.dispatchUpdatesTo(this);
    });
  }

  @NonNull
  private <T> DiffUtil.Callback getUpdateDiffCallback(
      @NonNull final List<T> newItems,
      @NonNull final BiFunction<T, T, Boolean> areItemsTheSameFunction,
      @NonNull final BiFunction<T, T, Boolean> areContentTheSameFunction) {
    return new DiffUtil.Callback() {
      @Override
      public int getOldListSize() {
        return items.size();
      }

      @Override
      public int getNewListSize() {
        return newItems.size();
      }

      @Override
      public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        try {
          //noinspection unchecked
          return areItemsTheSameFunction.apply(
              (T) items.get(oldItemPosition).getItem(),
              newItems.get(newItemPosition));
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
      }

      @Override
      public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        try {
          //noinspection unchecked
          return areContentTheSameFunction.apply(
              (T) items.get(oldItemPosition).getItem(),
              newItems.get(newItemPosition));
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
      }
    };
  }

  /**
   * Sets the items data for the recycler view and notifies any registered observers that the data set has
   * changed.
   *
   * @param type     Type of items.
   * @param newItems The items tobe added.
   */
  @MainThread
  @SuppressWarnings("WeakerAccess")
  protected <T extends RecycleItemType> void setItems(@NonNull T type, @Nullable List<?> newItems) {
    items.clear();
    addItems(type, newItems);
  }

  /**
   * Gets all the items count, including dividers.
   *
   * @return number of total items.
   */
  @Override
  public int getItemCount() {
    return items.size();
  }

  /**
   * Inflates the view layout/elements.
   *
   * @param parent      the parent viewgroup.
   * @param layoutResId the layout resource id.
   * @return the inflated view.
   */
  protected static View inflateView(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return layoutInflater.inflate(layoutResId, parent, false);
  }

  @CallSuper
  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    Element element = items.get(position);
    Object item = element.getItem();
    //noinspection unchecked
    element.getType().onBindViewHolder(viewHolder, item, position);
    if (viewHolder instanceof BindingItemViewHolder) {
      //noinspection unchecked
      ((BindingItemViewHolder) viewHolder).bindItem(item);
    }
  }

  /**
   * Gets the item type.
   *
   * @param position of the item among all items conforming the recycler view.
   * @return item divider type.
   */
  @Override
  public int getItemViewType(int position) {
    return types.indexOf(items.get(position).getType());
  }

  /**
   * Removes all items and notifies that the data has changed.
   */
  @MainThread
  @SuppressWarnings("WeakerAccess")
  public void clearAll() {
    items.clear();
    notifyDataSetChanged();
  }

  /**
   * Removes all items in the recyclerView of a specified type.
   *
   * @param itemType The specified type of items to be removed.
   */
  @MainThread
  @SuppressWarnings("unused")
  public void clearItems(@NonNull RecycleItemType itemType) {
    removeItemsByCondition(element -> Utils.equals(element.getType(), itemType));
  }

  private void removeItemsByCondition(@NonNull Function<Element, Boolean> conditionToRemoveItem) {
    if (Utils.isNullOrEmpty(items)) {
      return;
    }

    List<Integer> indexesToRemove = new ArrayList<>();
    for (int i = 0; i < items.size(); i++) {
      Element element = items.get(i);
      if (conditionToRemoveItem.apply(element)) {
        indexesToRemove.add(i);
      }
    }

    for (int i = indexesToRemove.size() - 1; i >= 0; i--) {
      int index = indexesToRemove.get(i);
      items.remove(index);
      notifyItemRemoved(index);
    }
  }

  private static class Element {
    @NonNull
    private final RecycleItemType type;
    @NonNull
    private final Object item;

    Element(@NonNull RecycleItemType type, @NonNull Object item) {
      this.type = type;
      this.item = item;
    }

    @NonNull
    RecycleItemType getType() {
      return type;
    }

    @NonNull
    Object getItem() {
      return item;
    }
  }
}
