package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ManySelectionActivity extends AppCompatActivity {
    private int cpt = 0;
    private TextView textDisplay;
    private TextView TVcpt ;
    private SQLite sqLite;
    private int idProduit;
    private int idUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many_selection);

        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("ids", Context.MODE_PRIVATE);
        idProduit = pref.getInt("ID_PRODUIT",0);
        idUtilisateur = pref.getInt("ID_UTILISATEUR",0);
        // idProduit = getIntent().getIntExtra("ID_PRODUIT",0);
        // idUtilisateur = getIntent().getIntExtra("ID_UTILISATEUR",0);

        sqLite = new SQLite(this.getApplicationContext());

        TVcpt = findViewById(R.id.quantityView);
        textDisplay = findViewById(R.id.messageView);
    }

    public void more(View v){
        cpt++;
        TVcpt.setText(""+cpt);
    }

    public void less(View v){
        cpt--;
        TVcpt.setText(""+cpt);
    }

    public void submit(View v){

        if(cpt < 1){
            textDisplay.setText(getResources().getString(R.string.cptincorrect));
            return;
        }

        sqLite.addAchat(idProduit, cpt);
        toast(cpt + getResources().getString(R.string.addproducts));
        textDisplay.setText("");

        Intent intent = new Intent(this, GlobalActivity.class);
        // intent.putExtra("ID_UTILISATEUR",idUtilisateur);
        startActivity(intent);
        this.finish();
    }

    // pop up message
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}