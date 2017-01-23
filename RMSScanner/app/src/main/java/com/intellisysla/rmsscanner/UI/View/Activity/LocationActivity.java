package com.intellisysla.rmsscanner.UI.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.intellisysla.rmsscanner.R;

public class LocationActivity extends AppCompatActivity {

    Button location_button;
    EditText location_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        location_button = (Button) findViewById(R.id.location_button);
        location_edit = (EditText) findViewById(R.id.location_edit);

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("location", location_edit.getText().toString());
                editor.apply();

                Intent i = new Intent(LocationActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });



    }
}
