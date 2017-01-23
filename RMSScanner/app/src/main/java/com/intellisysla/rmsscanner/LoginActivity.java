package com.intellisysla.rmsscanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.BLL.ReadXMLConfiguration;
import com.intellisysla.rmsscanner.DAL.Store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActitivy";
    private Spinner store_spinner;
    private Button login_button;
    private EditText user_edit, pass_edit;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        store_spinner = (Spinner) findViewById(R.id.store_list);
        login_button = (Button) findViewById(R.id.login_button);
        user_edit = (EditText) findViewById(R.id.user_edit);
        pass_edit = (EditText) findViewById(R.id.password_edit);

        try {

            String fullpath = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

            //copy setting at first run
            Resources r = getResources();
            AssetManager assetManager = r.getAssets();
            File f = new File(fullpath, getString(R.string.config_file_name));

            if (!f.exists()) {
                InputStream is = assetManager.open(getString(R.string.config_file_name));
                OutputStream os = new FileOutputStream(f, true);

                final int buffer_size = 1024 * 1024;
                try {
                    byte[] bytes = new byte[buffer_size];
                    for (; ; ) {
                        int count = is.read(bytes, 0, buffer_size);
                        if (count == -1)
                            break;
                        os.write(bytes, 0, count);
                    }
                    is.close();
                    os.close();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
            //Read xml configuration
            ReadXmlBackground readXmlBackground = new ReadXmlBackground();
            readXmlBackground.execute(getString(R.string.config_file_name));

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                store = (Store)store_spinner.getSelectedItem();

                String user = user_edit.getText().toString();
                String pass = pass_edit.getText().toString();

                if(!user.isEmpty() && !pass.isEmpty()){
                    LoginBackground loginBackground = new LoginBackground();
                    loginBackground.execute(user, pass);
                }
            }
        });


    }

    private class ReadXmlBackground extends AsyncTask<String, Void, List<Store>> {

        @Override
        protected List<Store> doInBackground(String... params) {

            try{

                ReadXMLConfiguration config = new ReadXMLConfiguration(params[0], LoginActivity.this);
                return config.getStores();

            }catch (Exception e){
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Store> stores) {

            if(stores == null)
                return;

            ArrayAdapter<Store> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, stores);
            store_spinner.setAdapter(adapter);
        }
    }

    private class LoginBackground extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            String user = params[0].trim();
            String pass = params[1].trim();

            if(user.isEmpty() || pass.isEmpty()){
                Toast.makeText(LoginActivity.this, "Usuario y Contrasena no puede estar vacio", Toast.LENGTH_SHORT).show();
                return false;
            }

            ExecuteQuery executeQuery = new ExecuteQuery(store);

            return executeQuery.login(user, pass);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

            if(isSuccess){
                SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("server", store.getIp());
                editor.putString("db", store.getDb());
                editor.putString("user", store.getUser());
                editor.putString("pass", store.getPassword());
                editor.putString("name", store.getName());
                editor.apply();

                Intent i = new Intent(LoginActivity.this, LocationActivity.class);
                i.putExtra("username", user_edit.getText().toString());
                i.putExtra("store", store_spinner.getSelectedItem().toString());
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "Intente denuevo", Toast.LENGTH_LONG).show();
            }
        }
    }
}
