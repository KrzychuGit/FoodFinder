package com.example.krzysztof.foodfinder.View;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.krzysztof.foodfinder.R;

public class SettingsActivity extends AppCompatActivity {

    Button btnSave;
    EditText etSettingsRadius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        btnSave= findViewById(R.id.btnSave);
        etSettingsRadius= findViewById(R.id.etSettingsRadius);


        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Ustawienia");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        etSettingsRadius.setText(intent.getStringExtra("currentRadius"));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try
                {
                    int i= Integer.parseInt(etSettingsRadius.getText().toString());
                    String j = etSettingsRadius.getText().toString().substring(0,1);
                    if(Integer.parseInt(j) != 0)
                    {
                        if(Integer.parseInt(etSettingsRadius.getText().toString()) <= 50000)
                        {
                            String newRadius= etSettingsRadius.getText().toString();
                            Intent intent= new Intent();
                            intent.putExtra("radius", newRadius);
                            setResult(RESULT_OK, intent);

                            SettingsActivity.this.finish();
                        }
                        else
                            Toast.makeText(SettingsActivity.this, "Nieprawidłowe dane!\n(max 50000)", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SettingsActivity.this, "Nieprawidłowe dane!", Toast.LENGTH_SHORT).show();


                }
                catch(Exception ex){
                    Toast.makeText(SettingsActivity.this, "Nieprawidłowe dane!", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }
}
