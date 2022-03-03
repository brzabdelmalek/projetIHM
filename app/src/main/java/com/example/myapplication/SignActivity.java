package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignActivity extends AppCompatActivity {
    private EditText name, password ;
    private TextView textDisplay;
    private SQLite sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        sqLite = new SQLite(this.getApplicationContext());

        name = findViewById(R.id.editName);
        password = findViewById(R.id.editPassword);
        textDisplay = findViewById(R.id.displayMessage);
    }

    public void submit(View v){
        if(name.getText().toString().equals("") || password.getText().toString().equals("")){
            textDisplay.setText(getResources().getString(R.string.usrpassempty));
            return;
        }

        toast("create user");
        sqLite.addUser(name.getText().toString(),password.getText().toString());

        Intent intent = new Intent(this, GlobalActivity.class);
        int id = sqLite.getUser(name.getText().toString(),password.getText().toString());

        // intent.putExtra("ID_UTILISATEUR",id);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ids", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ID_UTILISATEUR", id);
        editor.commit();

        startActivity(intent);
        this.finish();
    }

    // pop up message
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}