package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

class SQLite extends SQLiteOpenHelper {
    private static final long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;
    static SQLiteDatabase sqLite ;

    public SQLite(Context context) {
        super(context, "AITODO", null, 1);
    }

    public SQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SQLite(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLite = sqLiteDatabase ;
        String sql = "CREATE TABLE IF NOT EXISTS UTILISATEURS(idUtilisateur INTEGER PRIMARY KEY NOT NULL , nom VARCHAR(30), password VARCHAR(30))";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS PRODUITS(idProduit INTEGER PRIMARY KEY NOT NULL, nom VARCHAR(30), benef NUMBER(6,3), idUtilisateur INTEGER, FOREIGN KEY (idUtilisateur) REFERENCES UTILISATEURS(idUtilisateur) )";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS ACHAT(idAchat INTEGER PRIMARY KEY NOT NULL, idProduit INTEGER, date VARCHAR2(10), quantite INTEGER, FOREIGN KEY (idProduit) REFERENCES PRODUITS(idProduit) )";
        sqLiteDatabase.execSQL(sql);
    }

    public int countUsers() {
        int co = 0;
        String selectQuery = "SELECT COUNT (*) FROM UTILISATEURS";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            co = cursor.getInt(0);
        }
        return co;
    }

    public int countProduits() {
        int co = 0;
        String selectQuery = "SELECT COUNT (*) FROM PRODUITS";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            co = cursor.getInt(0);
        }
        System.out.println("produits : "+co);
        return co;
    }

    public int countAchats() {
        int co = 0;
        String selectQuery = "SELECT COUNT (*) FROM ACHAT";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            co = cursor.getInt(0);
        }
        return co;
    }

    public void addProduit(String name, double benef, int idUtilisateur) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PRODUITS VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (countProduits() + 1));
        statement.bindString(2, name);
        statement.bindDouble(3, benef);
        statement.bindDouble(4, idUtilisateur);
        statement.executeInsert();
/*

        String sql = "INSERT INTO PRODUITS VALUES ("+(countProduits() + 1)+",'"+name+"',"+benef+","+idUtilisateur+");";
        System.out.println(sql);
        sqLite.execSQL(sql);

        System.out.println("TEST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("idProduit", (countProduits() + 1));
        contentValues.put("nom", name);
        contentValues.put("benef", benef);
        contentValues.put("idUtilisateur", idUtilisateur);
        sqLite.insert("PRODUITS", null, contentValues);
        System.out.println("TEST");*/
    }

    public void addUser(String name, String password) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO UTILISATEURS VALUES (?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (countUsers() + 1));
        statement.bindString(2, name);
        statement.bindString(3, password);
        statement.executeInsert();
    }

    public int getUser(String name, String password) {
        String selectQuery = "SELECT * FROM UTILISATEURS WHERE nom = '"+name+"' AND password = '"+password+"' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int co = 0;
        if (cursor.moveToFirst()) {
            co = Integer.parseInt(cursor.getString(0));
        }
        return co;
    }

    public Produit getProduit(int idUtilisateur) {
        String selectQuery = "SELECT * FROM PRODUITS WHERE idUtilisateur = '"+idUtilisateur+"' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToLast()) {
            Produit p = new Produit(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(3)), Float.parseFloat(cursor.getString(2)));
            return p;
        }
        return null;
    }

    public float getDayStat(int idUtilisateurs) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        String selectQuery = "SELECT SUM(A.QUANTITE * P.BENEF) FROM ACHAT A, PRODUITS P WHERE P.idProduit = A.idProduit AND P.idUtilisateur = '"+idUtilisateurs+"' AND A.date = '"+dtf.format(now)+"' ;";
        // String selectQuery = "SELECT SUM(A.QUANTITE * P.BENEF) FROM ACHAT A, PRODUITS P WHERE P.idProduit = A.idProduit AND A.idProduit = '"+idProduit+"' AND A.date = '"+dtf.format(now)+"' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        float co = 0;
        if (cursor.moveToFirst()) {
            if(cursor.getString(0)!=null){
                System.out.println("ZBEB");
                co = Float.parseFloat(cursor.getString(0));
            }
        }
        return co;
    }

    public float getWeekStat(int idUtilisateurs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        Date out = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        SQLiteDatabase db = this.getWritableDatabase();

        float cpt = 0;
        for(int i=0;i<7;i++){
            // String selectQuery = "SELECT SUM(A.QUANTITE * P.BENEF) FROM ACHAT A, PRODUITS P WHERE P.idProduit = A.idProduit AND A.idProduit = '"+idProduit+"' AND A.date = '"+dateFormat.format(out)+"' ;";
            String selectQuery = "SELECT SUM(A.QUANTITE * P.BENEF) FROM ACHAT A, PRODUITS P WHERE P.idProduit = A.idProduit AND P.idUtilisateur = '"+idUtilisateurs+"' AND A.date = '"+dateFormat.format(out)+"' ;";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                if(cursor.getString(0)!=null){
                    cpt = cpt + Float.parseFloat(cursor.getString(0));
                }
            }
            out = new Date(out.getTime()-ONE_DAY_MILLI_SECONDS);
        }
        return cpt;
    }

    public void addAchat(int produit, int quantite) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ACHAT VALUES (?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.clearBindings();
        statement.bindDouble(1, (countAchats() + 1));
        statement.bindDouble(2, produit);
        statement.bindString(3, dtf.format(now) );
        statement.bindDouble(4, quantite);
        statement.executeInsert();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
