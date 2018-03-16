package com.xmartlabs.xmartrecyclerview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xmartlabs.xmartrecyclerview.emptysupport.RecyclerViewEmptySupport;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerViewEmptySupport recyclerView = findViewById(R.id.recyclerView);

    MainAdapter adapter = new MainAdapter();
    recyclerView.setAdapter(adapter);

    adapter.addItem();
  }
}
