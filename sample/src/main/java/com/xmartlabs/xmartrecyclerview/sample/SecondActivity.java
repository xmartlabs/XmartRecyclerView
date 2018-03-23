package com.xmartlabs.xmartrecyclerview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SecondActivity extends Activity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);

    RecyclerView recyclerView = findViewById(R.id.recyclerView);

    PersonAdapter adapter = new PersonAdapter(); // Change to another adapter type.
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    adapter.addItem(new Person(1, "Homer Simpson"));
    adapter.addItem(new Person(2, "Marge Simpson"));
  }
}
