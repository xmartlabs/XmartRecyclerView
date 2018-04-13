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

import com.xmartlabs.xmartrecyclerview.internal.paging.BaseOnDemandPageLoader;
import com.xmartlabs.xmartrecyclerview.internal.ItemCounter;
import com.xmartlabs.xmartrecyclerview.paging.OnDemandPageLoader;
import com.xmartlabs.xmartrecyclerview.paging.PageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A Base RecyclerViewAdapter with already implemented functions such as
 * setting, removing, adding items, getting its count among others.
 *
 * @param <T>  Items class.
 * @param <VH> A class that extends ViewHolder that will be used by the adapter.
 */
@SuppressWarnings("unused")
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
    implements ItemCounter {
  @NonNull
  private final List<Element<? extends T, ? extends VH, ?>> elements = new ArrayList<>();
  @NonNull
  private final List<RecycleItemType<? extends T, ? extends VH>> types = new ArrayList<>();
  private final UpdateItemsQueuedManager updateItemsQueuedManager = new UpdateItemsQueuedManager();
  @Nullable
  private BaseOnDemandPageLoader onDemandLoader;

  public BaseRecyclerViewAdapter() {}

  @Override
  public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return types.get(viewType).onCreateViewHolder(parent);
  }

  public void setPageLoader(@NonNull PageLoader pageLoader) {
    onDemandLoader = new BaseOnDemandPageLoader(pageLoader, this);
  }

  @Nullable
  public OnDemandPageLoader getOnDemandLoader() {
    return onDemandLoader;
  }

  /**
   * Removes an item from the data and any registered observers of its removal.
   *
   * @param item the item to be removed.
   */
  @MainThread
  public void removeItem(@NonNull T item) {
    removeItemsByCondition(element -> Utils.equals(item, element.getItem()));
  }

  /**
   * Removes a list of items from the data and any registered observers of its removal.
   *
   * @param items the list of items to be removed.
   */
  @MainThread
  public void removeItems(@NonNull List<? extends T> items) {
    for (T item : items) {
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
  protected <I extends T, H extends VH, VT extends RecycleItemType<I, H>> void addItemWithoutNotifying(@NonNull VT type,
                                                                                                       @NonNull I item,
                                                                                                       boolean addTypeIfNeeded) {
    addItemWithoutNotifying(elements.size(), type, item, addTypeIfNeeded);
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
  private <I extends T, H extends VH, VT extends RecycleItemType<I, H>> void addItemWithoutNotifying(int index,
                                                                                                     @NonNull VT type,
                                                                                                     @NonNull I item,
                                                                                                     boolean addTypeIfNeeded) {
    Element<I, H, VT> element = new Element<>(type, item);
    elements.add(index, element);
    if (addTypeIfNeeded) {
      addItemTypeIfNeeded(type);
    }
  }

  /**
   * Add the type to the collection type only if it is needed.
   *
   * @param type The type to be added.
   */
  private <VT extends RecycleItemType<? extends T, ? extends VH>> void addItemTypeIfNeeded(@NonNull VT type) {
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
  protected <I extends T, H extends VH, VT extends RecycleItemType<I, H>> void addItem(@NonNull VT type,
                                                                                       @NonNull I item) {
    addItemWithoutNotifying(type, item, true);
    notifyItemInserted(elements.size() - 1);
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
  protected <VT extends RecycleItemType<T, VH>> boolean addItems(int index, @NonNull VT type,
                                                                 @Nullable List<? extends T> items) {
    if (Utils.isNullOrEmpty(items)) {
      return false;
    }
    int lastItemCount = getItemCount();
    for (int i = 0; i < items.size(); i++) {
      T item = items.get(i);
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
  protected <VT extends RecycleItemType<T, VH>> boolean addItems(@NonNull VT type, @Nullable List<? extends T> items) {
    if (Utils.isNullOrEmpty(items)) {
      return false;
    }
    int lastItemCount = getItemCount();
    for (T item : items) {
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
  protected <VT extends RecycleItemType<T, VH>> void setItems(@NonNull VT type,
                                                              final @Nullable List<? extends T> newItems,
                                                              final @NonNull BiFunction<T, T, Boolean> areItemsTheSameFunction,
                                                              final @NonNull BiFunction<T, T, Boolean> areContentTheSameFunction) {
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
      elements.clear();
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
   * @param newItems                  Items to be added. Each Pair consists of an item and its RecycleItemType.
   * @param areItemsTheSameFunction   A function which checks that two items are the same.
   * @param areContentTheSameFunction A function which checks that the content of two items are the same.
   */
  protected void setMultipleTypeItems(final @Nullable List<Pair<? extends RecycleItemType<T, VH>, T>> newItems,
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
      elements.clear();
      for (Pair<? extends RecycleItemType<T, VH>, T> pair : newItems) {
        //noinspection ConstantConditions
        addItemWithoutNotifying(pair.first, pair.second, true);
      }
      diffResult.dispatchUpdatesTo(this);
    });
  }

  @NonNull
  private DiffUtil.Callback getUpdateDiffCallback(
      @NonNull final List<? extends T> newItems,
      @NonNull final BiFunction<T, T, Boolean> areItemsTheSameFunction,
      @NonNull final BiFunction<T, T, Boolean> areContentTheSameFunction) {
    return new DiffUtil.Callback() {
      @Override
      public int getOldListSize() {
        return elements.size();
      }

      @Override
      public int getNewListSize() {
        return newItems.size();
      }

      @Override
      public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        try {
          return areItemsTheSameFunction.apply(
              elements.get(oldItemPosition).getItem(),
              newItems.get(newItemPosition));
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
      }

      @Override
      public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        try {
          return areContentTheSameFunction.apply(
              elements.get(oldItemPosition).getItem(),
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
  protected <VT extends RecycleItemType<T, VH>> void setItems(@NonNull VT type, @Nullable List<? extends T> newItems) {
    elements.clear();
    addItems(type, newItems);
  }

  /**
   * Gets all the items count, including dividers.
   *
   * @return number of total items.
   */
  @Override
  public int getItemCount() {
    return elements.size();
  }

  /**
   * Inflates the view layout/elements.
   *
   * @param parent      the parent ViewGroup.
   * @param layoutResId the layout resource id.
   * @return the inflated view.
   */
  protected static View inflateView(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return layoutInflater.inflate(layoutResId, parent, false);
  }

  @CallSuper
  @Override
  public void onBindViewHolder(@NonNull VH viewHolder, int position) {
    if (onDemandLoader != null) {
      onDemandLoader.onItemConsumed(position);
    }
    Element element = elements.get(position);
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
    return types.indexOf(elements.get(position).getType());
  }

  /**
   * Removes all items and notifies that the data has changed.
   */
  @MainThread
  @SuppressWarnings("WeakerAccess")
  public void clearAll() {
    elements.clear();
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
    if (Utils.isNullOrEmpty(elements)) {
      return;
    }

    List<Integer> indexesToRemove = new ArrayList<>();
    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);
      if (conditionToRemoveItem.apply(element)) {
        indexesToRemove.add(i);
      }
    }

    for (int i = indexesToRemove.size() - 1; i >= 0; i--) {
      int index = indexesToRemove.get(i);
      elements.remove(index);
      notifyItemRemoved(index);
    }
  }

  private static class Element<T, VH extends RecyclerView.ViewHolder, VT extends RecycleItemType<T, VH>> {
    @NonNull
    private final VT type;
    @NonNull
    private final T item;

    Element(@NonNull VT type, @NonNull T item) {
      this.type = type;
      this.item = item;
    }

    @NonNull
    VT getType() {
      return type;
    }

    @NonNull
    T getItem() {
      return item;
    }
  }
}
