package com.intellisysla.rmsscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Intent i = new Intent(LocationActivity.this, SearchActivity.class);
                i.putExtra("location", location_edit.getText().toString().trim());
                Bundle bundle = getIntent().getExtras();
                i.putExtra("username", bundle.getString("username"));
                i.putExtra("store", bundle.getString("store"));
                startActivity(i);

            }
        });


    }
}
