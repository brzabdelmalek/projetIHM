package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GlobalActivity extends AppCompatActivity {
    private float day = 0;
    private float week = 0;
    private float prix = 0;
    private TextView textDisplay;
    TextView TVday, TVweek;
    private SQLite sqLite;
    private int idUtilisateur ;
    private int idProduit ;
    private boolean produitExist = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global);

        textDisplay = findViewById(R.id.messageProduct);
        TVday = findViewById(R.id.textViewDay);
        TVweek = findViewById(R.id.textViewWeek);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("ids", 0);
        idUtilisateur = pref.getInt("ID_UTILISATEUR",0);
        SharedPreferences.Editor editor = pref.edit();
        //idUtilisateur = getIntent().getIntExtra("ID_UTILISATEUR",0);

        System.out.println("CONNECTED"+idUtilisateur);
        sqLite = new SQLite(this.getApplicationContext());

        Produit produit = sqLite.getProduit(idUtilisateur);
        if(produit!=null){
            prix = produit.getBenef();
            idProduit = produit.getId();

            editor.putInt("ID_PRODUIT", idProduit);
            editor.commit();

            day = sqLite.getDayStat(idUtilisateur);
            week = sqLite.getWeekStat(idUtilisateur);

            TVday.setText(getResources().getString(R.string.today_s_global)+" "+day);
            TVweek.setText(getResources().getString(R.string.this_week_s_global)+" "+week);
        }else{
            System.out.println("pas de produit ");
            produitExist = false;
        }
    }

    public void insertProduct(View v){
        if(!produitExist){
            textDisplay.setText("Product doesn't exist");
            return;
        }

        textDisplay.setText("");
        sqLite.addAchat(idProduit, 1);
        day = day + prix ;
        week = week + prix ;
        TVday.setText(getResources().getString(R.string.today_s_global)+" "+day);
        TVweek.setText(getResources().getString(R.string.this_week_s_global)+" "+week);
        toast("Product inserted");
    }

    public void insertManyProducts(View v){
        if(!produitExist){
            textDisplay.setText("Product doesn't exist");
            return;
        }

        textDisplay.setText("");
        Intent intent = new Intent(this, ManySelectionActivity.class);
        // intent.putExtra("ID_PRODUIT",idProduit);
        System.out.println("envoir id : "+ idUtilisateur);
        // intent.putExtra("ID_UTILISATEUR",idUtilisateur);
        startActivity(intent);
        this.finish();
    }

    public void addProduct(View v){
        Intent intent = new Intent(this, AddProductActivity.class);
        // intent.putExtra("ID_UTILISATEUR",idUtilisateur);
        startActivity(intent);
        this.finish();
    }

    // pop up message
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}