package com.intellisysla.rmsscanner.UI.View.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.DAL.Input;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private Store store;
    private ListView listView;
    private String itemlookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        itemlookup = bundle.getString("itemlookup");

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        store = new Store(
                preferences.getString("server", ""),
                preferences.getString("db", ""),
                preferences.getString("user", ""),
                preferences.getString("pass", ""),
                preferences.getString("name", ""),
                preferences.getString("username", ""),
                preferences.getString("location", ""));

        if(!itemlookup.isEmpty()){
            ExecuteQuery executeQuery = new ExecuteQuery(store, this);
            ArrayList<Input> inputs = executeQuery.getInputs(itemlookup);

            MyAdapter adapter = new MyAdapter(this, inputs);
            listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(adapter);
        }
    }

    public class MyAdapter extends ArrayAdapter<Input>
    {
        public MyAdapter(Context context, ArrayList<Input> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Input input = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
            }
            // Lookup view for data population
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView quantity = (TextView) convertView.findViewById(R.id.description);
            TextView date = (TextView) convertView.findViewById(R.id.description3);
            TextView location = (TextView) convertView.findViewById(R.id.description2);

            // Populate the data into the template view using the data object
            title.setText(input.getName());
            quantity.setText("Qty: " + String.valueOf(input.getQuantity()));
            location.setText("Ubicacion: " + input.getLocation());
            date.setText("Fecha: " + input.getDate().toString());
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
