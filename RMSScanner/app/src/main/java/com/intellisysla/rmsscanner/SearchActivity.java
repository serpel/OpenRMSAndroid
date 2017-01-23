package com.intellisysla.rmsscanner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.DAL.Category;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText search_edit, new_quantity_edit, offer_edit;
    Button save_button;
    TextView description_text, itemcode_text, quantity_text, fisic_quantity, price_text, search_text,
            store_text, offer_text, begin_text, end_text;
    Spinner department_spinner, categoy_spinner;
    LinearLayout item_menu, item_list_menu, search_menu;
    String code, location, username ;
    Item foundItem;
    Store store;
    ArrayList<Department> departments;
    ImageView imageView;
    ProgressBar pbHeaderProgress;

    public static final String TAG = "SearchActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        pbHeaderProgress = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        imageView = (ImageView) findViewById(R.id.back_button);
        item_menu = (LinearLayout) findViewById(R.id.item_menu);
        item_list_menu = (LinearLayout) findViewById(R.id.item_list_menu);
        search_menu = (LinearLayout) findViewById(R.id.search_menu);

        search_edit = (EditText) findViewById(R.id.seach_edit);
        search_text = (TextView) findViewById(R.id.search_text);
        save_button = (Button) findViewById(R.id.save_button);
        itemcode_text = (TextView) findViewById(R.id.itemcode_text);
        description_text = (TextView) findViewById(R.id.description_text);
        quantity_text = (TextView) findViewById(R.id.quantity_text);
        fisic_quantity = (TextView) findViewById(R.id.fisicquantity_text);
        new_quantity_edit = (EditText) findViewById(R.id.new_quantity_edit);
        department_spinner = (Spinner) findViewById(R.id.department_spinner);
        store_text = (TextView) findViewById(R.id.store_text);
        price_text = (TextView) findViewById(R.id.price_text);
        offer_text = (TextView) findViewById(R.id.offer_text);
        begin_text = (TextView) findViewById(R.id.begin_text);
        end_text = (TextView) findViewById(R.id.end_text);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        location = bundle.getString("location");
        username = bundle.getString("username");
        foundItem = (Item) intent.getSerializableExtra("item");
        store_text.setText(bundle.getString("store"));

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        store = new Store(
                preferences.getString("server", ""),
                preferences.getString("db", ""),
                preferences.getString("user", ""),
                preferences.getString("pass", ""),
                preferences.getString("name", ""));

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    Search();
                    return true;
                }
                return false;
            }
        });


        final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(SearchActivity.this, R.style.Dialog)).create();

        alertDialog.setTitle(getString(R.string.dialog_name));
        alertDialog.setMessage(getString(R.string.dialog_message));

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int qty = Integer.valueOf(new_quantity_edit.getText().toString());

                        if(foundItem != null && qty > -99){
                            Save(qty);
                            ClearUI();
                        }
                    }
                });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        new_quantity_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    if(foundItem != null){

                        try {
                            String value = new_quantity_edit.getText().toString();

                            if (value.length() < 4) {
                                int qty = Integer.valueOf(value);
                                if (qty > -99 && qty <= 300) {
                                    Save(qty);
                                    ClearUI();
                                } else if (qty > 300 && qty <= 999) {
                                    alertDialog.show();
                                } else if (qty > 999) {
                                    Toast.makeText(SearchActivity.this, "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SearchActivity.this, "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(SearchActivity.this, "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e ){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(foundItem != null){

                    try {
                        String value = new_quantity_edit.getText().toString();

                        if (value.length() < 4) {
                            int qty = Integer.valueOf(value);
                            if (qty > -99 && qty <= 300) {
                                Save(qty);
                                ClearUI();
                            } else if (qty > 300 && qty < 999) {
                                alertDialog.show();
                            } else if (qty > 999) {
                                Toast.makeText(SearchActivity.this, "No puedes agregar esta cantidad", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(SearchActivity.this, "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            Toast.makeText(SearchActivity.this, "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });

        item_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SearchActivity.this, "item", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });

        item_list_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SearchActivity.this, "item_list", Toast.LENGTH_SHORT).show();
                if(foundItem != null){
                    Intent i = new Intent(getApplicationContext(),ItemListActivity.class);
                    i.putExtra("itemlookup", foundItem.getItem_code());
                    startActivity(i);
                }
            }
        });

        search_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SearchActivity.this, "search", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), SearchListActivity.class);
                i.putExtra("store", store_text.getText().toString());
                startActivity(i);
            }
        });

        if(foundItem != null){
            ExecuteQuery executeQuery = new ExecuteQuery(store);
            departments = executeQuery.getDepartments();
            fillData();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearUI();
            }
        });
    }

    private boolean Search(){
        code = search_edit.getText().toString().trim();

        if(!code.isEmpty()){
            SearchBackground searchBackground = new SearchBackground();
            searchBackground.execute(store);

            new_quantity_edit.requestFocus();

            return true;
        }

        return false;
    }

    private void fillData(){

        itemcode_text.setText(foundItem.getItem_code());
        description_text.setText(foundItem.getDescription());
        quantity_text.setText(String.valueOf(foundItem.getQuantity()));
        fisic_quantity.setText(String.valueOf(foundItem.getFisic_quatity()));
        offer_text.setText(String.valueOf(foundItem.getOffer()));
        price_text.setText(String.valueOf(foundItem.getPrice()));
        begin_text.setText(String.valueOf(foundItem.getSalesStartDate()));
        end_text.setText(String.valueOf(foundItem.getSalesEndDate()));

        if(departments != null){
            ArrayAdapter<Department> adapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, departments);
            department_spinner.setAdapter(adapter);

            int pos = adapter.getPosition(new Department(foundItem.getDepartpment_id(), foundItem.getDepartmentName()));
            department_spinner.setSelection(pos);
        }

        new_quantity_edit.requestFocus();
    }

    private void Save(int quantity)
    {
        int q = foundItem.getFisic_quatity() + quantity;

        if(q > 0){
            ExecuteQuery executeQuery = new ExecuteQuery(store);
            int result = executeQuery.updateQuantity(foundItem.getItem_code(), quantity);

            if(result > 0){
                executeQuery.insertInput(foundItem.getItem_code(), location, quantity, username);
                Toast.makeText(getApplicationContext(), "Guardado exitosamente", Toast.LENGTH_SHORT).show();
            }

            Department d = (Department) department_spinner.getSelectedItem();
            if(foundItem.getDepartpment_id() != d.getId())
            {
                executeQuery.updateDepartment(foundItem.getId(), d.getId());
            }
        }else{
            Toast.makeText(getBaseContext(), "Cantidad: " + q + " invalida", Toast.LENGTH_LONG).show();
        }

        search_edit.requestFocus();
    }

    private void ClearUI(){
        foundItem = null;
        search_edit.setText("");
        department_spinner.setSelection(0);
        //categoy_spinner.setSelection(0);
        itemcode_text.setText("");
        description_text.setText("");
        quantity_text.setText("0");
        fisic_quantity.setText("0");
        new_quantity_edit.setText("");
        price_text.setText("");
        offer_text.setText("");
        begin_text.setText("");
        end_text.setText("");
        search_edit.requestFocus();

    }

    private class SearchBackground extends AsyncTask<Store, Void, Void> {

        private static final String TAG = "SearchBackground";

        @Override
        protected void onPreExecute() {
            pbHeaderProgress.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Store... params) {

            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0]);

                foundItem = executeQuery.getItem(code);

                if(foundItem == null)
                    return null;

                departments = executeQuery.getDepartments();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            pbHeaderProgress.setVisibility(View.GONE);

            if(foundItem == null){
                ClearUI();
                Toast.makeText(getApplicationContext(), "Codigo no encontrado", Toast.LENGTH_SHORT).show();
            }else{
                fillData();
                new_quantity_edit.requestFocus();
            }
        }
    }
}
