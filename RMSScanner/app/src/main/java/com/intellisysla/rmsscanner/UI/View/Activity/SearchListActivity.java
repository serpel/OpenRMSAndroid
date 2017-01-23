package com.intellisysla.rmsscanner.UI.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;

import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Store store;
    private SearchView searchView;
    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchView = (SearchView) findViewById(R.id.searchView1);
        setupSearchView();

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        store = new Store(
                preferences.getString("server", ""),
                preferences.getString("db", ""),
                preferences.getString("user", ""),
                preferences.getString("pass", ""),
                preferences.getString("name", ""),
                preferences.getString("username", ""),
                preferences.getString("location",""));

        listView = (ListView) findViewById(R.id.listview);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) listView.getItemAtPosition(i);
                Intent intent = new Intent(SearchListActivity.this, SearchActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("location", intent.getExtras().getString("store",""));
                intent.putExtra("username", store.getUser());
                startActivity(intent);
            }
        });

    }



    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        //searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        if(!s.isEmpty()){
            ExecuteQuery executeQuery = new ExecuteQuery(store, this);
            ArrayList<Item> items = executeQuery.getItems(s);

            MyAdapter adapter = new MyAdapter(this, items);
            listView.setAdapter(adapter);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate( R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class MyAdapter extends ArrayAdapter<Item>
    {
        public MyAdapter(Context context, ArrayList<Item> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Item item = getItem(position);
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
            title.setText(item.getItem_code());
            quantity.setText("Descripcion: " + item.getDescription());
            location.setText("Department: " + item.getDepartmentName());
            date.setText("Precio: " + item.getPrice());
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
