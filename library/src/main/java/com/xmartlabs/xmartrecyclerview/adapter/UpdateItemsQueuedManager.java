package com.xmartlabs.xmartrecyclerview.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.xmartlabs.xmartrecyclerview.common.Consumer;

import java.util.ArrayDeque;

final class UpdateItemsQueuedManager {
  private final ArrayDeque<QueueItem> mPendingUpdates = new ArrayDeque<>();
  private final Handler mHandler = new Handler(Looper.getMainLooper());

  @MainThread
  void update(@NonNull DiffUtil.Callback diffCallback,
              @NonNull Consumer<DiffUtil.DiffResult> diffResultConsumer) {
    QueueItem queueItem = new QueueItem(diffCallback, diffResultConsumer);
    mPendingUpdates.add(queueItem);
    if (mPendingUpdates.size() == 1) {
      internalUpdate(queueItem);
    }
  }

  private void internalUpdate(@NonNull QueueItem queueItem) {
    new Thread(() -> {
      final DiffUtil.DiffResult result = DiffUtil.calculateDiff(queueItem.diffCallback, false);
      mHandler.post(() -> {
        queueItem.diffResultConsumer.accept(result);
        processQueue();
      });
    }).start();
  }

  @MainThread
  private void processQueue() {
    mPendingUpdates.remove();
    if (!mPendingUpdates.isEmpty()) {
      if (mPendingUpdates.size() > 1) {
        QueueItem last = mPendingUpdates.peekLast();
        mPendingUpdates.clear();
        mPendingUpdates.add(last);
      }
      internalUpdate(mPendingUpdates.peek());
    }
  }

  private static class QueueItem {
    @NonNull
    private final DiffUtil.Callback diffCallback;
    @NonNull
    private final Consumer<DiffUtil.DiffResult> diffResultConsumer;

    QueueItem(@NonNull DiffUtil.Callback diffCallback,
              @NonNull Consumer<DiffUtil.DiffResult> diffResultConsumer) {
      this.diffCallback = diffCallback;
      this.diffResultConsumer = diffResultConsumer;
    }
  }
}
