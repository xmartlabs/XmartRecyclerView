package com.xmartlabs.xmartrecyclerview.sample;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmartlabs.xmartrecyclerview.adapter.SingleItemTypeRecyclerViewAdapter;
import com.xmartlabs.xmartrecyclerview.adapter.SingleItemViewHolder;

public class PersonAdapter extends SingleItemTypeRecyclerViewAdapter<Person, PersonAdapter.PersonViewHolder> {
  @NonNull
  @Override
  public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    return new PersonViewHolder(inflateView(parent, R.layout.item));
  }

  static class PersonViewHolder extends SingleItemViewHolder<Person> {
    private final TextView textView;

    PersonViewHolder(@NonNull View view) {
      super(view);
      textView = view.findViewById(android.R.id.text1);
    }

    @Override
    public void bindItem(@NonNull Person person) {
      super.bindItem(person);
      textView.setText(person.getName());
    }
  }
}
