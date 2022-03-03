package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText name, password ;
    private TextView textDisplay;
    private SQLite sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLite = new SQLite(this.getApplicationContext());
        name = findViewById(R.id.editPersonName);
        password = findViewById(R.id.editTextPassword);
        textDisplay = findViewById(R.id.messageText);
    }

    public void connect(View v){
        if(name.getText().toString().equals("") || password.getText().toString().equals("")){
            textDisplay.setText(getResources().getString(R.string.usrpassempty));
            return;
        }

        int id = sqLite.getUser(name.getText().toString(),password.getText().toString());

        toast(getResources().getString(R.string.cnctuser));

        if(id==0){
            textDisplay.setText(getResources().getString(R.string.usrpassincorrect));
        }else{
            Intent intent = new Intent(this, GlobalActivity.class);
            // intent.putExtra("ID_UTILISATEUR",id);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("ids", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("ID_UTILISATEUR", id);
            editor.commit();
            textDisplay.setText("");
            startActivity(intent);
        }
    }

    public void signup(View v){
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ids", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("ID_UTILISATEUR");
        editor.remove("ID_PRODUIT");
        editor.commit();
    }

    // pop up message
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}