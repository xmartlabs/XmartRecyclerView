package com.xmartlabs.xmartrecyclerview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import com.xmartlabs.xmartrecyclerview.emptysupport.RecyclerViewEmptySupport;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
  private boolean loading = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecyclerViewEmptySupport recyclerView = findViewById(R.id.recyclerView);
    Button button = findViewById(R.id.populate_button);

    PersonAdapter adapter = new PersonAdapter();
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    recyclerView.setIsInLoadingStateProvider(() -> loading);

    button.setOnClickListener(view -> {
      loading = true;
      adapter.notifyDataSetChanged();

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          loading = false;
          recyclerView.post(() -> {
            adapter.addItem(new Person(1, "Homer Simpson"));
            adapter.addItem(new Person(2, "Marge Simpson"));
          });
        }
      }, 3000L);
    });
  }
}
