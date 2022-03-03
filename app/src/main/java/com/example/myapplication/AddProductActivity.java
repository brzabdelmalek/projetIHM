package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {
    private EditText name, price1, price2 ;
    private TextView textDisplay;
    private SQLite sqLite;
    private int idUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //idUtilisateur = getIntent().getIntExtra("ID_UTILISATEUR",0);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ids", 0);
        idUtilisateur = pref.getInt("ID_UTILISATEUR",0);

        sqLite = new SQLite(this.getApplicationContext());

        name = findViewById(R.id.productName);
        price1 = findViewById(R.id.productPrice1);
        price2 = findViewById(R.id.productPrice2);
        textDisplay = findViewById(R.id.textError);
    }

    public void submit(View v){
        if(price1.getText().toString().equals("") || price2.getText().toString().equals("") || price2.getText().toString().equals("")){
            textDisplay.setText(getResources().getString(R.string.fillinfo));
        }
        double benef = Double.parseDouble(price1.getText().toString()) - Double.parseDouble(price2.getText().toString());
        if(benef>0){
            toast("Product inserted");
            sqLite.addProduit(name.getText().toString(),benef,idUtilisateur);
            Intent intent = new Intent(this, GlobalActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            toast("There's no benefit in this product");
        }
        textDisplay.setText("");
    }

    // pop up message
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}